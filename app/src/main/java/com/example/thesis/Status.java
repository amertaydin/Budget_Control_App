package com.example.thesis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Status extends AppCompatActivity {
      private CardView cardViewTranscript;
      private CardView cardViewAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        cardViewTranscript = (CardView ) findViewById(R.id.CardViewTranskript);
        cardViewAssessment = (CardView) findViewById(R.id.CardViewAssessment);

        cardViewTranscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, Transcript.class);
                startActivity(intent);
            }
        });

        cardViewAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, Assessment.class );
                        startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( Status.this, Menu.class);
        startActivity(intent);
    }
}
