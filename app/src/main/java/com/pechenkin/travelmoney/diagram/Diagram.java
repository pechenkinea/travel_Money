package com.pechenkin.travelmoney.diagram;

import com.pechenkin.travelmoney.transaction.adapter.CostListItem;

public interface Diagram extends CostListItem {

    void setOnDiagramSelectItem(OnDiagramSelectItem onDiagramSelectItem);

}
