package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Manage extends AppCompatActivity {
    private CardView cardViewAdd;
    private CardView cardViewDelete;
    private CardView cardViewEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        cardViewAdd = (CardView) findViewById(R.id.CardViewAdd);
        cardViewDelete = (CardView) findViewById(R.id.CardViewDelete);
        cardViewEdit = (CardView) findViewById(R.id.CardViewEdit);

        cardViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAddAlert();
            }
        });

        cardViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDeleteAlert();
            }
        });


        cardViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openEditAlert();
            }
        });
/*
        double[] x = new double[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36};
        double[] y = new double[] {114,142,131,121,120,108,125,143,150,153,158,164,183,187,194,197,197,206,231,215,215,223,230,236,226,234,236,243,255,254,286,255,265,270,270,277};
        LinearRegression l = new LinearRegression(x,y);
        double inter = l.intercept();
        double slope = l.slope();
        double a = l.slopeStdErr();
        //Toast.makeText(getApplicationContext(),Double.toString(inter),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),Double.toString(slope),Toast.LENGTH_SHORT).show();
        Log.i("reg","inter:" + Double.toString(inter));
        Log.i("reg","slope:" + Double.toString(slope));
        Log.i("reg","slopestdError:" + a);
        Log.i("reg","r2:" + l.R2());
        Log.i("reg","interceptstdError:" + l.interceptStdErr());
*/
    }

    public void openAddAlert() {
        Addition_Alert addition_alert = new Addition_Alert();
        addition_alert.show(getSupportFragmentManager(), "addition alert");
    }

    public void openDeleteAlert() {

        Deletion_Alert deletion_alert = new Deletion_Alert();
        deletion_alert.show(getSupportFragmentManager(), "deletion alert");
    }

    public void openEditAlert() {

        Edition_Alert edition_alert = new Edition_Alert();
        edition_alert.show(getSupportFragmentManager(), "edition alert");

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( Manage.this, Menu.class);
        startActivity(intent);
    }
}
