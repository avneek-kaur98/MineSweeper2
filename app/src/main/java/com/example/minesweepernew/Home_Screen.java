package com.example.minesweepernew;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Home_Screen extends AppCompatActivity {

    RadioGroup Level;
    RadioButton easy;
    RadioButton medium;

    Button Next;

    int level;
    public static String Level_Key = "Level";
    boolean firstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);

        Level = findViewById(R.id.level);
        easy = findViewById(R.id.easy);
        medium = findViewById(R.id.medium);

        Next = findViewById(R.id.Next);


    }

    public void HomeToMain(View view) {
        Button button = (Button)view;
        Intent intent = new Intent(this,MainActivity.class);

        intent.putExtra(Level_Key,level);
        startActivity(intent);
        finish();

    }

    public void checkLevel(View view) {

        int id = Level.getCheckedRadioButtonId();

            if (id == easy.getId()) {
                level = 1;


            } else if (id == medium.getId()) {
                level = 2;

            }

        }



}
