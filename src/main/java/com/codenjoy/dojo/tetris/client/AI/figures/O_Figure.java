package com.codenjoy.dojo.tetris.client.AI.figures;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.tetris.client.GlassBoard;
import com.codenjoy.dojo.tetris.model.Elements;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aydar Rafikov
 */
@Getter
@Setter
public class O_Figure extends Figure {

    @Override
    public Point[] getPoints(int x, int y) {
        Point[] points = new Point[4];
        Point center = new PointImpl(x, y);
        points[0] = center;
        points[1] = center.copy().shiftRight(1);
        points[2] = center.copy().shiftBottom(1);
        points[3] = center.copy().shiftRight(1).shiftBottom(1);
        return points;
    }
}
