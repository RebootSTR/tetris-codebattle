package com.codenjoy.dojo.tetris.client.AI.figures;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aydar Rafikov
 */
@Getter
@Setter
public class J_Figure extends Figure {

//    @Override
//    public Point[] getPoints(int x, int y) {
//        Point[] points = new Point[4];
//        Point center = new PointImpl(x, y);
//        points[0] = center;
//        points[1] = center.copy().shiftTop(1);
//        points[2] = center.copy().shiftBottom(1);
//        points[3] = center.copy().shiftLeft(1).shiftBottom(1);
//        return points;
//    }
}
