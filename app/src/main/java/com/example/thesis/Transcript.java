package com.example.thesis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;

public class Transcript extends AppCompatActivity {
    BarChart barChart;
    Button buttonShowBarChart;
    RadioButton radioButtonPreviousYear,radioButtonCurrentYear;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transkript);


        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        radioButtonPreviousYear = findViewById(R.id.radioButtonPreviousYear);
        radioButtonCurrentYear = findViewById(R.id.radioButtonCurrentYear);

        buttonShowBarChart = findViewById(R.id.buttonShowBarChart);
        buttonShowBarChart.setEnabled(false);

        double[] incomeValuesPreviousYear = new double[12];

        double[] incomeValuesCurrentYear = new double[12];

        double[] expenseValuesPreviousYear = new double[12];

        double[] expenseValuesCurrentYear = new double[12];

        ArrayList<String> incomeDatesPreviousYear = new ArrayList<>();
        ArrayList<String> incomeDatesCurrentYear = new ArrayList<>();
        ArrayList<String> expenseDatesPreviousYear = new ArrayList<>();
        ArrayList<String> expenseDatesCurrentYear = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int previousYear = currentYear -1;

        String incomeBarQuery = "SELECT * FROM Table_Income WHERE USERNAME=?";
        Cursor cursorIncomeBar = db.rawQuery(incomeBarQuery,new String[] {dbHelper.getLoginUsername()});

        int incomeBarDateIndex = cursorIncomeBar.getColumnIndex("INCOMEDATE");
        int incomeBarValueIndex = cursorIncomeBar.getColumnIndex("VALUE");

        cursorIncomeBar.moveToFirst();
        if (cursorIncomeBar.getCount()==0)
        {
            Toast.makeText(getApplicationContext(),"No Income Data!",Toast.LENGTH_SHORT).show();

        }
        else
        {



            for(int i=0;i<cursorIncomeBar.getCount();i++)
            {
                String date = cursorIncomeBar.getString(incomeBarDateIndex);
                String[] dateArray = date.split("/");
                if(dateArray[2].equals(String.valueOf(previousYear)))
                {
                    incomeDatesPreviousYear.add(date);
                    int monthValue = Integer.parseInt(dateArray[1]);
                    monthValue -=1;
                   /* if (cursorIncomeBar.getString(incomeBarRepetitiveIndex).equals("1")) {
                        for (int j = monthValue; j < 12; j++) {
                            incomeValues2019[j] += Double.parseDouble(cursorIncomeBar.getString(incomeBarValueIndex));
                        }
                    }*/
                    //  else
                    //  {
                    incomeValuesPreviousYear[monthValue] +=Double.parseDouble(cursorIncomeBar.getString(incomeBarValueIndex));
                    // }
                }

                else if(dateArray[2].equals(String.valueOf(currentYear)))
                {
                    incomeDatesCurrentYear.add(date);
                    int monthValue = Integer.parseInt(dateArray[1]);
                    monthValue -= 1;
                   /* if(cursorIncomeBar.getString(incomeBarRepetitiveIndex).equals("1")) {
                        for (int j = monthValue; j < 12; j++) {
                            incomeValues2020[j] += Double.parseDouble(cursorIncomeBar.getString(incomeBarValueIndex));
                        }
                    }*/
                    // else
                    // {
                    incomeValuesCurrentYear[monthValue] +=Double.parseDouble(cursorIncomeBar.getString(incomeBarValueIndex));
                    // }
                }

                cursorIncomeBar.moveToNext();
            }
        }

        String expenseBarQuery = "SELECT * FROM Table_Expense WHERE USERNAME=?";
        Cursor cursorExpenseBar = db.rawQuery(expenseBarQuery,new String[]{dbHelper.getLoginUsername()});
        int expenseBarDateIndex = cursorExpenseBar.getColumnIndex("EXPENSEDATE");
        int expenseBarValueIndex = cursorExpenseBar.getColumnIndex("VALUE");


        cursorExpenseBar.moveToFirst();
        if(cursorExpenseBar.getCount()==0)
        {
            Toast.makeText(getApplicationContext(),"No Expense Data!",Toast.LENGTH_SHORT).show();

        }
        else
        {
            for(int i=0;i<cursorExpenseBar.getCount();i++)
            {
                String expenseDate = cursorExpenseBar.getString(expenseBarDateIndex);
                String[] expenseDateArray = expenseDate.split("/");
                if(expenseDateArray[2].equals(String.valueOf(previousYear)))
                {
                    expenseDatesPreviousYear.add(expenseDate);
                    int monthValue = Integer.parseInt(expenseDateArray[1]);
                    monthValue -=1;
                   /* if (cursorExpenseBar.getString(expenseBarRepetitiveIndex).equals("1")) {
                        for (int j = monthValue; j < 12; j++) {
                            expenseValues2019[j] += Double.parseDouble(cursorExpenseBar.getString(expenseBarValueIndex));
                        }
                    }*/
                    // else
                    //  {
                    expenseValuesPreviousYear[monthValue] += Double.parseDouble(cursorExpenseBar.getString(expenseBarValueIndex));
                    // }
                }

                else if (expenseDateArray[2].equals(String.valueOf(currentYear)))
                {
                    expenseDatesCurrentYear.add(expenseDate);
                    int monthValue = Integer.parseInt(expenseDateArray[1]);
                    monthValue -=1;
                  /*  if(cursorExpenseBar.getString(expenseBarRepetitiveIndex).equals("1")) {
                        for (int j = monthValue; j < 12; j++) {
                            expenseValues2020[j] += Double.parseDouble(cursorExpenseBar.getString(expenseBarValueIndex));
                        }
                    }*/
                    //  else
                    //  {
                    expenseValuesCurrentYear[monthValue] += Double.parseDouble(cursorExpenseBar.getString(expenseBarValueIndex));
                    //  }
                }

                cursorExpenseBar.moveToNext();
            }
        }

        final ArrayList<BarEntry> incomeBarValuesPreviousYear = new ArrayList<>();
        final ArrayList<BarEntry> incomeBarValuesCurrentYear = new ArrayList<>();
        final ArrayList<BarEntry> expenseBarValuesPreviousYear = new ArrayList<>();
        final ArrayList<BarEntry> expenseBarValuesCurrentYear = new ArrayList<>();

        for(int i=0;i<12;i++)
        {
            incomeBarValuesPreviousYear.add(new BarEntry(1,Math.round(incomeValuesPreviousYear[i])));
        }

        for(int i=0;i<12;i++)
        {
            incomeBarValuesCurrentYear.add(new BarEntry(1,Math.round(incomeValuesCurrentYear[i])));
        }

        for(int i=0;i<12;i++)
        {
            expenseBarValuesPreviousYear.add(new BarEntry(1,Math.round(expenseValuesPreviousYear[i])));
        }

        for(int i=0;i<12;i++)
        {
            expenseBarValuesCurrentYear.add(new BarEntry(1,Math.round(expenseValuesCurrentYear[i])));
        }

        final BarDataSet incomeDataSetPreviousYear = new BarDataSet(incomeBarValuesPreviousYear, String.valueOf(previousYear)+ " Incomes");
        incomeDataSetPreviousYear.setColor(Color.BLUE);

        final BarDataSet incomeDataSetCurrentYear = new BarDataSet(incomeBarValuesCurrentYear,String.valueOf(currentYear)+ " Incomes");
        incomeDataSetCurrentYear.setColor(Color.BLUE);

        final BarDataSet expenseDataSetPreviousYear = new BarDataSet(expenseBarValuesPreviousYear,String.valueOf(previousYear)+ " Expenses");
        expenseDataSetPreviousYear.setColor(Color.RED);

        final BarDataSet expenseDataSetCurrentYear = new BarDataSet(expenseBarValuesCurrentYear,String.valueOf(currentYear)+ " Expenses");
        expenseDataSetCurrentYear.setColor(Color.RED);


        radioButtonPreviousYear.setText(String.valueOf(previousYear));
        radioButtonCurrentYear.setText(String.valueOf(currentYear));

        radioButtonPreviousYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioButtonPreviousYear.isChecked()==true || radioButtonCurrentYear.isChecked()==true)
                    buttonShowBarChart.setEnabled(true);
            }
        });

        radioButtonCurrentYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioButtonPreviousYear.isChecked()==true || radioButtonCurrentYear.isChecked()==true)
                    buttonShowBarChart.setEnabled(true);
            }
        });

        buttonShowBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButtonPreviousYear.isChecked()==true) {
                    showBarChart(barChart,incomeDataSetPreviousYear,expenseDataSetPreviousYear);
                }
                else if(radioButtonCurrentYear.isChecked()==true)
                    showBarChart(barChart,incomeDataSetCurrentYear,expenseDataSetCurrentYear);
            }
        });

        String username = dbHelper.getLoginUsername();

        String query = "SELECT * FROM Table_Income WHERE USERNAME=?";

        Cursor c = db.rawQuery(query, new String[]{username});

        if (c.getCount() == 0) {
           // Toast.makeText(getApplicationContext(), "There are no Incomes!", Toast.LENGTH_SHORT).show();

        } else {
            int typeIndex = c.getColumnIndex("INCOME_TYPE");
            int valueIndex = c.getColumnIndex("VALUE");
            int incomeDateIndex = c.getColumnIndex("INCOMEDATE");

            LinearLayout linearLayoutIncome = findViewById(R.id.linearLayoutIncome);

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
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(500, 175);
                    cardView.setLayoutParams(lp);
                    cardView.setRadius(170f);
                    linearLayoutIncome.addView(cardView);

                    TextView textViewIncomeDate = new TextView(getApplicationContext());
                    textViewIncomeDate.setX(170f);
                    textViewIncomeDate.setY(12f);
                    textViewIncomeDate.setText(cursorForDate.getString(incomeDateIndexForDate));
                    cardView.addView(textViewIncomeDate);

                    TextView textViewType = new TextView(getApplicationContext());
                    textViewType.setX(60f);
                    textViewType.setY(60f);
                    String typeString = "Type: " + cursorForDate.getString(typeIndexForDate);
                    textViewType.setText(typeString);
                    cardView.addView(textViewType);

                    TextView textViewValue = new TextView(getApplicationContext());
                    textViewValue.setX(60f);
                    textViewValue.setY(120f);
                    String valueString = "Value: " + cursorForDate.getString(valueIndexForDate);
                    textViewValue.setText(valueString);
                    cardView.addView(textViewValue);

                    View view = new View(getApplicationContext());
                    ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(400, 10);
                    view.setLayoutParams(vlp);
                    view.setBackgroundColor(Color.BLACK);
                    view.setVisibility(View.INVISIBLE);
                    linearLayoutIncome.addView(view);

                    cursorForDate.moveToNext();
                }

            }
        }
        String expenseQuery = "SELECT * FROM Table_Expense WHERE USERNAME=?";
        Cursor cursorExpense = db.rawQuery(expenseQuery, new String[]{username});
        if (cursorExpense.getCount() == 0) {
            //Toast.makeText(getApplicationContext(), "There are no Expenses!", Toast.LENGTH_SHORT).show();
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
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(500, 175);
                    cardView.setLayoutParams(lp);
                    cardView.setRadius(170f);
                    linearLayoutExpense.addView(cardView);

                    TextView textViewExpenseDate = new TextView(getApplicationContext());
                    textViewExpenseDate.setX(170f);
                    textViewExpenseDate.setY(12f);
                    textViewExpenseDate.setText(cursorForExpenseDate.getString(expenseDateIndexForExpenseDate));
                    cardView.addView(textViewExpenseDate);

                    TextView textViewType = new TextView(getApplicationContext());
                    textViewType.setX(30f);
                    textViewType.setY(60f);
                    String typeString = "Type: " + cursorForExpenseDate.getString(typeIndexForExpenseDate);
                    textViewType.setText(typeString);
                    cardView.addView(textViewType);

                    TextView textViewValue = new TextView(getApplicationContext());
                    textViewValue.setX(40f);
                    textViewValue.setY(120f);
                    String valueString = "Value: " + cursorForExpenseDate.getString(valueIndexForExpenseDate);
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

    }

    public void showBarChart(BarChart barChart,BarDataSet incomeValues,BarDataSet expenseValues)
    {
        barChart = findViewById(R.id.barChart);

        barChart.setMaxVisibleValueCount(30);

        BarData data = new BarData(incomeValues,expenseValues/*,barDataSet3,barDataSet4*/);
        data.setValueTextSize(17);
        barChart.setData(data);

        String[] days = new String[]{"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "ost", "nov", "dec"};

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(3);

        float barSpace = 0.02f;
        float groupSpace = 0.57f;
        data.setBarWidth(0.2f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 13);
        barChart.getAxisLeft().setAxisMinimum(0);

        barChart.groupBars(0, groupSpace, barSpace);

        barChart.invalidate();
        showBarChart2(barChart,incomeValues,expenseValues);

    }

    public void showBarChart2(BarChart barChart,BarDataSet incomeValues,BarDataSet expenseValues)
    {
        barChart = findViewById(R.id.barChart);
        //barChart.setMinimumWidth(1500);
        barChart.setMaxVisibleValueCount(30);

        BarData data = new BarData(incomeValues,expenseValues/*,barDataSet3,barDataSet4*/);
        data.setValueTextSize(17);
        barChart.setData(data);

        String[] days = new String[]{"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "ost", "nov", "dec"};

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(3);

        float barSpace = 0.02f;
        float groupSpace = 0.57f;
        data.setBarWidth(0.2f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 13);
        barChart.getAxisLeft().setAxisMinimum(0);

        barChart.groupBars(0, groupSpace, barSpace);

        barChart.invalidate();

    }
}