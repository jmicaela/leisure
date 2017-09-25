import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

public class SmartPlayer {
	public String[] THEBOARD;
	public int COLORS;
	public char[] COLORSCHAR;

	public int[] THEMOVES;
	public SquareRemover SQRRMV;
	
	/*Class assumes the board has no match*/
	
	public static void main(String[] args) {
		
		
		String[] woMatches0 = {"RGBY", "BRYY", "RGRB", "RRGG"};
		String[] withMatches1 = {"RRGG", "RRBY", "YBBG", "GBBR"};
		String[] withMatches2 = {"YBYG", "GYYR", "RYYG", "BYYB"};
		String[] withMatches3 = {"RRRR", "RRRR", "RRRR", "RRRR"};
		
		//SmartPlayer s = new SmartPlayer(woMatches0, 4, "RGBY".toCharArray());
		//s.THEBUFFER = woMatches0;
		//s.STARTSEED = 1;
	}
	
//	public SmartPlayer(String[] theBoard, int colors, char[] colorsChar, SquareRemover squareRemover) {
	public SmartPlayer(SquareRemover squareRemover) {
		this.THEBOARD = squareRemover.THEBOARD;
		this.COLORS = squareRemover.COLORS;
		this.COLORSCHAR = squareRemover.COLORSCHAR;
		this.SQRRMV = squareRemover;
	}
	
	/* Returns pair of indices of tile placements on THEBOARD
	 * representing that tile's moves
	 * */
	int CHARWANTEDOCCURENCES = 0;
	char CHARWANTED = '\u0000';
	
	public int[] getMoves() {
		boolean isVertical = false;
		int[] match = existsMatch(); 	// returns left tile of horizontal match
										// returns top tile of vertical match
		 								// {[3/4], [2/4]}
		
		
		
		// search vertical match
		if (match[0] == -1 && match[2] == -1) {
			SQRRMV.rotateBoardCCwise();
			match = existsMatch();
			if (match[0] == -1 && match[2] == -1) {
				SQRRMV.rotateBoardCwise();
				return new int[] {-1, -1, -1, -1};
			}
			isVertical = true;
		}
		
		CHARWANTED = THEBOARD[(match[0] == -1) ? match[2] : match[0]].charAt((match[0] == -1) ? match[3] : match[1]);		
		
		// decide which index to drag occurance to
		int[] dragOccurencesTo = getMissingTilesOfMatch(match); 	
					// {[1st loc],[2nd loc]}
					// {[1st loc], -1, -1} if a 3/4 match				
				
		// rotate back THEBOARD if match is vertical
		if (isVertical) {
			SQRRMV.rotateBoardCwise();
			
			// rotate match indices
			isVertical = true;
			match = rotateTileIndices(match);
			dragOccurencesTo = rotateTileIndices(dragOccurencesTo);					
		}

		CHARWANTEDOCCURENCES = (match[0] == -1) ? 2 : 1;
		
		for (int i=0; i < CHARWANTEDOCCURENCES; i++) {
			
			// find occurences of CHARWANTED to be dragged towards dragOccurencesTo
			int[] occurance = findOccurance(CHARWANTED);
			
			// calculate path of swaps
			int[] swaps = collectPathSwaps(match, new int[] {dragOccurencesTo[2*i], dragOccurencesTo[2*i + 1]}, occurance);
			
			// collect swap indices for path
			
		}
		
		return new int[] {-1, -1};
	}
	
	// assumes rotation from CCwise to Cwise
	public int[] rotateTileIndices(int[] tiles) {
		int tilesNum = tiles.length/2;
		for (int i = 0; i < tilesNum; i++) {
			if (tiles[2*i] != -1) {
				int j = tiles[2*i];
				tiles[2*i] = tiles[(2*i) + 1];
				tiles[2*i + 1] = THEBOARD.length - 1 - j;
			}
		}
		return tiles;
	}
	
