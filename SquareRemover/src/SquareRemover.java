import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.lang.Math;
import java.lang.StringBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* TODO:
* -- complete playIt function
* -- add shell interaction
* 	-- make game manual
* 	-- print board results
* 	-- algorithm to choose 2 tiles
* 	-- refactor to consider 2 tiles either vertical/ horizontal
*/

public class SquareRemover {
	public int[] THEBUFFER;
	public int BUFFERIDX = 0; // not needed???
	public String[] THEBOARD;
	public int[] THEMOVES;
	public int SCORE;
	public int COLORS;
	public char[] COLORSCHAR;
	public int STARTSEED;
	
	public static void main (String []args) {
		
		SquareRemover s = new SquareRemover();
		String[] withMatches0 = {"RGBY", "BRYY", "RGRB", "RRGG"};
		String[] withMatches1 = {"RRGG", "RRBY", "YBBG", "GBBR"};
		String[] withMatches2 = {"YBYG", "GYYR", "RYYG", "BYYB"};
		String[] withMatches3 = {"RRRR", "RRRR", "RRRR", "RRRR"};
		
		s.setTheBoard(withMatches0);
		s.STARTSEED = 1;
		s.SCORE = 0;
		s.playIt (4, "RGBY".toCharArray(), withMatches0, s.STARTSEED);
		
		//int[] out = s.sniffTilesAround();
		//System.out.println(Arrays.toString(out));
		
		/**
		s.setTheBoard(withMatches1);
		//int[] out = s.sniffTilesAround(0,0);
		int[] out = s.sniffTilesAround(2,1);
		System.out.println(Arrays.toString(out)); //  [2, 1]
		out = s.sniffTilesAround(2,2);
		System.out.println(Arrays.toString(out)); // [2, 1]
		out = s.sniffTilesAround(3,1);
		System.out.println(Arrays.toString(out)); //  [2, 1]
		out = s.sniffTilesAround(3,2);
		System.out.println(Arrays.toString(out)); // [2, 1]
		out = s.sniffTilesAround(1,2);
		System.out.println(Arrays.toString(out)); // [-1, -1]
		
		out = s.sniffTilesAround(0,0);
		System.out.println(Arrays.toString(out)); // [0, 0]
		out = s.sniffTilesAround(0,1);
		System.out.println(Arrays.toString(out)); // [0, 0]
		out = s.sniffTilesAround(1,0);
		System.out.println(Arrays.toString(out)); // [0, 0]
		out = s.sniffTilesAround(1,1);
		System.out.println(Arrays.toString(out)); // [0, 0]
		
		s.setTheBoard(withMatches2);
		out = s.sniffTilesAround();
		System.out.println(Arrays.toString(out));
		
		s.setTheBoard(withMatches3);
		out = s.sniffTilesAround();
		System.out.println(Arrays.toString(out));

		
		* 4 <= colors <= 6
		* 8 <= board.length <= 16
		* 1 <= startSeed <= 2,147,483,646
		*/
		
		return;
	}
	
	public SquareRemover(int colors, char[] colorsChar, String[] board, int startSeed) {
		this.COLORSCHAR = colorsChar;
		this.COLORS = colors; 		// newcolor = buff[i] % colors
		this.THEBUFFER = genBuffer(startSeed, 4);
		
	}

