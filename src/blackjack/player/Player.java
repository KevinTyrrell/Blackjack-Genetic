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

package blackjack.player;

import blackjack.card.Card;
import blackjack.card.Face;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;


/**
 * Defines a Blackjack player
 *
 * Any participant at the table is considered a player
 */
public abstract class Player implements Consumer<Card>
{
    private final List<Card> field = new ArrayList<>();

    private int score = 0;
    private boolean hasAce = false;
    private final String name;

    /* Maximum player score - anything beyond is considered a 'bust' */
    public static final int MAXIMUM_SCORE = 21;

    private static final int ACE_ADDITIONAL_SCORE = 10;
    private static final String DEFAULT_NAME_FORMAT = "P-%s";

    public Player(final String name)
    {
        this.name = format(DEFAULT_NAME_FORMAT, requireNonNull(name));
    }

    public Player()
    {
        this.name = format(DEFAULT_NAME_FORMAT, Integer.toHexString(hashCode()));
    }

    /**
     * Determines whether or not the player should hit
     *
     * A player may hit if their score is less than 21.
     * A dealer must hit if his maximum score is less than 17.
     *
     * @return true if the player should hit
     */
    public abstract boolean hit();

    /**
     * Accepts a dealt card, incrementing the score
     *
     * @param card Card which is dealt to the player
     */
    @Override public void accept(final Card card)
    {
        field.add(requireNonNull(card));
        final Face f = card.getFace();
        score += f.getValue();
        if (f == Face.ACE)
            hasAce = true;
    }

    /**
     * @return true if the player has an ace in their hand
     */
    public boolean hasAce()
    {
        return hasAce;
    }

    /**
     * Calculates the player's maximum potential score
     *
     * A player's score can differ depending on if an ace is treated with a value of 1 or 11.
     * If a player's maximum score would bust, his normal score is returned.
     *
     * @return Current score of the player, avoiding busts
     */
    public int getSoftScore()
    {
        if (hasAce)
        {
            final int possible = score + ACE_ADDITIONAL_SCORE;
            if (possible <= MAXIMUM_SCORE)
                return possible;
        }

        return score;
    }

    /**
     * @return Current player's score, treating aces with a value of 1
     */
    public int getHardScore()
    {
        return score;
    }

    /**
     * @return Read-only list of cards on the player's field
     */
    public List<Card> getField()
    {
        return Collections.unmodifiableList(field);
    }

    /**
     * @return true if the player's score exceeds the 'bust' threshold
     */
    public boolean hasBusted()
    {
        return score > MAXIMUM_SCORE;
    }

    /**
     * @return true if the player's score is 21 (Blackjack)
     */
    public boolean hasBlackjack()
    {
        return getSoftScore() == MAXIMUM_SCORE;
    }

    /**
     * Resets the player's field
     */
    public void reset()
    {
        score = 0;
        hasAce = false;
        field.clear();
    }

    /**
     * @return String representation of the player
     */
    @Override public String toString()
    {
        return name;
    }
}
