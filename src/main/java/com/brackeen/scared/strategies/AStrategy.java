package com.brackeen.scared.strategies;

/**
 * Created by pisatel on 21.07.16.
 */
public class AStrategy {
    private double attack;
    private double defense;
    private double speed;
    private double reaction;
    private double fear;
    private double angry;
    private double pain;
    private double risk;

    public AStrategy(double attack, double defense, double speed, double reaction,
                        double fear, double angry, double pain, double risk) {
        this.angry = angry;
        this.attack = attack;
        this.risk = risk;
        this.defense = defense;
        this.fear = fear;
        this.speed = speed;
        this.reaction = reaction;
        this.pain = pain;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getReaction() {
        return reaction;
    }

    public void setReaction(double reaction) {
        this.reaction = reaction;
    }

    public double getFear() {
        return fear;
    }

    public void setFear(double fear) {
        this.fear = fear;
    }

    public double getAngry() {
        return angry;
    }

    public void setAngry(double angry) {
        this.angry = angry;
    }

    public double getPain() {
        return pain;
    }

    public void setPain(double pain) {
        this.pain = pain;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }
}