	public int[] playIt () {
		int movesCount = 10;		// 10K
		List<int[]> moves = new ArrayList<int[]>();
		
		adjustScore();
		int[] tiles;
		for (int i = 0; i < movesCount; i++) {
			
			System.out.println("playIt() "); 
			System.out.println(THEBOARD[0]); 
			System.out.println(THEBOARD[1]); 
			System.out.println(THEBOARD[2]); 
			System.out.println(THEBOARD[3]);
			
			tiles = searchForMove(); 	// search for move, center of board worst case
											// return index shift from tile
			moves.add(tiles);				// store move in THEMOVES
			
			System.out.println("move tile " + Arrays.toString(tiles));
			
			
			char c = THEBOARD[tiles[0]].charAt(tiles[1]);
			StringBuilder str = new StringBuilder(THEBOARD[tiles[0]]);
			str.replace(tiles[1], tiles[1] + 1, String.valueOf(THEBOARD[tiles[2]].charAt(tiles[3])));
			THEBOARD[tiles[0]] = str.toString();
			str = new StringBuilder(THEBOARD[tiles[2]]);
			str.replace(tiles[3], tiles[3] + 1, String.valueOf(c));
			THEBOARD[tiles[2]] = str.toString();
			
			int[] matches = sniffTilesAround(tiles[0], tiles[1]); //actual index of top left tile of match
			if (matches[0] != -1) {
				replaceTiles(matches[0], matches[1]);
			}
			
			//
			// recurse to sniff tiles around??? 
			//
			
			adjustScore();
		}
		
		// AdjustScore		
			// make 10K moves,
			  // choose 2 tiles, swap
			  // if render 2x2 uni-color tiles
			  // AdjustScore
			
			// return int[] of size 30K
			  // 3*i and 3*i+1 give row and column of one of the tiles used in your move
			  // 3*i+2 gives the direction from already specified tile to another tile used in your move
			  // 0 -- up, 1 -- right, 2 -- down, 3 -- left
		return null;
	}
	
	// yet to be tested
	public void adjustScore() {
		int[] matches = adjustScoreBrute();
		// increase score
		// replace matches with buffer tiles
		for (int i = 0; i < matches.length; i++) {
			if (matches[i] == -1) {
				return;
			}
			else if ((i % 2) == 0) {
				replaceTiles(matches[i], matches[i+1]);
				increaseScore();
			}
		}
	}
	
	// sniffs the board for a "move" DFS way
	// optimize for the "best move"
	// returns indices {2*i, 2*i+1} of 2 tiles to switch for each i
	public int[] sniffBoardHelper(int row, int col) {
		int[] matches = {-1, -1, -1, -1};
		int rowCpy = new Integer(row);
		int colCpy = new Integer(col);
		if (row < THEBOARD.length-1) {
			for (int i=1; i <= (THEBOARD.length-(row-1)); i++) {
				// check THEBOARD[row][col] matches bottom neighbour tile
				if (ifTileMatches(row, col, row+1, col)) {
					// if so, check col is not the last column
					if (col < THEBOARD.length-1) {
						
						// if so, check THEBOARD[row][col] matches (top) right neighbour tile
						if (ifTileMatches(row, col, row, col+1)) {
							
							// check swap options for bottom right tile
							if ((col < THEBOARD.length-2) && (ifTileMatches(row, col, row+1, col+2))) {
								return new int[] {row+1, col+1, row+1, col+2};
							}
							if ((row < THEBOARD.length-2) && (ifTileMatches(row, col, row+2, col+1))) {
								return new int[] {row+1, col+1, row+2, col+1};
							}
							
						}
						
						// else, check THEBOARD[row][col] matches (bottom) right neighbour tile
						else if (ifTileMatches(row, col, row+1, col+1)) {
							
							// check swap options for top right tile
							if ((col < THEBOARD.length-2) && (ifTileMatches(row, col, row, col+2))) {
								return new int[] {row, col+1, row, col+2};
							}
							if ((row < THEBOARD.length-2) && (ifTileMatches(row, col, row-1, col))) {
								return new int[] {row, col+1, row, col+2};
							}
						}
						
					}
				}
				row += 1;
			}
			// else recurse on the bottom neighbour, cuz DFS
			return sniffBoardHelper(rowCpy, colCpy+1);
		}
		// else row is last bottom row
		return matches;
	}

