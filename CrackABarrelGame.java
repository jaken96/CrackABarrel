import java.util.Scanner;

public class CrackABarrelGame {

	public static void main(String[] args) {
		// Welcome player
		System.out.println("Welcome to Crack-A-Barrel!\nAll inputs should be integers.\nEnter 0 at any time to quit.\n");
		// Initialize IO (in this case, scanner)
		Scanner reader = new Scanner(System.in);
		// Prompt player for board size
		System.out.println("Please select number of rows (Standard is 5):");
		int input = reader.nextInt();
		// Initialize game board
		GameBoard gb = new GameBoard(input);
		System.out.println(gb);
		// Prompt user for starting empty peg location
		System.out.println("Please select a starting position number: ");
		input = reader.nextInt();
		// Set starting node to empty
		gb.getNode(input).setEmpty(true);
		System.out.println(gb);
		// start game
		while(input > 0){
			// Prompt for empty space
			System.out.println("Please select an empty space: ");
			input = reader.nextInt();
			int emptySpace = input;
			// check that space is actually empty
			if(!gb.getNode(emptySpace).isEmpty() ){
				System.err.println("Oops, that space is full.");
				continue;
			}
			if(gb.getNode(emptySpace).getValidMoves().isEmpty()){
				System.err.println("Oops, that space has no valid moves.");
				continue;
			}
			// Prompt for peg to move to empty space
			System.out.println("Please select a peg to move: ");
			System.out.println(gb.getNode(input).getValidMoves().toString()+"\n");
			input = reader.nextInt();
			int pegToMove = input;
			gb.movePiece(gb.getNode(emptySpace), gb.getNode(pegToMove));


			System.out.println(gb);

		}
		System.out.println("Thanks for playing!");
		reader.close();
	}

}
