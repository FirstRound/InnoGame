package com.brackeen.scared.controllers;

import com.brackeen.scared.Map;
import com.brackeen.scared.Tile;
import com.brackeen.scared.entity.Enemy;
import com.brackeen.scared.entity.Entity;
import com.brackeen.scared.genetic.GeneticEvolution;

import java.util.LinkedList;

/**
 * Created by pisatel on 27.07.16.
 */
public class EnemyController {

    private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
    private GeneticEvolution geneticEvolution = new GeneticEvolution();
    private Map map;

    public EnemyController(Map map) {
        this.map = map;
    }

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

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }

    public void tickAll() {
        for (Enemy enemy : enemies) {
            tickEnemy(enemy);
        }
    }

    //BEGIN #ENEMY ACTION# PRIVATE METHODS
    private void doAction(Enemy enemy) {
        enemy.tick();// make decision
    }



    //END #ENEMY ACTION# PRIVATE METHODS

    //BEGIN #TICK ACTION# PRIVATE METHODS
    private boolean tickEnemy(Enemy enemy) {
        Tile oldTile = enemy.getTile();
        doAction(enemy);
        if (enemy.isDeleted()) {
            if (oldTile != null) {
                oldTile.removeEntity(enemy);
            }
            return true;
        } else {
            Tile newTile = getTileAt(enemy);
            if (oldTile != newTile) {
                if (oldTile != null) {
                    oldTile.removeEntity(enemy);
                }
                if (newTile != null) {
                    newTile.addEntity(enemy);
                }
            }
            return false;
        }
    }

    public Tile getTileAt(Entity entity) {
        return getTileAt((int) entity.getX(), (int) entity.getY());
    }

    public Tile getTileAt(int x, int y) {
        if (x < 0 || y < 0 || x >= map.getWidth() || y >= map.getHeight()) {
            return null;
        }

        return map.getTileAt(x, y);
    }
    //END #TICK ACTION#  PRIVATE METHODS
}
