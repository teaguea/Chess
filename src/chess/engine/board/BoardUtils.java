package chess.engine.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/***********************************************************************
 * This class provide general Utilities that aren't specific to one area
 * of the board.
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 ***********************************************************************/
public class BoardUtils {
	
	
	
	//Array of boolean values to represent position in the first column
	public static final boolean[] FILE_A = initFile(0);
	//Array of boolean values to represent position in the second column
	public static final boolean[] FILE_B = initFile(1);
	//Array of boolean values to represent position in the seventh column
	public static final boolean[] FILE_G = initFile(6);
	//Array of boolean values to represent position in the eighth column
	public static final boolean[] FILE_H = initFile(7);
	
	
	//Array of boolean values to represent position in the first row
	public static final boolean[] EIGHTH_RANK = initRank(0);
	//Array of boolean values to represent position in the second row
	public static final boolean[] SEVENTH_RANK = initRank(8);
	//Array of boolean values to represent position in the third row
	public static final boolean[] SIXTH_RANK = initRank(16);
	//Array of boolean values to represent position in the fourth row
	public static final boolean[] FIFTH_RANK = initRank(24);
	//Array of boolean values to represent position in the fifth row
	public static final boolean[] FORTH_RANK = initRank(32);
	//Array of boolean values to represent position in the sixth row
	public static final boolean[] THIRD_RANK = initRank(40);
	//Array of boolean values to represent position in the seventh row
	public static final boolean[] SECOND_RANK = initRank(48);
	//Array of boolean values to represent position in the eight row
	public static final boolean[] FIRST_RANK = initRank(56);
	
	public final static List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
	public final static Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
	
	
	//Constant integer of number of tiles on the board
	public static final int NUM_TILES = 64;
	//Constant integer of number of tiles on the board per Row
	public static final int NUM_TILES_PER_RANK = 8;
	//Constant integer of the starting tile
	public static final int START_TILE_INDEX = 0;

	/********************************************************************
	 * The Constructor will throw an exception if an object is created
	 * because just provides Utilities.
	 ********************************************************************/
	private BoardUtils(){
		throw new RuntimeException("This class can't be instantiated!");
	}

	/**********************************************************
	 * Creates a boolean array and initializes values to true
	 * at the row number passed in and sets the rest to
	 * false. 
	 * @param int rowNum
	 * @return boolean[] row
	 **********************************************************/
	private static boolean[] initRank(int rankNum) {
		final boolean[] row = new boolean[NUM_TILES];
		do {
			
			row[rankNum] = true;
			rankNum++;
			
		} while(rankNum % NUM_TILES_PER_RANK != 0);
		return row;
	}

	
	/*********************************************************
	 * Initializes the boolean array with the correct values
	 * @param int columnNumber
	 * @return boolean[] column
	 ********************************************************/
	private static boolean[] initFile(int fileNumber) {
		final boolean[] column = new boolean[NUM_TILES];
		
			for (int i = 0; i < NUM_TILES_PER_RANK; i++){
				column[i*NUM_TILES_PER_RANK+fileNumber] = true;
			}
		
		 return column;
	}

	/*************************************************************************************
	 * This method will test to see if the destination coordinate is valid.
	 * @param candidateDestinationCoordinate
	 * @return boolean value
	 *************************************************************************************/
	public static boolean isValidTileCoordinate(final int coordinate) {
		return coordinate >= 0 && coordinate < NUM_TILES;
	}

	/*****************************************************************
	 * Gets the integer position from the PGN string
	 * @param position
	 * @return POSITION_TO_COORDINATE.get(position)
	 ****************************************************************/
	public static int getCoordinateAtPosition(final String position) {
		return POSITION_TO_COORDINATE.get(position);
	}
	
	/*************************************************
	 * Returns the PGN notation of the move
	 * @param int coordinate
	 * @return ALGEBREIC_NOTATION[coordinate]
	 *************************************************/
	public static String getPositionAtCoordinate(final int coordinate){
		return ALGEBRAIC_NOTATION.get(coordinate);
	}
	
	/************************************************************
	 * Initialize all the algebraic notation into a list
	 * @return List<String>
	 ************************************************************/
	private static List<String> initializeAlgebraicNotation() {
        return ImmutableList.copyOf(new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        });
	}
	
	/**************************************************************************
	 * Maps an integer to each algebraic notation
	 * @return Map<String, Integer> positionToCoordinate
	 **************************************************************************/
	private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
	}
	
	

}
