package com.pechenkin.travelmoney.bd.local.table;

import com.pechenkin.travelmoney.bd.local.CostLocal;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;

@Deprecated
public class CostTable {

    public static final CostTable INSTANCE = new CostTable();

    private CostTable() {
    }

    public QueryResult<CostLocal> getAllByTripId(long t_id) {
        String sql = "SELECT * FROM " + Namespace.TABLE_COSTS + " where " + Namespace.FIELD_TRIP + " = '" + t_id + "' ORDER BY " + Namespace.FIELD_DATE + " DESC";
        return new QueryResult<>(sql, CostLocal.class);
    }

}
