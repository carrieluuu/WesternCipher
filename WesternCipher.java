import java.io.*;
import java.util.Scanner;

/**
 * This class represents an encoder and decoder for Western Cipher based on the
 * given rules.
 * 
 * @author Carrie Lu (251140757)
 */
public class WesternCipher {
	/**
	 * CircularArrayQueue to store encoding elements
	 */
	private CircularArrayQueue<Character> encodingQueue;
	/**
	 * CircularArrayQueue to store decoding elements
	 */
	private CircularArrayQueue<Character> decodingQueue;

	/**
	 * Constructor initializes both the encoding and decoding queues with a capacity
	 * of 10 and as type Character.
	 * 
	 */
	public WesternCipher() {
		encodingQueue = new CircularArrayQueue<Character>(10);
		decodingQueue = new CircularArrayQueue<Character>(10);

	}

	/**
	 * Constructor initializes both the encoding and decoding queues with the the
	 * capacity provided and as type Character.
	 * 
	 * @param capacity the specified capacity
	 */
	public WesternCipher(int capacity) {
		encodingQueue = new CircularArrayQueue<Character>(capacity);
		decodingQueue = new CircularArrayQueue<Character>(capacity);

	}

	/**
	 * Method that takes a string as input, splits the string into individual
	 * characters, applies the Western Cipher algorithm, rejoins the individual
	 * characters into a string and returns it
	 * 
	 * @param str String to be encoded
	 * @return String encoded string
	 */
	public String encode(String str) {
		// Split the string into individual characters and store them in a char array
		char[] cArray = str.toCharArray();

		// Enqueue characters from cArray into encodingQueue
		for (int i = 0; i < cArray.length; i++) {
			encodingQueue.enqueue(cArray[i]);

		}

		// Rule 3: Values A, E, I, O, U, Y are replaced with 1, 2, 3, 4, 5, 6,
		// respectively. This rule takes priority over other rules
		for (int i = 0; i < encodingQueue.size(); i++) {
			char ch = encodingQueue.dequeue();
			if (ch == 'A') {
				ch = '1';
			} else if (ch == 'E') {
				ch = '2';
			} else if (ch == 'I') {
				ch = '3';
			} else if (ch == 'O') {
				ch = '4';
			} else if (ch == 'U') {
				ch = '5';
			} else if (ch == 'Y') {
				ch = '6';
			}
			encodingQueue.enqueue(ch);

		}

		// Rule 1: . All letters are always shifted forwards 5 steps in the alphabet.
		for (int i = 0; i < encodingQueue.size(); i++) {
			char ch = encodingQueue.dequeue();

			ch = shiftChar(ch, 5);
			encodingQueue.enqueue(ch);
		}

		// Rule 2: All letters are shifted an additional 2 steps forward for every spot
		// in the length of the message they are in, starting with an index of 0.
		for (int i = 0; i < encodingQueue.size(); i++) {
			char ch = encodingQueue.dequeue();
			ch = shiftChar(ch, 2 * i);
			encodingQueue.enqueue(ch);
		}

		// Rule 5: If the previous letter was converted to a numerical value, and the
		// next value is also to be converted to a numerical value, A, E, I, O, U, Y are
		// converted to 3, 4, 5, 6, 1, 2, respectively.

		// int numeric to keep track of whether the previous letter is a numerical value
		int numeric = -1;
		for (int i = 0; i < encodingQueue.size(); i++) {
			char ch = encodingQueue.dequeue();

			// If ch is a numerical value from 1-9
			if (ch >= 48 && ch <= 57) {
				if (numeric == -1) {
					// First numerical value substituted from Rule 3
					numeric = ch - 48;
				} else {
					ch += 2;
					// If ch is greater than numerical value 6, subtract 6 from its ascii to rotate
					// back to 1 and onwards
					if (ch >= 55) {
						ch -= 6;
					}
				}
			} else {
				// reset numeric back to -1 once ch is not a numerical value
				numeric = -1;
			}
			encodingQueue.enqueue(ch);
		}

		// Rule 4: If the previous letter was converted to a numerical value, the letter
		// that follows should be shifted backwards by twice the amount that the letter
		// was converted to.
		numeric = -1;
		for (int i = 0; i < encodingQueue.size(); i++) {
			char ch = encodingQueue.dequeue();
			// If ch is a numerical value from 1-9
			if (ch >= 48 && ch <= 57) {
				numeric = ch - 48;
			}
			// If ch is a numerical value from 1-9 and the previous letter was converted to
			// a numerical value
			else if (ch >= 65 && ch <= 90 && numeric >= 0) {
				ch = shiftChar(ch, -2 * numeric);
				numeric = -1;
			}
			encodingQueue.enqueue(ch);

		}
		return printQueue(encodingQueue);

	}

