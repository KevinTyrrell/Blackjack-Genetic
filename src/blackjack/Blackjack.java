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

package blackjack;

import java.util.Objects;

public class Blackjack
{
    private final Dealer dealer;
    private final Shoe shoe;

    public Blackjack(final Dealer dealer, final int shoeSize)
    {
        this.dealer = Objects.requireNonNull(dealer);
        if (shoeSize <= 0) throw new IllegalArgumentException("Shoe size must be positive and non-zero");
        shoe = new Shoe(shoeSize);
    }

    /**
     * Plays through a round of Blackjack with the dealer and a specified player.
     * A returned value less than zero indicates a win, while positive indicates a loss.
     * After the round, the dealer and player will discard their field and scores.
     * The shoe is NOT shuffled after this function terminates.
     *
     * @param player Player to compete against the dealer.
     * @param bet Bet to place into the round.
     * @return -bet if the player wins, bet if the dealer wins, or 0 for a push.
     */
    public int playRound(final Player player, final int bet)
    {
        if (bet <= 0) throw new IllegalArgumentException("Bet must be positive and non-zero");
        final int result = bet * playRound(Objects.requireNonNull(player));
        player.reset();
        dealer.reset();
        return result;
    }

    /* Helper function for quick exiting out of the round. */
    private int playRound(final Player player)
    {
        dealer.accept(shoe.deal());
        dealer.accept(shoe.deal());
        player.accept(shoe.deal());
        player.accept(shoe.deal());

        /* Perform the player's turn. */
        if (player.getMaximumScore() != Player.MAXIMUM_SCORE && !playerTurn(player))
            return 1;
        /* Perform the dealer's turn. */
        if (dealer.getMaximumScore() != Player.MAXIMUM_SCORE && !playerTurn(dealer))
            return -1;

        final int pScore = player.getMaximumScore();
        final int dScore = dealer.getMaximumScore();
        if (pScore > dScore) return -1;
        if (pScore < dScore) return 1;
        return 0;
    }

    /* Performs a specified player's turn.
    * This method assumes the player has been dealt two cards prior. */
    private boolean playerTurn(final Player player)
    {
        assert player != null;
        while (true)
        {
            if (player.hit())
            {
                player.accept(shoe.deal());
                if (player.hasBusted())
                    return false;
                if (player.getMaximumScore() == Player.MAXIMUM_SCORE)
                    return true;
            }
            else return true;
        }
    }
}