	// rotates THEBOARD 90 deg clockwise
	/**
	 * |A B C|  		|G D A|
	 * |D E F|	|-->	|H E B|
	 * |G H I|			|I F C|
	 */
	public void rotateBoardCwise() {
		String[] rotatedBoard = new String[THEBOARD.length];
		for (int j = 0; j <= THEBOARD.length-1; j++) {
			String newRow = "";
			for (int i = THEBOARD.length-1; i >= 0; i--) {
				newRow += THEBOARD[i].charAt(j);
			}		
			rotatedBoard[j] = newRow;
		}
		THEBOARD = rotatedBoard;
		// System.out.println( Arrays.toString(THEBOARD) );
	}

	// rotates THEBOARD 90 deg counter clockwise
	public void rotateBoardCCwise() {
		String[] rotatedBoard = new String[THEBOARD.length];
		for (int j = THEBOARD.length-1; j >= 0; j--) {
			String newRow = "";
			for (int i = 0; i <= THEBOARD.length-1; i++) {
				newRow += THEBOARD[i].charAt(j);
			}		
			rotatedBoard[Math.abs(j - THEBOARD.length+1)] = newRow;
		}
		THEBOARD = rotatedBoard;
		// System.out.println( Arrays.toString(THEBOARD) );
	}
	
	// YET TO BE TESTED
	public int[] searchForMove() {
		int[] tiles = bruteSearchForBestMove();
		if (tiles[0] != -1) {
			return tiles;
		}
		tiles = bruteSearchForBetterMove();
		if (tiles[0] != -1) {
			return tiles;
		}
		
		// if no move, return center of THEBOARD tiles
		Random r = new Random();
		int row = r.nextInt(THEBOARD.length-2);
		return new int[] {row, row, row+1, row};
	}
	
	// returns indices of 2 adjacent tiles to switch
	// specifically looks for a point rendering move
	// bruteSearch-- methods must be used after adjustScoreBrute()
	public int[] bruteSearchForBestMove() {
		Pattern p = Pattern.compile("(([a-zA-Z])\\2)");
		Matcher m;
		// for each row, use regex for consecutive identical chars
		for (int i = 0; i < THEBOARD.length; i++) {
			m = p.matcher(THEBOARD[i]);
			while (m.find()) {
				// check if at least 1 tile below matches
				int[] incrV = {-1, 1};
				int[] incrH = {0, 1};
				for (int k : incrV) {
					for (int j : incrH) {
						
						if (0 <= i+k && i+k < THEBOARD.length) {
							if (ifTileMatches(i, m.start(), i+k, m.start()+j)) {
								
								// check neighbours of i+1, (j+1 % 2)
								if (0 <= i+(2*k) && i+(2*k) < THEBOARD.length && ifTileMatches(i, m.start(), i+(2*k), m.start()+((j+1) % 2))) {
									// check top/ bottom 
									return new int[] {i+k, m.start()+((j+1) % 2), i+(2*k), m.start()+((j+1) % 2)};
								}
								if (j == 0 && m.start() > 0 && ifTileMatches(i, m.start(), i+k, m.start()-1)) {
									// check adj left
									return new int[] {i+k, m.start()+((j+1) % 2), i+k, m.start()-1};
								}
								else if (m.start() < THEBOARD.length-3 && ifTileMatches(i, m.start(), i+k, m.start()+2)) {
									// check adj right
									return new int[] {i+k, m.start()+((j+1) % 2), i+k, m.start()+2};
								}
								
								
							}
						}
					}
				}
			}			
		}
		return new int[] {-1,-1,-1,-1};
	}
	
	public int[] LASTMATCH = {-1, -1}; // index of left tile matching "(([a-zA-Z])\\2)"
	public int[] LASTBETTERMATCH = {-1, -1}; // index of 1/4 mismatched tile
	public int[] WANTEDCHARIDX = {-1, -1};
	public char WANTEDCHAR = '\u0000';
	
