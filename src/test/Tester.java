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
import blackjack.player.Player;
import genetic.Agent;
import genetic.ConcreteAgent;
import genetic.Mutations;
import genetic.Simulation;

import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Random;
import java.util.function.ToIntFunction;

/**
 * Defines an entry-point into the program.
 *
 */
public class Tester
{
    public static void main(String[] args)
    {
        System.out.println("Starting.");

        final Random generator = new Random(423298675324L);
        final float MUTATION_RATE = 0.0f;
        final int POPULATION_SIZE = 2000;
        final int MAX_GENERATIONS = 100;
        final boolean MULTI_THREADED = true;
        final int BJ_ROUNDS_PER_AGENT = 25000;
        final int BJ_SHOE_SIZE = 8;
        final float BJ_SHOE_PENETRATION = 0.5f;

        /* try (final InputStream is = new FileInputStream("myObj"))
        {
            final ObjectInputStream ois = new ObjectInputStream(is);
            final ConcreteAgent ca = (ConcreteAgent)ois.readObject();
            final Blackjack bj = new Blackjack(8, 16, generator.nextLong());
            for (int i = 0; i < 100; i++)
                bj.playRound(ca, 1);
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        } */

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
                final Blackjack bJack = new Blackjack(BJ_SHOE_SIZE, generator.nextLong(), BJ_SHOE_PENETRATION);
                return agent ->
                {
                    bJack.dealIn(agent);
                    final Map<Player, Integer> results = bJack.getResults();
                    int cost = 0;
                    for (int i = 0; i < BJ_ROUNDS_PER_AGENT; i++)
                    {
                        bJack.playRound();
                        cost += results.get(agent);
                    }
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
        
        final IntSummaryStatistics issWeights[] = new IntSummaryStatistics[19 * 2];
        for (int i = 0; i < issWeights.length; i++) issWeights[i] = new IntSummaryStatistics();
        for (int i = 0; i < POPULATION_SIZE; i++)
        {
            final int[] weights = agents[i].getWeights();
            for (int j = 0; j < weights.length; j++)
                issWeights[j].accept(weights[j]);
        }

        System.out.print("Score");
        for (int i = 0; i < 19; i++)
            System.out.printf("  %5d ", i + 2);
        System.out.print("\nNoAce");
        for (int i = 0; i < 19; i++)
            System.out.printf("  %5s%%", String.format("%3.2f", issWeights[i].getAverage() * 100 / Integer.MAX_VALUE));
        System.out.println();

        System.out.print("\n  Ace");
        for (int i = 19; i < issWeights.length; i++)
            System.out.printf("  %5s%%", String.format("%3.2f", issWeights[i].getAverage() * 100 / Integer.MAX_VALUE));
        System.out.println();
        
//        final ConcreteAgent best = agents[0];
//        try (final FileOutputStream fos = new FileOutputStream("myObj"))
//        {
//            final ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(best);
//        }
//        catch (final IOException e)
//        {
//            e.printStackTrace();
//        }

        System.out.println("Done.");
    }
}
