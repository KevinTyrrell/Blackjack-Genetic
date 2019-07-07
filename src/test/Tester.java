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

import blackjack.Dealer;
import blackjack.Player;
import blackjack.Shoe;
import blackjack.card.Card;
import genetic.Agent;
import genetic.Cost;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Tester
{
    public static void main(String[] args)
    {
        /* Setup. */
        final int POPULATION = 100;
        final int MAX_GENERATIONS = 3;
        final int BJ_BET = 10;
        final int BJ_ROUNDS = 32;
        final int BJ_SHOE_SIZE = 8;
        final ObjectProperty<Card> dealersCard = new SimpleObjectProperty<>();
        final Agent[] agents = new Agent[POPULATION];
        final Dealer dealer = new Dealer();
        final Shoe shoe = new Shoe(BJ_SHOE_SIZE);

        // NOTE: Encapsulate dealer's card into the dealer class.
        // Allow access to the dealer's card from outside.

        for (int i = 0; i < POPULATION; i++)
            agents[i] = new Agent(dealersCard);

        for (int gen = 0; gen < MAX_GENERATIONS; gen++)
        {
            final Cost<Agent> costFunc = new Cost<>(POPULATION, (agent) ->
            {
                int cost = 0;

                for (int round = 0; round < BJ_ROUNDS; round++)
                {
                    final Card dc = shoe.deal();
                    dealersCard.setValue(dc);
                    dealer.accept(dc);

                    agent.accept(shoe.deal());
                    agent.accept(shoe.deal());

                    while (true)
                    {
                        if (agent.hasBusted())
                            cost += BJ_BET;
                        else if (agent.getMaximumScore() == Player.MAXIMUM_SCORE)
                            cost -= BJ_BET;
                        else
                        {
                            if (agent.hit())
                            {
                                agent.accept(shoe.deal());
                                continue;
                            }
                        }
                        break;
                    }

                    if (!agent.hasBusted())
                    {
                        dealer.accept(shoe.deal());
                        while (true)
                        {
                            if (dealer.hasBusted())
                                cost -= BJ_BET;
                            else if (dealer.hit())
                            {
                                dealer.accept(shoe.deal());
                                continue;
                            }
                            break;
                        }

                        // This is so ugly, fix.
                        if (!dealer.hasBusted())
                        {
                            final int dScore = dealer.getMaximumScore();
                            final int aScore = agent.getMaximumScore();
                            if (dScore < aScore)
                                cost -= BJ_BET;
                            else if (dScore > aScore)
                                cost += BJ_BET;
                        }
                    }

                    agent.reset();
                    dealer.reset();
                }

                shoe.shuffle();
                return 0;
            });
        }
    }
}
