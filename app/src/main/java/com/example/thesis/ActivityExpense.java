package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class ActivityExpense extends AppCompatActivity {
    PieChart expensePieChart;
    TextView totalExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        totalExpense = findViewById(R.id.totalExpense);

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        String username = dbHelper.getLoginUsername();

        //String query = "SELECT * FROM Table_Expense WHERE USERNAME=?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Cursor c = db.rawQuery(query, new String[]{username});
        //c.moveToFirst();
        /*if (c.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "There is no expense to show", Toast.LENGTH_SHORT).show();
            totalExpense.setText("0");
            return;
        }*/

        //int typeIndex = c.getColumnIndex("EXPENSE_TYPE");
        //int valueIndex = c.getColumnIndex("VALUE");
        //int expenseDateIndex = c.getColumnIndex("EXPENSEDATE");



        LinearLayout l = findViewById(R.id.linearLayoutExpense);

        String expenseQuery = "SELECT * FROM Table_Expense WHERE USERNAME=?";
        Cursor cursorExpense = db.rawQuery(expenseQuery, new String[]{username});
        if (cursorExpense.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "There are no Expenses!", Toast.LENGTH_SHORT).show();
        } else {
            int expenseDateIndex = cursorExpense.getColumnIndex("EXPENSEDATE");

            LinearLayout linearLayoutExpense = findViewById(R.id.linearLayoutExpense);

            ArrayList<String> expenseDates = new ArrayList<>();

            cursorExpense.moveToFirst();
            for (int i = 0; i < cursorExpense.getCount(); i++) {
                expenseDates.add(cursorExpense.getString(expenseDateIndex));
                cursorExpense.moveToNext();
            }

            ArrayList<java.util.Date> expenseDateArray = new ArrayList<>();
            for (int i = 0; i < expenseDates.size(); i++) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    java.util.Date date = formatter1.parse(expenseDates.get(i));
                    expenseDateArray.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Collections.sort(expenseDateArray);

            ArrayList<String> sortedExpenseDates = new ArrayList<>();

            for (int i = 0; i < expenseDateArray.size(); i++) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                String s = formatter1.format(expenseDateArray.get(i));
                sortedExpenseDates.add(s);
            }

            LinkedHashSet<String> hashSet = new LinkedHashSet<>(sortedExpenseDates);
            ArrayList<String> expenseDatesNoDuplicate = new ArrayList<>(hashSet);
            Collections.reverse(expenseDatesNoDuplicate);

            CardView cardViewHeader = new CardView(getApplicationContext());
            ViewGroup.LayoutParams lpHeader = new ViewGroup.LayoutParams(1030,75);
            cardViewHeader.setLayoutParams(lpHeader);
            cardViewHeader.setBackgroundColor(Color.LTGRAY);
            l.addView(cardViewHeader);

            TextView textViewHeaderType = new TextView(getApplicationContext());
            textViewHeaderType.setX(75f);
            textViewHeaderType.setY(10f);
            textViewHeaderType.setText("Expense Type");
            cardViewHeader.addView(textViewHeaderType);

            TextView textViewHeaderValue = new TextView(getApplicationContext());
            textViewHeaderValue.setX(450f);
            textViewHeaderValue.setY(10f);
            textViewHeaderValue.setText("Value");
            cardViewHeader.addView(textViewHeaderValue);

            TextView textViewHeaderDate = new TextView(getApplicationContext());
            textViewHeaderDate.setX(700f);
            textViewHeaderDate.setY(10f);
            textViewHeaderDate.setText("Expense Date");
            cardViewHeader.addView(textViewHeaderDate);

            View viewHeader = new View(getApplicationContext());
            ViewGroup.LayoutParams vlpHeader = new ViewGroup.LayoutParams(300,10);
            viewHeader.setLayoutParams(vlpHeader);
            viewHeader.setBackgroundColor(Color.BLACK);
            viewHeader.setVisibility(View.INVISIBLE);
            l.addView(viewHeader);

            for (int i = 0; i < expenseDatesNoDuplicate.size(); i++) {
                String result = new String();
                String x = expenseDatesNoDuplicate.get(i);
                if (x.length() == 10 && x.charAt(0) == '0' && x.charAt(3) == '0') {
                    StringBuilder sb = new StringBuilder(x);
                    sb.deleteCharAt(3);
                    sb.deleteCharAt(0);
                    result = sb.toString();

                }
                else if (x.length() == 10 && x.charAt(0) != '0' && x.charAt(3) == '0') {
                    StringBuilder sb = new StringBuilder(x);
                    sb.deleteCharAt(3);
                    result = sb.toString();
                }
                else if (x.length() == 10 && x.charAt(0) == '0' && x.charAt(3) != '0') {
                    StringBuilder sb = new StringBuilder(x);
                    sb.deleteCharAt(0);
                    result = sb.toString();
                }
                else {
                    StringBuilder sb = new StringBuilder(x);
                    result = sb.toString();
                }

                String queryForExpenseDate = "SELECT * FROM Table_Expense WHERE EXPENSEDATE=? AND USERNAME=?";
                Cursor cursorForExpenseDate = db.rawQuery(queryForExpenseDate, new String[]{result,dbHelper.getLoginUsername()});
                int typeIndexForExpenseDate = cursorForExpenseDate.getColumnIndex("EXPENSE_TYPE");
                int valueIndexForExpenseDate = cursorForExpenseDate.getColumnIndex("VALUE");
                int expenseDateIndexForExpenseDate = cursorForExpenseDate.getColumnIndex("EXPENSEDATE");

                cursorForExpenseDate.moveToFirst();
                for(int j=0;j<cursorForExpenseDate.getCount();j++)
                {
                    CardView cardView = new CardView(getApplicationContext());
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(1050, 175);
                    cardView.setLayoutParams(lp);
                    cardView.setRadius(170f);
                    linearLayoutExpense.addView(cardView);

                    TextView textViewExpenseDate = new TextView(getApplicationContext());
                    textViewExpenseDate.setX(725f);
                    textViewExpenseDate.setY(60f);
                    textViewExpenseDate.setText(cursorForExpenseDate.getString(expenseDateIndexForExpenseDate));
                    cardView.addView(textViewExpenseDate);

                    TextView textViewType = new TextView(getApplicationContext());
                    textViewType.setX(50f);
                    textViewType.setY(60f);
                    String typeString = cursorForExpenseDate.getString(typeIndexForExpenseDate);
                    textViewType.setText(typeString);
                    cardView.addView(textViewType);

                    TextView textViewValue = new TextView(getApplicationContext());
                    textViewValue.setX(463f);
                    textViewValue.setY(60f);
                    String valueString = cursorForExpenseDate.getString(valueIndexForExpenseDate);
                    textViewValue.setText(valueString);
                    cardView.addView(textViewValue);



                    View view = new View(getApplicationContext());
                    ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(400, 10);
                    view.setLayoutParams(vlp);
                    view.setBackgroundColor(Color.BLACK);
                    view.setVisibility(View.INVISIBLE);
                    linearLayoutExpense.addView(view);

                    cursorForExpenseDate.moveToNext();
                }

            }

        }

        totalExpense.setText(String.valueOf(dbHelper.calculateTotalExpense()));
        expensePieChart = findViewById(R.id.expensePieChart);

        expensePieChart.setUsePercentValues(true);
        expensePieChart.getDescription().setEnabled(false);
        expensePieChart.setExtraOffsets(5,10,5,5);

        expensePieChart.setDragDecelerationFrictionCoef(0.95f);
        expensePieChart.setDrawHoleEnabled(true);
        expensePieChart.setHoleColor(Color.WHITE);
        expensePieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> values = new ArrayList<>();
        Cursor cGroup = db.query("Table_Expense",new String[] {"EXPENSE_TYPE","SUM(VALUE)"},"USERNAME=?",new String[] {username},"EXPENSE_TYPE",null,null);
        int expenseTypeIndex = cGroup.getColumnIndex("EXPENSE_TYPE");
        int expenseValueIndex = cGroup.getColumnIndex("SUM(VALUE)");
        cGroup.moveToFirst();
        for(int i=0;i<cGroup.getCount();i++)
        {
            values.add(new PieEntry(Float.valueOf(cGroup.getString(expenseValueIndex)),cGroup.getString(expenseTypeIndex)));
            cGroup.moveToNext();
        }

        PieDataSet dataSet = new PieDataSet(values,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);

        expensePieChart.setData(pieData);


    }
}
