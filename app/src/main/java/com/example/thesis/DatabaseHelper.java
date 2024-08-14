package com.example.thesis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "UserRegistration14.db";
    public static final String REGISTER_TABLE = "Table_Register";
    public static final String COLUMN_1 = "ID";
    public static final String COLUMN_2 = "NAME";
    public static final String COLUMN_3 = "SURNAME";
    public static final String COLUMN_4 = "USERNAME";
    public static final String COLUMN_5 = "MAIL";
    public static final String COLUMN_6 = "PASSWORD";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS "+ REGISTER_TABLE + " (" + COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_2 + " VARCHAR," +
                COLUMN_3 + " VARCHAR," +
                COLUMN_4 + " VARCHAR," +
                COLUMN_5 + " VARCHAR," +
                COLUMN_6 + " VARCHAR)";
        db.execSQL(query);

        String queryLoginTable = "CREATE TABLE IF NOT EXISTS login_table (USERNAME VARCHAR,PASSWORD VARCHAR)";
        db.execSQL(queryLoginTable);

        String queryIncomeTable = "CREATE TABLE IF NOT EXISTS Table_Income (USERNAME VARCHAR,INCOME_TYPE VARCHAR,VALUE DOUBLE,INCOMEDATE VARCHAR)";
        db.execSQL(queryIncomeTable);

        String queryExpenseTable = "CREATE TABLE IF NOT EXISTS Table_Expense (USERNAME VARCHAR,EXPENSE_TYPE VARCHAR,VALUE DOUBLE,EXPENSEDATE VARCHAR)";
        db.execSQL(queryExpenseTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onOpen(SQLiteDatabase db)
    {

    }
    public void insertData(String name, String surname,String username, String mail, String password)
    {
        try {
            SQLiteDatabase myDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NAME", name);
            values.put("SURNAME", surname);
            values.put("USERNAME", username);
            values.put("MAIL", mail);
            values.put("PASSWORD", password);
            myDatabase.insert(REGISTER_TABLE, null, values);
        }
        catch (Exception e)
        {
            Log.i("checking", e.getStackTrace().toString());
        }

    }

    public boolean checkLoginStatus(String username, String password)
    {
        if(username == "" || password == "")
            return false;
        if ( username == null || password == null)
            return false;

        String registeredUsername = new String();
        String registeredPassword = new String();

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + REGISTER_TABLE + " WHERE USERNAME = ? ";
        Cursor c = db.rawQuery(query, new String[] {username});

        if (c.getCount()!=0)
        {
            c.moveToFirst();
            int usernameIndex = c.getColumnIndex(COLUMN_4);
            registeredUsername = c.getString(usernameIndex);
            int passwordIndex = c.getColumnIndex(COLUMN_6);
            registeredPassword = c.getString(passwordIndex);

        }

        if (registeredUsername.equals(username) && registeredPassword.equals(password))
            return true;
        else
            return false;
    }

    public void insertLoginTable(String username,String password)
    {

        SQLiteDatabase db = getWritableDatabase();
        String insertQuery = "INSERT INTO login_table (USERNAME,PASSWORD) VALUES (?,?)";
        db.execSQL(insertQuery,new String[] {username,password});


    }

    public void deleteAllLoginTable()
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM login_table";
        db.execSQL(query);
    }

    public String getLoginUsername()
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM login_table";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        int usernameIndex = c.getColumnIndex("USERNAME");
        String username = c.getString(usernameIndex);
        return username;
    }

    public void insertIncome(Income income)
    {
        SQLiteDatabase db = getWritableDatabase();
        String username = getLoginUsername();

        income.incomeDate = income.incomeDate.replace('.','/');

        String x = income.incomeDate;
        String result = new String();

        if (x.length()==10 && x.charAt(0)=='0' && x.charAt(3)=='0' )
        {
            StringBuilder sb = new StringBuilder(x);
            sb.deleteCharAt(3);
            sb.deleteCharAt(0);
            result = sb.toString();

        }
        else if (x.length()==10 && x.charAt(0)!='0' && x.charAt(3)=='0')
        {
            StringBuilder sb = new StringBuilder(x);
            sb.deleteCharAt(3);
            result = sb.toString();
        }
        else if (x.length()==10 && x.charAt(0)=='0' && x.charAt(3)!='0')
        {
            StringBuilder sb = new StringBuilder(x);
            sb.deleteCharAt(0);
            result = sb.toString();
        }
        else
        {
            StringBuilder sb = new StringBuilder(x);
            result = sb.toString();
        }


        ContentValues content = new ContentValues();
        content.put("USERNAME",username);
        content.put("INCOME_TYPE",income.type);
        content.put("VALUE",income.value);
        content.put("INCOMEDATE",result);
        db.insert("Table_Income",null,content);
    }

    public double calculateTotalIncome()
    {
        SQLiteDatabase db = getWritableDatabase();
        String username = getLoginUsername();
        double total = 0.0;
        String query = "SELECT * FROM Table_Income WHERE USERNAME=?";
        Cursor c = db.rawQuery(query,new String[] {username});
        if (c.getCount()==0)
            return 0;
        int valueIndex = c.getColumnIndex("VALUE");
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++)
        {
            total += Double.parseDouble(c.getString(valueIndex));
            c.moveToNext();
        }
        return total;
    }

    public boolean deleteIncome(String type, String date)
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM Table_Income WHERE USERNAME=? AND INCOME_TYPE=? AND INCOMEDATE=?";
        Cursor c = db.rawQuery(query,new String[] {getLoginUsername(),type,date});
        if(c.getCount()==0)
            return false;
        db.delete("Table_Income","INCOME_TYPE=? AND USERNAME=? AND INCOMEDATE=?",new String[] {type,getLoginUsername(),date});
        return true;

    }

    public boolean searchIncomeType(String type, String date)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Table_Income WHERE USERNAME=? AND INCOMEDATE=?",new String[]{getLoginUsername(),date});
        c.moveToFirst();
        int typeIndex = c.getColumnIndex("INCOME_TYPE");
        int dateIndex = c.getColumnIndex("INCOMEDATE");
        for(int i=0;i<c.getCount();i++)
        {
            String t = c.getString(typeIndex);
            String d = c.getString(dateIndex);
            if (t.equals(type) && d.equals(date))
                return true;
            else
                c.moveToNext();
        }
        return false;
    }

    public void updateIncomeType(Income i,String type,String date)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USERNAME",getLoginUsername());
        values.put("INCOME_TYPE",i.type);
        values.put("VALUE",i.value);
        values.put("INCOMEDATE",i.incomeDate);
        db.update("Table_Income",values ,"USERNAME=? AND INCOME_TYPE=? AND INCOMEDATE=?",new String[] {getLoginUsername(),type,date});
    }


    public void insertExpense(Expense expense)
    {
        SQLiteDatabase db = getWritableDatabase();
        String username = getLoginUsername();

        expense.expenseDate = expense.expenseDate.replace('.','/');

        String x = expense.expenseDate;
        String result = new String();

        if (x.length()==10 && x.charAt(0)=='0' && x.charAt(3)=='0' )
        {
            StringBuilder sb = new StringBuilder(x);
            sb.deleteCharAt(3);
            sb.deleteCharAt(0);
            result = sb.toString();

        }
        else if (x.length()==10 && x.charAt(0)!='0' && x.charAt(3)=='0')
        {
            StringBuilder sb = new StringBuilder(x);
            sb.deleteCharAt(3);
            result = sb.toString();
        }
        else if (x.length()==10 && x.charAt(0)=='0' && x.charAt(3)!='0')
        {
            StringBuilder sb = new StringBuilder(x);
            sb.deleteCharAt(0);
            result = sb.toString();
        }
        else
        {
            StringBuilder sb = new StringBuilder(x);
            result = sb.toString();
        }

        ContentValues content = new ContentValues();
        content.put("USERNAME",username);
        content.put("EXPENSE_TYPE", expense.type);
        content.put("VALUE", expense.value);
        content.put("EXPENSEDATE", result);
        db.insert("Table_Expense",null,content);
    }

    public double calculateTotalExpense()
    {
        SQLiteDatabase db = getWritableDatabase();
        String username = getLoginUsername();
        double total = 0.0;
        String query = "SELECT * FROM Table_Expense WHERE USERNAME=?";
        Cursor c = db.rawQuery(query, new String[] {username});
        if (c.getCount()==0)
            return 0;
        int valueIndex = c.getColumnIndex("VALUE");
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++)
        {
            total += Double.parseDouble(c.getString(valueIndex));
            c.moveToNext();
        }
        return total;
    }

    public boolean deleteExpense(String type, String date)
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM Table_Expense WHERE USERNAME=? AND EXPENSE_TYPE=? AND EXPENSEDATE=?";
        Cursor c = db.rawQuery(query,new String[]{getLoginUsername(),type,date});
        if(c.getCount()==0)
            return false;
        db.delete("Table_Expense","USERNAME=? AND EXPENSE_TYPE=? AND EXPENSEDATE=?",new String[] {getLoginUsername(),type,date});
        return true;
    }

    public boolean searchExpenseType(String type,String date)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Table_Expense WHERE USERNAME=? AND EXPENSEDATE=?",new String[]{getLoginUsername(),date});
        c.moveToFirst();
        int typeIndex = c.getColumnIndex("EXPENSE_TYPE");
        int dateIndex = c.getColumnIndex("EXPENSEDATE");
        for(int i=0;i<c.getCount();i++)
        {
            String t = c.getString(typeIndex);
            String d = c.getString(dateIndex);

            if (t.equals(type) && d.equals(date))
                return true;
            else
                c.moveToNext();
        }
        return false;
    }

    public void updateExpenseType(Expense e,String type,String date)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USERNAME",getLoginUsername());
        values.put("EXPENSE_TYPE",e.type);
        values.put("VALUE",e.value);
        values.put("EXPENSEDATE",e.expenseDate);
        db.update("Table_Expense",values ,"USERNAME =? AND EXPENSE_TYPE=? AND EXPENSEDATE=?",new String[] {getLoginUsername(),type,date});
    }
}