	public int[] existsMatch() {
		int[] ret = new int[] {-1, -1, -1, -1}; // {[3/4], [2/4]}
		Pattern p = Pattern.compile("(([a-zA-Z])\\2)");
		Matcher m;
		for (int row = 0; row < THEBOARD.length-1; row++) {
			m = p.matcher(THEBOARD[row]);
			if (m.find()) {
				
				int[] incr = {-1, 1};
				int col = m.start();
				char wantedChar = THEBOARD[row].charAt(col);
				
				for (int j : incr) {
					if (0 <= (row+j) && (row+j) < THEBOARD.length) {
						
						// if 3/4 match, check if there's a 4th tile somewhere else in THEBOARD
						if (THEBOARD[row].charAt(col) == THEBOARD[row+j].charAt(col)) {
							if (charExists(row, 1, wantedChar)){
								return new int[] {row+j, col, -1, -1};
							}
							continue;
						}
						else if (THEBOARD[row].charAt(col) == THEBOARD[row+j].charAt(col+1)) {
							if (charExists(row, 1, wantedChar)){
								return new int[] {row+j, col+1, -1, -1};
							}
							continue;
						}
						
						// else check if valid 2/4 match
						if (charExists(row, 2, wantedChar)){
							return new int[] {-1, -1, row+j, col+1};
						}
					}
					
				}
			}
		}
		return ret;
	}
	
	// assumes match is a horizontal match on THEBOARD
	// returns indices of unmatched tiles
	public int[] getMissingTilesOfMatch(int[] match) {
		int rowToAvoid = match[0] == -1 ? match[2] : match[0];
		int col = match[0] == -1 ? match[3] : match[1];
		
		// look for 1 slot if match is a 3/4
		if (match[2] == -1) {
			if (0 < rowToAvoid) {
				if (THEBOARD[rowToAvoid-1].charAt(col) == THEBOARD[rowToAvoid].charAt(col)) {
					return new int[] {rowToAvoid-1, col+1, -1, -1};
				} else if (THEBOARD[rowToAvoid-1].charAt(col+1) == THEBOARD[rowToAvoid].charAt(col)) {
					return new int[] {rowToAvoid-1, col, -1, -1};
				}
			}
		
			// else check THEBOARD[rowToAvoid+1]
			if (THEBOARD[rowToAvoid+1].charAt(col) == THEBOARD[rowToAvoid].charAt(col)) {
				return new int[] {rowToAvoid+1, col+1, -1, -1};
			} else if (THEBOARD[rowToAvoid+1].charAt(col+1) == THEBOARD[rowToAvoid].charAt(col)) {
				return new int[] {rowToAvoid+1, col, -1, -1};
			}
			
			return new int[] {-1, -1, -1, -1,}; // code should never reach this line
		}
		
		// else return a 2 slot if match is 2/4
		if (0 < rowToAvoid) {
			return new int[] {rowToAvoid-1, col, rowToAvoid-1, col+1};
		}		
		
		return new int[] {rowToAvoid+1, col, rowToAvoid+1, col+1};
	}
	
	
	
	
	
