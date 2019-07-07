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

import java.util.Objects;

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
}