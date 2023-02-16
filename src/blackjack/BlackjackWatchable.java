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

package blackjack;

import blackjack.card.Card;
import blackjack.player.Dealer;
import blackjack.player.Player;


public interface BlackjackWatchable
{
    /**
     * Indicates a round of Blackjack has begun
     */
    void roundStart();

    /**
     * Indicates a player has been dealt a card
     *
     * @param p Player which was dealt a card
     * @param c Card which was dealt
     */
    void cardDealt(final Player p, final Card c);

    /**
     * Indicates the dealer has dealt a card to himself face down
     *
     * @param d Dealer who hid their card
     */
    void dealerHideCard(final Dealer d);

    /**
     * Indicates the dealer has revealed his face-down card
     *
     * This always happens on the dealer's turn, whether they takes their turn or not
     *
     * @param d Dealer who revealed their card
     * @param c Card which was revealed
     */
    void dealerRevealCard(final Dealer d, final Card c);

    /**
     * Indicates a player has hit into a 'bust'
     *
     * @param p Player who has bust
     */
    void playerBust(final Player p);

    /**
     * Indicates a player has a 'Blackjack'
     *
     * @param p Player who has a Blackjack
     */
    void playerBlackjack(final Player p);

    /**
     * Indicates the round is over
     */
    void reset();
}
