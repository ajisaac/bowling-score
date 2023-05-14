package co.aisaac;


/*

Bowling scoring

What I'd like you to build is a concurrent application that scores bowling games. For background on scoring in 10-pin
bowling see https://www.bowl.com/Welcome/Welcome_Home/Keeping_Score/  Note that if you've never played bowling scoring
is a bit weird and may take a bit to get your head around.

An input file containing 1000 games is attached (see format notes below). You should use 5 threads or processes and
divide up the games between them. The program should report at the end the mean, median, and standard deviation of the
scores.

This is not meant to be a trick question at all, if you have any questions please let me know.

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

import co.aisaac.scoring.GameScore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) throws IOException {

		for (int i = 0; i < 1; i++) { // testing loop if you want to warm the jvm up with > 50 iterations
			long start = System.currentTimeMillis();

			try (Stream<String> lines = Files.lines(Path.of("data.csv"));
				 ExecutorService executorService = Executors.newFixedThreadPool(5)) {

				List<Callable<GameScore>> tasks = lines
						.map(s -> (Callable<GameScore>) () -> new GameScore(s))
						.toList();

				List<GameScore> results = new ArrayList<>();
				for (var result : executorService.invokeAll(tasks)) {
					try {
						GameScore gs = result.get();
						results.add(gs);
					} catch (ExecutionException ex) {
						System.out.println(ex.getMessage());
					}
				}

				results.sort(Comparator.comparingInt(GameScore::getScore));

				// median
				int median = results.get((results.size() / 2) - 1).getScore();

				// mean
				double total = results.size();
				double scoreSum = 0;
				for (var result : results) {
					scoreSum += result.getScore();
				}
				double mean = scoreSum / total;

				// standard deviation
				double summedSqr = 0;
				for (var result : results) {
					summedSqr += Math.pow((result.getScore() - mean), 2);
				}
				double stdDev = Math.sqrt(summedSqr / total);

				System.out.println("Mean is " + mean + ".");
				System.out.println("Median is " + median + ".");
				System.out.println("Standard deviation is " + stdDev + ".");


			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			System.out.println("Time to run: " + (System.currentTimeMillis() - start) + " ms.");
		}
	}
}
