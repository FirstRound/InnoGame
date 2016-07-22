package com.brackeen.scared.strategies;

import com.brackeen.scared.action.SpecialAction;

/**
 * Created by pisatel on 22.07.16.
 */
public class Hunter extends AStrategy {

    public Hunter(){}

    public Hunter(double attack, double defense, double speed, double reaction,
                   double fear, double angry, double pain, double risk) {
        super(attack, defense, speed, reaction, fear, angry, pain, risk);
    }

    public SpecialAction useSkill() {
        return null;
    }
}
