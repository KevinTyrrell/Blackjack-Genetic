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
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class Dealer extends Player
{
    private static int MINIMUM_DEALER_SCORE = 17;

    /**
     * Reference to the card which the dealer has revealed.
     */
    private final ReadOnlyObjectWrapper<Card> revealedCard = new ReadOnlyObjectWrapper<>();

    /**
     * Determines whether or not the player should hit.
     * A player may hit if their score is less than 21.
     * A dealer must hit if his maximum score is less than 17.
     *
     * @return true if the player should hit.
     */
    @Override public boolean hit()
    {
        return getMaximumScore() < MINIMUM_DEALER_SCORE;
    }

    /**
     * Accepts a dealt card, incrementing the score.
     * The dealer will also reveal the first card.
     *
     * @param card Card which is dealt.
     */
    @Override public void accept(final Card card)
    {
        if (getScore() <= 0)
            revealedCard.set(card);
        super.accept(card);
    }

    /**
     * Resets the dealers's field.
     */
    @Override public void reset()
    {
        revealedCard.set(null);
        super.reset();
    }

    /**
     * @return Reference to the dealer's revealed card.
     */
    public ReadOnlyObjectProperty<Card> getRevealedCard()
    {
        return revealedCard.getReadOnlyProperty();
    }

    /**
     * @return String representation of the dealer.
     */
    @Override public String toString()
    {
        return super.toString().replace("Player", "Dealer");
    }
}
