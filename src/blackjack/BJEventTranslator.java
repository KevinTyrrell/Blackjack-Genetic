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

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static blackjack.BJEventTranslator.BlackjackState.*;


/**
 * Defines a Blackjack event translator
 *
 * This class translates card deal events into a full array of Blackjack game events
 */
public class BJEventTranslator extends Blackjack
{
    private final BlackjackWatchable eventHandler;

    private BlackjackState state = INIT_PLAYERS;

    private Map<Player, Integer> results = null;
    private Dealer dealer = null;
    private Card hiddenCard = null;
    private int cardsDealt = 0;
    private int participants = 0;

    /**
     * @param eventHandler Handler to-be passed Blackjack events
     * @param shoeSize Number of decks to be combined into a shoe
     * @param seed Random seed sequence
     * @param penetration Percentage [0.0,1.0] of how many cards of the shoe have
     *                    been dealt out before a shuffle should take place.
     */
    public BJEventTranslator(final BlackjackWatchable eventHandler,
                             final int shoeSize, final long seed, final float penetration)
    {
        super(shoeSize, seed, penetration);
        this.eventHandler = requireNonNull(eventHandler);
    }

    @Override public void reset()
    {
        /*
         * Game ended without dealer needing to deal himself a card. ex.
         * -> All players busted, therefore dealer does not need to take his turn.
         * -> Dealer stood or got Blackjack with two cards.
         * In these scenarios, we still have to reveal the hidden card.
         */
        if (state == AWAIT_DEALER)
            eventHandler.dealerRevealCard(dealer, hiddenCard);
        cardsDealt = 0;
        state = ROUND_START;
        super.reset();
        eventHandler.reset();
    }

    @Override void dealTo(Player player, Card card)
    {
        super.dealTo(player, card);
        cardsDealt++;

        /* Blackjack class does not support inspecting the round while in-progress.
         * Therefore, use dealTo events to determine what stage the game is currently in. */
        switch (state)
        {
            case INIT_PLAYERS:
                results = getResults();
                participants = results.size() + 1; // Include the dealer
            case ROUND_START:
                eventHandler.roundStart();
                state = FIND_DEALER;
            case FIND_DEALER:
                // Dealer always deals to himself after each player is dealt once
                if (cardsDealt == participants)
                {
                    dealer = (Dealer)player;
                    state = DEAL_PLAYERS;
                }
                eventHandler.cardDealt(player, card); break;
            case DEAL_PLAYERS:
                if (cardsDealt == 2 * (participants + 1))
                {
                    // Dealer's hidden card has to be treated in a special manner
                    eventHandler.dealerHideCard(dealer);
                    hiddenCard = card;
                    state = PLAYER_TURNS;
                }
                else
                {
                    eventHandler.cardDealt(player, card);
                    if (player.hasBlackjack()) // Player cannot bust in the deal stage
                        eventHandler.playerBlackjack(player);
                } break;
            case AWAIT_DEALER:
                // At some point the dealer will take his turn, but we're not sure when
                if (player == dealer)
                {
                    eventHandler.dealerRevealCard(dealer, hiddenCard);
                    state = PLAYER_TURNS; // No need to reveal the card again
                }
            case PLAYER_TURNS:
                eventHandler.cardDealt(player, card);
                if (!player.hasBusted()) // Busting and Blackjack are mutually exclusive
                    if (player.hasBlackjack())
                        eventHandler.playerBlackjack(player);
                else eventHandler.playerBust(player);
        }
    }

    /* Determines the state of the game based on what cards have been dealt out */
    enum BlackjackState
    {
        INIT_PLAYERS,       // Game has yet to start, and we don't know who is at the table.
        ROUND_START,        // Round has just begun, but no cards have yet to be dealt out.
        FIND_DEALER,        // Players are being dealt their first card, awaiting dealer to identify himself.
        DEAL_PLAYERS,       // Players are being dealt their second card, awaiting dealer to lay hidden card.
        AWAIT_DEALER,       // Players have begun their turns, await the dealer to reveal his hidden card.
        PLAYER_TURNS,       // Players (including the dealer) play until a game result occurs for each player.
    }
}
