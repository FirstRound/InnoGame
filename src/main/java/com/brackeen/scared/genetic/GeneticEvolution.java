package com.brackeen.scared.genetic;

import com.brackeen.scared.entity.Enemy;

import java.util.*;

/**
 * Created by pisatel on 22.07.16.
 */
public class GeneticEvolution {
    private Queue<Genome> genomes = new LinkedList<Genome>();

    private final int MAX_MUTATION_COUNT = 30;

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
        Genome g = genomes.peek();
        genomes.remove();
        return g;
    }


    public void makeNewPopulation(LinkedList<Enemy> enemies) {
        genomes.clear();
        arrangeEnemiesByStat(enemies);
        for (int i = 0; i < enemies.size()/2; i++) {
            for (int j = 0; j < enemies.size()/2; j++) {
                genomes.add(getChild(enemies.get(i).getGenome(), enemies.get(j).getGenome()));
            }
        }
    }

    private void arrangeEnemiesByStat(LinkedList<Enemy> enemies) {
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Enemy)o1).getStatistics().compareTo(((Enemy)o2).getStatistics());
            }
        };
        Collections.sort(enemies, comp);

    }

    private Genome getChild(Genome mother, Genome father) {
        boolean[] childGenome = new boolean[Genome.GENOME_SIZE];
        Random rand = new Random();
        int crossPoint = rand.nextInt(Genome.GENOME_SIZE-6) + 3;
        boolean[] motherPart = Arrays.copyOfRange(mother.getRawGenomeArray(), 0, crossPoint);
        boolean[] fatherPart = Arrays.copyOfRange(mother.getRawGenomeArray(),
                                                    crossPoint, Genome.GENOME_SIZE);
        for (int i = 0; i < crossPoint; i++) {

            childGenome[i] = motherPart[i];
        }
        for (int i = crossPoint; i < Genome.GENOME_SIZE; i++) {

            childGenome[i] = fatherPart[i-crossPoint];
        }

        //mutate
        int countOfMutation = rand.nextInt(MAX_MUTATION_COUNT);
        for (int i = 0; i < countOfMutation; i++) {
            childGenome[rand.nextInt(Genome.GENOME_SIZE)] = rand.nextBoolean();
        }
        return new Genome(childGenome);
    }
}
