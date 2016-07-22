package com.brackeen.scared.controllers;

import com.brackeen.scared.strategies.*;

/**
 * Created by pisatel on 22.07.16.
 */
public class StrategyController {
    private Bully bully;
    private Coward coward;
    private Demure demure;
    private Hunter hunter;
    private Warrior warrior;
    private PersonalQualities pq;

    public StrategyController(Bully bully,
                             Coward coward,
                             Demure demure,
                             Hunter hunter,
                             Warrior warrior) {
        this.bully   = bully;
        this.coward  = coward;
        this.demure  = demure;
        this.hunter  = hunter;
        this.warrior = warrior;
    }



    public double willAttack() {return 0.0;}
    public double willFlee() {return 0.0;}
    public double willHide() {return 0.0;}
    public double willPursue() {return 0.0;}
    public double willChangeTarget() {return 0.0;}




    private double willChangeDecision() {return 0.0;}

}
