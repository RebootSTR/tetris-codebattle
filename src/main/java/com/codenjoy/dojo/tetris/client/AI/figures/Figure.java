package com.codenjoy.dojo.tetris.client.AI.figures;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.tetris.client.GlassBoard;
import com.codenjoy.dojo.tetris.model.Elements;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aydar Rafikov
 */
@NoArgsConstructor
@Setter
@Getter
public abstract class Figure {

    Point[] currentPoints;

    abstract Point[] getPoints(int x, int y);

    public Point getBetterCenter(int currentX, int currentY, GlassBoard glassBoard) {
        currentPoints = getPoints(currentX, currentY);

        List<Integer> possibleCenterX = new ArrayList<>();
        for (int x = 0; x < 18; x++) {
            if (isCanPlace(x, currentY, glassBoard)) {
                possibleCenterX.add(x);
            }
        }
        Point betterCenter = new PointImpl(possibleCenterX.get(0), currentY);
        for (Integer x : possibleCenterX) {
            int y = getMinimalY(x, currentY, glassBoard);
            if (y < betterCenter.getY()) {
                betterCenter.move(x, y);
            }
        }

        return betterCenter;
    }

    public boolean isCanPlace(int x, int y, GlassBoard glassBoard) {
        Point[] points = getPoints(x, y);
        for (Point point : points) {
            if (point.getY() < 0 ||
                    point.getX() < 0 || point.getX() > 17) {
                return false;
            }
            // Если не (клетка_свободна или клетка_за_экраном) и не клетка_занята_мной
            if (!(glassBoard.isFree(point.getX(), point.getY()) || point.getY() > 17) &&
                    !Arrays.asList(currentPoints).contains(point)) {
                return false;
            }
        }
        return true;
    }

    public int getMinimalY(int x, int y, GlassBoard glassBoard) {
        while (isCanPlace(x, y, glassBoard)) {
            y--;
        }
        return y+1;
    }

}