	public int[] collectPathSwaps(int[] match, int[] occuranceToIdx, int[] occuranceFromIdx) {
		List<Integer> moves = new ArrayList<Integer>();
		
		// if occuranceToIdx, occuranceFromIdx not aligned by row/col
		/*int axis = -1;
		if (occuranceToIdx[0] == occuranceFromIdx[0]) {
			axis = 0;
		} else if (occuranceToIdx[1] == occuranceFromIdx[1]) {
			axis = 1;
		}
		
		int skipAxis = 0;	// 0 => shift along row
							// 1 => shift along col
		
		// store moves to align row/col of occuranceToIdx occuranceFromIdx
		int ctr = 1;
		if (axis == -1) {	
			skipAxis = (Math.abs(occuranceToIdx[0] - occuranceFromIdx[0]) < Math.abs(occuranceToIdx[1] - occuranceFromIdx[1])) ? 0 : 1;
			int lastLoc = occuranceFromIdx[skipAxis];
			if (skipAxis == 0) {
				while (lastLoc != occuranceToIdx[skipAxis]) {
					moves.add(occuranceFromIdx[skipAxis] + ctr - 1);
					moves.add(occuranceFromIdx[1]);
					moves.add(occuranceFromIdx[skipAxis] + ctr);
					moves.add(occuranceFromIdx[1]);
					ctr += 1;
				}
				occuranceFromIdx[1] = occuranceFromIdx[1] + ctr - 1;
			}
			else {
				while (lastLoc != occuranceToIdx[skipAxis]) {
					moves.add(occuranceFromIdx[0]);
					moves.add(occuranceFromIdx[skipAxis] + ctr - 1);
					moves.add(occuranceFromIdx[0]);
					moves.add(occuranceFromIdx[skipAxis] + ctr);
					ctr += 1;
				}
				occuranceFromIdx[0] = occuranceFromIdx[0] + ctr - 1;
			}			
		}*/
		
		int[] someMoves = dragTile(occuranceFromIdx, occuranceToIdx);

		
		int matchInBetween = -1; 	// match in between is {match[2*matchInBetween], match[2*matchInBetween + 1]}
									// match in between is either of tiles in matches[]
		// check if has matching tile in between
		for (int i=0; i<2; i++) {
			if (match[(2*i) + axis] == occuranceToIdx[axis]) {
				int j = Math.floorMod(i-1, 2);
				if ((occuranceToIdx[j] < match[(2*i)+j] && match[(2*i)+j] < occuranceFromIdx[j]) || 
						(occuranceToIdx[j] > match[(2*i)+j] && match[(2*i)+j] > occuranceFromIdx[j])) {
					matchInBetween = i;
				}
			}
		}
		
		
		// if applicable, move matchInBetween towards occuranceToIdx
		if (matchInBetween != -1) {
			moves.add(match[2*matchInBetween]);
			moves.add(match[2*matchInBetween+1]);
			moves.add(occuranceToIdx[0]);
			moves.add(occuranceToIdx[1]);
		}
		
		
		// now move occuranceFromIdx towards {match[2*matchInBetween], match[2*matchInBetween + 1]}
		skipAxis = Math.floorMod(skipAxis-1, 2);
		int lastLoc = occuranceFromIdx[skipAxis];
		ctr = 1;
		if (skipAxis == 0) {
			while (lastLoc != match[2*matchInBetween + skipAxis]) {
				moves.add(occuranceFromIdx[skipAxis] + ctr - 1);
				moves.add(occuranceFromIdx[1]);
				moves.add(occuranceFromIdx[skipAxis] + ctr);
				moves.add(occuranceFromIdx[1]);
				ctr += 1;
			}
			occuranceFromIdx[1] = occuranceFromIdx[1] + ctr - 1;
		}
		else {
			while (lastLoc != match[2*matchInBetween + skipAxis]) {
				moves.add(occuranceFromIdx[0]);
				moves.add(occuranceFromIdx[skipAxis] + ctr - 1);
				moves.add(occuranceFromIdx[0]);
				moves.add(occuranceFromIdx[skipAxis] + ctr);
				ctr += 1;
			}
			occuranceFromIdx[0] = occuranceFromIdx[0] + ctr - 1;
		}			

		return ArrayUtils.toPrimitive(moves.toArray(new Integer[moves.size()]));
	}
	
	// return moves to drag occuranceFromIdx to occuranceToIdx
	// assumes both tiles are aligned by either row/ col but doesn't know which
	// notice this method doesnt' requrie THEBOARD
	int[] dragTile(int[] occuranceFromIdx, int[] occuranceToIdx) {
		List<Integer> moves = new ArrayList<Integer>();
		
		int dragAxis = (occuranceToIdx[0] == occuranceFromIdx[0]) ? 0 : 1;
		
		int notDragAxis = Math.floorMod(dragAxis-1, 2);
		int direction = (occuranceFromIdx[notDragAxis] - occuranceToIdx[notDragAxis]) > 0 ? -1 : 1;
		int ctr = 1*direction;
		
		if (dragAxis == 1) {
			while (occuranceToIdx[notDragAxis] != (occuranceFromIdx[notDragAxis] + ctr - direction)) {
				moves.add(occuranceFromIdx[notDragAxis] + ctr - direction);
				moves.add(occuranceFromIdx[dragAxis]);
				moves.add(occuranceFromIdx[notDragAxis] + ctr);
				moves.add(occuranceFromIdx[dragAxis]);
				ctr += direction;
			}
		} 
		else {
			while (occuranceToIdx[notDragAxis] != (occuranceFromIdx[notDragAxis] + ctr - direction)) {
				moves.add(occuranceFromIdx[dragAxis]);
				moves.add(occuranceFromIdx[notDragAxis] + ctr - direction);
				moves.add(occuranceFromIdx[dragAxis]);
				moves.add(occuranceFromIdx[notDragAxis] + ctr);
				ctr += direction;
			}
		}
		

		//System.out.println(moves);
		
		return ArrayUtils.toPrimitive(moves.toArray(new Integer[moves.size()]));
	}
	
	
	/* returns index of charWanted that's not in row rowToAvoid
	 * */
	public int[] findOccurance(char charWanted) {
		Pattern p = Pattern.compile(String.valueOf(charWanted));
		Matcher m;
		for (int row = 0; row < THEBOARD.length-1; row++) {
			m = p.matcher(THEBOARD[row]);
			if (m.find()) {
				return new int[] {row, m.start()};
			}
		}
		return new int[] {-1, -1};
	}
	
	
	/* checks whether there are occurencesWanted occurences
	 * of charWanted not on the row rowToAvoid
	 * */
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
}
