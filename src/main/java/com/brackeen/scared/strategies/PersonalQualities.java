package com.brackeen.scared.strategies;

import java.util.HashMap;

/**
 * Created by pisatel on 22.07.16.
 */
public class PersonalQualities {


    HashMap<String, Double> fields = new HashMap<String, Double>();

    public PersonalQualities(){}

    public PersonalQualities(double mood, double aggressivity, double insubordination, double intelligence) {
        fields.put("mood", mood);
        fields.put("aggressivity", aggressivity);
        fields.put("insubordination", insubordination);
        fields.put("intelligence", intelligence);
    }

    public double getMood() {
        return fields.get("mood");
    }

    public void setMood(double mood) {
        fields.put("mood", mood);
    }

    public double getAggressivity() {
        return fields.get("aggressivity");
    }

    public void setAgressivity(double agressivity) {
        fields.put("agressivity", agressivity);
    }

    public double getInsubordination() {
        return fields.get("insubordination");
    }

    public void setInsubordination(double insubordination) {
        fields.put("insubordination", insubordination);
    }

    public double getIntelligence() {
        return fields.get("intelligence");
    }

    public void setIntelligence(double intelligence) {
        fields.put("intelligence", intelligence);
    }

    public void setValue(String field, double value) {
        fields.put(field, value);
    }
}
