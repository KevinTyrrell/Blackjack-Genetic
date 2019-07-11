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
import blackjack.Dealer;
import genetic.ConcreteAgent;
import genetic.Crossover;
import genetic.Simulation;

import java.util.List;
import java.util.Random;

import static blackjack.Blackjack.rand;

public class Tester
{
    public static void main(String[] args)
    {
        System.out.println("Starting.");
        
        final Random r = new Random(50);

        final int a = 211;
        final int b = 202;
        Crossover.uniform(a, b, 0.5f, r);
        
        System.out.println("Done.");
        if (System.currentTimeMillis() > 0) return;
        
        rand.setSeed(1232323512L);
        final int BJ_ROUNDS_PER_AGENT = 5000;
        final int BJ_BET = 1;
        final Dealer dealer = new Dealer();
        /* All player's must know what the dealer's revealed card is. */
        final Blackjack bjack = new Blackjack(dealer, 8, 32);

        /* Setup the upcoming simulation, initialize cost function, etc. */
        final Simulation<ConcreteAgent> sim = new Simulation<>(10000, 100, ConcreteAgent::new,
                agent ->
                {
                    int cost = 0;
                    for (int i = 0; i < BJ_ROUNDS_PER_AGENT; i++)
                        cost += bjack.playRound(agent, BJ_BET);
                    bjack.reset();
                    return cost;
                });
        sim.startSimulation((iss, gen) -> 
                System.out.format("Generation #%-4d   Average: %-10.3f   Best: %-10.3f   Worst: %-10.3f\n", 
                        gen, iss.getAverage() / BJ_ROUNDS_PER_AGENT, (double)iss.getMin() / BJ_ROUNDS_PER_AGENT, 
                        (double)iss.getMax() / BJ_ROUNDS_PER_AGENT));

        final List<ConcreteAgent> convergence = sim.getAgents();
        final ConcreteAgent best = convergence.get(0);

        best.printWeights();

        System.out.println("Done.");
    }
}
