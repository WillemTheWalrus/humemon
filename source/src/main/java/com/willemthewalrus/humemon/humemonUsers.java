package com.willemthewalrus.humemon;

/**
 * Created by William on 3/24/2018.
 * This class sets up the attacks and buff for each class
 * that a user can be
 */

public class humemonUsers {

    public static void setupBeanLady(humemonObject beans){
        beans.setBuff("Pinto Power Up","damage",10);
        beans.setAttack1("Magical Fruit Toot",20, 20);
        beans.setAttack2("Bean Blast", 30,-10);
    }

    public static void setupGymBro(humemonObject bro){
        bro.setBuff("Furious Flex", "speed", 20);
        bro.setAttack1("Sweaty Unsettle", 20,20);
        bro.setAttack2("Dumbbell Dash", 30, -10);

    }

    public  static  void setupReverseMermaid(humemonObject maid){
        maid.setBuff("Moisturize", "defense", 10);
        maid.setAttack1("Fin Slap", 10, 20);
        maid.setAttack2("Human Kick", 30, -10);
    }

    public static  void setupCowtaur(humemonObject cow){
        cow.setBuff("Lactate", "damage", 10);
        cow.setAttack1("Cow Patty", 10, 20);
        cow.setAttack2("Cheese Charge", 30, -10);
    }
}
