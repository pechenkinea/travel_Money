package com.pechenkin.travelmoney.speech.recognition;

import android.text.TextUtils;

import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.Division;
import com.pechenkin.travelmoney.TMConst;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.utils.NamesHashMap;
import com.pechenkin.travelmoney.bd.TripManager;

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
    private int sums = 0;
    private final List<Member> toMembers = new ArrayList<>();
    private final List<String> commentsList = new ArrayList<>();


    private String comment;
    private WordAction lastAction = WordAction.none;

    public CostCreator(String text, String comment) {
        this.comment = comment;
        this.text = text;
        this.words = new WordCollection(text);
        execute();
    }


    private DraftTransaction draftTransaction = new DraftTransaction();

    public DraftTransaction getDraftTransaction() {
        return draftTransaction;
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

    private void createCosts() {
        if (master != null && sums > 0) {

            sums = sums * 100;
            if (sums > TMConst.ERROR_SUM) {
                sums = TMConst.ERROR_SUM;
            }

            draftTransaction.addTransactionItem(new DraftTransactionItem(master, 0 , sums));

            Division division = new Division(sums, toMembers.size());


            for (Member toMember : toMembers) {
                Member to = (toMember == null) ? master : toMember; //Для случаев когда мастер идет не первом в фразе

                draftTransaction.addTransactionItem(new DraftTransactionItem(to, division.getNext(), 0));
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
            List<Member> membersTrip = TripManager.INSTANCE.getActiveTrip().getActiveMembers();
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
                    Member me = TripManager.INSTANCE.getActiveTrip().getMe();
                    if (me != null) {
                        removeToMember(me);
                    } else
                        break;
                } else {
                    //Поиск с учетом падежей
                    Member toMember = getByNameCase(words.viewNext(i));
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
            Member toMember = TripManager.INSTANCE.getActiveTrip().getMe();
            if (toMember != null) {
                addToMember(toMember);
                return;
            }
        }

        //Я
        if (text.equals(WordCollection.OWNER)) {
            Member meMaster = TripManager.INSTANCE.getActiveTrip().getMe();
            if (meMaster != null) {
                setMaster(meMaster);
                return;
            }
        }

        // Если нашли по полному совпадению то это мастер
        Member master = getByName(text);
        if (master != null) {
            setMaster(master);
            return;
        }

        //Поиск с учетом падежей
        Member toMember = getByNameCase(text);
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
     * @param name строка для поиска
     * @return id сотудника или -1
     */
    private Member getByNameCase(String name) {

        String nameCase = NamesHashMap.keyValidate(name);

        Member row = getByName(name);
        List<Member> members = TripManager.INSTANCE.getActiveTrip().getActiveMembers();

        while (row == null && nameCase.length() > 2 && nameCase.length() / name.length() > 0.7) {
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

    /**
     * Ищет участника по точному совпадению имени
     */
    private Member getByName(String name){
        String nameCase = NamesHashMap.keyValidate(name);

        List<Member> members = TripManager.INSTANCE.getActiveTrip().getActiveMembers();
        for (Member member : members) {
            if (NamesHashMap.keyValidate(member.getName()).equals(nameCase)) {
                return member;
            }
        }
        return null;
    }


    public String getText() {
        return text;
    }


}
