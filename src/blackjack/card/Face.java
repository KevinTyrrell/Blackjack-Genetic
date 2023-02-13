/*
 *     Genetic algorithm which teaches agents how to play Blackjack.
 *     Copyright (C) 2019-2023  Kevin Tyrrell
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package blackjack.card;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Defines a Poker card face, from Ace to King
 *
 * Ten, Jack, Queen, and King share the same point value.
 * Ace's point value increases by 10 if the hand's value is less than 12.
 */
public enum Face
{
    ACE(1, 'A'),
    TWO(2, '2'),
    THREE(3, '3'),
    FOUR(4, '4'),
    FIVE(5, '5'),
    SIX(6, '6'),
    SEVEN(7, '7'),
    EIGHT(8, '8'),
    NINE(9, '9'),
    TEN(10, 'T'),
    JACK(10, 'J'),
    QUEEN(10, 'Q'),
    KING(10, 'K');

    private final int value;
    private final String name;
    private final char letter;

    private static final Set<Face> set = Collections.unmodifiableSet(EnumSet.allOf(Face.class));

    /**
     * @return Read-only set of Face enum values
     */
    public static Set<Face> set()
    {
        return set;
    }

    Face(final int value, final char letter)
    {
        assert value > 0;
        this.value = value;
        final String n = name();
        assert n.length() > 1;
        name = n.charAt(0) + n.substring(1).toLowerCase();
        this.letter = letter;
    }

    /**
     * Retrieve the amount of points the card face is worth
     *
     * @return Value of the card face
     */
    public int getValue()
    {
        return value;
    }

    /**
     * @return Name of the card face
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return letter symbol representing the card face
     */
    public char getLetter()
    {
        return letter;
    }

    /**
     * @return String representation of the card face
     */
    @Override public String toString()
    {
        return name;
    }
}
