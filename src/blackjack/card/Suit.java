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

import static java.util.Objects.requireNonNull;


/**
 * Defines a Poker card suit
 *
 * Suits and their ranks have no relevance in traditional Blackjack
 */
public enum Suit
{
    SPADES("♠"),
    HEARTS("♥"),
    CLUBS("♦"),
    DIAMONDS("♣");

    private final String name;
    private final String symbol;

    private static final Set<Suit> set = Collections.unmodifiableSet(EnumSet.allOf(Suit.class));

    /**
     * @return Read-only set of Suit enum values
     */
    public static Set<Suit> set()
    {
        return set;
    }

    Suit(final String symbol)
    {
        final String n = name();
        name = n.charAt(0) + n.substring(1).toLowerCase();
        this.symbol = requireNonNull(symbol);
    }

    /**
     * @return Name of the card suit
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return Unicode symbol of the card suit
     */
    public String getSymbol()
    {
        return symbol;
    }

    /**
     * @return String representation of the card suit
     */
    @Override public String toString()
    {
        return name;
    }
}
