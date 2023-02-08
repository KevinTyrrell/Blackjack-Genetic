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

import java.util.Objects;

/**
 * Defines a Poker card, feature a face and a suit.
 * The suit has no impact in traditional Blackjack.
 *
 * @since 1.0
 */
public class Card
{
    private final Face face;
    private final Suit suit;

    /**
     * Number of cards inside a standard playing-card deck.
     */
    public static final int CARDS_IN_DECK = 52;

    /**
     * @param face Face of the card.
     * @param suit Suit of the card.
     */
    public Card(final Face face, final Suit suit)
    {
        this.face = Objects.requireNonNull(face);
        this.suit = Objects.requireNonNull(suit);
    }

    /**
     * @return Face of the card.
     */
    public Face getFace()
    {
        return face;
    }

    /**
     * @return Suit of the card.
     */
    public Suit getSuit()
    {
        return suit;
    }

    /**
     * @return String representation of the card.
     */
    @Override public String toString()
    {
        return String.format("%s of %s", face.toString(), suit.toString());
    }
}