	/**
	 * Method that takes a string as input, splits the string into individual
	 * characters, undoes the Western Cipher algorithm, rejoins the individual
	 * characters into a string and returns it
	 * 
	 * @param str String to be decoded
	 * @return String decoded string
	 */
	public String decode(String str) {
		// Split the string into individual characters and store them in a char array
		char[] cArray = str.toCharArray();

		// Enqueue characters from cArray into encodingQueue
		for (int i = 0; i < cArray.length; i++) {
			decodingQueue.enqueue(cArray[i]);

		}

		// Reverse rule 4
		int numeric = -1;
		for (int i = 0; i < decodingQueue.size(); i++) {
			char ch = decodingQueue.dequeue();
			if (ch >= 48 && ch <= 57) {
				numeric = ch - 48;
			} else if (ch >= 65 && ch <= 90 && numeric >= 0) {
				// Shift backwards by twice the amount that the letter
				// was converted to
				ch = shiftChar(ch, 2 * numeric);
				numeric = -1;
			}
			decodingQueue.enqueue(ch);

		}

		// Reverse rule 5
		numeric = -1;
		for (int i = 0; i < decodingQueue.size(); i++) {
			char ch = decodingQueue.dequeue();
			if (ch >= 48 && ch <= 57) {
				if (numeric == -1) {
					// first numeric value shifted from rule 3
					numeric = ch - 48;
				} else {
					ch -= 2;
					if (ch <= 48) {
						ch += 6;
					}
				}
			} else {
				numeric = -1;
			}
			decodingQueue.enqueue(ch);
		}

		// Reverse rule 2
		for (int i = 0; i < decodingQueue.size(); i++) {
			char ch = decodingQueue.dequeue();
			// Shift a 2 steps backwards for every spot in the length of the message they
			// are in, starting with an index of 0.
			ch = shiftChar(ch, -2 * i);
			decodingQueue.enqueue(ch);
		}

		// Reverse rule 1
		for (int i = 0; i < decodingQueue.size(); i++) {
			char ch = decodingQueue.dequeue();
			// Shift 5 steps backwards
			ch = shiftChar(ch, -5);
			decodingQueue.enqueue(ch);
		}

		// Reverse rule 3 to convert from 1, 2, 3, 4, 5, 6 to A, E, I, O, U,
		// respectively
		for (int i = 0; i < decodingQueue.size(); i++) {
			char ch = decodingQueue.dequeue();
			if (ch == '1') {
				ch = 'A';
			} else if (ch == '2') {
				ch = 'E';
			} else if (ch == '3') {
				ch = 'I';
			} else if (ch == '4') {
				ch = 'O';
			} else if (ch == '5') {
				ch = 'U';
			} else if (ch == '6') {
				ch = 'Y';
			}
			decodingQueue.enqueue(ch);

		}

		return printQueue(decodingQueue);

	}

	/**
	 * Main method that prompts the user whether they would like to encode or decode
	 * a string and prints the encoded/decoded string as appropriate
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		WesternCipher wc = new WesternCipher();

		Scanner getData = new Scanner(System.in);

		// Prompt the user about whether they would like to encode or decode a string
		System.out.println("Please enter 1 for encode and 2 for decode.");
		int input = getData.nextInt();
		getData.nextLine();

		// If user enters 1 for encoding
		if (input == 1) {
			String str = "";
			// Prompt the user if they would like to enter another string until no is
			// entered
			while (!str.equalsIgnoreCase("no")) {
				System.out.println("Please enter a string to encode. Enter no to exit.");
				str = getData.nextLine();
				// Call encode method to encode the inputted String
				System.out.println(wc.encode(str));
			}
			System.exit(0);

		}
		// If user enters 2 for decoding
		else if (input == 2) {
			String str = "";
			while (!str.equalsIgnoreCase("no")) {
				// Prompt the user if they would like to enter another string until no is
				// entered
				System.out.println("Please enter a string to decode. Enter no to exit.");
				str = getData.nextLine();
				// Call encode method to decode the inputted String
				System.out.println(wc.decode(str));

			}
			System.exit(0);
		}

		getData.close();

	}

	/**
	 * Method that shifts the character a specified number of steps
	 * 
	 * @param current character that has been dequeued
	 * @param steps   number of steps the character is to be shifted
	 * @return char shifted character or if the character is a numerical value, the
	 *         original character
	 */
	private char shiftChar(char current, int steps) {
		// If character is within A-Z, shift steps
		if (current >= 65 && current <= 90) {
			// Steps mod 26 (total number of characters from A-Z )to ensure steps being
			// shifted is within range
			steps %= 26;
			int newAscii = current + steps;
			// if newAscii goes under range of A-Z, rotate back to Z
			if (newAscii < 65) {
				newAscii += 26;
			}
			// if newAscii goes over range of A-Z, rotate back to A
			else if (newAscii > 90) {
				newAscii -= 26;
			}
			return (char) newAscii;
		}
		// Character is a numerical value, so no need to shift
		else {
			return current;

		}
	}

	/**
	 * Method that builds encoded/decoded string
	 * 
	 * @param queue encoding or decoding queue
	 * @return formatted string of encoded/decoded string built and formatted by
	 *         StringBuilder
	 */
	private String printQueue(CircularArrayQueue<Character> queue) {
		StringBuilder sb = new StringBuilder();
		while (!queue.isEmpty()) {
			sb.append(queue.dequeue());

		}
		return sb.toString();
	}

}