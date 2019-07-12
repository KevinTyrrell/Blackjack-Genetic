/*
 *     <one line to give the program's name and a brief idea of what it does.>
 *     Copyright (C) 2019  Kevin Tyrrell
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
 * Defines a Poker card face, from Ace to King.
 * Ace has a special point rule in Blackjack.
 * Ten, Jack, Queen, and King share the same point value.
 *
 * @since 1.0
 */
public enum Face
{
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10);

    private final int value;
    private final String name;

    private static final Set<Face> set = Collections.unmodifiableSet(EnumSet.allOf(Face.class));

    /**
     * @return Read-only set of Face enum values.
     */
    public static Set<Face> set()
    {
        return set;
    }

    Face(final int value)
    {
        assert value > 0;
        this.value = value;
        final String n = name();
        name = n.charAt(0) + n.substring(1).toLowerCase();
    }

    /**
     * Retrieve the amount of points the card face is worth.
     * Note: Aces return a value of 1, but can be treated as 11.
     *
     * @return Value of the card face.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * @return Name of the card face.
     */
    public String getName()
    {
        return name;
    }

    /**
     * This is equivalent to calling Face.getName()
     *
     * @return String representation of the card face.
     */
    @Override public String toString()
    {
        return name;
    }
}
