package com.example.thesis;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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



public class Addition_Alert extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    DatabaseHelper dbhelper;

    //private EditText editTextType;
    private EditText editTextValue;
    //private EditText editTextDate;

    private TextView editTextDate;
    private RadioGroup g1;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private RadioButton radioIncome;
    private RadioButton radioExpense;
    private CheckBox checkBox;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)  {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_addition__alert,null);

        final Spinner spinner1 = view.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.type_income, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        g1 = view.findViewById(R.id.radiogroup);

        g1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        //editTextType = view.findViewById(R.id.edit_type);
        editTextValue = view.findViewById(R.id.edit_value);
        editTextDate = view.findViewById(R.id.edit_date);

        radioIncome = view.findViewById(R.id.check_income);
        radioExpense = view.findViewById(R.id.check_expense);


        editTextDate.setOnClickListener(new View.OnClickListener() {


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
                editTextDate.setText(date);
            }
        };

    builder.setView(view)
            .setTitle("Add")
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    Toast.makeText(getContext(), "Adding is Cancelled", Toast.LENGTH_SHORT).show();
                }
            })

            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {


                    if(editTextDate.getText().toString().isEmpty() || spinner1.getSelectedItem().toString().isEmpty() || editTextValue.getText().toString().isEmpty() )
                    {
                        Toast.makeText(getContext(),"Please, Fill All the Informations", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        dbhelper = new DatabaseHelper(getContext());

                        if (radioIncome.isChecked()) {
                            Income income = new Income();
                            income.type = spinner1.getSelectedItem().toString();
                            income.value = Double.parseDouble(editTextValue.getText().toString());
                            income.incomeDate = editTextDate.getText().toString();




                            dbhelper.insertIncome(income);
                            Toast.makeText(getContext(), "Adding Income is Successful", Toast.LENGTH_SHORT).show();
                            double total = dbhelper.calculateTotalIncome();

                        }
                        if (radioExpense.isChecked()) {
                            Expense expense = new Expense();
                            expense.type = spinner1.getSelectedItem().toString();
                            expense.value = Double.parseDouble(editTextValue.getText().toString());
                            expense.expenseDate = editTextDate.getText().toString();

                            dbhelper.insertExpense(expense);
                            Toast.makeText(getContext(), "Adding Expense is Successful", Toast.LENGTH_SHORT).show();

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
