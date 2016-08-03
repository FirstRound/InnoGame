package com.brackeen.scared.controllers;

import com.brackeen.app.App;
import com.brackeen.scared.Map;
import com.brackeen.scared.Tile;
import com.brackeen.scared.controllers.DecisionController.DECISION;
import com.brackeen.scared.entity.Enemy;
import com.brackeen.scared.entity.Entity;
import com.brackeen.scared.genetic.GeneticEvolution;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pisatel on 27.07.16.
 */
public class EnemiesController {

    private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
    private GeneticEvolution geneticEvolution = new GeneticEvolution();
    private Map map;
    private DECISION decision;

    public EnemiesController(Map map) {
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
        List<Point2D> empty = new LinkedList<Point2D>();
        for (Enemy enemy : enemies) {
            if (!enemy.isDeleted()) {
                tickEnemy(enemy);
            }
        }

    }

    //BEGIN #ENEMY ACTION# PRIVATE METHODS
    private void doAction(Enemy enemy) {
        //enemy.tick();// make decision
        DecisionController currentDC = enemy.getDecisionController();
        DECISION typeDecision = currentDC.getDecisionType();
        if (enemy.canMakeAction()) {
            switch (typeDecision) {
                case MOVE:
                    enemy.setState(Enemy.STATE_TERMINATE);
                    Point2D nextPoint = currentDC.applyNextMovement();
                    enemy.setLocation((float) nextPoint.getX(), (float) nextPoint.getY());
                    break;
                case FIGHT:
                    List<Enemy> aliveEnemies = getAliveEnemies();
                    if (aliveEnemies.size() > 0) {
                        Enemy fightEnemy = currentDC.selectEnemyForFight(aliveEnemies);
                        if (fightEnemy != null) {
                            enemy.setState(Enemy.STATE_FIRE);
                            App.getApp().getAudio("/sound/laser0.wav").play();
                            int damage = currentDC.calcRealDamage(enemy.getDamage());
                            fightEnemy.hurt(damage);
                            enemy.addPoints(damage);
                        }
                        else {
                            break;
                        }
                    }
                    break;
            }
        }
        enemy.tick();
    }


    //END #ENEMY ACTION# PRIVATE METHODS

    //BEGIN #TICK ACTION# PRIVATE METHODS
    private List<Enemy> getAliveEnemies() {
        List<Enemy> list = new LinkedList<Enemy>();
        for (Enemy e : enemies) {
            if(!e.isDeleted()) {
                list.add(e);
            }
        }
        return list;
    }

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
