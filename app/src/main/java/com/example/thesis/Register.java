package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    DatabaseHelper dbHelper;
    CardView cardView;
    SQLiteDatabase db;
    EditText registerName,registerSurname,registerUsername,registerMail,registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new DatabaseHelper(this);
        registerName = findViewById(R.id.NameRegister);
        registerSurname = findViewById(R.id.SurnameRegister);
        registerUsername = findViewById(R.id.UsernameRegister);
        registerMail = findViewById(R.id.MailRegister);
        registerPassword =findViewById(R.id.PasswordRegister);
        cardView = (CardView)findViewById(R.id.cardview);

        db = dbHelper.getWritableDatabase();


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registerName.getText().toString();
                String surname = registerSurname.getText().toString();
                String username = registerUsername.getText().toString();
                String mail = registerMail.getText().toString();
                String password = registerPassword.getText().toString();

                if(false && registerName.getText().toString().isEmpty() || registerSurname.getText().toString().isEmpty() || registerUsername.getText().toString().isEmpty() || registerMail.getText().toString().isEmpty() || registerPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Fill All Entries",Toast.LENGTH_SHORT).show();
                }
                else if( mail.contains("@")==false || mail.contains(".com")==false)
                {
                    Toast.makeText(getApplicationContext(),"Invalid mail entered! Please provide a mail including '@' and '.com'",Toast.LENGTH_SHORT).show();

                }
                else if(mail.indexOf('@')==(mail.indexOf(".com")-1))
                {
                    Toast.makeText(getApplicationContext(),"Please provide a domain between '@' and '.com",Toast.LENGTH_SHORT).show();
                }
                else if(password.length()!=4)
                {
                    Toast.makeText(getApplicationContext(),"Please provide a password with 4-digit length", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String q = "SELECT * FROM Table_Register WHERE USERNAME=?";
                    Cursor cursorUsername = db.rawQuery(q,new String[]{username});

                    String w = "SELECT * FROM Table_Register WHERE MAIL=?";
                    Cursor cursorMail = db.rawQuery(w,new String[] {mail});

                    if(cursorUsername.getCount()!=0 || cursorMail.getCount()!=0)
                    {
                        Toast.makeText(getApplicationContext(),"There is an existing user with the same username or mail",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        dbHelper.insertData(name,surname,username,mail,password);
                        Toast.makeText(getApplicationContext(),"Registration Successful", Toast.LENGTH_LONG).show();
                        register();
                    }

                }


            }
        });
    }

    public void register(){
        Intent intent = new Intent(Register.this,MainActivity.class);
        startActivity(intent);
    }
}