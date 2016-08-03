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
import java.util.Random;

/**
 * Created by pisatel on 27.07.16.
 */
public class EnemiesController {

    private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
    private GeneticEvolution geneticEvolution = new GeneticEvolution();
    private Map map;
    private DECISION decision;

    private boolean needNextGeneration = false;

    public EnemiesController(Map map) {
        this.map = map;
    }

    public void initGenetic() {
        geneticEvolution.generateStartPopulation(enemies.size());

        setGenomeToEnemies(enemies);
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
        List<Enemy> aliveList = getAliveEnemies();
        if(aliveList.size() > 1) {
            for (Enemy enemy : enemies) {
                if (!enemy.isDeleted()) {
                    tickEnemy(enemy);
                }
            }
        }
        else {
            if(aliveList.size() == 1){
                aliveList.get(0).calcStat();
            }
                generateNewPopulation();
        }

    }

    //BEGIN #ENEMY ACTION# PRIVATE METHODS
    private void doAction(Enemy enemy) {
        DecisionController currentDC = enemy.getDecisionController();
        DECISION typeDecision = currentDC.getDecisionType();
        List<Enemy> aliveEnemies = getAliveEnemiesForEnemy(enemy);
        if (enemy.canMakeAction() && aliveEnemies.size() > 0) {
            switch (typeDecision) {
                case MOVE:
                    enemy.setState(Enemy.STATE_TERMINATE);
                    Point2D nextPoint = currentDC.applyNextMovement();
                    enemy.setLocation((float) nextPoint.getX(), (float) nextPoint.getY());
                    break;
                case FIGHT:
                        Enemy fightEnemy = currentDC.selectEnemyForFight(aliveEnemies);
                        if (fightEnemy != null) {
                            enemy.setState(Enemy.STATE_FIRE);
                            App.getApp().getAudio("/sound/laser0.wav").play();
                            int damage = currentDC.calcRealDamage(enemy.getDamage());
                            fightEnemy.hurt(damage);
                            if (fightEnemy.isDeleted())
                                enemy.addKill();
                            enemy.addPoints(damage);
                        }
                        else {
                            break;
                        }
                    break;
            }

        }
        enemy.tick();

    }


    //END #ENEMY ACTION# PRIVATE METHODS

    //BEGIN #TICK ACTION# PRIVATE METHODS

    private void generateNewPopulation() {
        System.out.println("++++++++++!!!NEW GENERATION!!!++++++++++");
        Random rand = new Random();
        geneticEvolution.makeNewPopulation(enemies);
        int size = enemies.size();
        enemies.clear();
        for (int i = 0; i < size; i++) {
            map.createEnemy(new Point2D.Double(rand.nextInt(map.getWidth()),rand.nextInt(map.getHeight())));
        }
        setGenomeToEnemies(enemies);
    }

    private void setGenomeToEnemies(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            enemy.setGenome(geneticEvolution.getNextGenome());
            enemy.initDecisionController();
        }
    }

    private List<Enemy> getAliveEnemiesForEnemy(Enemy enemy) {
        List<Enemy> list = new LinkedList<Enemy>();
        for (Enemy e : enemies) {
            if(!e.isDeleted() && !e.equals(enemy)) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Enemy> getAliveEnemies() {
        List<Enemy> list = new LinkedList<Enemy>();
        int counter = 0;
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
