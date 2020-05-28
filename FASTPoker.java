import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.SplittableRandom;

public class FASTPoker {
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		Random r = new Random();

		// generate a new and never touched deck
		for (int i = 0; i < numbersUntouched.length; i += 1) {
			numbersUntouched[i] = i;
		}

		for (int i = 0; i < 10000000; i += 1) {
			generateDeck();
		}

		float total = rfAmount + sfAmount + foakAmount + fhAmount + flushAmount + straightAmount + toakAmount + tpAmount
				+ opAmount + nothingAmount;

		System.out.println("RoyalFlush: " + (rfAmount / total) * 100 + "%");
		System.out.println("StraigtFlush: " + (sfAmount / total) * 100 + "%");
		System.out.println("4 of a Kind: " + (foakAmount / total) * 100 + "%");
		System.out.println("Full House: " + (fhAmount / total) * 100 + "%");
		System.out.println("Flush: " + (flushAmount / total) * 100 + "%");
		System.out.println("Straight: " + (straightAmount / total) * 100 + "%");
		System.out.println("3 of a kind: " + (toakAmount / total) * 100 + "%");
		System.out.println("2 pairs: " + (tpAmount / total) * 100 + "%");
		System.out.println("one Pair: " + (opAmount / total) * 100 + "%");
		System.out.println("Nothing: " + (nothingAmount / total) * 100 + "%");

		System.out
				.println("it took " + Double.toString((System.nanoTime() - startTime) / Math.pow(10, 9)) + " seconds.");
	}

	static final int[] numbersUntouched = new int[52];
	static int[] numbersSwitched = new int[52];
	static int[] selectedNumbers = new int[5];
	static int randomNumber = 0;
	static Random random = new Random();
	static SplittableRandom sR = new SplittableRandom();
	static int pairs = 0;
	static int min = 0;
	static int max = 0;

	static int moduloMax = 0;
	static int moduloMaxNOT12 = 0;
	static int moduloMin = 0;

	static int rfAmount = 0;
	static int sfAmount = 0;
	static int foakAmount = 0;
	static int fhAmount = 0;
	static int flushAmount = 0;
	static int straightAmount = 0;
	static int toakAmount = 0;
	static int tpAmount = 0;
	static int opAmount = 0;
	static int nothingAmount = 0;

	public static void generateDeck() {
		numbersSwitched = numbersUntouched;
		pairs = 0;

		for (int i = 0; i < 5; i += 1) {
			randomNumber = random.nextInt(51 - i);
			// randomNumber = ThreadLocalRandom.current().nextInt(0, 51-i);
			// randomNumber = sR.nextInt(0, 51);
			selectedNumbers[i] = numbersSwitched[randomNumber];
			numbersSwitched[randomNumber] = numbersSwitched[51 - i];
			numbersSwitched[51 - i] = selectedNumbers[i];

			for (int j = 0; j < i; j += 1) {
				if (selectedNumbers[j] % 13 == selectedNumbers[i] % 13) {
					pairs += 1;
				}
			}
		}

		// CHECKS FOR COMBINATIONS
		if (pairs == 0) {
			min = 53;
			max = 0;

			for (int i = 0; i < selectedNumbers.length; i += 1) {
				if (selectedNumbers[i] < min) {
					min = selectedNumbers[i];
				}
				if (selectedNumbers[i] > max) {
					max = selectedNumbers[i];
				}
			}

			if (max - min == 4) {
				if (min % 13 == 8) {
					rfAmount++;
					return;
				}

				if (min - (min % 13) == max - (max % 13)) {
					sfAmount++;
					return;
				}
			}

			if (max % 13 == 12) {
				if ((max - 12) - min == 4 && max - min <= 12) {
					printArray(selectedNumbers);
					sfAmount++;
					return;
				}
			}

			if (min - (min % 13) == max - (max % 13)) {
				flushAmount++;
				return;
			}

			moduloMax = 0;
			moduloMaxNOT12 = 0;
			moduloMin = 53;
			for (int j = 0; j < selectedNumbers.length; j += 1) {
				if (selectedNumbers[j] % 13 < moduloMin) {
					moduloMin = selectedNumbers[j] % 13;
				}
				if (selectedNumbers[j] % 13 > moduloMax) {
					moduloMax = selectedNumbers[j] % 13;
				}
				if (selectedNumbers[j] % 13 != 12 && selectedNumbers[j] % 13 > moduloMaxNOT12) {
					moduloMaxNOT12 = selectedNumbers[j] % 13;
				}
			}

			if (moduloMax - moduloMin == 4 || (moduloMax == 12 && moduloMaxNOT12 == 3 && moduloMin == 0)) {
				straightAmount++;
				return;
			}

			nothingAmount++;
			return;
		}

		// IF PAIRS != 0
		// if pairs exist
		if (pairs == 6) {
			foakAmount++;
			return;
		}
		if (pairs == 4) {
			fhAmount++;
			return;
		}

		if (pairs == 3) {
			toakAmount++;
			return;
		}
		if (pairs == 2) {
			tpAmount++;
			return;
		}
		opAmount++;
		return;
	}

	public static void printArray(int[] n) {
		for (int i = 0; i < n.length; i += 1) {
			System.out.print(n[i] + " ");
		}
		System.out.println("");

	}
}