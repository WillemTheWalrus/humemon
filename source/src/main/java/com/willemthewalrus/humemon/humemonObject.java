package com.willemthewalrus.humemon;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by William on 3/24/2018.
 */

public class humemonObject {

    private int level;
    private int currentxp;
    private int dmg;
    private int health;
    private int defense;
    private int speed;
    private int luck;
    private Bitmap bitmap;
    private int buff = 0;
    private int attack1 = 0;
    private int attack2 = 0;
    private String attack1name;
    private int attack1speedmod;
    private String attack2name;
    private int attack2speedmod;
    private String buffname;
    private String bufftype;
    private boolean buffstate;
    private boolean isUser;
    private String charname;




    String type;
    String beanlady = "beanlady";
    String gymbro = "gymbro";
    String cowtaur = "cowtaur";
    String reversemermaid = "reversemermaid";
    int[] xpcapperlevel = new int[40];
    public static String[] philisophicalquotes;

    HashMap<String, String> traitmap = new HashMap<String, String>();

    //constructor with no parameters
    public humemonObject(){
        //initialize level cap
        for(int i = 0; i < 40; i++){
            xpcapperlevel[i] = (i +1 * 10) + (i*10);
        }

        philisophicalquotes = new String[]{
                "Great job!",
                "Excellent Work!",
                "Got 'em",
                "You sure showed them!",
                "Wow look at you go",
                "You're really getting the hang of this",
                "Still going? Wow!",
                "You're on Fire!",
                "That was pretty brutal",
                "Smackdown!",
                "That was intense!",
                "Woah!",
                "Hey how about a break?",
                "Still going?",
                "Do you know what you're doing?",
                "Are you still enjoying this?",
                "Please, they need a break",
                "Was that guy bleeding?",
                "Why are you doing this?",
                "Please, they have families!",
                "You've gotten out of control",
                "You have the bloodlust",
                "Please.... stop",
                "You're going too far!",
                "What have we become?",
                "That one had a wife and children, you monster!",
                "You must face the consequences of your actions",
                "you truly are a monster",
                "what seperates you from them?",
                "He who fights monsters...."
        };

        buffstate = false;

        createTraitMap();
    }

    //constructor with all standard parameters except for a name
    public humemonObject(int dm , int h , int d , int s , int l, boolean user){
        level = 0;
        dmg = dm;
        health = h;
        defense = d;
        speed = s;
        luck = l;
        isUser = user;

        currentxp = 0;
        xpcapperlevel = new int[40];

        //initialize level cap
        for(int i = 0; i < 40; i++){
            xpcapperlevel[i] = ((i +1) * 10) + (i*10);
        }

        philisophicalquotes = new String[]{
                "Great job!",
                "Excellent Work!",
                "Got 'em",
                "You sure showed them!",
                "Wow look at you go",
                "You're really getting the hang of this",
                "Still going? Wow!",
                "You're on Fire!",
                "That was pretty brutal",
                "Smackdown!",
                "That was intense!",
                "Woah!",
                "Hey how about a break?",
                "Still going?",
                "Do you know what you're doing?",
                "Are you still enjoying this?",
                "Please, they need a break",
                "Was that guy bleeding?",
                "Why are you doing this?",
                "Please, they have families!",
                "You've gotten out of control",
                "You have the bloodlust",
                "Please.... stop",
                "You're going too far!",
                "What have we become?",
                "That one had a wife and children, you monster!",
                "You must face the consequences of your actions",
                "you truly are a monster",
                "what seperates you from them?",
                "He who fights monsters...."
        };

        createTraitMap();
        buffstate = false;
    }

    //constructor with all standard parameters
    public humemonObject(int dm, int h, int d, int s, int l, boolean user, String name){
        level = 0;
        dmg = dm;
        health = h;
        defense = d;
        speed = s;
        luck = l;
        isUser = user;

        currentxp = 0;
        xpcapperlevel = new int[40];

        //initialize level cap
        for(int i = 0; i < 40; i++){
            xpcapperlevel[i] = (i +1 * 10) + (i*10);
        }

        philisophicalquotes = new String[]{
                "Great job!",
                "Excellent Work!",
                "Got 'em",
                "You sure showed them!",
                "Wow look at you go",
                "You're really getting the hang of this",
                "Still going? Wow!",
                "You're on Fire!",
                "That was pretty brutal",
                "Smackdown!",
                "That was intense!",
                "Woah!",
                "Hey how about a break?",
                "Still going?",
                "Do you know what you're doing?",
                "Are you still enjoying this?",
                "Please, they need a break",
                "Was that guy bleeding?",
                "Why are you doing this?",
                "Please, they have families!",
                "You've gotten out of control",
                "You have the bloodlust",
                "Please.... stop",
                "You're going too far!",
                "What have we become?",
                "That one had a wife and children, you monster!",
                "You must face the consequences of your actions",
                "you truly are a monster",
                "what seperates you from them?",
                "He who fights monsters...."
        };

        createTraitMap();
        buffstate = false;
        charname = name;
    }

