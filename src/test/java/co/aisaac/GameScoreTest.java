package co.aisaac;

import co.aisaac.scoring.GameScore;
import co.aisaac.scoring.StateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*

Bowling scoring

Input file format

There are 10 frames when bowling, each frame consists of 1 to 3 rolls.
Frames 1-9 can have a maximum of two rolls,
frame 10 can have a maximum of three rolls.
The data is a comma-separated text file, each line corresponding to one game.
Each game is an integer id, followed by the 21 rolls in the game (two for each frame except the tenth where there is three).
Each roll is either:
 a digit from 0 to 9 (representing the number of pins hit),
 X (representing a strike),
 / (representing a spare),
 or - (representing an unused roll, for example after a strike)

Examples

A) 6678,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,X,X
B) 5532,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-

A is a perfect game of all strikes, and has score of 300.
B is a game where you hit 1 pin every roll, and has score of 20.

 */
class GameScoreTest {
	@Test
	@DisplayName("Handles null or blank input.")
	void handlesNullBlankInput() {
		assertThrows(StateException.class, () -> new GameScore(null));
		assertThrows(StateException.class, () -> new GameScore(""));
		assertThrows(StateException.class, () -> new GameScore("   "));
	}

	@Test
	@DisplayName("Happy path tests")
	void happyPathTests() throws StateException {

		GameScore gs = new GameScore("7000,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-");
		assertEquals(20, gs.getScore());
		gs = new GameScore("7000,X,-,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-");
		assertEquals(28, gs.getScore());
		gs = new GameScore("7000,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,-");
		assertEquals(18, gs.getScore());
		gs = new GameScore("7000,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,X,X");
		assertEquals(300, gs.getScore());
		gs = new GameScore("7000,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,X,0");
		assertEquals(290, gs.getScore());
		gs = new GameScore("7000,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,X,5");
		assertEquals(295, gs.getScore());
		gs = new GameScore("7000,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,5,/");
		assertEquals(285, gs.getScore());
		gs = new GameScore("7000,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,-,X,5,5");
		assertEquals(285, gs.getScore());
	}
}
