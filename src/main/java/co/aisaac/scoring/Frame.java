package co.aisaac.scoring;

/**
 * Bowling scores suffer from combinatorial explosion. Way many edge cases.
 */
class Frame {
	private final Roll firstRoll;

	private final Roll secondRoll;

	private final Roll thirdRoll;

	// the score within the context of a frame
	private final int initialScore;

	// the score within the context of a game, which includes pins and such
	private int finalScore = 0;

	public Frame(String first, String second) throws StateException {
		this(first, second, null);
	}

	public Frame(String first, String second, String third) throws StateException {
		if (first == null || first.strip().isBlank()) {
			throw new StateException("Roll data is malformed.");
		}
		if (second == null || second.strip().isBlank()) {
			throw new StateException("Roll data is malformed.");
		}
		if (third != null && third.strip().isBlank()) {
			throw new StateException("Roll data is malformed.");
		}

		Roll firstRoll = new Roll(first);
		Roll secondRoll = new Roll(second);
		Roll thirdRoll = null;
		if (third != null) {
			thirdRoll = new Roll(third);
		}

		if (secondRoll.isSpare()) {
			secondRoll.value = 10 - firstRoll.value;
		}
		if (thirdRoll != null && thirdRoll.isSpare()) {
			thirdRoll.value = 10 - secondRoll.value;
		}

		if (thirdRoll == null) {
			stateValidation(firstRoll, secondRoll);
			initialScore = firstRoll.value + secondRoll.value;
			finalScore = firstRoll.value + secondRoll.value;
		} else {
			stateValidation(firstRoll, secondRoll, thirdRoll);
			initialScore = firstRoll.value + secondRoll.value + thirdRoll.value;
			finalScore = firstRoll.value + secondRoll.value + thirdRoll.value;
		}

		this.firstRoll = firstRoll;
		this.secondRoll = secondRoll;
		this.thirdRoll = thirdRoll;

	}

	/**
	 * If this is a 2 roll frame, and you had spare or strike, you can add some extra pins to the score
	 */
	public void addExtraPins(int pins) throws StateException {
		if (thirdRoll != null) {
			throw new StateException("Can't add pins to score of a 3 roll frame.");
		}
		if (!firstRoll.isStrike() && !secondRoll.isSpare()) {
			throw new StateException("Can't add pins to score of a 2 roll frame without a Strike or Spare.");
		}
		finalScore = initialScore + pins;
	}

	/**
	 * Validation for a 3 roll frame.
	 */
	private void stateValidation(Roll firstRoll, Roll secondRoll, Roll thirdRoll) throws StateException {
		// unused
		if (firstRoll.isUnused() || secondRoll.isUnused()) {
			throw new StateException("Roll wasn't taken.");
		}

		// unused third roll
		if (firstRoll.isStrike() || secondRoll.isStrike() || secondRoll.isSpare() && (thirdRoll.isUnused())) {
			throw new StateException("Roll wasn't taken.");
		}

		// valid spare?
		if (firstRoll.isSpare()) {
			throw new StateException("Invalid spare.");
		}
		if (firstRoll.isStrike() && secondRoll.isSpare()) {
			throw new StateException("Invalid spare.");
		}
		if ((secondRoll.isStrike() || secondRoll.isSpare()) && thirdRoll.isSpare()) {
			throw new StateException("Invalid spare.");
		}

		// numbers
		if (firstRoll.isRegularRoll() && (firstRoll.value + secondRoll.value > 10)) {
			throw new StateException("Combined roll values greater than 10.");
		}
		if (firstRoll.isStrike() && secondRoll.isRegularRoll() && (secondRoll.value + thirdRoll.value > 10)) {
			throw new StateException("Combined roll values greater than 10.");
		}
		if (firstRoll.isRegularRoll() && secondRoll.isRegularRoll() && (firstRoll.value + secondRoll.value == 10)) {
			throw new StateException("Second roll should be a spare.");
		}
	}

	/**
	 * Validation for a regular 2 roll frame.
	 */
	private void stateValidation(Roll firstRoll, Roll secondRoll) throws StateException {
		if (firstRoll.isUnused()) {
			throw new StateException("First roll wasn't taken.");
		}
		if (firstRoll.isSpare()) {
			throw new StateException("First roll can't be a spare.");
		}
		if (secondRoll.isStrike()) {
			throw new StateException("Second roll can't be a strike.");
		}

		if (firstRoll.isStrike()) {
			if (!secondRoll.isUnused()) {
				throw new StateException("In a 2 roll frame, first roll is strike, second roll was still taken.");
			} else {
				return;
			}
		}

		if (firstRoll.isRegularRoll()) {
			if (secondRoll.isUnused()) {
				throw new StateException("In a 2 roll frame, first roll is not a strike, second roll was not taken.");
			}
		}

		if (firstRoll.value + secondRoll.value > 10) {
			throw new StateException("Roll values equal to greater than 10.");
		}
	}

	public Roll getFirstRoll() {
		return firstRoll;
	}

	public Roll getSecondRoll() {
		return secondRoll;
	}

	public int getFinalScore() {
		return finalScore;
	}
}
