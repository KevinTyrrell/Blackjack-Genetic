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

package genetic.agent;

import blackjack.player.Player;

import java.io.Serializable;
import java.util.Random;


/**
 * Defines a fully-fledged Blackjack player.
 */
public class ConcreteAgent extends Player implements Agent<ConcreteAgent>, Serializable
{
    private final Random generator;
    // Structure: [weights from score, no aces][17][weights from score, at least one ace][19]
    private final int[] weights = new int[17 + 19];

    /**
     * @param generator Random seed sequence
     */
    public ConcreteAgent(final long seed)
    {
        this.generator = new Random(seed);
    }

    /**
     * Determines whether or not the player should hit
     *
     * A player may hit if their score is less than 21.
     * A dealer must hit if his maximum score is less than 17.
     *
     * @return true if the player should hit
     */
    @Override public boolean hit()
    {
        // Equation via 'Multiple Linear Regression' calculator
        final int index = -4 + 19 * (hasAce() ? 1: 0) + getHardScore();
        /* If the agent doesn't have an ace, then only scores [4, 20] are possible.
        If the agent has at least one ace, then scores [2, 20] are possible.
        Therefore there are 17 + 19 => 36 possible cases (indexes) to map. */
        return weights[index] > generator.nextInt(Integer.MAX_VALUE);
    }

    /**
     * Randomizes the agent's weights
     *
     * Each weight is randomized from [0, Integer.MAX_VALUE].
     * This function randomizes the agent's disposition towards all actions.
     */
    public void randomizeWeights()
    {
        randomizeWeights(generator);
    }

    /**
     * Retrieves the agent's weights
     *
     * Weights represent the agent's disposition towards an action.
     * The index of the weight represents the combined inputs of an agent.
     * The value at the index represents their disposition, given the inputs.
     *
     * For multiple inputs, the array should be treated as a multi-dimensional
     *
     * @return Weights of the agent
     */
    @Override public int[] getWeights()
    {
        return weights;
    }
}
