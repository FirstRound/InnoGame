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

    public Point2D getNextMovement() {
        if(!movingController.hasNextMovement()) {
            Point2D dest = this.findDestPoint();
            movingController.calculateWay(dest);
        }
        return movingController.getNextMovement();
    }

    //END PUBLIC METHODS

    //BEGIN PRIVATE METHODS
    private DECISION makeDecisionAboutType() {
        return DECISION.MOVE;
    }

    //TODO: GET OPTIMAL POINT
    private Point2D findDestPoint() {
        Random rand = new Random();
        Point2D dest = new Point2D.Double();
        dest.setLocation(rand.nextInt(movingController.getMapWidth()),movingController.getMapHeight());
        while(!movingController.canMoveTo(dest)) {
            dest.setLocation(rand.nextInt(movingController.getMapWidth()),movingController.getMapHeight());
        }
        return dest;
    }
    //END PRIVATE METHODS

    /*END #DECISION GETTERS#*/
}
