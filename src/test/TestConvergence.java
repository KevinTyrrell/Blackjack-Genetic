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

import blackjack.Blackjack;
import blackjack.BlackjackSolo;
import blackjack.player.Player;
import genetic.Population;
import genetic.Simulation;
import genetic.agent.ConcreteAgent;
import genetic.gene.Crossover;
import genetic.gene.Mutation;
import genetic.gradient.BirdshotGradient;
import genetic.gradient.Gradient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Random;


public class TestConvergence
{
    private static final long SEED = 5213821584128L;
    private static final int GENERATION_TARGET = 25, NUM_AGENTS = 8;
    private static final float MUTATION_RATE = 0.15f, GRADIENT_SCALAR = 15;

    private static final int BJ_SHOE_SIZE = 8, BJ_ROUNDS_PER_AGENT = 100000;
    private static final float BJ_SHOE_PEN = 0.35f;

    private static final Random generator = new Random(SEED);
    private static final Mutation mutator = Mutation.UNIFORM;
    private static final Crossover crosser = Crossover.UNIFORM;
    private static final Gradient<ConcreteAgent> gradient =
            new BirdshotGradient<>(generator, GRADIENT_SCALAR);

    private Population<ConcreteAgent> pop;

    @BeforeEach public void setup()
    {
        pop = new Population<>(NUM_AGENTS, generator, crosser, mutator, MUTATION_RATE)
        {
            @Override public double evaluateFitness(final ConcreteAgent agent)
            {
                final Blackjack bj = new BlackjackSolo(BJ_SHOE_SIZE, SEED, BJ_SHOE_PEN);
                bj.dealIn(agent);
                final Map<Player, Integer> m = bj.getResults();
                double cost = 0.0;
                for (int i = 0; i < BJ_ROUNDS_PER_AGENT; i++)
                    cost += m.get(agent);
                return cost;
            }

            @Override public ConcreteAgent initAgent()
            {
                final ConcreteAgent ca = new ConcreteAgent(SEED);
                ca.randomizeWeights();
                return ca;
            }
        };
    }

    @Test public void convergence()
    {
        final Simulation<ConcreteAgent> sim = new Simulation<>()
        {
            @Override public void genCostStatsCallback(DoubleSummaryStatistics dss, int generation)
            {
                System.out.format("Generation #%-4d   Average: %-10.3f   Best: %-10.3f   Worst: %-10.3f\n",
                        generation, dss.getAverage() / BJ_ROUNDS_PER_AGENT, dss.getMin() / BJ_ROUNDS_PER_AGENT,
                        dss.getMax() / BJ_ROUNDS_PER_AGENT);
            }
        };
        sim.run(pop, GENERATION_TARGET, generator, gradient, false);


    }
}
