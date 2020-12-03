package com.codenjoy.dojo.tetris.client.AI;

import com.codenjoy.dojo.services.Command;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.tetris.client.AI.figures.*;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.client.GlassBoard;
import com.codenjoy.dojo.tetris.model.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aydar Rafikov
 */
public class Bot {

    public List<Command> getAnswerList(Board board) {

        GlassBoard glassBoard = board.getGlass();
        Elements type = board.getCurrentFigureType();
        Point currentCenter = board.getCurrentFigurePoint();
        Point betterCenter = null;
        Figure figure = null;
        //     BLUE('I', 2),
        //    CYAN('J', 3),
        //    ORANGE('L', 4),
        //    YELLOW('O', 1),
        //    GREEN('S', 5),
        //    PURPLE('T', 7),
        //    RED('Z', 6),
        //    NONE('.', 0);

        switch (type) {
            case YELLOW:
                figure = new O_Figure();
                break;
            case BLUE:
                figure = new I_Figure();
                break;
            case CYAN:
                figure = new J_Figure();
                break;
            case ORANGE:
                figure = new L_Figure();
                break;
            case GREEN:
                figure = new S_Figure();
                break;
            case PURPLE:
                figure = new T_Figure();
                break;
            case RED:
                figure = new Z_Figure();
                break;
            default:
                System.out.println("IDK THIS TYPE");
        }

        if (figure != null) {
            betterCenter = figure.getBetterCenter(currentCenter.getX(), currentCenter.getY(), glassBoard);
        }

        List<Command> result = new ArrayList<Command>();
        if (betterCenter == null) {
            return result;
        }

        int difference = currentCenter.getX() - betterCenter.getX();
        if (difference > 0) {
            for (int i = 0; i < difference; i++) {
                result.add(Command.LEFT);
            }
        } else {
            for (int i = 0; i < -difference; i++) {
                result.add(Command.RIGHT);
            }
        }

        result.add(Command.DOWN);


        System.out.println(type);
        System.out.println(betterCenter.getX() + " better " + betterCenter.getY());
        System.out.println(currentCenter.getX() + " current " + currentCenter.getY());
        System.out.println(result);

        return result;
    }
}
