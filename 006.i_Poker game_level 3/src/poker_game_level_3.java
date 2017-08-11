/*
 * Practice 006.i_Poker game_level 3
 * Date 20170801
 */

import java.util.Scanner;
import java.util.Arrays;

public class poker_game_level_3 {

	public static Scanner scanner = new Scanner(System.in);
	public static int[][] cards;
	
	public static void main(String[] args) {
		
		//Generate cards.
		create_Cards();
		
		//Shuffle cards.
		shuffle(cards, 52);

		int status = 1;
		int[][] drawn_cards = new int [2][5];
		do {
			switch (status) {
			case 0: //Ask user's choice.
				System.out.print("What do you want to do next? \n1)Continue drawing. 2)Reshffuling. 3)Quit.:");
				int input = scanner.nextInt();
				
				if (input == 1) 
					status = 1;
				else if (input == 2) 
					status = 2;
				else if (input == 3)
					status = -1;
				break;
		
			case 1: //Draw cards.
				if (cards[0].length <= 8) {
					System.out.print("Insufficient cards, please re-shuffle.");
					status = 0;
					break;
				}
				drawn_cards = draw(cards, 5);
				
				//Show results.
				print(drawn_cards, 5);
				status = 3;
				break;
				
			case 2: //Re-shuffle cards.
				create_Cards();
				shuffle(cards, 52);
				status = 0;
				break;
				
			case 3: //Decide cards combination type.
				String result;
				result = decide(drawn_cards);
				System.out.printf("You got %5s!\n", result);
				status = 0;
				break;
			}
		} while (status != -1);
		
		System.out.print("Progaram terminate!");

	}
	
	public static void create_Cards() {
		//cards[0][point], cards[1][suit], suit(0,1,2,3) = (clubs, diamonds, hearts, spades)
		cards = new int [2][52];
		int i = 0;
		for (int j = 0; j < 4; j++) {
			for (int k = 1; k < 14; k++) 
				cards[0][i++] = k;
		}
		
		i = 0;
		for (int j = 0; j < 4; j++) {
			for (int k = 0; k < 13; k++) {
				cards[1][i++] = j;
			}
		}
	}
	
	public static void shuffle(int[][] array, int n) {
		int k, temp_0, temp_1;
		for (int i = 0; i < n; i++) {
			k = (int)(Math.random()*52);
			temp_0 = array[0][i];
			temp_1 = array[1][i];
			array[0][i] = array[0][k];
			array[1][i] = array[1][k];
			array[0][k] = temp_0;
			array[1][k] = temp_1;
		}
	}
	
	public static int[][] draw(int[][] array, int n){
		int[][] result = new int [2][n];
		int k, status = 0;
		int[] draws = new int[n];
		
		for (int i = 0; i < 5; i++) {
			result[0][i] = cards[0][i];
			result[1][i] = cards[1][i];
		}
		
//		//Generate which index will be drawn.
//		k = (int)(Math.random()*(cards[0].length)); 
//		draws[0] = k;
//		
//		int a = 1;
//		while (a < n) {
//			k = (int)(Math.random()*(cards[0].length));
//			for (int j = 0; j < a ; j++) {
//				//If drawn card is replicated, redraw.
//				if(k == draws [j]) 
//					break;
//				if(j == a - 1) {
//					draws[a++] = k;
//					break;
//				}
//			}
//		}
//
//		
//		//Pick out cards.
//		for (int i = 0; i < n; i++) {
//			result[0][i] = array[0][draws[i]];
//			array[0][draws[i]] = -1;
//			
//			result[1][i] = array[1][draws[i]];
//			array[1][draws[i]] = -1;
//		}
//		
//		//Remove picked cards.
//		for (int i = 0; i < cards[0].length; i++) {
//			while (cards[0][i] == -1) {
//				System.arraycopy(cards[0], i + 1, cards[0], i, (cards[0].length - i - 1));
//				System.arraycopy(cards[1], i + 1, cards[1], i, (cards[1].length - i - 1));
//			}
//		}
		
		//Resize array and deduct first 5 elements.
		System.arraycopy(cards[0], 5, cards[0], 0, cards[0].length - 5);
		System.arraycopy(cards[1], 5, cards[1], 0, cards[0].length - 5);
		return result;
	}
	
	public static void print(int[][] array, int n) {
		String suit;
		for (int i = 0; i < n; i++) {
			if (array[1][i] == 0)
				suit = " club";
			else if(array[1][i] == 1)
				suit = " cube";
			else if(array[1][i] == 2)
				suit = "heart";
			else
				suit = "spade";
			System.out.printf("%2d: %s %2d\n",(i + 1), suit, array[0][i]);
		}
	}
	
	public static String decide(int[][] array) {
		boolean flush = false, three = false;
		//Arrange array in order from smallest to largest using bubble sorting.
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4 - i; j++) {
				if (array[0][j] > array[0][j + 1]) {
					int temp_0 = array[0][j];
					int temp_1 = array[1][j];
					array[0][j] = array[0][j + 1];
					array[1][j] = array[1][j + 1];
					array[0][j + 1] = temp_0;
					array[1][j + 1] = temp_1;
				}
			}
		}
		//Straight flush (including royal flush).
		if (array[1][0] == array[1][1] && array[1][0] == array[1][2] && array[1][0] == array[1][3] && array[1][0] == array[1][4]) {
			if (array[0][0] == 1 && array[0][4] == 13)
				return "Royal flush.";
			else if (array[0][4] == array[0][3] + 1 && array[0][3] == array[0][2] + 1 && array[0][2] == array[0][1] + 1 && array[0][1] == array[0][0] + 1 ) {
				return "Straight flush.";
			}
			flush = true;
		}
		
		//Four of a kind.(decide if three or not at first.
		if (array[0][1] == array[0][2] && array[0][2] == array[0][3]) {
			three = true;
			if (array[0][0] == array[0][1] || array[0][4] == array[0][3])
				return "Four of a kind";
		}
		
		//Full house.
		if ((array[0][3] == array[0][4] && array[0][0] == array[0][1] && array[0][1] == array[0][2]) || (array[0][0] == array[0][1] && array[0][2] == array[0][3] && array[0][3] == array[0][4]))
			return "Full house";
		
		//Flush.
		if (flush == true)
			return "Flush";
		
		//Straight.
		if (array[0][4] == array[0][3] + 1 && array[0][3] == array[0][2] + 1 && array[0][2] == array[0][1] + 1 && array[0][1] == array[0][0] + 1 )
			return "Straight";
		
		//Three.
		if (three == true)
			return "Three";
		if ((array[0][0] == array[0][1] && array[0][1] == array[0][2]) || (array[0][2] == array[0][3] && array[0][3] == array[0][4]))
			return "Three";
		
		//Pairs.
		int count = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 5; j++) {
				if (array[0][i] == array[0][j])
					count++;
			}
		}
		if (count == 2)
			return "Two pairs";
		if (count == 1)
			return "One pair";
		return "High card";
	}


}
