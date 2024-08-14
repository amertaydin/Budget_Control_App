package com.example.thesis;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;

public class Deletion_Alert extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    // private EditText editTextDeleteType;
    DatabaseHelper dbhelper;
    RadioButton radioIncomeButton;
    RadioButton radioExpenseButton;
    RadioGroup g2;
    TextView deleteSearch;
    private DatePickerDialog.OnDateSetListener dDateSetListener;
    Calendar calendar = Calendar.getInstance();
    int month = calendar.get(Calendar.MONTH) +1;

    int year = calendar.get(Calendar.YEAR) ;
    int day = calendar.get(Calendar.DAY_OF_MONTH) +1;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_deletion__alert, null);

        Log.i("calendar", String.valueOf(day) + String.valueOf(month) + String.valueOf(year));


        final Spinner spinner1 = view.findViewById(R.id.delete_spinner);
        //editTextDeleteType = view.findViewById(R.id.delete_type);
        radioExpenseButton = view.findViewById(R.id.delete_expense);
        radioIncomeButton = view.findViewById(R.id.delete_income);
        deleteSearch = view.findViewById(R.id.delete_search);

        deleteSearch.setText(String.valueOf(day)+ "/" + month + "/" + year);

        g2 = view.findViewById(R.id.radiogroup1);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.type_income, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        g2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.delete_income)
                {
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.type_income, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter1);
                    //spinner1.setOnItemSelectedListener(getActivity());
                }

                if(checkedId==R.id.delete_expense)
                {
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.type_expense, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter1);


                }
            }
        });

        deleteSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity()
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dDateSetListener, year, month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;

                String date = dayOfMonth + "/" + month + "/" + year;
                deleteSearch.setText(date);
            }
        };

        builder.setView(view)
                .setTitle("Delete")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(getContext(),"Deleting is Cancelled",Toast.LENGTH_SHORT).show();
                    }
                })

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        dbhelper = new DatabaseHelper(getContext());
                        String deleter = spinner1.getSelectedItem().toString();

                        if(deleter.isEmpty())
                        {
                            Toast.makeText(getContext(), "Nothing is Deleted", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            if (radioIncomeButton.isChecked()) {
                                boolean result = dbhelper.deleteIncome(deleter, deleteSearch.getText().toString());
                                if(result==true)
                                    Toast.makeText(getContext(), "Deleting Income is Successful", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getContext(),"There is no income named "+ deleter + " at specified date",Toast.LENGTH_SHORT).show();
                            }

                            if (radioExpenseButton.isChecked()) {
                                boolean result = dbhelper.deleteExpense(deleter, deleteSearch.getText().toString());
                                if(result==true)
                                    Toast.makeText(getContext(), "Deleting Expense is Successful", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getContext(),"There is no expense named "+ deleter + " at specified date",Toast.LENGTH_SHORT).show();
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


