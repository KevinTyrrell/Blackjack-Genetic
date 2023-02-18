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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Defines a shoe at a Blackjack table
 *
 * A shoe is a larger deck made up of multiple decks
 */
public class Shoe
{
    private final List<Card> shoe;
    private final int size;
    private final Random generator;

    /* Indicates which cards of the shoe have already been dealt */
    private int cardsDealt = 0;

    /* Standard deck size, one card of each face and suit */
    private static final int DEFAULT_DECK_SIZE = Face.values().length * Suit.values().length;

    /**
     * @param decks Number of decks to be shuffled together
     * @param seed Random seed sequence
     */
    public Shoe(final int decks, final long seed)
    {
        if (decks < 1)
            throw new IllegalArgumentException("Number of decks in the shoe must be possible and non-zero");
        generator = new Random(seed);
        size = decks * DEFAULT_DECK_SIZE;
        shoe = Suit.set().stream()
                .flatMap(s -> Face.set().stream()
                        .flatMap(f -> Stream.generate(() -> new Card(f, s)).limit(decks)))
                .collect(Collectors.toList());
    }

    /**
     * Deals a random card from the deck
     *
     * Performs a fischer-yates shuffle step on every card dealt.
     * After all cards are dealt, the shoe is effectively shuffled as-is.
     *
     * @return Card which was dealt
     */
    public Card deal()
    {
        if (cardsDealt >= size)
            throw new NoSuchElementException("Shoe is empty");
        /* Randomly select a card from the unused partition of the shoe */
        final int dealtIndex = cardsDealt + generator.nextInt(size - cardsDealt);
        final Card dealt = shoe.get(dealtIndex);
        /* Swap the dealt card with whatever card is at the bottom of the deck */
        shoe.set(dealtIndex, shoe.get(cardsDealt));
        shoe.set(cardsDealt++, dealt);
        return dealt;
    }

    /**
     * @return Percentage [0.0,1.0] of how many cards of the shoe have been dealt out
     */
    public float penetration()
    {
        return cardsDealt / (float)size;
    }

    /**
     * Shuffles the shoe
     *
     * A shoe should not be shuffled if dealt cards are still in-play
     */
    public void shuffle()
    {
        cardsDealt = 0;
    }

    /**
     * @return Maximum number of cards contained in the shoe
     */
    public int size()
    {
        return size;
    }
}
