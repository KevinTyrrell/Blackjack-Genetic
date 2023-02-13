/*
 *     Genetic algorithm which teaches agents how to play Blackjack.
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

package blackjack;

import blackjack.card.Card;
import blackjack.player.Dealer;
import blackjack.player.Player;

import java.util.List;
import java.util.Map;

import static blackjack.ConsoleBlackjack.BlackjackState.*;


public class ConsoleBlackjack extends Blackjack
{
    private BlackjackState state = INIT;

    private Map<Player, Integer> results = null;
    private Player dealer = null;
    private Card hiddenCard = null;
    private int cardsDealt = 0;

    private static final String FMT_CARD_DEALT = "%s is dealt: %s"; // "Player A is dealt: A♠-🂡"

    public ConsoleBlackjack(int shoeSize, long seed, float penetration)
    {
        super(shoeSize, seed, penetration);
    }

    @Override public void reset()
    {
        switch (state)
        {
            case AWAIT_DEALER:
                /*
                 * Game ended without dealer needing to deal himself a card. ex.
                 * -> All players busted, therefore dealer does not need to take his turn.
                 * -> Dealer stood or got Blackjack with two cards.
                 * In these scenarios, we still have to reveal the hidden card.
                 */
                System.out.printf("The Dealer reveals: %s %s\n", hiddenCard, dealer.getField());

        }

        for (Map.Entry<Player, Integer> e : results.entrySet())
        {
            switch (e.getValue())
            {
                case -1: System.out.println(e.getKey() + " has lost the round."); break;
                case 1: System.out.println(e.getKey() + " has won the round."); break;
                default: System.out.println(e.getKey() + "has pushed with the dealer.");
            }
        }

        cardsDealt = 0;
        state = INIT;
        super.reset();
    }

    @Override void dealTo(Player player, Card card)
    {
        super.dealTo(player, card);
        switch (state)
        {
            case INIT:
                results = getResults();
                state = DEAL_PLAYERS;
                System.out.println("A round of Blackjack has begun.");
                // Fall to next case
            case DEAL_PLAYERS:
                // Count cards until we reach the hidden card
                if (++cardsDealt <= 2 * results.size() + 1)
                {
                    announceCard(player, card);
                    if (cardsDealt == results.size() + 1)
                        dealer = player; // Keep track of the dealer for the reveal function
                    if (cardsDealt > results.size() + 1) // If player has a second card
                        checkBlackjack(player);
                }
                else
                {
                    // The hidden card cannot be revealed to the user, treat it differently
                    System.out.printf((FMT_CARD_DEALT) + "%n", player, "??-" + Card.CARD_BACK_SYMBOL);
                    hiddenCard = card; // Remember the card for later, when it is revealed
                    state = AWAIT_DEALER;
                }
                break;
            case AWAIT_DEALER:
                if (player instanceof Dealer)
                {
                    final List<Card> df = dealer.getField();
                    // Don't reveal the latest card when revealing, as the deal event already does this
                    System.out.printf("The Dealer reveals: %s %s\n", hiddenCard, df.subList(0, df.size() - 1));
                    state = PLAYER_TURNS;
                } // Fall through to announce the drawn card
            case PLAYER_TURNS:
                announceCard(player, card);
                if (!checkBust(player)) // Busting and Blackjack are mutually exclusive
                    checkBlackjack(player);
        }
    }

    /* Determines the state of the game based on what cards have been dealt out */
    enum BlackjackState
    {
        INIT,
        DEAL_PLAYERS,
        AWAIT_DEALER,
        PLAYER_TURNS,
    }

    private void announceCard(final Player p, final Card c)
    {
        System.out.printf((FMT_CARD_DEALT) + " %s%n", p, c, p.getField());
    }

    private void checkBlackjack(final Player p)
    {
        assert p != null;
        if (p.hasBlackjack())
            System.out.printf("BLACKJACK! %s is out of the round.\n", p);
    }

    private boolean checkBust(final Player p)
    {
        assert p != null;
        if (p.hasBusted())
        {
            System.out.printf("BUST! %s is out of the round.\n", p);
            return true;
        }
        return false;
    }
}
