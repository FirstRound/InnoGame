package com.brackeen.scared.controllers;

import com.brackeen.scared.entity.Enemy;
import com.brackeen.scared.genetic.Genome;
import com.brackeen.scared.Tile;

/**
 * Created by pisatel on 22.07.16.
 */
public class DecisionController {

    public enum DECISION {
        MOVE, RUN, FIGHT
    }

    private StrategyController strategyController;
    private EnemyMovingController movingController;
    private ActionController actionController;

    private Enemy enemy;

    public DecisionController(Enemy enemy, Tile[][] tiles) {
        this.enemy = enemy;
        createStrategyController();
        movingController = new EnemyMovingController(tiles);
        movingController.setCurrentPosition(new POint2D(enemy.getX(), enemy.getY()));
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

    public Point2D getNextWaypoint() {
        return movingController.calculateNextWaypoint();
    }
    //END PUBLIC METHODS

    //BEGIN PRIVATE METHODS
    private DECISION makeDecisionAboutType() {
        return DECISION.MOVE;
    }
    //END PRIVATE METHODS

    /*END #DECISION GETTERS#*/
}