	public int[] bruteSearchForBetterMove() { // assumes there is no 2x2 match of board + only works horizontally
		Pattern p = Pattern.compile("(([a-zA-Z])\\2)");
		Matcher m;
		
		// look for workable a match
		if (LASTBETTERMATCH[0] == -1 && LASTMATCH[0] == -1) {
			for (int row = 0; row < THEBOARD.length-1; row++) {
				m = p.matcher(THEBOARD[row]);
				if (m.find()) {
					int[] incr = {-1, 1};
					int col = m.start();
					WANTEDCHAR = THEBOARD[row].charAt(col);
					
					for (int j : incr) {
						if (0 <= (row+j) && (row+j)  < THEBOARD.length) {
							if (THEBOARD[row].charAt(col) == THEBOARD[row+j].charAt(col)) {
								LASTBETTERMATCH = new int[] {row+j, col};
							}
							else if (THEBOARD[row].charAt(col) == THEBOARD[row+j].charAt(col+1)) {
								LASTBETTERMATCH = new int[] {row+j, col+1};
							}
						}
						if (LASTBETTERMATCH[0] == -1) {
							LASTMATCH = new int[] {row, col};
						}
					}
					
					// 
					//	find 3rd or 4th tile to complete match
					//
					
					if (charExists(row, LASTMATCH[0] == -1 ? 2 : 1, WANTEDCHAR)) {
						// find index of wanted cahr
						WANTEDCHARIDX = getWantedCharIdx(row, WANTEDCHAR); // searches for char not in this row
						return dragWantedChar(WANTEDCHARIDX, LASTMATCH[0] == -1 ? LASTBETTERMATCH : LASTMATCH);  /////????????????????????????????
						// return move that puts wanted char at WANTEDCHARIDX closer to last matches
					
					}
					break;
				}			
			}
			// if no match on the entire board, return random index
			if (LASTBETTERMATCH[0] == -1 && LASTMATCH[0] == -1) {
				return new int[] {-1,-1,-1,-1};		
			}
		}
		
		// else if working on a workable match
		// can assume there exists enough chars wanted on the board
		
		int[] ret = dragWantedChar(WANTEDCHARIDX, WANTEDCHAR);
		
		// check if need another WANTEDCHAR
		// check if done collecting WANTEDCHAR, reset WANTEDCHAR = '\u0000', WANTEDCHARIDX = {-1, -1}, LASTMATCHes(?)
		
		return ret;		
	}
	
	// given index of wanted char, return a move int[4] 
	// that puts wanted char closer to LASTMATCH or LASTBETTERMATCH
	public int[] dragWantedChar(int[] wantedCharIdx, int[] pullToIdx) {
		// find move that puts charWanted closer to pullToIdx
		// update current WANTEDCHARIDX
	}
	public int[] getWantedCharIdx(int rowToAvoid, char charWanted) {
		Pattern p = Pattern.compile(String.valueOf(charWanted));
		Matcher m;
		for (int row = 0; row < THEBOARD.length-1 && row != rowToAvoid; row++) {
			m = p.matcher(THEBOARD[row]);
			if (m.find()) {
				return new int[] {row, m.start()};
			}
		}
		return new int[] {-1, -1};
	}
	
	public boolean charExists(int rowToAvoid, int occurencesWanted, char charWanted) {
		Pattern p = Pattern.compile(String.valueOf(charWanted));
		Matcher m;
		for (int row = 0; row < THEBOARD.length-1 && row != rowToAvoid; row++) {
			m = p.matcher(THEBOARD[row]);
			while (m.find() && occurencesWanted > 0) {
				occurencesWanted -= 1;
			}
		}
		return occurencesWanted == 0;
	}
	
