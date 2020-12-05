package com.codenjoy.dojo.tetris.client.AI;

/**
 * @author Aydar Rafikov
 */
public class BoardSimulatorWithAllOLogic extends BoardSimulator {

    @Override
    public void updateBan() {
        ban = false;
    }

    @Override
    public double getCost(double startCost) {
        return startCost;
    }
}
