package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    CardView cardView;
    EditText loginUsername, loginPassword;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = (TextView)findViewById(R.id.textView2);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();


            }
        });

        cardView = (CardView) findViewById(R.id.cardview);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUsername = findViewById(R.id.LoginUsername);
                loginPassword = findViewById((R.id.LoginPassword));

                String enteredUsername = loginUsername.getText().toString();
                String enteredPassword = loginPassword.getText().toString();

                dbHelper = new DatabaseHelper(getApplicationContext());
                dbHelper.deleteAllLoginTable();

                boolean status = dbHelper.checkLoginStatus(enteredUsername,enteredPassword);
                if(enteredUsername.isEmpty() || enteredPassword.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Entered Username or Password Is Wrong, Please Try Again",Toast.LENGTH_SHORT).show();
                }

                else if (status == true)
                {
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    dbHelper.insertLoginTable(enteredUsername,enteredPassword);
                    openMenu();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Username or Password is wrong!",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    //FUNCTIONS
    public void openRegister(){
        Intent intent=new Intent(MainActivity.this,Register.class);
        startActivity(intent);
    }

    public void openMenu(){
        Intent intent = new Intent(MainActivity.this,Menu.class);
        startActivity(intent);
    }
}
