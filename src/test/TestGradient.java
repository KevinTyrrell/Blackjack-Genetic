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
import genetic.gradient.BirdshotGradient;
import genetic.gradient.Gradient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;


class TestGradient
{
    private static final int NUM_AGENTS = 16;
    private static final long SEED = 418512845194L;
    private static final Random gen = new Random(SEED);

    private Population<ConcreteAgent> pop;

    @BeforeEach void setUp()
    {
        pop = new Population<>(NUM_AGENTS, () ->
        {
            final ConcreteAgent ca = new ConcreteAgent(SEED);
            ca.randomizeWeights();
            return ca;
        });
    }

    @Test void apply()
    {
        pop.performFitnessTest(value -> gen.nextDouble() * 25.0);
        System.out.println(pop.getPopulation() + " - " + Arrays.toString(pop.getFitnessCosts()));
        pop.sortPopulation();
        System.out.println(pop.getPopulation() + " - " + Arrays.toString(pop.getFitnessCosts()));
        final Gradient<ConcreteAgent> gradient = new BirdshotGradient<>(gen, 5.0f);
        gradient.apply(pop);
        System.out.println(pop.getPopulation() + " - " + Arrays.toString(pop.getFitnessCosts()));
    }
}