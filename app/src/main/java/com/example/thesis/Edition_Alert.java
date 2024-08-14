package com.example.thesis;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.util.Calendar;

public class Edition_Alert extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener{
    Button search;
    DatabaseHelper dbHelper;
    TextView editDate,editSearchDate;
    EditText keyword,editValue;
    //EditText editType;
    CheckBox editRepetitive;
    RadioButton radioButtonIncome,radioButtonExpense;
    SQLiteDatabase db;
    RadioGroup g3;
    int index;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener sDateSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_edition__alert, null);

        dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();

        //keyword = view.findViewById(R.id.keyword);
        //editType = view.findViewById(R.id.edit_type);
        editValue = view.findViewById(R.id.edit_value);
        editDate = view.findViewById(R.id.editDate);
        editSearchDate = view.findViewById(R.id.check_editdate);
        editSearchDate.setEnabled(true);

        radioButtonIncome = view.findViewById(R.id.check_income);
        radioButtonExpense = view.findViewById(R.id.check_expense);
        search = view.findViewById(R.id.searchButton);
        g3 = view.findViewById(R.id.radiogroup3);
        final Spinner spinner1 = view.findViewById(R.id.edit_spinner);
        final Spinner spinner2 = view.findViewById(R.id.new_spinner);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.type_income, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        g3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.check_income)
                {
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.type_income, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter1);
                    //spinner1.setOnItemSelectedListener(getActivity());
                }

                if(checkedId==R.id.check_expense)
                {
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.type_expense, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter1);


                }
            }
        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner1.getSelectedItem().toString().isEmpty() || editSearchDate.getText().toString().isEmpty() || editSearchDate.getText()=="") {
                    Toast.makeText(getContext(), "Enter Type and Date to Be Searched", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (radioButtonIncome.isChecked() == true) {

                        boolean result = dbHelper.searchIncomeType(spinner1.getSelectedItem().toString(), editSearchDate.getText().toString());


                        if (result == true) {
                            Toast.makeText(getContext(), "Find Successful", Toast.LENGTH_SHORT).show();
                            index = spinner1.getSelectedItemPosition();
                            spinner1.setEnabled(false);
                            spinner2.setEnabled(true);
                            editValue.setEnabled(true);
                            editDate.setEnabled(true);
                            search.setEnabled(false);
                            editSearchDate.setEnabled(false);
                            //keyword.setEnabled(false);
                            radioButtonExpense.setEnabled(false);

                            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.type_income, android.R.layout.simple_spinner_item);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(adapter2);

                            String query = "SELECT * FROM Table_Income WHERE INCOME_TYPE=? AND USERNAME=? AND INCOMEDATE=?";
                            Cursor c = db.rawQuery(query, new String[]{spinner1.getSelectedItem().toString(),dbHelper.getLoginUsername(),editSearchDate.getText().toString()});
                            c.moveToFirst();

                            int typeIndex = c.getColumnIndex("INCOME_TYPE");
                            int valueIndex = c.getColumnIndex("VALUE");
                            int dateIndex = c.getColumnIndex("INCOMEDATE");

                            spinner2.setSelection(index);
                            //c.getString(typeIndex));
                            editValue.setText(c.getString(valueIndex));
                            editDate.setText(c.getString(dateIndex));

                        }
                        else
                            Toast.makeText(getContext(), "Not Found.", Toast.LENGTH_SHORT).show();
                    }

                    if (radioButtonExpense.isChecked() == true) {

                        boolean result = dbHelper.searchExpenseType(spinner1.getSelectedItem().toString(),editSearchDate.getText().toString());
                        if (result == true) {
                            Toast.makeText(getContext(), "Found Successfully", Toast.LENGTH_SHORT).show();
                            index = spinner1.getSelectedItemPosition();
                            spinner1.setEnabled(false);
                            spinner2.setEnabled(true);
                            editValue.setEnabled(true);
                            editDate.setEnabled(true);
                            editSearchDate.setEnabled(false);
                            search.setEnabled(false);
                            // keyword.setEnabled(false);
                            radioButtonIncome.setEnabled(false);

                            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.type_expense, android.R.layout.simple_spinner_item);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(adapter2);

                            String query = "SELECT * FROM Table_Expense WHERE EXPENSE_TYPE=? AND USERNAME=?";
                            Cursor c = db.rawQuery(query, new String[]{spinner1.getSelectedItem().toString(),dbHelper.getLoginUsername()});
                            c.moveToFirst();

                            int typeIndex = c.getColumnIndex("EXPENSE_TYPE");
                            int valueIndex = c.getColumnIndex("VALUE");
                            int dateIndex = c.getColumnIndex("EXPENSEDATE");

                            spinner2.setSelection(index);
                            //c.getString(typeIndex));
                            editValue.setText(c.getString(valueIndex));
                            editDate.setText(c.getString(dateIndex));

                        } else
                            Toast.makeText(getContext(), "Not Found.", Toast.LENGTH_SHORT).show();
                    }
                }

            }



        });

        editSearchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity()
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        sDateSetListener, year, month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        sDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;

                String date = dayOfMonth + "/" + month + "/" + year;
                editSearchDate.setText(date);
            }
        };


        editDate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity()
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year, month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;



                String date = dayOfMonth + "/" + month + "/" + year;
                editDate.setText(date);
            }
        };


        builder.setView(view)
                .setTitle("Edit")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Editing is Cancelled.",Toast.LENGTH_SHORT).show();
                    }
                })

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(spinner1.getSelectedItem().toString().isEmpty())
                        {
                            Toast.makeText(getContext(),"Nothing Is Edited", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            if (spinner2.getSelectedItem().toString().isEmpty() || editValue.getText().toString().isEmpty() || editDate.getText().toString().isEmpty()) {
                                Toast.makeText(getContext(), "Please, Fill All the Informations", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                if (radioButtonIncome.isChecked() == true) {
                                    Income income = new Income();
                                    income.type = spinner2.getSelectedItem().toString();
                                    income.value = Double.parseDouble(editValue.getText().toString());
                                    income.incomeDate = editDate.getText().toString();

                                    dbHelper.updateIncomeType(income, spinner1.getSelectedItem().toString(),editSearchDate.getText().toString());
                                    Toast.makeText(getContext(), "Editing Income is Successful", Toast.LENGTH_SHORT).show();
                                }
                                if (radioButtonExpense.isChecked() == true) {
                                    Expense expense = new Expense();
                                    expense.type = spinner2.getSelectedItem().toString();
                                    expense.value = Double.parseDouble(editValue.getText().toString());
                                    expense.expenseDate = editDate.getText().toString();

                                    dbHelper.updateExpenseType(expense, spinner1.getSelectedItem().toString(),editSearchDate.getText().toString());
                                    Toast.makeText(getContext(), "Editing Expense is Successful", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                    }
                });

        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}