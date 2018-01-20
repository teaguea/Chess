package chess.engine.board;

import java.util.HashMap;
import java.util.Map;

import chess.engine.pieces.Piece;

import com.google.common.collect.ImmutableMap;

/****************************************************************************
 * Defines an abstract Chess Tile
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 ****************************************************************************/
public abstract class Tile {
	
	//Represents the position the tile's position on the board
	protected final int tileCoordinate;
	//Maps an Empty Tile to every coordinate on the board to create a cache of empty tiles
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
	
	/***************************************************
	 * Constructor that creates a tile object based on
	 * the coordinate passed in.
	 * @param tileCoordinate
	 ***************************************************/
	private Tile(final int tileCoordinate){
		//Assigns the tile
		this.tileCoordinate = tileCoordinate; 
	}
	
	/***********************************************************************************************
	 * Factory method for creating tiles. Checks if the tile coordinate is null. If it is not null
	 * then an OccupiedTile will be created. If it is not null then a empty tile object is returned
	 * from the EMPTY_TILE cache
	 * @param tileCoordinate
	 * @param piece
	 * @return New Occupied Tile or a Empty Tile from the Cache
	 *************************************************************************************************/
	public static Tile createTile(final int tileCoordinate, final Piece piece){
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}
	
	/*****************************************************************
	 * Creates 64 Empty Tile objects maps them to all coordinates on
	 * the board in a Hash Map called emptyTileMap and returns it.
	 * @return HashMap emptyTileMap 
	 *****************************************************************/
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		
		for(int i = 0; i < 64; i++)
			emptyTileMap.put(i, new EmptyTile(i));
		
		//returns an immutable copy of emptyTileMap 
		return ImmutableMap.copyOf(emptyTileMap);
	}

	/**********************************
	 * Tests if the Tile is occupied
	 * @return
	 **********************************/
	public abstract boolean isTileOccupied();
	
	/**************************************
	 * Will return the piece on the Tile
	 * @return
	 **************************************/
	public abstract Piece getPiece();
	
	/*************************************
	 * Returns the coordinate of the Tile
	 * @return int tileCoordinate
	 **************************************/
	public int getTileCoordinate(){
		return this.tileCoordinate;
	}
	
//<--------------------------------------------------------------------------------------------------------------------------------------------------------->	
	/***************************************************
	 * Inner class that defines an empty title object
	 * @author Aaron Teague
	 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
	 * and chessprogramming.wikispaces.com
	 ***************************************************/
	public static final class EmptyTile extends Tile {
		
		/******************************************************
		 * Creates EmptyTile based on the coordinate passed 
		 * in by calling the constructor from the Tile class
		 * the Tile class 
		 * @param coordinate
		 ******************************************************/
		private EmptyTile(final int coordinate){
			super(coordinate);
		}
		
		/********************************************
		 * Prints - character for debugging purposes
		 ********************************************/
		@Override
		public String toString(){
			return "-";
		}
		
		/*******************************************************
		 * This is an empty Tile therefore it will return false
		 * @return false
		 *******************************************************/
		@Override
		public boolean isTileOccupied() {
			return false;
		}
		
		
		/*****************************************************
		 * This is an empty Tile therefore there is no piece
		 * occupying it therefore it will return null.
		 * @return null
		 *****************************************************/
		@Override
		public Piece getPiece(){
			return null;
		}
		
	}
	
//<-------------------------------------------------------------------------------------------------------------------------------------------------------->	
	/****************************************************
	 * Inner class that defines an Occupied Tile object
	 * @author Aaron Teague
	 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
	 * and chessprogramming.wikispaces.com
	 *****************************************************/
	public static final class OccupiedTile extends Tile {
		
		//Represents the piece on the Tile
		private final Piece pieceOnTile;

		/******************************************************
		 * Creates an occupied tile bases on the coordinate and 
		 * piece passed in.
		 * @param tileCoordinate
		 * @param pieceOnTile
		 ******************************************************/
		private OccupiedTile(int tileCoordinate, final Piece pieceOnTile) {
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}
		
		@Override
		public String toString(){
			return getPiece().getPieceTeam().isBlack() ? getPiece().toString().toLowerCase() :
				getPiece().toString();
		}

		/**************************************************
		 * This is an occupied tile therefore it will
		 * always return true.
		 * @return true
		 **************************************************/
		@Override
		public boolean isTileOccupied() {
			return true;
		}

		/***********************************************
		 * Will return the piece object that is occupying
		 * this tile.
		 * @return Piece pieceOnTile
		 **********************************************/
		@Override
		public Piece getPiece() {
			return this.pieceOnTile;
		}
		
	}
//<---------------------------------------------------------------------------------------------------------------------------------------------------------->
	
}

