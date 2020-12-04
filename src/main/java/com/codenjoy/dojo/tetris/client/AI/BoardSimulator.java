package com.codenjoy.dojo.tetris.client.AI;

import com.codenjoy.dojo.services.Point;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aydar Rafikov
 */
@Getter
public class BoardSimulator {

    public static final String NOTHING = "..................";

    String oldLayersAsString;
    List<StringBuilder> layers;

    public void setLayers(String layersAsString) {
        List<String> layersAsList = new ArrayList<>(splitOn18(layersAsString));
        oldLayersAsString = layersAsString;
        this.layers = new ArrayList<>();
        for (String layer : layersAsList) {
            this.layers.add(new StringBuilder(layer));
        }
    }

    public List<String> splitOn18(String toSplit) {
        List<String> result = new ArrayList<>();
        result.add(NOTHING);
        result.add(NOTHING);
        result.add(NOTHING);
        result.add(NOTHING);
        for (int i = 0; i < 18*18; i+=18) {
            result.add(toSplit.substring(i, i+18));
        }
        return result;
    }

    public void put(Point[] points) {
        for (Point point : points) {
            layers.get(layers.size() - 1 - point.getY()).setCharAt(point.getX(), ' ');
        }
    }

    public int countReadyToClearRows() {
        int count = 0;
        for (StringBuilder layer : layers) {
            if (layer.indexOf(".") == -1) {
                count++;
            }
        }
        // System.out.print(count + " ");
        return count;
    }

    public void reset() {
        setLayers(oldLayersAsString);
    }
}
