package com.brackeen.scared.controllers;

import com.brackeen.scared.Tile;
import com.brackeen.scared.entity.Enemy;
import com.brackeen.scared.genetic.Genome;

import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by pisatel on 22.07.16.
 */
public class DecisionController {

    private Random rand = new Random();

    public enum DECISION {
        MOVE, RUN, FIGHT
    }

    private StrategyController strategyController;
    private MovingController movingController;
    private ActionController actionController;

    private Enemy enemy;

    public DecisionController(Enemy enemy, Tile[][] tiles) {
        this.enemy = enemy;
        createStrategyController();
        movingController = new MovingController(tiles);
        movingController.setCurrentPosition(new Point2D.Double(enemy.getX(), enemy.getY()));
        actionController = new ActionController();
    }

    private void createStrategyController() {
        Genome genome = enemy.getGenome();
        strategyController = new StrategyController(genome.getBully(),
                                                    genome.getCoward(),
                                                    genome.getDemure(),
                                                    genome.getHunter(),
                                                    genome.getWarrior());
    }

    /*BEGIN #DECISION GETTERS#*/

    //BEGIN PUBLIC METHODS
    public DECISION getDecisionType() {
        return makeDecisionAboutType();
    } 

    public Point2D applyNextMovement() {
        if(!movingController.hasNextMovement()) {
            Point2D dest = this.findDestPoint();
            if (dest.equals(enemy.getLocation())) {
                return dest;
            }
            movingController.calculateWay(dest);
        }
        Point2D point = movingController.getNextMovement();
        movingController.setCurrentPosition(point);
        return point;
    }

    //END PUBLIC METHODS

    //BEGIN PRIVATE METHODS
    private DECISION makeDecisionAboutType() {
        if(rand.nextBoolean()) {
            return DECISION.MOVE;
        }
        else {
            return DECISION.FIGHT;
        }
    }

    //TODO: GET OPTIMAL POINT
    private Point2D findDestPoint() {
        //return new Point2D.Double(2.0, 3.0);
        ///*
        Random rand = new Random();
        Point2D dest = new Point2D.Double();
        dest.setLocation(rand.nextInt(movingController.getMapWidth()),movingController.getMapHeight());
        int a = 0, b = 0;
        while(!movingController.canMoveTo(dest) && !dest.equals(enemy.getLocation())) {
            a = rand.nextInt(movingController.getMapWidth());
            b = rand.nextInt(movingController.getMapHeight());
            dest.setLocation(a, b);
        }
        System.out.println("New: " + a + " : " + b);
        return dest;
        //*/

    }
    //END PRIVATE METHODS

    /*END #DECISION GETTERS#*/
}
