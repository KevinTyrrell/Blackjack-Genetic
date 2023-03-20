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

import blackjack.player.Player;
import util.MutableSingletonMap;

import java.util.Map;


/**
 * Blackjack optimized for solo-play, improving traversal and look-up speeds
 */
public class BlackjackSolo extends Blackjack
{
    private final Map<Player, Integer> player = new MutableSingletonMap<>();

    /**
     * Constructs a Blackjack table instance
     *
     * @param shoeSize    Number of decks to be combined into a shoe
     * @param seed        Random seed sequence
     * @param penetration Percentage [0.0,1.0] of how many cards of the shoe have
     *                    been dealt out before a shuffle should take place.
     */
    public BlackjackSolo(int shoeSize, long seed, float penetration)
    {
        super(shoeSize, seed, penetration);
    }

    /**
     * Deal in all players at the table, ensuring they are dealt cards each round
     *
     * @param players Players to deal in at the table
     */
    @Override public void dealIn(Player... players)
    {
        player.put(players[0], 0);
    }

    /**
     * Retrieves the results the Blackjack game, updated per-round
     * <p>
     * Each round, the results of the round will be stored in the map.
     * Only one `getResults` call is needed, as no new map will be created.
     * Values signify results, and are among this set { 1: win, -1: loss, 0: push }.
     * Result values will not update until a new round concludes.
     * Result values are initialized to `0` (push) until the first round concludes.
     * The dealer is not included in the set of players.
     *
     * @return Read-only map of players -> results of their most recent round results
     */
    @Override public Map<Player, Integer> getResults()
    {
        return player;
    }
}