	// AdjustScore
	// While there is a 2x2 square on the board where all 4 tiles have the same color:
		// Let S be the topmost of such squares.
		// If there are several topmost squares, then let S be the leftmost among them.
		// Increase score by 1.
		// Replace the top left tile in S with the leftmost tile taken from the buffer.
		// Replace the top right tile in S with the leftmost tile taken from the buffer.
		// Replace the bottom left tile in S with the leftmost tile taken from the buffer.
		// Replace the bottom right tile in S with the leftmost tile taken from the buffer.
		
	
	// takes row, col such that THEBOARD[row][col] of left of tile switched (after switching)
	// returns top left tile indeces of (<= 2) matches
	public int[] adjustScore(int row, int col) { 
		Boolean A1HasMatch = false;
		Boolean A2HasMatch = false;
		
		
		// THEBOARD[matches[2*i]][matches[2*i + 1]] is top left tile of 2x2 match
		// for each match i (0 based)
		int[] matches = {-1, -1, -1, -1};
		
		// check THEBOARD[row][col] left edge exists
		if (col > 0) {
			// if THEBOARD[row][col] left edge matches
			if (ifTileMatches(row, col, row, col-1)) {
				// check THEBOARD[row][col] at top edge
				if (row > 0) {
					if (ifTileMatches(row, col, row-1, col)) {
						// check if top left diag matches
						if (ifTileMatches(row, col, row-1, col-1)) {
							// store 2x2 match
							matches[0] = row-1;
							matches[1] = col-1;
							// indicate match for A1 to skip checking for bottom match
							A1HasMatch = true;
						}
					}
				}
				// first check if top of a1 didn't match
				if (!A1HasMatch) {	
					// else check THEBOARD[row][col] at bottom edge
					if (row < (THEBOARD.length - 1)) {
						if (ifTileMatches(row, col, row+1, col)) {
							// check if bottom left diag matches
							if (ifTileMatches(row, col, row+1, col-1)) {
								// store 2x2 match
								matches[0] = row;
								matches[1] = col-1;
								// indicate match for A1 just cuz
								A1HasMatch = true;
							}				
						}
					}
				}
			}
			//
			// else no match for A1
			//
		}
		
		// check THEBOARD[row][col+1] right edge exists
		if (col < (THEBOARD.length - 2)) {
			// check THEBOARD[row][col+1] right edge matches
			if (ifTileMatches(row, col+1, row-1, col+1)) {
				// check THEBOARD[row][col+1] top edge matches
				if (row > 0) {
					if (ifTileMatches(row, col+1, row-1, col+1)) {
						// check if top right diag matches
						if (ifTileMatches(row, col+1, row-1, col-2)) {
							// store 2x2 match
							matches[0] = row-1;
							matches[1] = col+1;
							// indicate match for A1 to skip checking for bottom match
							A2HasMatch = true;
						}
					}
				}
		
				// first check if top A2 didn't match
				if (!A2HasMatch) {	
					// else check THEBOARD[row][col+1] at bottom edge
					if (row < (THEBOARD.length - 1)) {
						if (ifTileMatches(row, col+1, row+1, col+1)) {
							// check if bottom right diag matches
							if (ifTileMatches(row, col+1, row+1, col+2)) {
								// store 2x2 match
								matches[0] = row;
								matches[1] = col+1;
								// indicate match for A2 just cuz
								A2HasMatch = true;
							}				
						}
					}
				}
			}
			//
			// else no match for A2
			//
		}
		
		return matches;
	}
	
	// sniff tiles around 1 tile
	// returns [idx1, idx2] the index of top left tile of match
	public int[] sniffTilesAround(int row, int col) {
		/**
		 * row-1, col-1		--		row-1, col+1
		 * |								   |
		 * |	--		 row, col		--     |
		 * |		 						   |
		 * row+1, col-1		--		row+1, col+1
		 */
		int[] incr = {-1, 1};
		for (int i: incr) { 		// increment row
			for (int j : incr) {	// increment col
				// check match if neighbours are in range
				if ((0 <= (row + i) && (row + i) < THEBOARD.length) && (0 <= (col + j) && (col + j) < THEBOARD.length)) {
					if (ifTileMatches(row, col, row+i, col+j)){
						if (ifTileMatches(row, col, row, col+j) && ifTileMatches(row, col, row+i, col)) {
							return new int[] {Math.min(row, row+i), Math.min(col, col+j)};
						}
					}
					//else {
					//	break; // move on to next diag neighbour
					//}
				}
			}
		}
		return new int[] {-1, -1};
	}
	
