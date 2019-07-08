package com.pechenkin.travelmoney.bd.table;

import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.result.BaseQueryResult;
import com.pechenkin.travelmoney.bd.table.result.QueryResultFactory;

public class t_colors {

	static public BaseQueryResult getAll()
	{
		String sql = "SELECT * FROM " + Namespace.TABLE_COLORS;
		return QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);

	}

}
