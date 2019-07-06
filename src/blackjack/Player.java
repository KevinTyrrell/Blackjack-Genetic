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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class Player implements Consumer<Card>
{
    private final List<Card> field = new ArrayList<>();

    private int score = 0;
    private boolean hasAce = false;

    /* Maximum score a player can have before busting. */
    public static int MAXIMUM_SCORE = 21;
    private static int ACE_ADDITIONAL_SCORE = 10;

    /**
     * Determines whether or not the player should hit.
     * A player may hit if their score is less than 21.
     * A dealer must hit if his maximum score is less than 17.
     *
     * @return true if the player should hit.
     */
    public abstract boolean hit();

    /**
     * Accepts a dealt card, incrementing the score.
     *
     * @param card Card which is dealt.
     */
    @Override public void accept(final Card card)
    {
        field.add(Objects.requireNonNull(card));
        final Face f = card.getFace();
        score += f.getValue();
        if (f == Face.ACE)
            hasAce = true;
    }

    /**
     * Calculates the player's maximum potential score.
     * A player's score can differ depending on if an ace is treated as a 1 or 11.
     * If a player's maximum score would bust, his normal score is returned.
     *
     * @return Current score of the player, avoiding busts.
     */
    public int getMaximumScore()
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
     * @return Current player's score, treating aces as value of 1.
     */
    public int getScore()
    {
        return score;
    }

    /**
     * @return Read-only list of cards on the player's field.
     */
    public List<Card> getField()
    {
        return Collections.unmodifiableList(field);
    }

    /**
     * @return true if the player busted.
     */
    public boolean hasBusted()
    {
        return score <= MAXIMUM_SCORE;
    }

    /**
     * Resets the player's field.
     */
    public void reset()
    {
        score = 0;
        hasAce = false;
        field.clear();
    }

    /**
     * @return String representation of the player.
     */
    @Override public String toString()
    {
        return String.format("Player{score=%d,maxScore=%d,field=%s}", score, getMaximumScore(), field.toString());
    }
}
