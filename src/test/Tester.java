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
import blackjack.card.Card;
import blackjack.card.Face;
import genetic.Agent;
import genetic.Simulation;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.List;

import static blackjack.Blackjack.rand;

public class Tester
{
    public static void main(String[] args)
    {
        System.out.println("Starting.");


        /* Blackjack setup. */
        final long SEED = 1242323512L;
        rand.setSeed(SEED);
        final int NUM_OF_DECKS = 16;
        final int BET_AMOUNT = 10;
        final int ROUNDS_PER_AGENT = 70;
        final Dealer dealer = new Dealer();
        /* All player's must know what the dealer's revealed card is. */
        final ReadOnlyObjectProperty<Card> revealedPtr = dealer.getRevealedCard();
        final Blackjack bjack = new Blackjack(dealer, NUM_OF_DECKS);

        /* Setup the upcoming simulation, initialize cost function, etc. */
        final Simulation<Agent> sim = new Simulation<>(1000, 50,
                () -> new Agent(revealedPtr),
                agent ->
                {
                    int cost = 0;
                    for (int i = 0; i < ROUNDS_PER_AGENT; i++)
                        cost += bjack.playRound(agent, BET_AMOUNT);
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
