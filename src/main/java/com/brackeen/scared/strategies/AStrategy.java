package com.brackeen.scared.strategies;

import com.brackeen.scared.action.SpecialAction;

import java.util.HashMap;

/**
 * Created by pisatel on 21.07.16.
 */
public class AStrategy {

    HashMap<String, Double> fields = new HashMap<String, Double>();

    public AStrategy(){}

    public AStrategy(double attack, double defense, double speed, double reaction,
                        double fear, double angry, double pain, double risk) {
        fields.put("attack", attack);
        fields.put("defense", defense);
        fields.put("speed", speed);
        fields.put("reaction", reaction);
        fields.put("fear", fear);
        fields.put("angry", angry);
        fields.put("pain", pain);
        fields.put("risk", risk);
    }

    public SpecialAction useSkill(){
        return null;
    }

    public double getAttack() {
        return fields.get("attack");
    }

    public void setAttack(double attack) {
        fields.put("attack", attack);
    }

    public double getDefense() {
        return fields.get("defense");
    }

    public void setDefense(double defense) {
        fields.put("defense", defense);
    }

    public double getSpeed() {
        return fields.get("speed");
    }

    public void setSpeed(double speed) {
        fields.put("speed", speed);
    }

    public double getReaction() {
        return fields.get("reaction");
    }

    public void setReaction(double reaction) {
        fields.put("reaction", reaction);
    }

    public double getFear() {
        return fields.get("fear");
    }

    public void setFear(double fear) {
        fields.put("fear", fear);
    }

    public double getAngry() {
        return fields.get("angry");
    }

    public void setAngry(double angry) {
        fields.put("angry", angry);
    }

    public double getPain() {
        return fields.get("pain");
    }

    public void setPain(double pain) {
        fields.put("pain", pain);
    }

    public double getRisk() {
        return fields.get("risk");
    }

    public void setRisk(double risk) {
        fields.put("risk", risk);
    }

    public void setValue(String field, double value) {
        fields.put(field, value);
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}
