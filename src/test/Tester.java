/*
 *     <one line to give the program's name and a brief idea of what it does.>
 *     Copyright (C) 2019  Kevin Tyrrell
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
import genetic.Agent;
import genetic.ConcreteAgent;
import genetic.Mutations;
import genetic.Simulation;

import java.util.IntSummaryStatistics;
import java.util.Random;
import java.util.function.ToIntFunction;

/**
 * Defines an entry-point into the program.
 *
 * @since 1.0
 */
public class Tester
{
    public static void main(String[] args)
    {
        System.out.println("Starting.");
        
        final Random generator = new Random(42512324L);
        final float MUTATION_RATE = 0.35f;
        final int POPULATION_SIZE = 10000;
        final int MAX_GENERATIONS = 50;
        final boolean MULTI_THREADED = true;//16.48 5
        final int BJ_ROUNDS_PER_AGENT = 10000;
        final int BJ_SHOE_SIZE = 8;
        final int BJ_ROUNDS_PER_SHUFFLE = 16;
        
        /* Controls the entire lifecycle of agents. */
        final Simulation<ConcreteAgent> sim = new Simulation<>()
        {
            @Override public ConcreteAgent initAgent()
            {
                return new ConcreteAgent(generator.nextLong());
            }

            @Override public void mutateAgent(final Agent agent)
            {
                Mutations.flip(agent, MUTATION_RATE, generator);
            }

            /* Creates a cost function to be used when assessing agents. */
            @Override public ToIntFunction<ConcreteAgent> initCostFunc()
            {
                final Blackjack bJack = new Blackjack(BJ_SHOE_SIZE, BJ_ROUNDS_PER_SHUFFLE, generator.nextLong());
                return agent ->
                {
                    int cost = 0;
                    for (int i = 0; i < BJ_ROUNDS_PER_AGENT; i++)
                        cost += bJack.playRound(agent, 1);
                    bJack.reset();
                    return cost;
                };
            }

            /* Indicates how well each generation is performing. */
            @Override public void genCostStatsCallback(final IntSummaryStatistics iss, final int generation)
            { 
                System.out.format("Generation #%-4d   Average: %-10.3f   Best: %-10.3f   Worst: %-10.3f\n", 
                        generation, iss.getAverage() / BJ_ROUNDS_PER_AGENT, (double)iss.getMin() / BJ_ROUNDS_PER_AGENT, 
                        (double)iss.getMax() / BJ_ROUNDS_PER_AGENT);
            }
        };
        
        /* Initialize a population of random agents. */
        final ConcreteAgent[] agents = new ConcreteAgent[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++)
        {
            final ConcreteAgent ca = new ConcreteAgent(generator.nextLong());
            ca.randomizeWeights(generator);
            agents[i] = ca;
        }
        sim.run(agents, MAX_GENERATIONS, generator, MULTI_THREADED);
        
        agents[0].printWeights();

        System.out.println("Done.");
    }
}
