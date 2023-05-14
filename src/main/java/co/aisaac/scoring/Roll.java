package co.aisaac.scoring;

class Roll {
	final RollType rollType;
	int value;

	public Roll(String data) throws StateException {
		if (data.equals("X")) {
			value = 10;
			rollType = RollType.STRIKE;
			return;
		}

		if (data.equals("/")) {
			rollType = RollType.SPARE;
			return;
		}

		if (data.equals("-")) {
			value = 0;
			rollType = RollType.UNUSED;
			return;
		}

		try {
			int i = Integer.parseInt(data);

			if (i < 0 || i > 9) {
				throw new StateException(data + " is not a valid roll.");
			}

			rollType = RollType.SOME_OR_NO_PINS;
			value = i;
		} catch (NumberFormatException nfe) {
			throw new StateException(nfe.getMessage(), nfe);
		}
	}

	boolean isStrike() {
		return rollType == RollType.STRIKE;
	}

	boolean isSpare() {
		return rollType == RollType.SPARE;
	}

	boolean isUnused() {
		return rollType == RollType.UNUSED;
	}

	boolean isRegularRoll() {
		return rollType == RollType.SOME_OR_NO_PINS;
	}


	enum RollType {
		// knock down all pins on your first roll
		STRIKE,

		// knock down all pins within 2 rolls, only can get this on second roll
		SPARE,

		// a regular hit, some pins were knocked over, pins still standing
		SOME_OR_NO_PINS,

		// no roll was needed, applies to second roll if first was strike
		UNUSED
	}
}
