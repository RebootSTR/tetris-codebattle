package com.codenjoy.dojo.tetris.client.AI.figures;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Rotation;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.client.GlassBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Aydar Rafikov
 */
@NoArgsConstructor
@Setter
@Getter
public class Figure {

    Point[] currentPoints;
    Board board;
    int rotate;

    public Point[] getPoints(int x, int y) {
        switch (rotate) {
            case 1:
                return board.predictCurrentFigurePoints(new PointImpl(x, y), Rotation.CLOCKWIZE_90);
            case 2:
                return board.predictCurrentFigurePoints(new PointImpl(x, y), Rotation.CLOCKWIZE_180);
            case 3:
                return board.predictCurrentFigurePoints(new PointImpl(x, y), Rotation.CLOCKWIZE_270);
            default:
                return board.predictCurrentFigurePoints(new PointImpl(x, y));
        }
    }

    public Point getBetterCenter(int currentX, int currentY, Board board) {
        this.board = board;
        GlassBoard glassBoard = board.getGlass();
        currentPoints = getPoints(currentX, currentY);
        Point betterCenter = new PointImpl(8, currentY);
        double minimalCost = 1000;
        int betterRotate = -1;
        int minimalCountFreePointsBottom = 5;

        for (int i = 0; i < 4; i++) {
            rotate = i;

            List<Integer> possibleCenterX = new ArrayList<>();
            for (int x = 0; x < 18; x++) {
                if (isCanPlace(x, currentY, glassBoard)) {
                    possibleCenterX.add(x);
                }
            }

            for (Integer x : possibleCenterX) {
                int y = getMinimalCenterY(x, currentY, glassBoard);

                Point[] points = getPoints(x, y);

                int countFreeBottomPointsBottom = getCountFreePointBottom(points);
                double cost = getCost(points, countFreeBottomPointsBottom);
                if (cost < minimalCost) {
                    betterCenter.move(x, y);
                    betterRotate = rotate;
                    minimalCost = cost;
                }
            }
        }
        rotate = betterRotate;
        return betterCenter;
    }

    public boolean isCanPlace(int x, int y, GlassBoard glassBoard) {
        Point[] points = getPoints(x, y);
        for (Point point : points) {
            if (point.getY() < 0 ||
                    point.getX() < 0 || point.getX() > getRightWall()) {
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

    public int getMinimalCenterY(int x, int y, GlassBoard glassBoard) {
        while (isCanPlace(x, y, glassBoard)) {
            y--;
        }
        return y+1;
    }

    public int getRightWall() {
        return 17;
    }

    public int getCountFreePointBottom(Point[] points) {
        List<Point> downPoints = getBottomPoints(points);

        int count = 0;
        for (Point downPoint : downPoints) {
            Point pointBottom = new PointImpl(downPoint.getX(), downPoint.getY() - 1);
            while (board.getGlass().isFree(pointBottom.getX(), pointBottom.getY())) {
                count++;
                pointBottom.setY(pointBottom.getY() - 1);
            }
        }
        return count;
    }

    public double getCost(Point[] points, int countFreePoints) {
        int maximumY = getMaximumY(points);

        return Math.pow(maximumY, 1.1) +
                countFreePoints * 3.14;
    }

    public int getMaximumY(Point[] points) {
        int maximumY = -1;
        for (Point point : points) {
            if (point.getY() > maximumY) {
                maximumY = point.getY();
            }
        }
        return maximumY;
    }

    public int getMinimumY(Point[] points) {
        int minimumY = 100;
        for (Point point : points) {
            if (point.getY() < minimumY) {
                minimumY = point.getY();
            }
        }
        return minimumY;
    }

    public List<Point> getBottomPoints(Point[] points) {
        HashMap<Integer, Point> bottomPointsHashMap = new HashMap<>();
        for (Point point : points) {
            if (bottomPointsHashMap.containsKey(point.getX())) {
                if (bottomPointsHashMap.get(point.getX()).getY() > point.getY()) {
                    bottomPointsHashMap.put(point.getX(), point);
                }
            } else {
                bottomPointsHashMap.put(point.getX(), point);
            }
        }
        return new ArrayList<>(bottomPointsHashMap.values());
    }
}
