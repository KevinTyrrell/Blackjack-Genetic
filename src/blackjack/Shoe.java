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

package blackjack;

import blackjack.card.Card;
import blackjack.card.Face;
import blackjack.card.Suit;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static blackjack.Blackjack.rand;

public class Shoe
{
    private final List<Card> shoe;
    private final int size;

    /* Indicator which acts as a wall for cards which have already been dealt. */
    private int cardsDealt = 0;

    /**
     * @param decks Number of decks to be included into the shoe.
     */
    public Shoe(final int decks)
    {
        if (decks < 1)
            throw new IllegalArgumentException("Number of decks in the shoe must be possible and non-zero");
        size = decks * Card.CARDS_IN_DECK;
        shoe = new ArrayList<>(size);
        for (final Suit suit : Suit.set())
            for (final Face face : Face.set())
            {
                final Card card = new Card(face, suit);
                for (int i = 0; i < decks; i++)
                    shoe.add(card);
            }
    }

    /**
     * Deals a random card from the deck.
     *
     * Performs fischer-yates-like selection as each individual card is dealt.
     * An exception will be thrown if the shoe is empty.
     *
     * @return Card which was dealt.
     */
    public Card deal()
    {
        if (cardsDealt >= size)
            throw new NoSuchElementException("Shoe is empty");
        /* Randomly select a card from the unused partition of the shoe. */
        final int dealtIndex = cardsDealt + rand.nextInt(size - cardsDealt);
        final Card dealt = shoe.get(dealtIndex);
        /* Swap the card we chose with a different un-chosen card. */
        shoe.set(dealtIndex, shoe.get(cardsDealt));
        shoe.set(cardsDealt++, dealt);
        return dealt;
    }

    /**
     * Shuffles the shoe, allowing cards previously dealt to be dealt again.
     * A shoe should not be shuffled if dealt cards are still in-play.
     */
    public void shuffle()
    {
        cardsDealt = 0;
    }

    /**
     * @return Maximum number of cards contained in the shoe.
     */
    public int size()
    {
        return size;
    }

    /**
     * @return Number of cards which have yet to be dealt.
     */
    public int cardsLeft()
    {
        return size - cardsDealt;
    }
}