	// checks entire THEBOARD for 2x2 match, using REGEX
	public int[] adjustScoreBrute() {
		Pattern p = Pattern.compile("(([a-zA-Z])\\2)");
		Matcher m;
		List<Integer> prevRowMatches = new ArrayList<Integer>();		
		List<Integer> currRowMatches = new ArrayList<Integer>();

		// stores top left idx of a 2x2 match, 
		// for each match i (0 based) stored at row: boardMatches[i], col: boardMatches[i+1]
		int[] boardMatches = new int[(THEBOARD.length/2)*(THEBOARD.length)];		
		Arrays.fill(boardMatches, -1);

		int j = 0, k = 0;
		for (int i = 0; i < THEBOARD.length; i++) {
			m = p.matcher(THEBOARD[i]);
			if (j == 0) {
				while (m.find()) {
					prevRowMatches.add(m.start());
				}
			}
			else {
				while (m.find()) {
					if (prevRowMatches.contains(m.start())) {
						boardMatches[k] = j-1;
						boardMatches[k+1] = m.start();
						k += 2;
					}
					else {
						currRowMatches.add(m.start());
					}
				}
			}
			if (j > 0) {
				prevRowMatches = new ArrayList<Integer>(currRowMatches);
				currRowMatches = new ArrayList<Integer>();
			}
			j += 1; 
		} 
		return boardMatches;
	}
	
	
	// THEBOARD[row][col] top left of tiles to be replaced
	public void replaceTiles(int row, int col) {
		StringBuilder str = new StringBuilder(THEBOARD[row]);
		str.replace(col, col+1, String.valueOf(COLORSCHAR[THEBUFFER[0]]));
		str.replace(col+1, col+2, String.valueOf(COLORSCHAR[THEBUFFER[1]]));
		THEBOARD[row] = str.toString();
		
		str = new StringBuilder(THEBOARD[row+1]);
		str.replace(col, col+1, String.valueOf(COLORSCHAR[THEBUFFER[2]]));
		str.replace(col+1, col+2, String.valueOf(COLORSCHAR[THEBUFFER[3]]));
		THEBOARD[row+1] = str.toString();
		
		this.THEBUFFER = genBuffer(this.STARTSEED, 4);
	}

	public void increaseScore() {
		SCORE += 1;
	}	
	

	public int[] genBuffer (int startSeed, int boardLen) {
		int buffSize = boardLen;
		int i = 1;
		int[] buff = new int[buffSize];
		buff[0] = startSeed;
		Random r = new Random();
		while (i < buffSize) {
			buff[i] = (buff[i-1] * r.nextInt(48271) % COLORS);
			i += 1;	
		}
		//System.out.println("buffer is " + Arrays.toString(buff));
		return buff;	
	}
	

	// returns idx on the THEBUFFER of valid tile
	// need to call THEBUFFER[idx] = -1; every execution
	public int getBuffTile(int idx) {
		if (THEBUFFER[idx] >= 0) {
			if (idx >= 1) {
				// if left invalid and current valid
				if (THEBUFFER[idx - 1] < 0)  {
					return idx;
				}
				// else move index to the left and recurse
				return getBuffTile(idx - 1);
			}
				// current is the 0th tile and is valid buffer tile
				return idx;
		} 
		// elif current < 0; move idx to the right
		if (idx == (THEBUFFER.length - 1)) {
			// buffer has ran out of tiles
			THEBUFFER = genBuffer(STARTSEED, THEBOARD.length);
			return 0;
		}
		else {
			// else move index to the right and recurse
			return getBuffTile(idx + 1);
		}
	}

	// check if THEBOARD[row][col] color == THEBOARD[compareToRow][compareToCol] color
	public boolean ifTileMatches(int row, int col, int compareToRow, int compareToCol){
		return (THEBOARD[row].charAt(col) == THEBOARD[compareToRow].charAt(compareToCol));
	}
	public void setTheBoard(String[] board) {
		THEBOARD = board;
	}
}
