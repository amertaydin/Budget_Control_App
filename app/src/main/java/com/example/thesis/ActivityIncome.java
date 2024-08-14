package com.example.thesis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

public class ActivityIncome extends AppCompatActivity {
    PieChart pieChart;
    TextView total;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        total = findViewById(R.id.incomeTotal);

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        //String username = dbHelper.getLoginUsername();

        //String query = "SELECT * FROM Table_Income WHERE USERNAME=?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Cursor c = db.rawQuery(query, new String[]{username});

        /*if (c.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "There is no income to show", Toast.LENGTH_SHORT).show();
            total.setText("0");
            return;
        }*/

        /*int typeIndex = c.getColumnIndex("INCOME_TYPE");
        int valueIndex = c.getColumnIndex("VALUE");
        int incomeDateIndex = c.getColumnIndex("INCOMEDATE");*/

        LinearLayout l = findViewById(R.id.linearLayout);

        String username = dbHelper.getLoginUsername();

        String query = "SELECT * FROM Table_Income WHERE USERNAME=?";

        Cursor c = db.rawQuery(query, new String[]{username});

        if (c.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "There are no Incomes!", Toast.LENGTH_SHORT).show();

        } else {
            int typeIndex = c.getColumnIndex("INCOME_TYPE");
            int valueIndex = c.getColumnIndex("VALUE");
            int incomeDateIndex = c.getColumnIndex("INCOMEDATE");

            //LinearLayout linearLayoutIncome = findViewById(R.id.linearLayoutIncome);

            ArrayList<String> incomeDates = new ArrayList<>();
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                incomeDates.add(c.getString(incomeDateIndex));
                c.moveToNext();
            }


            ArrayList<java.util.Date> incomeDateArray = new ArrayList<>();
            for (int i = 0; i < incomeDates.size(); i++) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    java.util.Date date = formatter1.parse(incomeDates.get(i));
                    incomeDateArray.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            Collections.sort(incomeDateArray);
            ArrayList<String> sortedIncomeDates = new ArrayList<>();

            for (int i = 0; i < incomeDateArray.size(); i++) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                String s = formatter1.format(incomeDateArray.get(i));
                sortedIncomeDates.add(s);
            }

            LinkedHashSet<String> hashSet = new LinkedHashSet<>(sortedIncomeDates);
            ArrayList<String> incomeDatesNoDuplicate = new ArrayList<>(hashSet);
            Collections.reverse(incomeDatesNoDuplicate);

            CardView cardViewHeader = new CardView(getApplicationContext());
            ViewGroup.LayoutParams lpHeader = new ViewGroup.LayoutParams(1030, 75);
            cardViewHeader.setLayoutParams(lpHeader);
            cardViewHeader.setBackgroundColor(Color.LTGRAY);
            l.addView(cardViewHeader);

            TextView textViewHeaderType = new TextView(getApplicationContext());
            textViewHeaderType.setX(60f);
            textViewHeaderType.setY(10f);
            textViewHeaderType.setText("Income Type");
            cardViewHeader.addView(textViewHeaderType);

            TextView textViewHeaderValue = new TextView(getApplicationContext());
            textViewHeaderValue.setX(450f);
            textViewHeaderValue.setY(10f);
            textViewHeaderValue.setText("Value");
            cardViewHeader.addView(textViewHeaderValue);

            TextView textViewHeaderDate = new TextView(getApplicationContext());
            textViewHeaderDate.setX(700f);
            textViewHeaderDate.setY(10f);
            textViewHeaderDate.setText("Income Date");
            cardViewHeader.addView(textViewHeaderDate);

            View viewHeader = new View(getApplicationContext());
            ViewGroup.LayoutParams vlpHeader = new ViewGroup.LayoutParams(300, 10);
            viewHeader.setLayoutParams(vlpHeader);
            viewHeader.setBackgroundColor(Color.BLACK);
            viewHeader.setVisibility(View.INVISIBLE);
            l.addView(viewHeader);

            for (int i = 0; i < incomeDatesNoDuplicate.size(); i++) {

                String result = new String();
                String x = incomeDatesNoDuplicate.get(i);
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



                String queryForDate = "SELECT * FROM Table_Income WHERE INCOMEDATE=? AND USERNAME=?";
                Cursor cursorForDate = db.rawQuery(queryForDate, new String[]{result,dbHelper.getLoginUsername()});
                int typeIndexForDate = cursorForDate.getColumnIndex("INCOME_TYPE");
                int valueIndexForDate = cursorForDate.getColumnIndex("VALUE");
                int incomeDateIndexForDate = cursorForDate.getColumnIndex("INCOMEDATE");

                cursorForDate.moveToFirst();
                for(int j=0;j<cursorForDate.getCount();j++)
                {
                    CardView cardView = new CardView(getApplicationContext());
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(1050, 175);
                    cardView.setLayoutParams(lp);
                    cardView.setRadius(170f);
                    l.addView(cardView);

                    TextView textViewIncomeDate = new TextView(getApplicationContext());
                    textViewIncomeDate.setX(725f);
                    textViewIncomeDate.setY(60f);
                    textViewIncomeDate.setText(cursorForDate.getString(incomeDateIndexForDate));
                    cardView.addView(textViewIncomeDate);

                    TextView textViewType = new TextView(getApplicationContext());
                    textViewType.setX(90f);
                    textViewType.setY(60f);
                    String typeString = cursorForDate.getString(typeIndexForDate);
                    textViewType.setText(typeString);
                    cardView.addView(textViewType);

                    TextView textViewValue = new TextView(getApplicationContext());
                    textViewValue.setX(463f);
                    textViewValue.setY(60f);
                    String valueString = cursorForDate.getString(valueIndexForDate);
                    textViewValue.setText(valueString);
                    cardView.addView(textViewValue);

                    View view = new View(getApplicationContext());
                    ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(400, 10);
                    view.setLayoutParams(vlp);
                    view.setBackgroundColor(Color.BLACK);
                    view.setVisibility(View.INVISIBLE);
                    l.addView(view);

                    cursorForDate.moveToNext();
                }

            }
        }

        total.setText(String.valueOf(dbHelper.calculateTotalIncome()));

        pieChart = findViewById(R.id.incomePieChart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> values = new ArrayList<>();
        Cursor cGroup = db.query("Table_Income", new String[]{"INCOME_TYPE", "SUM(VALUE)"}, "USERNAME=?",
                new String[]{username}, "INCOME_TYPE", null, null);
        cGroup.moveToFirst();
        int groupTypeIndex = cGroup.getColumnIndex("INCOME_TYPE");
        int groupValueIndex = cGroup.getColumnIndex("SUM(VALUE)");
        for (int i = 0; i < cGroup.getCount(); i++) {
            values.add(new PieEntry(Float.valueOf(cGroup.getString(groupValueIndex)), cGroup.getString(groupTypeIndex)));
            cGroup.moveToNext();
        }

        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);

        pieChart.setData(pieData);
    }
}
