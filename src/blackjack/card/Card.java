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

package blackjack.card;

import static java.util.Objects.requireNonNull;


/**
 * Defines a Poker card, featuring a face and a suit
 *
 * The suit has no impact in traditional Blackjack
 */
public class Card
{
    private final Face face;
    private final Suit suit;

    /* Unicode symbol of the back-face of any particular card */
    public static final String CARD_BACK_SYMBOL = "\uD83C\uDCA0";

    /* Relates [Suit][Face] to their Unicode symbol counterparts */
    private static final String[][] cardSymbols = new String[][]{
            {"\uD83C\uDCA1", "\uD83C\uDCA2", "\uD83C\uDCA3", "\uD83C\uDCA4", "\uD83C\uDCA5",
                    "\uD83C\uDCA6", "\uD83C\uDCA7", "\uD83C\uDCA8", "\uD83C\uDCA9",
                    "\uD83C\uDCAA", "\uD83C\uDCAB", "\uD83C\uDCAD", "\uD83C\uDCAE"},
            {"\uD83C\uDCB1", "\uD83C\uDCB2", "\uD83C\uDCB3", "\uD83C\uDCB4", "\uD83C\uDCB5",
                    "\uD83C\uDCB6", "\uD83C\uDCB7", "\uD83C\uDCB8", "\uD83C\uDCB9",
                    "\uD83C\uDCBA", "\uD83C\uDCBB", "\uD83C\uDCBD", "\uD83C\uDCBE"},
            {"\uD83C\uDCD1", "\uD83C\uDCD2", "\uD83C\uDCD3", "\uD83C\uDCD4", "\uD83C\uDCD5",
                    "\uD83C\uDCD6", "\uD83C\uDCD7", "\uD83C\uDCD8", "\uD83C\uDCD9",
                    "\uD83C\uDCDA", "\uD83C\uDCDB", "\uD83C\uDCDD", "\uD83C\uDCDE"},
            {"\uD83C\uDCC1", "\uD83C\uDCC2", "\uD83C\uDCC3", "\uD83C\uDCC4", "\uD83C\uDCC5",
                    "\uD83C\uDCC6", "\uD83C\uDCC7", "\uD83C\uDCC8", "\uD83C\uDCC9",
                    "\uD83C\uDCCA", "\uD83C\uDCCB", "\uD83C\uDCCD", "\uD83C\uDCCE"}
    };

    private static final String CARD_FORMAT = "%c%s-%s"; // ex. J♣-🃋

    /**
     * @param face Face of the card
     * @param suit Suit of the card
     */
    public Card(final Face face, final Suit suit)
    {
        this.face = requireNonNull(face);
        this.suit = requireNonNull(suit);
    }

    /**
     * @return Face of the card
     */
    public Face getFace()
    {
        return face;
    }

    /**
     * @return Suit of the card
     */
    public Suit getSuit()
    {
        return suit;
    }

    /**
     * @return Unicode symbol of the card
     */
    public String getCardSymbol()
    {
        return cardSymbols[suit.ordinal()][face.ordinal()];
    }

    /**
     * @return String representation of the card
     */
    @Override public String toString()
    {
        return String.format(CARD_FORMAT, face.getLetter(), suit.getSymbol(), getCardSymbol());
    }
}
