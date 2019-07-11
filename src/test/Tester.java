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
import genetic.Agent;
import genetic.Simulation;

import java.util.List;

import static blackjack.Blackjack.rand;

public class Tester
{
    public static void main(String[] args)
    {
        System.out.println("Starting.");
        
        rand.setSeed(1232323512L);
        final int BJ_ROUNDS_PER_AGENT = 50000;
        final Dealer dealer = new Dealer();
        /* All player's must know what the dealer's revealed card is. */
        final Blackjack bjack = new Blackjack(dealer, 8, 32);

        /* Setup the upcoming simulation, initialize cost function, etc. */
        final Simulation<Agent> sim = new Simulation<>(1000, 10, Agent::new,
                agent ->
                {
                    int cost = 0;
                    for (int i = 0; i < BJ_ROUNDS_PER_AGENT; i++)
                        cost += bjack.playRound(agent, 1);
                    bjack.reset();
                    return cost;
                });
        sim.startSimulation();

        final List<Agent> convergence = sim.getAgents();
        final Agent best = convergence.get(0);

        best.printWeights();

        System.out.println("Done.");
    }
}
