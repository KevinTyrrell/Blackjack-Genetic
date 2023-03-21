/*
 *     <one line to give the program's name and a brief idea of what it does.>
 *     Copyright (C) 2023  Kevin Tyrrell
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

package io;

import blackjack.BlackjackWatchable;
import blackjack.card.Card;
import blackjack.player.Dealer;
import blackjack.player.Player;

import java.util.List;
import java.util.Map;


public class BlackjackConsoleView implements BlackjackWatchable
{
    private static final String FMT_CARD_DEALT = "%s is dealt: %s"; // "Player A is dealt: A♠-🂡"

    /**
     * Indicates a round of Blackjack has begun
     */
    @Override public void roundStart()
    {
        System.out.println("A round of Blackjack has begun.");
    }

    /**
     * Indicates a player has been dealt a card
     *
     * @param p Player which was dealt a card
     * @param c Card which was dealt
     */
    @Override public void cardDealt(final Player p, final Card c)
    {
        System.out.printf((FMT_CARD_DEALT) + " %s%n", p, c, p.getField());
    }

    /**
     * Indicates the dealer has dealt a card to himself face down
     *
     * @param d Dealer who hid their card
     */
    @Override public void dealerHideCard(final Dealer d)
    {
        System.out.printf((FMT_CARD_DEALT) + "%n", d, "??-" + Card.CARD_BACK_SYMBOL);
    }

    /**
     * Indicates the dealer has revealed his face-down card
     * <p>
     * This always happens on the dealer's turn, whether they takes their turn or not
     *
     * @param d Dealer who revealed their card
     * @param c Card which was revealed
     * @param afterRound true if reveal is happening while the round is already over
     */
    @Override public void dealerRevealCard(final Dealer d, final Card c, final boolean afterRound)
    {
        final List<Card> f = d.getField();
        /*
         * Print the dealer's field when revealing their hidden card, to remind the user of their score
         * However, a problem arises because the reveal card event happens BEFORE the dealer is dealt their next card.
         * This results in the reveal text showing the field containing a card that has yet to be dealt out.
         * The solution to this problem is to omit the last card of the dealer's field IF the game is in progress.
         */
        System.out.printf("The Dealer reveals: %s %s\n", c, afterRound ? f : f.subList(0, f.size() - 1));
    }

    /**
     * Indicates a player has hit into a 'bust'
     *
     * @param p Player who has bust
     */
    @Override public void playerBust(final Player p)
    {
        System.out.printf("BUST! %s is out of the round.\n", p);
    }

    /**
     * Indicates a player has a 'Blackjack'
     *
     * @param p Player who has a Blackjack
     */
    @Override public void playerBlackjack(final Player p)
    {
        System.out.printf("BLACKJACK! %s is out of the round.\n", p);
    }

    /**
     * Indicates the round is over
     */
    @Override public void reset(final Map<Player, Integer> results)
    {
        for (final Map.Entry<Player, Integer> e : results.entrySet())
        {
            switch (e.getValue())
            {
                case -1: System.out.println(e.getKey() + " has lost the round."); break;
                case 1: System.out.println(e.getKey() + " has won the round."); break;
                default: System.out.println(e.getKey() + " has pushed with the dealer.");
            }
        }
        System.out.print("\n\n");
    }
}
