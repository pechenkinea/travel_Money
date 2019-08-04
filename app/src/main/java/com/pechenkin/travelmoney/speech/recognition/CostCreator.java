package com.pechenkin.travelmoney.speech.recognition;

import android.text.TextUtils;

import com.pechenkin.travelmoney.bd.table.result.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.ShortCost;
import com.pechenkin.travelmoney.page.cost.add.master.MasterCostInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pechenkin on 21.05.2018.
 * Класс для преобразования строки распознавания голоса в предварительный набор операций
 */

public class CostCreator {
    private final WordCollection words;
    private final String text;

    public CostCreator(String text)
    {
        this.text = text;
        this.words = new WordCollection(text);
        t_members.updateMembersCache();
        execute();
    }
    public CostCreator(String text, String comment)
    {
        this.comment = comment;
        this.text = text;
        this.words = new WordCollection(text);
        t_members.updateMembersCache();
        execute();
    }


    private final List<ShortCost> costs = new ArrayList<>();

    public ShortCost[] getCosts()
    {
        return  costs.toArray(new ShortCost[0]);
    }
    public  boolean hasCosts()
    {
        return  costs.size() > 0;
    }


    private long masterId = -1;
    private double sums = 0;
    private final List<Long> toMembers = new ArrayList<>();
    private final List<String> commentsList = new ArrayList<>();



    private String comment = "";
    private WordAction lastAction = WordAction.none;


    public String getComment() {
        if (comment.length() == 0)
        {
            return TextUtils.join(" ", commentsList).replaceAll(" {2}", " ").trim();
        }
        else {
            return comment;
        }
    }

    private void execute()
    {
        while (words.hasNext())
        {
            String word = words.getNext();
            identifyType(word);
        }

        if (masterId < 0 && toMembers.size() > 0)
        {
            masterId = toMembers.get(0);
            toMembers.remove(0);
        }

        createCosts();
    }

    private int groupCost = 0;
    private void createCosts()
    {
        if (masterId > -1 && sums > 0) {
                if (sums > MasterCostInfo.ERROR_SUM)
                {
                    sums = MasterCostInfo.ERROR_SUM;
                }

                groupCost++;
                for (long toMember : toMembers) {
                    long to = (toMember < 0)? masterId:toMember; //Для случаев когда мастер идет не первом в фразе
                    costs.add(new ShortCost(masterId, to, sums/toMembers.size(), getComment(), groupCost));
                }


            toMembers.clear();
            sums = 0;
        }
    }


    private void setMaster(long master)
    {
        if (masterId < 0) {
            masterId = master;
            lastAction = WordAction.addMaster;
        }
        else
        {
            addToMember(master);
        }
    }

    private  void  addToMember(long toMember)
    {
        if (toMembers.size() > 0 && lastAction == WordAction.addSum)
            createCosts();

        if (!toMembers.contains(toMember)) {
            toMembers.add(toMember);
        }
        lastAction = WordAction.addToMember;
    }

    private  void removeToMember(long toMember)
    {
        int index = toMembers.indexOf(toMember);
        if (index >=0) {
            toMembers.remove(index);
        }
    }



    private void identifyType(String text)
    {
        if (text.matches("(\\d+(\\.\\d+)?)")) {
            if (sums > 0 && lastAction == WordAction.addToMember)
                createCosts();

            sums += Double.valueOf(text);
            lastAction = WordAction.addSum;
            return;
        }

        //За всех
        if (text.equals(WordCollection.ALL))
        {
            MembersQueryResult membersTrip = t_members.getAllByTripId(t_trips.ActiveTrip.id);
            if (membersTrip.hasRows()) {
                for (MemberBaseTableRow toMember:membersTrip.getAllRows()) {
                    addToMember(toMember.id);
                }
            }
            return;
        }

        if (text.equals("кроме")) {
            int i = 1;
            while (words.viewNext(i).length() > 0)
            {
                if (words.viewNext(i).equals(WordCollection.MASTER))
                {
                    removeToMember(masterId);
                }
                else if (words.viewNext(i).equals(WordCollection.ME))
                {
                    long me = t_members.getMe();
                    if (me > -1) {
                        removeToMember(me);
                    }
                    else
                        break;
                }
                else
                {
                    //Поиск с учетом падежей
                    long toMember = t_members.getIdByNameCase(words.viewNext(i));
                    if (toMember > -1) {
                        removeToMember(toMember);
                    }
                    else {
                        break;
                    }
                }

                i++;
            }
            words.movePosition(i-1);
            return;
        }

        //За себя
        if ( text.equals(WordCollection.MASTER))
        {
            addToMember(masterId);
            return;
        }

        //За меня
        if ( text.equals(WordCollection.ME))
        {
            long toMember = t_members.getMe();
            if (toMember > -1) {
                addToMember(toMember);
                return;
            }
        }

        if ( text.equals(WordCollection.OWNER))
        {
            long meMaster = t_members.getMe();
            if (meMaster > -1) {
                setMaster(meMaster);
                return;
            }
        }

        long master = t_members.getIdByNameCache(text);
        if (master > -1) {
            setMaster(master);
            return;
        }

        //Поиск с учетом падежей
        long toMember = t_members.getIdByNameCase(text);
        if (toMember > -1) {
            addToMember(toMember);
            return;
        }

        commentsList.add(text);

    }


    public String getText() {
        return text;
    }


}
