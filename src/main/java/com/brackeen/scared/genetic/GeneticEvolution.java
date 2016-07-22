package com.brackeen.scared.genetic;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by pisatel on 22.07.16.
 */
public class GeneticEvolution {
    private Queue<Genome> genomes = new LinkedList<Genome>();

    public void generateStartPopulation(int size) {
        for (int i = 0; i < size; i++) {
            boolean[] gens = new boolean[Genome.GENOME_SIZE];
            for (int j = 0; j < Genome.GENOME_SIZE; j++) {
                if (Math.random() < 0.5)
                    gens[j] = true;
                else
                    gens[j] = false;
            }
            Genome g = new Genome(gens);
            System.out.println(g.toString());
            genomes.add(g);
        }
    }


    public Genome getNextGenome() {
        return genomes.peek();
    }
}
