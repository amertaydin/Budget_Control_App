package com.example.thesis;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class Assessment extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_assessment);

        PieChart pieChartForecast = findViewById(R.id.pieChartForecast);

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Double> incomeValues = new ArrayList<>(); // asıl
        ArrayList<Double> movingAverage = new ArrayList<>();// ma
        ArrayList<Double> centeredMovingAverage = new ArrayList<>(); // cma
        ArrayList<Double> seasonalityAndIrregularity = new ArrayList<>(); // asıl/cma
        double[] seasonality = new double[12]; // St
        ArrayList<Double> deseasonalize = new ArrayList<>(); // deaseasonalize
        ArrayList<Double> trendTime = new ArrayList<>(); // Tt
        ArrayList<Double> forecast = new ArrayList<>(); // forecast

        ArrayList<Double> expenseValues = new ArrayList<>(); // asıl
        ArrayList<Double> emovingAverage = new ArrayList<>();// ma
        ArrayList<Double> ecenteredMovingAverage = new ArrayList<>(); // cma
        ArrayList<Double> eseasonalityAndIrregularity = new ArrayList<>(); // asıl/cma
        double[] eseasonality = new double[12]; // St
        ArrayList<Double> edeseasonalize = new ArrayList<>(); // deaseasonalize
        ArrayList<Double> etrendTime = new ArrayList<>(); // Tt
        ArrayList<Double> eforecast = new ArrayList<>(); // forecast


        double[] yearValues = new double[12];
        double[] eyearValues = new double[12];
        for (int i = 0; i < 12; i++) {
            yearValues[i] = 0;
            eyearValues[i] = 0;
        }


        String incomeQ = "SELECT * FROM Table_Income WHERE USERNAME=?";
        Cursor cursorIncome = db.rawQuery(incomeQ, new String[]{dbHelper.getLoginUsername()});

        int incomeDateIndex = cursorIncome.getColumnIndex("INCOMEDATE");
        int incomeValueIndex = cursorIncome.getColumnIndex("VALUE");

        cursorIncome.moveToFirst();
        if (cursorIncome.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No Income Data!", Toast.LENGTH_SHORT).show();
        } else {
            int currentYear = 0, firstMonth = 0;
            String date = cursorIncome.getString(incomeDateIndex);
            String[] dateArray = date.split("/");
            currentYear = Integer.valueOf(dateArray[2]);
            firstMonth = Integer.valueOf(dateArray[1]);
            cursorIncome.moveToFirst();
            for (int i = 0; i < cursorIncome.getCount(); i++)// income ları çekiyor
            {
                String incomeDate = cursorIncome.getString(incomeDateIndex);
                String[] incomeDateArray = incomeDate.split("/");
                if (Integer.valueOf(incomeDateArray[2]) - currentYear == 0) {
                    int month = Integer.valueOf(incomeDateArray[1]);
                    int value = Integer.valueOf(cursorIncome.getString(incomeValueIndex));
                    yearValues[month - 1] += value;
                    cursorIncome.moveToNext();
                } else {
                    for (int j = 0; j < 12; j++) {
                        incomeValues.add(yearValues[j]);
                        yearValues[j] = 0;
                    }
                    currentYear = Integer.valueOf(incomeDateArray[2]);
                    int month = Integer.valueOf(incomeDateArray[1]);
                    int value = Integer.valueOf(cursorIncome.getString(incomeValueIndex));
                    yearValues[month - 1] += value;
                    cursorIncome.moveToNext();


                }
            }
            for (int x = 0; x < 12; x++) {
                incomeValues.add(yearValues[x]);
            }
            /*for(int i=0;i<incomeValues.size();i++)
            {
                Log.i("income values",String.valueOf(incomeValues.get(i)));
            }*/
            cursorIncome.close();
        }

        String expenseQ = "SELECT * FROM Table_Expense WHERE USERNAME=?";
        Cursor cursorExpense = db.rawQuery(expenseQ, new String[]{dbHelper.getLoginUsername()});

        int expenseDateIndex = cursorExpense.getColumnIndex("EXPENSEDATE");
        int expenseValueIndex = cursorExpense.getColumnIndex("VALUE");

        cursorExpense.moveToFirst();
        if (cursorExpense.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No Expense Data!", Toast.LENGTH_SHORT).show();

        } else {
            int ecurrentYear = 0, efirstMonth = 0;
            String edate = cursorExpense.getString(expenseDateIndex);
            String[] edateArray = edate.split("/");
            ecurrentYear = Integer.valueOf(edateArray[2]);
            efirstMonth = Integer.valueOf(edateArray[1]);
            cursorExpense.moveToFirst();
            for (int i = 0; i < cursorExpense.getCount(); i++)// expense leri çekiyor
            {
                String expenseDate = cursorExpense.getString(expenseDateIndex);
                String[] expenseDateArray = expenseDate.split("/");
                if (Integer.valueOf(expenseDateArray[2]) - ecurrentYear == 0) {
                    int month = Integer.valueOf(expenseDateArray[1]);
                    int value = Integer.valueOf(cursorExpense.getString(expenseValueIndex));
                    eyearValues[month - 1] += value;
                    cursorExpense.moveToNext();
                } else {
                    for (int j = 0; j < 12; j++) {
                        expenseValues.add(eyearValues[j]);
                        eyearValues[j] = 0;
                    }
                    ecurrentYear = Integer.valueOf(expenseDateArray[2]);
                    int month = Integer.valueOf(expenseDateArray[1]);
                    int value = Integer.valueOf(cursorExpense.getString(expenseValueIndex));
                    eyearValues[month - 1] += value;
                    cursorExpense.moveToNext();


                }
            }
            for (int x = 0; x < 12; x++) {
                expenseValues.add(eyearValues[x]);
            }

        }

        for (int i = 0; i <= incomeValues.size() - 12; i++)// ma yı hesaplıyor
        {
            double total = 0;
            for (int j = 0; j < 12; j++) {
                total += incomeValues.get(i + j);
            }
            total /= 12;
            movingAverage.add(total);
        }

        for (int i = 0; i <= expenseValues.size() - 12; i++) // ma expense
        {
            double total = 0;
            for (int j = 0; j < 12; j++) {
                total += expenseValues.get(i + j);
            }
            total /= 12;
            emovingAverage.add(total);
        }

            /*for(int i=0;i<movingAverage.size();i++)
            {
                Log.i("edcrfv",String.valueOf(movingAverage.get(i)));
            }*/


        for (int i = 0; i < movingAverage.size() - 1; i++)// cma hesaplıyor
        {
            double total = 0;
            for (int j = 0; j < 2; j++) {
                total += movingAverage.get(i + j);
            }
            total /= 2;
            centeredMovingAverage.add(total);
        }

        for (int i = 0; i < emovingAverage.size() - 1; i++)// cma expense
        {
            double total = 0;
            for (int j = 0; j < 2; j++) {
                total += emovingAverage.get(i + j);
            }
            total /= 2;
            ecenteredMovingAverage.add(total);
        }

            /*for(int i=0;i<centeredMovingAverage.size();i++)
            {
                Log.i("tgbyhn",String.valueOf(centeredMovingAverage.get(i)));
            }*/

        for (int i = 12; i < incomeValues.size(); i++) {
            double result = incomeValues.get(i) / centeredMovingAverage.get(i - 12);
            seasonalityAndIrregularity.add(result);
        }

        for (int i = 12; i < expenseValues.size(); i++)// expense
        {
            double result = expenseValues.get(i) / ecenteredMovingAverage.get(i - 12);
            eseasonalityAndIrregularity.add(result);
        }

            /*for(int i=0;i<seasonalityAndIrregularity.size();i++)
            {
                Log.i("azsxdc",String.valueOf(seasonalityAndIrregularity.get(i)));
            }*/


        for (int i = 0; i < seasonality.length; i++) {
            int count = 0;
            double total = 0;
            for (int j = 0; j < seasonalityAndIrregularity.size(); j += 12) {
                total += seasonalityAndIrregularity.get(i + j);
                count++;
            }
            total /= count;
            seasonality[i] = total;
        }

        for (int i = 0; i < eseasonality.length; i++) // expense
        {
            int count = 0;
            double total = 0;
            for (int j = 0; j < eseasonalityAndIrregularity.size(); j += 12) {
                total += eseasonalityAndIrregularity.get(i + j);
                count++;
            }
            total /= count;
            eseasonality[i] = total;
        }

            /*for(int i=0;i<12;i++)
            {
                Log.i("zzxxcc",String.valueOf(seasonality[i]));
            }*/

        for (int i = 0; i < incomeValues.size(); i++) {
            double result = incomeValues.get(i) / seasonality[i % 12];
            deseasonalize.add(result);
        }

        for (int i = 0; i < expenseValues.size(); i++) // expense
        {
            double result = expenseValues.get(i) / eseasonality[i % 12];
            edeseasonalize.add(result);
        }

            /*for(int i=0;i<deseasonalize.size();i++)
            {
                Log.i("Kylo Ren",String.valueOf(deseasonalize.get(i)));
            }*/

        int deseasonSize = deseasonalize.size();
        int timeSize = incomeValues.size();

        double[] regressionX = new double[timeSize];
        double[] regressionY = new double[deseasonSize];

        int edeseasonSize = edeseasonalize.size();
        int etimeSize = expenseValues.size();

        double[] eregressionX = new double[etimeSize];
        double[] eregressionY = new double[edeseasonSize];

        for (int i = 0; i < deseasonalize.size(); i++) {
            regressionX[i] = i + 1;
            regressionY[i] = deseasonalize.get(i);
        }

        for (int i = 0; i < edeseasonalize.size(); i++) // expense
        {
            eregressionX[i] = i + 1;
            eregressionY[i] = edeseasonalize.get(i);
        }

        LinearRegression l = new LinearRegression(regressionX, regressionY);
        double intercept = l.intercept();
        double slope = l.slope();

        LinearRegression el = new LinearRegression(eregressionX, eregressionY);
        double eintercept = el.intercept();
        double eslope = el.slope();

        //Log.i("razor","intercept:" + Double.toString(intercept));
        //Log.i("razor","slope:" + Double.toString(slope));

        for (int i = 0; i < deseasonalize.size(); i++) {
            double result = intercept + slope * regressionX[i];
            trendTime.add(result);
        }

        for (int i = 0; i < edeseasonalize.size(); i++) // expense
        {
            double result = eintercept + eslope * eregressionX[i];
            etrendTime.add(result);
        }

            /*for(int i=0;i<trendTime.size();i++)
            {
                Log.i("mia", String.valueOf(trendTime.get(i)));
            }*/

        for (int i = 0; i < trendTime.size(); i++) {
            double result = trendTime.get(i) * seasonality[i % 12];
            forecast.add(result);
        }

        for (int i = 0; i < etrendTime.size(); i++) // expense
        {
            double result = etrendTime.get(i) * eseasonality[i % 12];
            eforecast.add(result);
        }

        for (int i = 0; i < forecast.size(); i++) {
            Log.i("ghj", String.valueOf(forecast.get(i)));
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        //int nextMonthTime = regressionX.length +1;
        //double nextMonthTrendTimeIncome = intercept + slope * nextMonthTime;
        //double incomeForecastNextMonth = nextMonthTrendTimeIncome * seasonality[month%12];
        //double expenseForecastNextMonth
        int timeValue = regressionX.length+1;
        ArrayList<Double> incomeForecast = new ArrayList<>();
        ArrayList<Double> expenseForecast = new ArrayList<>();
        for(int i=month;i<12;i++)
        {
            double incomeTrendTimeValue = intercept + slope * timeValue;
            double expenseTrendTimeValue = eintercept + eslope * timeValue;
            timeValue++;
            double incomeResult = incomeTrendTimeValue * seasonality[month%12];
            double expenseResult = expenseTrendTimeValue * eseasonality[month%12];

            incomeForecast.add(incomeResult);
            expenseForecast.add(expenseResult);
        }
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        TextView forecastDate = findViewById(R.id.forecastDate);
        forecastDate.setText(monthName[month] + " " + year);



        ArrayList<PieEntry> values = new ArrayList<>();
        double incomeExpected = incomeForecast.get(0);
        double expenseExpected = expenseForecast.get(0);
        values.add(new PieEntry(Float.valueOf(100), "Incomes"));
        values.add(new PieEntry(Float.valueOf(200), "Expenses"));

        TextView expectedBalanceValue = findViewById(R.id.expectedBalanceValue);
        TextView expectedWarning = findViewById(R.id.forecastWarning);
        double balance = incomeExpected - expenseExpected;
        expectedBalanceValue.setText(String.valueOf(balance));
        if (balance < 0) {
            expectedWarning.setText("You will be in debt!");
            expectedWarning.setTextColor(Color.RED);
        } else {
            expectedWarning.setEnabled(false);
        }

        PieDataSet dataSet = new PieDataSet(values, "Expected Status");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        //dataSet.setDrawValues(false);
        //dataSet.

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(20f);

        pieData.setValueTextColor(Color.WHITE);

        pieChartForecast.getDescription().setEnabled(false);
        pieChartForecast.setDrawMarkers(false);
        //pieChartForecast.setDrawCenterText(false);
        pieChartForecast.setDrawEntryLabels(false);
        //pieChartForecast.setDrawCenterText(false);
        //pieChartForecast.
        pieChartForecast.setData(pieData);

        LinearLayout linearLayoutExpectedIncome = findViewById(R.id.linearLayoutExpectedIncome);
        LinearLayout linearLayoutExpectedExpense = findViewById(R.id.linearLayoutExpectedExpense);

        for(int i=1;i<incomeForecast.size();i++)
        {
            CardView cardView = new CardView(getApplicationContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(500, 175);
            cardView.setLayoutParams(lp);
            cardView.setRadius(170f);
            linearLayoutExpectedIncome.addView(cardView);

            month++;
            TextView textViewDate = new TextView(getApplicationContext());
            textViewDate.setX(170f);
            textViewDate.setY(12f);
            textViewDate.setText(monthName[month] + " " + year);
            cardView.addView(textViewDate);

            TextView textViewValue = new TextView(getApplicationContext());
            textViewValue.setX(220f);
            textViewValue.setY(100f);
            textViewValue.setText(String.valueOf(incomeForecast.get(i)));
            cardView.addView(textViewValue);

            View view = new View(getApplicationContext());
            ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(400, 10);
            view.setLayoutParams(vlp);
            view.setBackgroundColor(Color.BLACK);
            view.setVisibility(View.INVISIBLE);
            linearLayoutExpectedIncome.addView(view);
        }
        int imonth = calendar.get(Calendar.MONTH);
        for(int i=1;i<expenseForecast.size();i++)
        {
            CardView cardView = new CardView(getApplicationContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(500, 175);
            cardView.setLayoutParams(lp);
            cardView.setRadius(170f);
            linearLayoutExpectedExpense.addView(cardView);

            imonth++;
            TextView textViewDate = new TextView(getApplicationContext());
            textViewDate.setX(170f);
            textViewDate.setY(12f);
            textViewDate.setText(monthName[imonth] + " " + year);
            cardView.addView(textViewDate);

            TextView textViewValue = new TextView(getApplicationContext());
            textViewValue.setX(220f);
            textViewValue.setY(100f);
            textViewValue.setText(String.valueOf(expenseForecast.get(i)));
            cardView.addView(textViewValue);

            View view = new View(getApplicationContext());
            ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(400, 10);
            view.setLayoutParams(vlp);
            view.setBackgroundColor(Color.BLACK);
            view.setVisibility(View.INVISIBLE);
            linearLayoutExpectedExpense.addView(view);


        }

    }

}

