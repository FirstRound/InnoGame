package com.brackeen.scared.genetic;

import com.brackeen.scared.strategies.*;

/**
 * Created by pisatel on 22.07.16.
 */
public class Genome {
    public static final int GENOME_SIZE = 352;//5*(8*8) + 4*4
    private final int BYTE = 8;
    private boolean[] genome;

    Bully bully;
    Coward coward;
    Demure demure;
    Hunter hunter;
    Warrior warrior;
    PersonalQualities pq;

    public Genome(boolean[] genome) {
        this.genome = genome;
        init();
    }

    public Bully getBully() {
        return bully;
    }
    public Coward getCoward() {
        return coward;
    }
    public Demure getDemure() {
        return  demure;
    }
    public Hunter getHunter() {
        return hunter;
    }
    public Warrior getWarrior() {
        return warrior;
    }

    private void init() {
        bully = new Bully();
        decodeStrategies(bully, 0);

        coward = new Coward();
        decodeStrategies(coward, 64);

        demure = new Demure();
        decodeStrategies(demure, 128);

        hunter = new Hunter();
        decodeStrategies(hunter, 192);

        warrior = new Warrior();
        decodeStrategies(warrior, 256);

        pq = new PersonalQualities();
        decodePersonalQualities(pq, 320);
    }

    private void decodeStrategies(AStrategy strat, int from) {
        String[] start_fields = {"attack", "defense", "speed", "reaction", "fear",
                                "angry", "pain", "risk"};
        for (String s : start_fields) {
            StringBuilder str = new StringBuilder();
            for (int j = from; j < from+BYTE; j++) {
                str.append(((genome[j] == true) ? "1" : "0"));
            }
            strat.setValue(s, Integer.parseInt(str.toString(), 2)*100.0/256.0);
            from+=4;
        }
    }

    private void decodePersonalQualities(PersonalQualities pq, int from) {
        String[] pq_fields = {"mood", "aggressivity", "insubordination", "intelligence"};
        for (String s : pq_fields) {
            StringBuilder str = new StringBuilder();
            for (int j = from; j < from+BYTE; j++) {
                str.append(((genome[j] == true) ? "1" : "0"));
            }
            pq.setValue(s, Integer.parseInt(str.toString(), 2)*100.0/256.0);
            from+=4;
        }
    }

    @Override
    public String toString() {
        return genome.toString();
    }

    public boolean[] getRawGenomeArray() {
        return genome;
    }
}
