/*
 *     <one line to give the program's name and a brief idea of what it does.>
 *     Copyright (C) 2023  Kevin Tyrrell
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

/**
 * Defines a dealer at a Blackjack table
 *
 * A dealer must follow specific rules when it comes to score
 */
public class Dealer extends Player
{
    /* A dealer must hit if their score is less than 17 */
    private static int MINIMUM_DEALER_SCORE = 17;

    private final String name;

    public Dealer()
    {
        /* Cache the dealer's name */
        name = super.toString().replace("Player", "Dealer");
    }

    /**
     * Determines whether or not the dealer should hit
     *
     * A dealer must hit if their score is less than 17
     *
     * @return true if the player should hit
     */
    @Override public boolean hit()
    {
        return getSoftScore() < MINIMUM_DEALER_SCORE;
    }
    
    /**
     * @return String representation of the dealer
     */
    @Override public String toString()
    {
        return name;
    }
}
