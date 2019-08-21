package com.pechenkin.travelmoney.speech.recognition;

import android.text.TextUtils;

import com.pechenkin.travelmoney.TMConst;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.NamesHashMap;
import com.pechenkin.travelmoney.bd.local.table.t_members;
import com.pechenkin.travelmoney.bd.local.table.t_trips;
import com.pechenkin.travelmoney.cost.ShortCost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pechenkin on 21.05.2018.
 * Класс для преобразования строки распознавания голоса в предварительный набор операций
 */

public class CostCreator {
    private final WordCollection words;
    private final String text;

    private Member master = null;
    private double sums = 0;
    private final List<Member> toMembers = new ArrayList<>();
    private final List<String> commentsList = new ArrayList<>();


    private String comment;
    private WordAction lastAction = WordAction.none;

    public CostCreator(String text, String comment) {
        this.comment = comment;
        this.text = text;
        this.words = new WordCollection(text);
        t_members.updateMembersCache();
        execute();
    }


    private final List<ShortCost> costs = new ArrayList<>();

    public ShortCost[] getCosts() {
        return costs.toArray(new ShortCost[0]);
    }

    public boolean hasCosts() {
        return costs.size() > 0;
    }


    public String getComment() {
        if (comment.length() == 0) {
            return TextUtils.join(" ", commentsList).replaceAll(" {2}", " ").trim();
        } else {
            return comment;
        }
    }

    private void execute() {
        while (words.hasNext()) {
            String word = words.getNext();
            identifyType(word);
        }

        if (master == null && toMembers.size() > 0) {
            master = toMembers.get(0);
            toMembers.remove(0);
        }

        createCosts();
    }

    private int groupCost = 0;

    private void createCosts() {
        if (master != null && sums > 0) {
            if (sums > TMConst.ERROR_SUM) {
                sums = TMConst.ERROR_SUM;
            }

            groupCost++;
            for (Member toMember : toMembers) {
                Member to = (toMember == null) ? master : toMember; //Для случаев когда мастер идет не первом в фразе
                costs.add(new ShortCost(master, to, sums / toMembers.size(), getComment(), groupCost));
            }


            toMembers.clear();
            sums = 0;
        }
    }


    private void setMaster(Member master) {
        if (this.master == null) {
            this.master = master;
            lastAction = WordAction.addMaster;
        } else {
            addToMember(master);
        }
    }

    private void addToMember(Member toMember) {
        if (toMembers.size() > 0 && lastAction == WordAction.addSum)
            createCosts();

        if (!toMembers.contains(toMember)) {
            toMembers.add(toMember);
        }
        lastAction = WordAction.addToMember;
    }

    private void removeToMember(Member toMember) {
        int index = toMembers.indexOf(toMember);
        if (index >= 0) {
            toMembers.remove(index);
        }
    }


    private void identifyType(String text) {
        if (text.matches("(\\d+(\\.\\d+)?)")) {
            if (sums > 0 && lastAction == WordAction.addToMember)
                createCosts();

            sums += Double.valueOf(text);
            lastAction = WordAction.addSum;
            return;
        }

        //За всех
        if (text.equals(WordCollection.ALL)) {
            Member[] membersTrip = t_trips.getActiveTrip().getActiveMembers();
            for (Member toMember : membersTrip) {
                addToMember(toMember);
            }
            return;
        }

        if (text.equals("кроме")) {
            int i = 1;
            while (words.viewNext(i).length() > 0) {
                if (words.viewNext(i).equals(WordCollection.MASTER)) {
                    removeToMember(master);
                } else if (words.viewNext(i).equals(WordCollection.ME)) {
                    Member me = t_trips.getActiveTrip().getMe();
                    if (me != null) {
                        removeToMember(me);
                    } else
                        break;
                } else {
                    //Поиск с учетом падежей
                    Member toMember = getIdByNameCase(words.viewNext(i));
                    if (toMember != null) {
                        removeToMember(toMember);
                    } else {
                        break;
                    }
                }

                i++;
            }
            words.movePosition(i - 1);
            return;
        }

        //За себя
        if (text.equals(WordCollection.MASTER)) {
            addToMember(master);
            return;
        }

        //За меня
        if (text.equals(WordCollection.ME)) {
            Member toMember = t_trips.getActiveTrip().getMe();
            if (toMember != null) {
                addToMember(toMember);
                return;
            }
        }

        if (text.equals(WordCollection.OWNER)) {
            Member meMaster = t_trips.getActiveTrip().getMe();
            if (meMaster != null) {
                setMaster(meMaster);
                return;
            }
        }

        //TODO может оно и не надо и можно объединить со следующим блоком
        Member master = t_members.getIdByNameCache(text);
        if (master != null) {
            setMaster(master);
            return;
        }

        //Поиск с учетом падежей
        Member toMember = getIdByNameCase(text);
        if (toMember != null) {
            addToMember(toMember);
            return;
        }

        commentsList.add(text);

    }


    /**
     * Ищет участника с учетом падежей.
     * Для этого пробует найти участника по переданному имени. Если не удалось найти убирает последнюю букву от переданного значения и ищет по совпадению парвых символов.
     * Так продолжается до тех пор пока не уменьшим переданное значение на 30% или оно не станет меньше 2х букв
     *
     * @param m_name строка для поиска
     * @return id сотудника или -1
     */
    private Member getIdByNameCase(String m_name) {

        String nameCase = NamesHashMap.keyValidate(m_name);

        Member[] members = t_trips.getActiveTrip().getActiveMembers();
        for (Member member : members) {
            if (NamesHashMap.keyValidate(member.getName()).equals(nameCase)) {
                return member;
            }
        }

        Member row = null;
        while (row == null && nameCase.length() > 2 && nameCase.length() / m_name.length() > 0.7) {
            nameCase = nameCase.substring(0, nameCase.length() - 1);

            for (Member member : members) {
                String rowCache = NamesHashMap.keyValidate(member.getName());
                if (rowCache.startsWith(nameCase)) {
                    row = member;
                    break;
                }
            }
        }

        return row;

    }


    public String getText() {
        return text;
    }


}
