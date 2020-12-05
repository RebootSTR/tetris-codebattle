package com.codenjoy.dojo.tetris.client.AI;

/**
 * @author Aydar Rafikov
 */
public enum Stratrgy {

    SURVIVAL,
    POINTS;

    public static Stratrgy getStrategy(int countFreeSpace) {
        if (countFreeSpace < 144) {
            return Stratrgy.SURVIVAL;
        } else return Stratrgy.POINTS;
    }
}
