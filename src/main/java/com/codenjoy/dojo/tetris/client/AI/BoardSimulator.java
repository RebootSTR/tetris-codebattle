package com.codenjoy.dojo.tetris.client.AI;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.client.GlassBoard;
import lombok.Getter;
import scala.Char;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aydar Rafikov
 */
@Getter
public class BoardSimulator {

    public static final String NOTHING = "..................";

    /**
     * Костыль, стобы не врезаться в потолок
     */
    public static final int COUNT_NOTHING = 4;

    private int countFreeSpace;
    private int size;

    List<StringBuilder> layers;

    public BoardSimulator() {
        size = 18+COUNT_NOTHING;
    }

    public void setLayers(String layersAsString, Point[] currentFigurePoints) {
        restore(layersAsString);
        remove(currentFigurePoints);
    }

    public void restore(String layersAsString) {
        List<String> layersAsList = new ArrayList<>(splitOn18(layersAsString));
        this.layers = new ArrayList<>();
        for (String layer : layersAsList) {
            this.layers.add(new StringBuilder(layer));
        }
    }

    public void remove(Point[] points) {
        for (Point point : points) {
            layers.get(offsetY(point.getY())).setCharAt(point.getX(), '.');
        }
    }

    public String getBackup() {
        StringBuilder backup = new StringBuilder();
        for (int i = COUNT_NOTHING; i < layers.size(); i++) {
            backup.append(layers.get(i));
        }
        return backup.toString();
    }

    /**
     * Делит исходную строку содержащую поле на список строк по 18 значений
     * @param toSplit
     * @return
     */
    public List<String> splitOn18(String toSplit) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < COUNT_NOTHING; i++) {
            result.add(NOTHING);
        }
        for (int i = 0; i < 18*18; i+=18) {
            result.add(toSplit.substring(i, i+18));
        }
        return result;
    }

    public boolean isFree(int x, int y) {
        int banColumn = countFreeSpace < 396 - 85 ? 19 : 17;

        if (x < 0 || x > 17 || y < 0 || y > 17) {
            return false;
        }
        if (layers.get(offsetY(y)).charAt(x) != '.') {
            return false;
        }
        if (x == banColumn) {
            return false;
        }
        return true;
    }

    public int getCountFreeSpace() {
        List<Character> freeSpaces = new ArrayList<>();
        layers.forEach(l -> {
            for (char ch : l.toString().toCharArray()) {
                if (ch == '.') {
                    freeSpaces.add(ch);
                }
            }
        });
        return freeSpaces.size();
    }

    public void put(Point[] points) {
        for (Point point : points) {
            layers.get(offsetY(point.getY())).setCharAt(point.getX(), '#');
        }
        countFreeSpace = getCountFreeSpace();
    }

    public int clear() {
        List<Integer> toClear = new ArrayList<>();
        for (int i = 1; i < layers.size(); i++) {
            if (layers.get(i).indexOf(".") == -1) {
                toClear.add(i);
            }
        }
        for (int i : toClear) {
            for (int j = i; j < 0; j++) {
                layers.set(j, layers.get(j-1));
            }
        }
        return toClear.size();
    }

    public double getCost(double startCost) {
        // очищенные ряды
        int cleared = clear();

        // отверстия
        int holes = getCountHoles();
        // колодцы
        int sumps = getCountSumps();
        // вертикальные переходы
        int columnJumps = getCountColumnJumps();
        // горизонтальные переходы
        int rowJumps = getCountRowJumps();

        double cost = startCost;

        // бесполезная идея, но на всякий случай пусть будет, мб придумаю что сделать можно
        Stratrgy stratrgy = Stratrgy.POINTS;
        switch (stratrgy) {
            case SURVIVAL:
                cost += holes * 26.8;
                cost += cleared;
                cost += sumps * 15.8;
                cost += columnJumps * 27.6;
                cost += rowJumps * 30.2;
                break;
            case POINTS:
                cost += holes * 26.8;
                cost -= Math.pow(cleared, 8);
                cost += sumps * 15.8;
                cost += columnJumps * 27.6;
                cost += rowJumps * 30.2;
                break;
        }

        return cost;
    }

    private int getCountHoles() {
        int countHoles = 0;

        for (int i = 1; i < size; i++) {
            for (int j = 0; j < 18; j++) {
                if (layers.get(i).charAt(j) == '.') {
                    if (layers.get(i-1).charAt(j) != '.') {
                        countHoles++;
                    }
                }
            }
        }

        return countHoles;
    }

    private int getCountSumps() {
        int countSumps = 0;

        for (int y = 0; y < 18; y++) {
            for (int x = 0; x < 18; x++) {
                if (isFree(x, y)) {
                    if (isFree(x, y+1) && !isFree(x-1, y) && !isFree(x+1, y)) {
                        countSumps++;
                        break;
                    }
                }
            }
        }

        return countSumps;
    }

    private int getCountColumnJumps() {
        int countColumnJumps = 0;

        for (int x = 0; x < 18; x++) {
            boolean hasJump = false;
            for (int y = 1; y < 18; y++) {
                if (isFree(x, y-1) != isFree(x,y)) {
                    countColumnJumps++;
                    hasJump = true;
                }
            }
            // верхний переход не учитывется
            if (hasJump) {
                countColumnJumps--;
            }
        }

        return countColumnJumps;
    }

    private int getCountRowJumps() {
        int countRowJumps = 0;

        for (int y = 0; y < 18; y++) {
            if (layers.get(offsetY(y)).toString().matches("\\.{18}")) {
                break;
            }
            for (int x = 0; x < 19; x++) {
                if (isFree(x-1, y) != isFree(x,y)) {
                    countRowJumps++;
                }
            }
        }

        return countRowJumps;
    }

    /**
     * Расчет смещения для импользования Y в layers.get как координаты поля.
     * @param y координата на поле
     */
    private int offsetY(int y) {
        return size - 1 - y;
    }

    /**
     * Выводит поле, для отладки
     */
    private void print() {
        layers.forEach(System.out::println);
        System.out.println("**********************************************8");
    }
}
