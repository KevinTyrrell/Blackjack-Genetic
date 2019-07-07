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
import genetic.Agent;

import java.util.List;

public class Tester
{
    public static void main(String[] args)
    {
        final int POPULATION = 100;
        final int MAX_GENERATIONS = 3;
        final int BJ_ROUNDS = 16;
        final int BJ_SHOE_SIZE = 8;
        final Agent[] agents = new Agent[POPULATION];


        final Dealer dealer = new Dealer();


    }
}
