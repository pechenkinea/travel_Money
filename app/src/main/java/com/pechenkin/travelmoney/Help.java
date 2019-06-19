package com.pechenkin.travelmoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.bd.table.result.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.list.CostMemberBaseTableRow;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class Help {


    private Help(){}

    //скрыть клавиатуру с экрана
	static public void hideKeyboard()
	{

		//Find the currently focused view, so we can grab the correct window token from it.
		View view = MainActivity.INSTANCE.getCurrentFocus();
		//If no view currently has focus, create a new one, just so we can grab a window token from it
		if (view == null) {
			view = new View(MainActivity.INSTANCE);
		}
		InputMethodManager imm = (InputMethodManager) MainActivity.INSTANCE.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	// простое уведомление
	static public void message (String mes)
	{
		
		Toast toast = Toast.makeText(MainActivity.INSTANCE.getApplicationContext(), mes, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();  
	}

	static public void alertHelp(String mes)
	{
		alert(mes, "Подсказка");
	}
	static public void alertError(String mes)
	{
		alert(mes, "Ошибка");
	}

	//сообщение с кнопкой
	static public void alert(String mes)
	{
		alert(mes, "");
	}

	private static void alert(String mes, String title)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
		builder	.setTitle(title)
				.setMessage(mes)
				.setCancelable(false)
				.setNegativeButton("ОК",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// заполнить выпадающий список
	public static void fill_cost_Spinner(BaseTableRow[] rows, Spinner spinner)
	{
		ArrayList<String> nameList = new ArrayList<>();
		int selectedObject = 0;
		for (BaseTableRow row : rows) {
			nameList.add(row.name);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.INSTANCE.getApplicationContext(), R.layout.spinner, nameList);
		adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setSelection(selectedObject);
	}

	// высота listView
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}


	// два массыва в один
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}


	public static  void setActiveEditText(int fieldId){
		setActiveEditText(fieldId, false);
	}

    public static  void setActiveEditText(int fieldId, boolean selectAll)
    {
        EditText field= MainActivity.INSTANCE.findViewById(fieldId);
		setActiveEditText(field, selectAll);
    }

	public static  void setActiveEditText(EditText field, boolean selectAll)
	{
		if (field != null) {
			field.requestFocus();
			field.setSelection(field.getText().length());
			InputMethodManager imm = (InputMethodManager) MainActivity.INSTANCE.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);
			}

			if (selectAll){
				field.selectAll();
			}
		}
	}

    public static void fill_list_view(long t_id, ListView list1)
    {
        MembersQueryResult c = t_members.getAllByTripId(t_id);

        list1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        if (!c.hasRows())
        {
            Help.message("Нет данных");
            list1.setAdapter(null);
        }
        else
        {
            AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMemberBaseTableRow.createCostMemberBaseTableRow(c.getAllRows(), 0), false);
            list1.setAdapter(adapter);

        }
    }

    public static double StringToDouble(String value)
	{
		if (value == null || value.length() == 0 )
			return 0;
		try {
			return Double.valueOf(value.replaceAll(" ", ""));
		}
		catch (Exception ex)
		{
			return 0;
		}
	}

	public static String DoubleToString(double value)
	{
		if (value == 0)
			return "0";

		String result = doubleFormat.format(value);
		if (result.endsWith(".00"))
		{
			result = result.replace(".00", "");
		}
		if (result.startsWith("."))
		{
			result = "0" + result;
		}
		return result;
	}

	private static DecimalFormat doubleFormat;
    static {
		doubleFormat = new DecimalFormat("#.00");
		doubleFormat.setGroupingUsed(true);
		doubleFormat.setGroupingSize(3);


		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormatSymbols.setGroupingSeparator(' ');

		doubleFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	}



    static void createBigCostList()
    {

		final ProgressDialog progDailog = ProgressDialog.show(MainActivity.INSTANCE,
				"",
				MainActivity.INSTANCE.getString(R.string.wait), true);

		Thread printCostThready =  new Thread(new Runnable() {
			@Override
			public void run() {

				MembersQueryResult currentTripMembers = t_members.getAllByTripId(t_trips.ActiveTrip.id);
				BaseTableRow[] members = currentTripMembers.getAllRows();

				try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
					for (int i = 0; i < 1000; i++) {

						Date now = new Date();
						String date = String.valueOf(now.getTime());

						ContentValues cv = new ContentValues();
						cv.put(Namespace.FIELD_MEMBER, members[new Random().nextInt(members.length)].id);
						cv.put(Namespace.FIELD_TO_MEMBER, members[new Random().nextInt(members.length)].id);
						cv.put(Namespace.FIELD_COMMENT, "");
						cv.put(Namespace.FIELD_SUM, String.valueOf(new Random().nextInt(300)));
						cv.put(Namespace.FIELD_IMAGE_DIR, "");
						cv.put(Namespace.FIELD_ACTIVE, 1);
						cv.put(Namespace.FIELD_TRIP, t_trips.ActiveTrip.id);
						cv.put(Namespace.FIELD_DATE, date);

						db.insert(Namespace.TABLE_COSTS, null, cv);

					}
				}
				progDailog.dismiss();
			}
		});

		printCostThready.start();


    }


}
