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

package test;

import genetic.Population;
import genetic.agent.ConcreteAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;


class TestGradient
{
    private static final int NUM_AGENTS = 100;
    private static final long SEED = 418512845194L;
    private static final Random gen = new Random(SEED);

    @BeforeEach void setUp()
    {
        final Population<ConcreteAgent> pop = new Population<>()
        {
            @Override public double[] getFitnessCosts()
            {
                return new double[0];
            }

            @Override public List<ConcreteAgent> getPopulation()
            {
                return null;
            }

            @Override public void populate(int agentCount)
            {

            }
        };
    }

    @Test void apply()
    {
    }
}