    //set a humemon's bitmap
    public void setBitmap(Bitmap map){
        bitmap = map;
    }

    //set a humemon's name
    public void setName(String name){
        charname = name;
    }

    //retreive a humemon's name
    public String getName(){
        return charname;
    }

    public void setLevel(int l){
        level = l;
    }

    public void setDamage(int d){
        dmg = d;
    }

    public void setHealth(int h){
        health = h;
    }

    public void setDefense(int d){
        defense = d;
    }

    public void setType(String ty){
        type = ty;
    }

    public void setSpeed(int h){
        speed = h;
    }

    public void setIsUser(boolean u){
        isUser = u;
    }

    public boolean getIsUSer(){
        return isUser;
    }



    public void createTraitMap(){
        traitmap.put("girl",beanlady);
        traitmap.put("product",gymbro);
        traitmap.put("face", gymbro);
        traitmap.put("footwear",gymbro);
        traitmap.put("floor", beanlady);
        traitmap.put("flooring",beanlady);
        traitmap.put("leg",reversemermaid);
        traitmap.put("shoe",gymbro);
        traitmap.put("wood",cowtaur);
        traitmap.put("angle",cowtaur);
        traitmap.put("outdoor",reversemermaid);
        traitmap.put("hair", beanlady);
        traitmap.put("smile", reversemermaid);
        traitmap.put("eyebrow", gymbro);
        traitmap.put("human hair color", cowtaur);
        traitmap.put("cheek", cowtaur);
        traitmap.put("head", beanlady);
        traitmap.put("water", reversemermaid);
        traitmap.put("vegetation", cowtaur);
        traitmap.put("liquid", reversemermaid);
        traitmap.put("muscle", gymbro);
        traitmap.put("beverage",reversemermaid);
        traitmap.put("food", beanlady);
        traitmap.put("produce",beanlady);
        traitmap.put("product",gymbro);
        traitmap.put("nature", cowtaur);
        traitmap.put("chin",gymbro);
        traitmap.put("eye",gymbro);
        traitmap.put("child", reversemermaid);
        traitmap.put("forehead",cowtaur);
        traitmap.put("selfie", beanlady);
        traitmap.put("finger", cowtaur);
        traitmap.put("ligth", cowtaur);
        traitmap.put("technology",gymbro);
        traitmap.put("fun", cowtaur);
        traitmap.put("electronic device", gymbro);
        traitmap.put("nose", reversemermaid);
        traitmap.put("toddler",reversemermaid);
        traitmap.put("mouth", gymbro);
        traitmap.put("facial hair", gymbro);
        traitmap.put("hairstyle", gymbro);
        traitmap.put("lip", reversemermaid);
        traitmap.put("close up", cowtaur);
        traitmap.put("textile", gymbro);
        traitmap.put("organ", cowtaur);
        traitmap.put("black hair", beanlady);


    }

    public HashMap<String, String> getTraitmap(){
        return traitmap;
    }

    public String getType(){
        return type;
    }

    public void addXP(int xp){
        if( currentxp + xp >= xpcapperlevel[level]){
            currentxp = currentxp - xpcapperlevel[level] + xp;
            level += 1;
            dmg+=10;
            speed +=10;
            health +=10;
            defense += 10;
            luck += 10;
        }
        else{
            currentxp += xp;
        }
    }

    public int getXP(){
        return currentxp;
    }

    public int getDamage(){
        return dmg;
    }

    public int getDefense(){
        return defense;
    }

    public int getLevel(){
        return level;
    }

    public int getCurrentxp(){
        return currentxp;
    }

    public int getHealth(){
        return health;
    }

    public int getSpeed(){
        return speed;
    }

    public int getLuck(){
        return luck;
    }

    public void setBuff(String name, String type, int scale){

        buff = scale;
        buffname = name;
        bufftype = type;
    }
    public Boolean buffActive(){
        return buffstate;
    }
    public void setBuffstate(boolean state){
        buffstate = state;
    }

    public void setAttack1(String name, int scale, int speedscale ){

        attack1name = name;
        attack1 = scale;
        attack1speedmod = speedscale;


    }
    public void setAttack2(String name, int scale, int speedscale ){

        attack2name = name;
        attack2 = scale;
        attack2speedmod = speedscale;


    }

    public String getBuffname(){
        return buffname;
    }
    public String getBufftype(){
        return bufftype;
    }
    public int getBuff(){
        return buff;
    }

    public String getAttack1name(){
        return attack1name;
    }
    public int getAttack1SpeedMod(){
        return attack1speedmod;
    }
    public int getAttack1(){
        return attack1;
    }

    public int getAttack2(){
        return attack2;
    }
    public int getAttack2SpeedMod(){
        return attack2speedmod;
    }
    public String getAttack2name(){
        return attack2name;
    }


    public Bitmap getBitmap(){
        return bitmap;
    }

}
