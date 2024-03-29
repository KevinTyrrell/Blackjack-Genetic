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

import blackjack.BJEventTranslator;
import blackjack.Blackjack;
import blackjack.player.Player;
import blackjack.player.UserPlayer;
import genetic.agent.ConcreteAgent;
import io.AgentWeightFormatter;
import io.BlackjackConsoleView;
import org.junit.jupiter.api.Test;


public final class TestOutput
{
    @Test public void test()
    {
        final ConcreteAgent ca = new ConcreteAgent(0);
        ca.randomizeWeights();
        final String[] s = AgentWeightFormatter.formatWeightMatrix(ca);

        for (int i = 0; i < s.length; i += 2)
        {
            System.out.println(s[i]);
            System.out.printf("%s\n\n", s[i + 1]);
        }

        final Blackjack bj = new BJEventTranslator(new BlackjackConsoleView(), 8, 42141451, 0.35f);
        final Player user = new UserPlayer();
        bj.dealIn(user, ca);
        bj.playRound();
    }
}
