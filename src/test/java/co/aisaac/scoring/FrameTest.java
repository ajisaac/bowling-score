package co.aisaac.scoring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Have probably missed a few edge cases.
 */
class FrameTest {
	@Test
	@DisplayName("Valid and Invalid frames.")
	void validAndInvalidFrames() {
		// 2 roll frames
		String[][] invalidFrames = new String[][]
				{
						{"X", "X"}, // 2 strikes in 2 roll frame
						{"X", "0"}, // a second roll in 2 roll frame
						{"X", "/"}, // strike then spare in 2 roll frame

						{"/", "X"}, // spare then strike in 2 roll frame
						{"/", "0"}, // spare in first roll
						{"/", "/"}, // 2 spares
						{"/", "-"}, // first roll spare

						// can't just skip a roll
						{"", "0"},
						{"0", ""},
						{"0", "-"},
						{"-", "0"},

						// bad math
						{"9", "9"},
						{"-9", "0"},
						{"0", "19"},

						// invalid input
						{"0", "x"},
						{"0", "-"},
						{"0", ".5"},
						{"0", "_"},
						{"0", "="},
						{"", ""},
						{"", "-"},

				};

		for (var frame : invalidFrames) {
			assertThrows(StateException.class, () -> new Frame(frame[0], frame[1]), frame[0] + ", " + frame[1]);
		}

		// 3 roll frames
		invalidFrames = new String[][]
				{
						{"X", "X", "/"},
						{"X", "/", "0"},
						{"/", "X", "/"},
						{"0", "/", "/"},
						{"9", "9", "0"},
						{"5", "5", "/"},
						{"5", "6", "X"},
						{"X", "5", "6"},
						{"X", "5", "-"},
						{"5", "/", "-"},
						{"X", "X", "-"},
						{"-", "X", "X"},
						{"X", "-", "X"},

				};

		for (var frame : invalidFrames) {
			assertThrows(StateException.class, () -> new Frame(frame[0], frame[1], frame[2]),
					frame[0] + ", " + frame[1] + ", " + frame[2]);
		}
	}
}
