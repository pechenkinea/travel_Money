package com.pechenkin.travelmoney.cost.group;

import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Для агрегации информации по нескольким проводкам.
 * Что бы можно было группировать
 */
public abstract class GroupCost implements CostListItem {

    /**
     * группирует операции по дате и комментарию
     *
     * @param costs отсортированный по дате массив трат
     */
    public static GroupCost[] group(Cost[] costs) {
        List<GroupCost> groupCostList = new ArrayList<>();
        String lastKey = "";

        ArrayList<Cost> listCosts = new ArrayList<>();

        for (Cost cost : costs) {
            String key = cost.getDate().getTime() + cost.getComment();
            if (!key.equals(lastKey)) {
                if (listCosts.size() > 0) {
                    groupCostList.add(createGroupCost(listCosts.toArray(new Cost[0])));
                    listCosts.clear();
                }
                lastKey = key;
            }
            listCosts.add(cost);
        }

        if (listCosts.size() > 0) {
            groupCostList.add(createGroupCost(listCosts.toArray(new Cost[0])));
        }

        return groupCostList.toArray(new GroupCost[0]);
    }

    private static GroupCost createGroupCost(Cost[] listCosts) {

        if (listCosts.length == 0) {
            throw new RuntimeException("В группе должен быть хотя бы один элемент");
        }

        if (listCosts.length == 1) {
            return new OneItemGroup(listCosts[0]);
        }
        return new ManyItemsGroup(listCosts);
    }


    @Override
    public boolean isClicked() {
        return true;
    }


}
