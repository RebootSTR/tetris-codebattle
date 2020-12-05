package com.codenjoy.dojo.tetris.client.AI;

import com.codenjoy.dojo.services.Command;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.client.GlassBoard;
import com.codenjoy.dojo.tetris.model.Elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Aydar Rafikov
 */
public class Bot {

    public static boolean allOMode = true;

    public void checkAllOMode(Elements type) {
        if (allOMode) {
            if (!type.equals(Elements.YELLOW)) {
                allOMode = false;
            }
        }
    }

    public List<Command> getAnswerList(Board board) {
        Elements type = board.getCurrentFigureType();
        Point currentCenter = board.getCurrentFigurePoint();
        Point betterCenter = null;

        checkAllOMode(type);

        Queue<Elements> elements = new LinkedList<>();

        elements.add(type);
        if (!allOMode) {
            elements.add(board.getFutureFigures().get(0)); // самое то
        }
//        elements.add(board.getFutureFigures().get(1)); // 3 фигуры реально, но долго, появляются артефакты
//        elements.add(board.getFutureFigures().get(2)); // 4 тоже фиг просчитаешь
//        elements.add(board.getFutureFigures().get(3)); // просчитать 5 фигур невозможно

        System.out.println(elements);

        Calculator.calculateBetterCenter(
                board.getGlass().getLayersString().get(0),
                elements,
                board.predictCurrentFigurePoints()
        );
        betterCenter = Calculator.betterCenter;

        List<Command> result = new ArrayList<>();
        if (betterCenter == null) {
            return result;
        }

        // определение нажатий относительно центра и поворота
        int difference = currentCenter.getX() - betterCenter.getX();
        if (Calculator.betterRotate > 0) {
            for (int i = 0; i < Calculator.betterRotate; i++) {
                result.add(Command.ROTATE_CLOCKWISE_90);
            }
        }
        if (difference > 0) {
            for (int i = 0; i < difference; i++) {
                result.add(Command.LEFT);
            }
        } else {
            for (int i = 0; i < -difference; i++) {
                result.add(Command.RIGHT);
            }
        }

        // сброс фигуры вниз
        result.add(Command.DOWN);

        // вывод небольшой информации
        System.out.println(type);
        System.out.println(result);

        return result;
    }
}
