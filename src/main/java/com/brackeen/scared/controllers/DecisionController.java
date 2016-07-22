package com.brackeen.scared.controllers;

import com.brackeen.scared.entity.Enemy;
import com.brackeen.scared.genetic.Genome;

/**
 * Created by pisatel on 22.07.16.
 */
public class DecisionController {

    StrategyController strategyController;
    Enemy enemy;

    public DecisionController(Enemy enemy) {
        this.enemy = enemy;
        createStrategyController();
    }

    private void createStrategyController() {
        Genome genome = enemy.getGenome();
        strategyController = new StrategyController(genome.getBully(),
                                                    genome.getCoward(),
                                                    genome.getDemure(),
                                                    genome.getHunter(),
                                                    genome.getWarrior());
    }
}
