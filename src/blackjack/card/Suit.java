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
 * Defines a Poker card suit, from Spades to Diamonds.
 * Suits have no impact in traditional Blackjack.
 *
 * @since 1.0
 */
public enum Suit
{
    SPADES, HEARTS, CLUBS, DIAMONDS;

    private final String name;

    private static final Set<Suit> set = Collections.unmodifiableSet(EnumSet.allOf(Suit.class));

    /**
     * @return Read-only set of Suit enum values.
     */
    public static Set<Suit> set()
    {
        return set;
    }

    Suit()
    {
        final String n = name();
        name = n.charAt(0) + n.substring(1).toLowerCase();
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
