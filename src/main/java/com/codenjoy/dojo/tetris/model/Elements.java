package com.codenjoy.dojo.tetris.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.printer.CharElements;
import lombok.Getter;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:56 PM
 */
public enum Elements implements CharElements {

    BLUE('I', 2, 2),
    CYAN('J', 3, 4),
    ORANGE('L', 4, 4),
    YELLOW('O', 1, 1),
    GREEN('S', 5, 2),
    PURPLE('T', 7, 4),
    RED('Z', 6, 2),
    NONE('.', 0, 0);

    final char ch;
    final int index;
    @Getter
    final int countRotates;

    Elements(char ch, int index, int countRotates) {
        this.ch = ch;
        this.index = index;
        this.countRotates = countRotates;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public int index() {
        return index;
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }
}
