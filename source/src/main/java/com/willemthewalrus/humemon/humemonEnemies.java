package com.willemthewalrus.humemon;

/**
 * Created by William on 3/24/2018.
 * A class to initialize humemon enemy objects
 */

import com.willemthewalrus.humemon.humemonObject;
public class humemonEnemies {

    //static enemy humemon objects are declared here to be used throughout other classes
    public static humemonObject soccerMom = new humemonObject(25,100,15,10,3,false,"Soccer Mom");
    public static humemonObject queenBeeSwarm = new humemonObject(15, 80, 10, 100, 2,false, "Queen Been Swarm");
    public static humemonObject hippieWizard = new humemonObject(20, 140, 20, 20, 2,false, "Hippie Wizard");
    public static humemonObject knifeCrab = new humemonObject(30, 300, 30,15,4 ,false, "Knife Crab");
    public static humemonObject genericEnemy = new humemonObject(10,180,10,10,1,false, "Generic Enemy");
    public static humemonObject frogKnight = new humemonObject(30, 250, 30, 70,4,false, "Frog Knight");

    /*
     * This method initializes all the attacks and buffs for the enemies
     */
    public static void setupEnemies(){
        soccerMom.setAttack1("Roadrage",50,10 );
        soccerMom.setAttack2("Skreech", 30, 30);
        soccerMom.setBuff("Gossip","defense", 10);

        queenBeeSwarm.setAttack1("Sting", 20, 20);
        queenBeeSwarm.setAttack2("Honey Flash Flood", 30, 0 );
        queenBeeSwarm.setBuff("Pollinate", "damage", 20);

        hippieWizard.setAttack1("Magic Mushroom Mele", 40, 0);
        hippieWizard.setAttack2("Stank Shock", 20, 40);
        hippieWizard.setBuff("Align Chakras", "defense", 10);

        knifeCrab.setAttack1("Stab Stab", 40, 0);
        knifeCrab.setAttack2("Pinch Pinch", 20, 30);
        knifeCrab.setBuff("Molt Shell", "speed", 20);

        genericEnemy.setAttack1("Bite", 30, 30);
        genericEnemy.setAttack2("Headbutt", 50, 0);
        genericEnemy.setBuff("Generic Buff", "damage", 10);

        frogKnight.setAttack1("Saliva Slash", 50, 0);
        frogKnight.setAttack2("Shield Strike", 30, 30);
        frogKnight.setBuff("Fly Feast", "speed", 20);


    }

}
