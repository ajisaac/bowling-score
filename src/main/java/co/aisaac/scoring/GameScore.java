package co.aisaac.scoring;

import java.util.ArrayList;
import java.util.List;

public class GameScore {

	private int gameNumber;

	private int score;

	private final List<Frame> frames = new ArrayList<>(10);

	/**
	 * Creates a valid Game score for a game of bowling.
	 *
	 * @param scoreString Some valid String of game data for bowling.
	 * @throws StateException If the String is malformed or has illegal state per the rules of bowling.
	 */
	public GameScore(String scoreString) throws StateException {
		try {

			if (scoreString == null || scoreString.strip().equals("")) {
				throw new StateException("GameScore string input must not be blank or null.");
			}

			String[] gameData = scoreString.split(",");

			if (gameData.length < 22) {
				throw new StateException("GameScore is missing rolls.");
			}

			if (gameData.length > 22) {
				throw new StateException("GameScore has too many rolls.");
			}

			parseGameNumber(gameData);

			parseFrames(gameData);

			calculateScore();
		} catch (StateException ex) {
			throw new StateException("Invalid game input: " + scoreString, ex);
		}
	}

	private void calculateScore() throws StateException {
		// calculate the score for the first 9 frames
		for (int i = 0; i < 9; i++) {
			Frame currentFrame = frames.get(i);
			Frame nextFrame = frames.get(i + 1);

			if (currentFrame.getFirstRoll().isStrike()) {
				if (i == 8) { // last 2 roll frame, next is 3 roll
					currentFrame.addExtraPins(nextFrame.getFirstRoll().value + nextFrame.getSecondRoll().value);
				}

				if (i < 8) { // first 8 frames, 2 rolls each
					if (nextFrame.getFirstRoll().isStrike()) { // we have to look into next 2 frames
						currentFrame.addExtraPins(nextFrame.getFirstRoll().value + frames.get(i + 2).getFirstRoll().value);
					} else { // we just need the next frame alone
						currentFrame.addExtraPins(nextFrame.getFirstRoll().value + nextFrame.getSecondRoll().value);
					}
				}
			}

			if (currentFrame.getSecondRoll().isSpare()) {
				currentFrame.addExtraPins(nextFrame.getFirstRoll().value);
			}
		}

		// total score
		for (Frame f : frames) {
			this.score += f.getFinalScore();
		}
	}

	private void parseFrames(String[] gameData) throws StateException {
		// first frames
		for (int i = 1; i < 18; i += 2) {
			frames.add(new Frame(gameData[i], gameData[i + 1]));
		}
		// last frame
		frames.add(new Frame(gameData[19], gameData[20], gameData[21]));
	}

	private void parseGameNumber(String[] input) throws StateException {
		try {
			this.gameNumber = Integer.parseInt(input[0]);
		} catch (NumberFormatException ex) {
			throw new StateException("Invalid numerical input for game number: " + input[0], ex);
		}
		if (this.gameNumber < 0) {
			throw new StateException("Game number must be positive.");
		}
	}

	public int getScore() {
		return this.score;
	}

}
