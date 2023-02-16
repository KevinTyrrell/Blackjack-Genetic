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
import blackjack.card.Shoe;
import blackjack.player.Dealer;
import blackjack.player.Player;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.requireNonNull;


/**
 * Defines a game of Blackjack
 *
 * Players must attempt to score as close to, but not exceeding, 21.
 * Score is determined by the card values in each player's hand.
 *
 * TODO: Allow for multiple players to face the dealer
 * TODO: Allow extension in which you could view the play-by-play of the game
 */
public class Blackjack
{
    private final Shoe shoe;
    private final float penetration;
    private final Player dealer = new Dealer();
    // TODO: Subclass Blackjack specifically for 1 player, as an optimization.
    // TODO: Subclassed class should create a fake 'Map<Player, Integer> facade.
    private final Map<Player, Integer> players, players_ro;

    /* Percentage of the deck dealt out such that a shuffle should occur */
    private static final float DEFAULT_PENETRATION = 0.5f;
    /* Standard shoe size for most Blackjack tables is 8 */
    private static final int DEFAULT_SHOE_SIZE = 8;
    /* Round result constants depending on the end state of the Blackjack round  */
    private static final int RESULT_WIN = 1, RESULT_LOSS = -1, RESULT_PUSH = 0;

    public Blackjack() { this(DEFAULT_SHOE_SIZE, currentTimeMillis(), DEFAULT_SHOE_SIZE); }
    public Blackjack(final int shoeSize) { this(shoeSize, currentTimeMillis(), DEFAULT_PENETRATION); }
    public Blackjack(final int shoeSize, final long seed) { this(shoeSize, seed, DEFAULT_PENETRATION); }

    /**
     * Constructs a Blackjack table instance
     *
     * @param shoeSize Number of decks to be combined into a shoe
     * @param seed Random seed sequence
     * @param penetration Percentage [0.0,1.0] of how many cards of the shoe have
     *                    been dealt out before a shuffle should take place.
     */
    public Blackjack(final int shoeSize, final long seed, final float penetration)
    {
        if (shoeSize <= 0) throw new IllegalArgumentException("Shoe size must be positive and non-zero");
        if (penetration < 0.0f || penetration > 1.0f)
            throw new IllegalArgumentException("Penetration threshold must be within bounds [0.0, 1.0]");
        shoe = new Shoe(shoeSize, seed);
        this.penetration = penetration;
        // Track all players participating at the Blackjack table
        players = new LinkedHashMap<>();
        players_ro = Collections.unmodifiableMap(players);
    }

    /**
     * Deal in all players at the table, ensuring they are dealt cards each round
     *
     * @param players Players to deal in at the table
     */
    public void dealIn(final Player ...players)
    {
        Arrays.stream(players).forEach(Objects::requireNonNull);
        this.players.putAll(Arrays.stream(requireNonNull(players))
                .collect(Collectors.toMap(
                        Function.identity(), v -> RESULT_PUSH)));
    }

    /**
     * Plays a round of Blackjack with the players currently dealt-in
     *
     * The round proceeds until all players either bust, blackjack, or stand.
     * This game of Blackjack uses the SOFT-17 rule-set (dealer stands on soft 17).
     * This game of Blackjack uses the LOSS on dealer/player PUSH rule-set.
     */
    public void playRound()
    {
        if (players.isEmpty()) throw new IllegalStateException("Cannot play round without any player participants");
        // Card deal order is always one card to each player, one to dealer, and so on.
        // This order is imperative for subclasses which track the game event-by-event.
        players.keySet().forEach(p -> dealTo(p, shoe.deal()));
        dealTo(dealer, shoe.deal());
        players.keySet().forEach(p -> dealTo(p, shoe.deal()));
        dealTo(dealer, shoe.deal());

        //noinspection ReplaceInefficientStreamCount
        final boolean fullRound = players.keySet().stream()
                .filter(this::playerTurn)
                .count() > 0;

        if (fullRound) // Only proceed with dealer's turn if a player didn't bust
        {
            final boolean dealerBust = !playerTurn(dealer);
            for (final Map.Entry<Player, Integer> e : players.entrySet())
            {
                final Player player = e.getKey();
                if (player.hasBusted()) e.setValue(RESULT_LOSS);
                else if (dealerBust) e.setValue(RESULT_WIN);
                else e.setValue(Integer.compare(player.getSoftScore(), dealer.getSoftScore()));
            }
        }
        else players.entrySet().forEach(e -> e.setValue(RESULT_LOSS));

        // If the specified percentage of the deck has been penetrated, perform a shuffle
        if (shoe.penetration() >= penetration) shoe.shuffle();
        reset();
    }

    /**
     * Performs the specified player's turn
     *
     * A player may act assuming he hasn't busted or hit Blackjack.
     * A player must either hit (dealing a new card) or stand (end turn).
     *
     * This function requires the player to have been dealt two cards prior
     *
     * @return false if the player busted
     */
    private boolean playerTurn(final Player player)
    {
        if (player.hasBlackjack()) return true; // Blackjack on first two cards
        do
        {
            if (player.hit())
                dealTo(player, shoe.deal());
            else break;
            if (player.hasBusted()) return false;
            // Check for both kinds of Blackjack (soft & hard)
        } while (!player.hasBlackjack());
        return true;
    }

    /**
     * Retrieves the results the Blackjack game, updated per-round
     *
     * Each round, the results of the round will be stored in the map.
     * Only one `getResults` call is needed, as no new map will be created.
     * Values signify results, and are among this set { 1: win, -1: loss, 0: push }.
     * Result values will not update until a new round concludes.
     * Result values are initialized to `0` (push) until the first round concludes.
     * The dealer is not included in the set of players.
     *
     * @return Read-only map of players -> results of their most recent round results
     */
    public Map<Player, Integer> getResults()
    {
        return players_ro;
    }

    /**
     * Resets the Blackjack table
     */
    public void reset()
    {
        dealer.reset();
        players.keySet().forEach(Player::reset);
    }

    /**
     * @param player Player to deal a card to
     */
    void dealTo(final Player player, final Card card)
    {
        player.accept(card);
    }
}
