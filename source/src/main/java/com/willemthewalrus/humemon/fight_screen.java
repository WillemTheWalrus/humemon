package com.willemthewalrus.humemon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class fight_screen extends AppCompatActivity {

    ProgressBar enemyHealth;
    ProgressBar userHealth;
    Button attack1;
    Button attack2;
    Button buff;
    Button run;
    TextView enemyName;
    TextView username;
    TextView fightText;
    humemonObject enemy;
    humemonObject userHumemon;
    int userinitialHealth;
    int enemyinitialHealth;
    Random generator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight_screen);

        //initialize enemyHumemon objects
        humemonEnemies.setupEnemies();
        generator = new Random();

        //grab humemon from previous activity
        userHumemon = MainActivity.currentHumemon;

        //load pictures into imageviews and select enemy to fight
        setPics();
        fightText = findViewById(R.id.fightstatus);
        enemyHealth = findViewById(R.id.enemyhealthbar);
        userHealth = findViewById(R.id.playerHealthBar);
        attack1 = findViewById(R.id.attack1);
        attack2 = findViewById(R.id.attack2);
        run = findViewById(R.id.run);
        buff = findViewById(R.id.buff);



        //record humemons' full health so that it can be restored after the fight is over
        userinitialHealth = userHumemon.getHealth();
        enemyinitialHealth = enemy.getHealth();
        enemyName = findViewById(R.id.enemyName);
        username = findViewById(R.id.username);

        //have the button text correspond to the
        attack1.setText("Fast Attack: " + userHumemon.getAttack1name());
        attack2.setText("Slow Attack: " + userHumemon.getAttack2name());
        buff.setText("Buff: " + userHumemon.getBuffname());


        //set up namefields and healthbars
        enemyName.setText(enemy.getName());
        username.setText(userHumemon.getType());
        enemyHealth.setMax(enemy.getHealth());

        /*
         * READ HERE
         * in order to animate the progress bars to update with health information
         * I had to change the min SDK from 21 to 24. In order to allow older phones to run
         * you would have to find a substitute for the progress bars to represent health
         * (such as a text field that simply displays the health value)
         */
        enemyHealth.setProgress(enemy.getHealth(),true);
        Log.i("enemyhealth",Integer.toString(enemy.getHealth()));
        userHealth.setMax(userHumemon.getHealth());
        userHealth.setProgress(userHumemon.getHealth(),true);
        Log.i("health", Integer.toString(userHumemon.getHealth()));

    }

    /*
    * Run this in the onCreate method in order to set up imageviews and determine the opponent
    * to fight against
     */
    public void setPics(){

        ImageView uview = findViewById(R.id.userIMG);
        ImageView eview = findViewById(R.id.enemyIMG);




       uview.setImageBitmap(userHumemon.getBitmap());

       //randomly select an enemy fight and load their image into the proper imageview
        int enemynum = generator.nextInt(6) +1;

        if(enemynum == 1){
            Picasso.with(this).load(R.drawable.frog_knight).into(eview);
            enemy = humemonEnemies.frogKnight;
        }
        else if(enemynum == 2){
            Picasso.with(this).load(R.drawable.generic_character).into(eview);
            enemy = humemonEnemies.genericEnemy;
        }
        else if(enemynum == 3){
            Picasso.with(this).load(R.drawable.knife_crab).into(eview);
            enemy = humemonEnemies.knifeCrab;
        }
        else if(enemynum == 4){
            Picasso.with(this).load(R.drawable.queen_bee).into(eview);
            enemy = humemonEnemies.queenBeeSwarm;
        }
        else if (enemynum == 5){
            Picasso.with(this).load(R.drawable.soccer_mom).into(eview);
            enemy = humemonEnemies.soccerMom;
        }
        else if(enemynum == 6){
            Picasso.with(this).load(R.drawable.wizard).into(eview);
            enemy = humemonEnemies.hippieWizard;
        }


    }

    /*
    * this method checks to see if the round is over, and if is it determines a winner, assigns
    * xp if needed, and makes all buttons other than the menu button unclickable
     */
    public void checkCloseRound(){

        //make sure this hasnt already been called in this current run of the activity
        if(attack1.isEnabled()) {
            //branches here when the bot has no health
            if (enemy.getHealth() <= 0) {
                fightText.setText("Congrats on your victory! go back to the main menu to play again");

                //reset health of both humemons
                userHumemon.setHealth(userinitialHealth);
                enemy.setHealth(enemyinitialHealth);

                //reset buffer states so they arent true when the next round starts
                userHumemon.setBuffstate(false);
                enemy.setBuffstate(false);

                //give the user some XP upon victory
                userHumemon.addXP((enemy.getLevel() + 1) *10);
                Log.i("level", Integer.toString(userHumemon.getLevel()));
                Log.i("XP", Integer.toString(userHumemon.getXP()));

                //increment through the philisophocal quotes array after each win
                if(!MainActivity.needQuotes){
                    MainActivity.needQuotes = true;
                    MainActivity.quoteNum++;
                }
                else{
                    MainActivity.quoteNum++;
                }


                //disable buttons
                attack1.setEnabled(false);
                attack2.setEnabled(false);
                buff.setEnabled(false);


            }
            //branches here when the user has no health
            else if (userHumemon.getHealth() <= 0) {
                fightText.setText("You lost, press menu and try again");

                //make sure the quotes dont display on a defeat
                MainActivity.needQuotes = false;

                //reset health to original max health
                userHumemon.setHealth(userinitialHealth);
                enemy.setHealth(enemyinitialHealth);

                //reset buffer abilities
                userHumemon.setBuffstate(false);
                enemy.setBuffstate(false);

                //disable buttons
                attack1.setEnabled(false);
                attack2.setEnabled(false);
                buff.setEnabled(false);

            }
            run.setText("menu");
        }
    }

    /*
     * This method simulates the attacks between the two humemons when the user decides to use a
     * fast attack
     */
    public void doattack1(View view){

        //clear fighttext log
        fightText.setText("");

        //attack the enemy humemon with a fast attack
        Attack(userHumemon, enemy, 1);


        //will branch here when the enemy has activated their buffer ability
        if(enemy.buffActive()){
            int action = (generator.nextInt(2 )+1);
            Attack(enemy,userHumemon,action);
        }
        //branches here when the enemy as yet to use his buffer ability
        else{
            int action = generator.nextInt(3)+1;
            Attack(enemy,userHumemon,action);
        }

        //check to see if anyone has won yet
       checkCloseRound();
    }

    /*
    * This method simulates the attacks between the two humemons when the user presses the
    * heavy attack button
     */
    public void doattack2(View view){
        //clear fight text log
        fightText.setText("");

        //attack the enemy humemon with a slow and heavy attack
        Attack(userHumemon, enemy, 2);

        //will branch here when the enemy has activated their buffer ability
        if(enemy.buffActive()){
            int action = (generator.nextInt(2 )+1);
            Attack(enemy,userHumemon,action);
        }
        //branches here when the enemy as yet to use his buffer ability
        else{
            int action = generator.nextInt(3)+1;
            Attack(enemy,userHumemon,action);
        }
        checkCloseRound();

    }


    /*
    * This method simulates the user activating his buffer and being attacked by the enemy humemon
     */
    public void dobuff(View view){
        //clear fight text log
        fightText.setText("");

        //activates the user's buff
        Attack(userHumemon, enemy, 3);


        //will branch here when the enemy has activated their buffer abili
        if(enemy.buffActive()){
            int action = (generator.nextInt(1 )+1);
            Attack(enemy,userHumemon,action);
        }
        //branches here when the enemy as yet to use his buffer ability
        else{
            int action = generator.nextInt(2)+1;
            Attack(enemy,userHumemon,action);
        }
        checkCloseRound();
    }

    /*
     * This method contains the meat of the battle logic, is implemented in multiple other methods
     */
    public void Attack(humemonObject Attacker, humemonObject Victim, int actionnum){
        int victimspeed = Victim.getSpeed();
        int attackerspeed = Attacker.getSpeed();
        int attackerdamage = Attacker.getDamage();
        int victimdefense = Victim.getDefense();

            //branches here upon quick attack
            if(actionnum ==1){

                //add ability damage to base damage
                attackerdamage += Attacker.getAttack1();

                //apply effects from buffers if any are active
                if(Attacker.getBufftype().equals("speed") && Attacker.buffActive()) attackerspeed += Attacker.getAttack1SpeedMod() +Attacker.getBuff();
                if(Attacker.getBufftype().equals("damage") && Attacker.buffActive()) attackerdamage += Attacker.getBuff();
                if(Victim.getBufftype().equals("defense") && Victim.buffActive()) victimdefense += Victim.getBuff();

                //if the person being attacked has a higher speed rating, they have a chance to dodge
                if(victimspeed > attackerspeed){
                    int dodgechance = victimspeed - attackerspeed;
                    generator = new Random();
                    if(generator.nextInt(100)+1 <= dodgechance){
                        Log.i("attack", "attack dodged");
                        //set text within battle log
                        if(Victim.getIsUSer()){
                            fightText.setText(fightText.getText()+ " \n" +Victim.getType() + " dogded " + Attacker.getName() + "'s attack! ");
                        }
                        else{
                            fightText.setText(fightText.getText()+ " \n" + Victim.getName() + " dogded " + Attacker.getType() + "'s attack! ");
                        }
                    }
                    else{
                        // if the rng dodge roll did not yeild a dodge, then the
                        // method will branch here, determining total damage based off of current calculated damage and the attackee's
                        // defense rating
                        int damage = attackerdamage-victimdefense;
                        Victim.setHealth(Victim.getHealth()-(damage));
                        Log.i("victimhealth",Integer.toString(Victim.getHealth()));

                        if(Victim.getIsUSer()){
                            fightText.setText(fightText.getText() +" " + Attacker.getName() + " did "+ Attacker.getAttack1name()+ " for " + Integer.toString(damage)+ " damage to " + Victim.getType()+ " \n" );
                        }
                        else{
                            fightText.setText(fightText.getText() + " " + Attacker.getType() + " did "+ Attacker.getAttack1name()+ " for " + Integer.toString(damage)+ " damage to " + Victim.getName()+ " \n" );
                        }

                    }
                }
                else{
                    //if the attackee's speed rating is lower than the attacker's speed rating then
                    // there is no chance to dodge and the method will branch here, determining total
                    // damage based off of current calculated damage and the attackee's defense rating
                    int damage = attackerdamage-victimdefense;
                    Victim.setHealth(Victim.getHealth()-(attackerdamage-victimdefense));
                    Log.i("victimhealth",Integer.toString(Victim.getHealth()));

                    //set battle log text
                    if(Victim.getIsUSer()){
                        fightText.setText(fightText.getText() + Attacker.getName() + " did "+ Attacker.getAttack1name()+ " for " + Integer.toString(damage)+ " damage to " + Victim.getType()+ " \n" );
                    }
                    else{
                        fightText.setText(fightText.getText() + Attacker.getType() + " did "+ Attacker.getAttack1name()+ " for " + Integer.toString(damage)+ " damage to " + Victim.getName()+ " \n" );
                    }
                }
            }

            //branches here when we are simuating a heavy attack
            else if(actionnum == 2){

                //add ability damage to base damage
                attackerdamage += Attacker.getAttack2();

                //apply effects from buffers if any are active
                if(Attacker.getBufftype().equals("speed") && Attacker.buffActive()) attackerspeed += Attacker.getAttack2SpeedMod() +Attacker.getBuff();
                if(Attacker.getBufftype().equals("damage") && Attacker.buffActive()) attackerdamage += Attacker.getBuff();
                if(Victim.getBufftype().equals("defense") && Victim.buffActive()) victimdefense += Victim.getBuff();

                //if the person being attacked has a higher speed rating, they have a chance to dodge
                if(victimspeed > attackerspeed){
                    int dodgechance = attackerspeed -victimspeed;
                    generator = new Random();
                    if(generator.nextInt(100)+1 <= dodgechance){
                        Log.i("attack", "attack dodged");
                        if(Victim.getIsUSer()){
                            fightText.setText(fightText.getText()+ " \n" +Victim.getType() + " dogded " + Attacker.getName() + "'s attack!");
                        }
                        else{
                            fightText.setText(fightText.getText()+ " \n" + Victim.getName() + " dogded " + Attacker.getType() + "'s attack!");
                        }
                    }else{
                        // if the rng dodge roll did not yeild a dodge, then the
                        // method will branch here, determining total damage based off of current calculated damage and the attackee's
                        // defense rating
                        int damage = attackerdamage-victimdefense;
                        Victim.setHealth(Victim.getHealth()-(attackerdamage-victimdefense));
                        Log.i("victimhealth",Integer.toString(Victim.getHealth()));
                        if(Victim.getIsUSer()){
                            fightText.setText(fightText.getText() +" "+ Attacker.getName() + " did "+ Attacker.getAttack2name()+ " for " + Integer.toString(damage)+ " damage to " + Victim.getType()+ " \n" );
                        }
                        else{
                            fightText.setText(fightText.getText() + " "+ Attacker.getType() + " did "+ Attacker.getAttack2name()+ " for " + Integer.toString(damage)+ " damage to " + Victim.getName()+ " \n" );
                        }

                    }
                }
                else{
                    //if the attackee's speed rating is lower than the attacker's speed rating then
                    // there is no chance to dodge and the method will branch here, determining total
                    // damage based off of current calculated damage and the attackee's defense rating
                    int damage = attackerdamage-victimdefense;
                    Victim.setHealth(Victim.getHealth()-(attackerdamage-victimdefense));
                    Log.i("victimhealth",Integer.toString(Victim.getHealth()));
                    //set battle log text
                    if(Victim.getIsUSer()){
                        fightText.setText(fightText.getText()+ " " + Attacker.getName() + " did "+ Attacker.getAttack2name()+ " for " + Integer.toString(damage)+ " damage to " + Victim.getType()+ " \n" );
                    }
                    else{
                        fightText.setText(fightText.getText() +" "+ Attacker.getType() + " did "+ Attacker.getAttack2name()+ " for " + Integer.toString(damage)+ " damage to " + Victim.getName()+ " \n" );
                    }
                }
            }

            //branches here when a user chooses to activate their buff
            else if(actionnum == 3){
                Attacker.setBuffstate(true);
                if(Victim.getIsUSer()){
                    fightText.setText(Attacker.getName()+ " used " + Attacker.getBuffname() + "\n");
                }
                else{
                    fightText.setText(Attacker.getType() + " used " + Attacker.getBuffname() + "\n");
                }

            }
            else{
                Log.i("attack", "invalid attack num");
            }
        //update both UI healthbars
        userHealth.setProgress(userHumemon.getHealth(),true);
        enemyHealth.setProgress(enemy.getHealth(),true);
    }

    /*
    * this method returns the user to the MainActivity
     */
    public void goBack(View view){

        //reset health and buffs and then return to the menu screen via and intent
        MainActivity.firstvisit = false;
        userHumemon.setHealth(userinitialHealth);
        enemy.setHealth(enemyinitialHealth);
        userHumemon.setBuffstate(false);
        enemy.setBuffstate(false);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
   }
}
