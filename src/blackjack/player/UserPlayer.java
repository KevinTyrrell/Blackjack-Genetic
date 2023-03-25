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

package blackjack.player;

import java.util.Collections;
import java.util.Map;
import java.util.Scanner;


public class UserPlayer extends Player
{
    private static final Scanner input = new Scanner(System.in);
    /* Key words in which are expected from the user -> whether to hit or stand */
    private static final Map<String, Boolean> keywords = Collections.unmodifiableMap(
            Map.of("hit", true, "stand", false));
    private static final String FMT_HIT_PROMPT = "%s Turn - confirm 'hit' or 'stand': ";

    @Override public boolean hit()
    {
        do
        {
            System.out.printf(FMT_HIT_PROMPT, this);

            if (input.hasNext())
            {
                final String t = input.nextLine().trim().toLowerCase();
                final Boolean decision = keywords.get(t);
                if (decision != null)
                    return decision;
                System.out.println("Invalid input - Please try again.\n");
            }
            else throw new IllegalStateException("Scanner is not able to read user input.");
        } while (true);
    }
}
