package com.willemthewalrus.humemon;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class stat_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Grab UI components
        setContentView(R.layout.activity_stat_screen);
        TextView health = findViewById(R.id.totalhealth);
        TextView damage = findViewById(R.id.damagetext);
        TextView speed = findViewById(R.id.speed);
        TextView defense = findViewById(R.id.defensetext);
        TextView luck = findViewById(R.id.lucktext);
        TextView level = findViewById(R.id.level);
        TextView xp = findViewById(R.id.xp);
        TextView type = findViewById(R.id.type);

        //set UI components to our Humemon's stats
        health.setText("Your health: " + Integer.toString(MainActivity.currentHumemon.getHealth()));
        damage.setText("Your Damage: " + Integer.toString(MainActivity.currentHumemon.getDamage()));
        speed.setText("Your speed: " + Integer.toString(MainActivity.currentHumemon.getSpeed()));
        defense.setText("Your defense: " + Integer.toString(MainActivity.currentHumemon.getDefense()));
        luck.setText("Your Luck: " + Integer.toString(MainActivity.currentHumemon.getLuck()));
        level.setText("Your level: " + Integer.toString(MainActivity.currentHumemon.getLevel()));
        xp.setText("Current XP: "+ Integer.toString(MainActivity.currentHumemon.getXP()));
        type.setText("Type: " + MainActivity.currentHumemon.getType());
    }

    /*
     * changes the activity to the MainActivity
     */
    public void goback(View view){
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.firstvisit = false;
        recreate();
        startActivity(intent);

    }
}
