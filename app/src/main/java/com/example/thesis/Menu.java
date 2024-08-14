package com.example.thesis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Calendar;

public class Menu extends AppCompatActivity {
    CardView cardViewIncome, cardViewExpenses, cardViewManage, cardViewStatus;
    PieChart pieChartGeneral;
    DatabaseHelper dbHelper;
    TextView currentDate,totalIncomeCurrent,totalExpenseCurrent,balanceValue,warning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        currentDate = findViewById(R.id.currentDate);
        totalIncomeCurrent = findViewById(R.id.totalIncomeCurrentMonth);
        totalExpenseCurrent = findViewById(R.id.totalExpenseCurrentMonth);
        balanceValue = findViewById(R.id.balanceValue);
        warning = findViewById(R.id.warning);

        String[] monthName = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};

        Calendar calendar = Calendar.getInstance();
        String month = monthName[calendar.get(Calendar.MONTH)];
        int year = calendar.get(Calendar.YEAR);
        int monthNumeric = calendar.get(Calendar.MONTH);
        monthNumeric += 1;
        String monthYear = month + " " + String.valueOf(year);
        currentDate.setText(monthYear);


        cardViewIncome = findViewById(R.id.CardViewIncome);
        cardViewIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, ActivityIncome.class);
                startActivity(intent);
            }
        });

        cardViewExpenses = findViewById(R.id.CardViewExpenses);
        cardViewExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, ActivityExpense.class);
                startActivity(intent);
            }
        });

        cardViewManage = findViewById(R.id.CardViewManage);
        cardViewManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Manage.class);
                startActivity(intent);
            }
        });
        cardViewStatus = findViewById(R.id.CardViewStatus);
        cardViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Status.class);
                startActivity(intent);
            }
        });

        pieChartGeneral = findViewById(R.id.pieChartGeneral);
        pieChartGeneral.setUsePercentValues(true);
        pieChartGeneral.getDescription().setEnabled(false);
        pieChartGeneral.setExtraOffsets(5, 10, 5, 5);

        pieChartGeneral.setDragDecelerationFrictionCoef(0.95f);
        pieChartGeneral.setDrawHoleEnabled(true);
        pieChartGeneral.setHoleColor(Color.WHITE);
        pieChartGeneral.setTransparentCircleRadius(61f);

        dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        double totalIncome = 0.0, totalExpense = 0.0;

        String queryIncome = "SELECT * FROM Table_Income WHERE USERNAME=?";
        Cursor cursorIncome = db.rawQuery(queryIncome, new String[]{dbHelper.getLoginUsername()});
        if (cursorIncome.getCount() == 0) {
            totalIncome = 0;
            totalIncomeCurrent.setText("0");
        } else {
            int valueIndex = cursorIncome.getColumnIndex("VALUE");
            int incomeDateIndex = cursorIncome.getColumnIndex("INCOMEDATE");
            cursorIncome.moveToFirst();
            for (int i = 0; i < cursorIncome.getCount(); i++) {
                String date = cursorIncome.getString(incomeDateIndex);
                String[] dateArray = new String[3];
                if (date.contains(".")) {
                    dateArray = date.split("\\.");
                } else if (date.contains("/"))
                    dateArray = date.split("/");
                int monthValueOfEntry = Integer.valueOf(dateArray[1]);
                int yearValueOfEntry = Integer.valueOf(dateArray[2]);

                if (monthValueOfEntry == monthNumeric && year == yearValueOfEntry) {
                    totalIncome += Double.parseDouble(cursorIncome.getString(valueIndex));
                    cursorIncome.moveToNext();
                } else
                    cursorIncome.moveToNext();

            }
        }
        totalIncomeCurrent.setText(String.valueOf(totalIncome));
        String queryExpense = "SELECT * FROM Table_Expense WHERE USERNAME=?";
        Cursor cursorExpense = db.rawQuery(queryExpense, new String[]{dbHelper.getLoginUsername()});
        if (cursorExpense.getCount() == 0) {

            totalExpense = 0;
            totalExpenseCurrent.setText("0");
        } else {
            int valueIndex = cursorExpense.getColumnIndex("VALUE");
            int dueDateIndex = cursorExpense.getColumnIndex("EXPENSEDATE");
            cursorExpense.moveToFirst();
            for (int i = 0; i < cursorExpense.getCount(); i++) {
                String date = cursorExpense.getString(dueDateIndex);
                String[] dateArray = new String[3];
                if (date.contains(".")) {
                    dateArray = date.split("\\.");
                } else if (date.contains("/"))
                    dateArray = date.split("/");

                int monthValueOfEntry = Integer.valueOf(dateArray[1]);
                int yearValueOfEntry = Integer.valueOf(dateArray[2]);
                if (monthValueOfEntry == monthNumeric && year == yearValueOfEntry) {
                    totalExpense += Double.parseDouble(cursorExpense.getString(valueIndex));
                    cursorExpense.moveToNext();
                } else
                    cursorExpense.moveToNext();


            }
        }
        totalExpenseCurrent.setText(String.valueOf(totalExpense));
        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(Float.valueOf(String.valueOf(totalIncome)), "Incomes"));
        values.add(new PieEntry(Float.valueOf(String.valueOf(totalExpense)), "Expenses"));

        double balance = totalIncome - totalExpense;
        balanceValue.setText(String.valueOf(balance));
        if (balance < 0) {
            warning.setText("You are in debt!");
            warning.setTextColor(Color.RED);
        } else {
            warning.setEnabled(false);
        }

        PieDataSet dataSet = new PieDataSet(values, "Status");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);

        pieChartGeneral.setData(pieData);
     } catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finishAffinity();
                        Intent intent = new Intent(Menu.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }}
