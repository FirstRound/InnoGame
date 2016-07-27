package com.brackeen.scared.controllers;

import com.brackeen.scared.Map;
import com.brackeen.scared.entity.Enemy;
import com.brackeen.scared.genetic.GeneticEvolution;

import java.util.LinkedList;

/**
 * Created by pisatel on 27.07.16.
 */
public class EnemyController {

    private LinkedList<Enemy> enemies;
    private GeneticEvolution geneticEvolution = new GeneticEvolution();
    private Map map;

    public void initGenetic() {
        geneticEvolution.generateStartPopulation(enemies.size());

        for (Enemy enemy : enemies) {
            enemy.setGenome(geneticEvolution.getNextGenome());
            enemy.initDecisionController();
        }
    }


    public void addEnemiesList(LinkedList<Enemy> enemies) {
        this.enemies = enemies;
    }


    public void setMap(Map map) {
        this.map = map;
    }


}
