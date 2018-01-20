package chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Knight;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Queen;
import chess.engine.pieces.Rook;
import chess.engine.pieces.Team;
import chess.engine.player.BlackPlayer;
import chess.engine.player.Player;
import chess.engine.player.WhitePlayer;
/*********************************************************************
 * The Board class will be able to create a start a new game.
 * Track who's turn it is, the active pieces, and all legal moves on
 * the board.
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 *********************************************************************/
public class Board {
	
	//A List that represents the Tiles on the board
	private final List<Tile> gameBoard;
	//Keeps track of the White Pieces on the board
	private final Collection<Piece> whitePieces;
	//Keeps track of the Black Pieces on the board
    private final Collection<Piece> blackPieces;
    
    //Represents the White Player
    private final WhitePlayer whitePlayer;
    //Represents the Black Player
    private final BlackPlayer blackPlayer;
    //Represents the Current Player
    private final Player currentPlayer;
    
    
    private final Pawn enPassantPawn;
    
	
	/*************************************
	 * The Board constructor.
	 * @param builder
	 *************************************/
	private Board(final Builder builder){
		//Creates the actual board
		this.gameBoard = createGameBoard(builder);
		//Finds all active white pieces
		this.whitePieces = calculateAcitvePieces(this.gameBoard, Team.WHITE);
		//Finds all active black pieces
		this.blackPieces = calculateAcitvePieces(this.gameBoard, Team.BLACK);
		this.enPassantPawn = builder.enPassantPawn;
		
		//Represent all possible move the white team can perform
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		//Represent all possible move the black team can perform
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
		
		//The black standard moves are needed to calculate the castle moves
		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		//The white standard moves are need to calculate the castle moves
		this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		//Sets the player who can make a move
		currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}
	
	/*****************************************
	 * To String method for debugging purposes
	 *****************************************/
	@Override
	public String toString(){
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i < BoardUtils.NUM_TILES; i++){
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			
			if((i + 1) % BoardUtils.NUM_TILES_PER_RANK == 0) 
				builder.append("\n");
			
			
		}
		
		return builder.toString();
	}
	
	/**************************************************
	 * Returns the Pawn that be taken by En Passant
	 * @return
	 **************************************************/
	public Pawn getEnPassantPawn(){
		return this.enPassantPawn;
	}
	
	/******************************************
	 * Returns the current player of the game
	 * @return Player currentPlayer
	 ******************************************/
	public Player currentPlayer(){
		return this.currentPlayer;
	}
	
	/*****************************************
	 * Returns all Black Pieces
	 * @return blackPieces
	 *****************************************/
	public Collection<Piece> getBlackPieces(){
		return this.blackPieces;
	}
	
	/*******************************************
	 * Returns all White Pieces
	 * @return whitePieces
	 *******************************************/
	public Collection<Piece> getWhitePieces(){
		return this.whitePieces;
	}
	
	
	/*******************************
	 * Returns the black player
	 * @return Player blackPlayer
	 *******************************/
	public Player getBlackPlayer(){
		return this.blackPlayer;
	}
	
	/*******************************
	 * Returns the white player
	 * @return Player whitePlayer
	 *******************************/
	public Player getWhitePlayer(){
		return this.whitePlayer;
	}
	
	 public List<Tile> getGameBoard() {
	        return this.gameBoard;
	}

	
	/****************************************************************************
	 * Calculates all the legal moves for one Team.
	 * @param pieces
	 * @return List<move> legalMoves
	 ****************************************************************************/
	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		
		//A list that holds all of the legal in moves for specific team
		final List<Move> legalMoves = new ArrayList<>(35);
		
		//Loops through all the pieces on one side
		for(final Piece piece : pieces){
			//Calculates the legal moves and adds them to the list
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		
		return ImmutableList.copyOf(legalMoves);
	}

	/*************************************************************************
	 * Loops through the gameBoard to find all active pieces that are on the
	 * same team as the team passed in
	 * @param gameBoard
	 * @param team
	 * @return List<Piece> activePieces
	 *************************************************************************/
	private static Collection<Piece> calculateAcitvePieces(final List<Tile> gameBoard,
			final Team team) {
		
		final List<Piece> activePieces = new ArrayList<>();
		
		//Loops through all of the tile to find active pieces
		for(final Tile tile : gameBoard){
			if(tile.isTileOccupied()){
				final Piece piece = tile.getPiece();
				
				//checks if the piece is on the right team
				if(piece.getPieceTeam() == team)
					activePieces.add(piece);
				
			}
			
		}
		
		return ImmutableList.copyOf(activePieces);
	}
	
	
	public Iterable<Piece> getAllPieces() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePieces, this.blackPieces));
	}
	

	/*****************************************************
	 * Creates the actual game board
	 * @param builder
	 * @return Tile[] tiles
	 *****************************************************/
	private List<Tile> createGameBoard(final Builder builder) {
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		
		//Loop through the boardConfig to create the Tiles needed for the Board
		for(int i = 0; i < BoardUtils.NUM_TILES; i++){
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		
		return ImmutableList.copyOf(tiles);
	}

	/**************************************************
	 * Returns the tile associated with the coordinate 
	 * passed in.
	 * @param tileCoordinate
	 * @return A the tile on coordinate tileCoordinate
	 **************************************************/
	public Tile getTile(int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}
	
	/*******************************************
	 * Creates the initial Standard Position.
	 * @return
	 *******************************************/
	public static Board createStandardBoard(){
		final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(Team.BLACK, 0));
        builder.setPiece(new Knight(Team.BLACK, 1));
        builder.setPiece(new Bishop(Team.BLACK, 2));
        builder.setPiece(new Queen(Team.BLACK, 3));
        builder.setPiece(new King(Team.BLACK, 4, true, true));
        builder.setPiece(new Bishop(Team.BLACK, 5));
        builder.setPiece(new Knight(Team.BLACK, 6));
        builder.setPiece(new Rook(Team.BLACK, 7));
        builder.setPiece(new Pawn(Team.BLACK, 8));
        builder.setPiece(new Pawn(Team.BLACK, 9));
        builder.setPiece(new Pawn(Team.BLACK, 10));
        builder.setPiece(new Pawn(Team.BLACK, 11));
        builder.setPiece(new Pawn(Team.BLACK, 12));
        builder.setPiece(new Pawn(Team.BLACK, 13));
        builder.setPiece(new Pawn(Team.BLACK, 14));
        builder.setPiece(new Pawn(Team.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(Team.WHITE, 48));
        builder.setPiece(new Pawn(Team.WHITE, 49));
        builder.setPiece(new Pawn(Team.WHITE, 50));
        builder.setPiece(new Pawn(Team.WHITE, 51));
        builder.setPiece(new Pawn(Team.WHITE, 52));
        builder.setPiece(new Pawn(Team.WHITE, 53));
        builder.setPiece(new Pawn(Team.WHITE, 54));
        builder.setPiece(new Pawn(Team.WHITE, 55));
        builder.setPiece(new Rook(Team.WHITE, 56));
        builder.setPiece(new Knight(Team.WHITE, 57));
        builder.setPiece(new Bishop(Team.WHITE, 58));
        builder.setPiece(new Queen(Team.WHITE, 59));
        builder.setPiece(new King(Team.WHITE, 60, true, true));
        builder.setPiece(new Bishop(Team.WHITE, 61));
        builder.setPiece(new Knight(Team.WHITE, 62));
        builder.setPiece(new Rook(Team.WHITE, 63));
        //white to move
        builder.setMoveMaker(Team.WHITE);
        //build the board
        return builder.build();
		
	}
	
	/**********************************************************************
	 * This uses Guava's concat function to combine the white legal moves
	 * and black legal moves and returns the result
	 * @return Iterable<Move>
	 **********************************************************************/
	public Iterable<Move> getAllLegalMoves() {
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),
				this.blackPlayer.getLegalMoves()));
	}
	
	
//<-------------------------------------------------------------------------------------------------------------------------------------------------------->	
	/**************************************************************
	 * Inner Class that will help in the construction of the Board
	 * @author Aaron Teague
	 * I was help by the sites www.stackoverflow.com, www.stackexchange.com,
	 * and chessprogramming.wikispaces.com
	 **************************************************************/
	public static class Builder{
		
		//Represents the configuration of the Pieces on the Board
		Map<Integer, Piece> boardConfig;
		
		//Represents the person is moving a piece on this Board.
		Team nextMoveMaker;

		//Represent a pawn that has executed a Pawn Jump and can be captured by enPassant
		Pawn enPassantPawn;
		
		/********************************
		* The Builder Constructor
		*********************************/
		public Builder(){
			this.boardConfig = new HashMap<>();
		}
		
		/************************************************************
		 * Set the Piece in the board's configuration
		 * @param piece
		 * @return Build object with the altered Board Configuration
		 ************************************************************/
		public Builder setPiece(final Piece piece){
			//Puts the piece and it's position into the the Board's configuration
			this.boardConfig.put(piece.getPiecePosition(), piece);
			//returns the builder object
			return this;
			
		}
		
		/******************************************************
		 * Sets who is able to move a piece on the board
		 * @param nextMoveMaker
		 * @return Build object with a new move maker
		 ******************************************************/
		public Builder setMoveMaker(final Team nextMoveMaker){
			//Sets who is able to move a piece
			this.nextMoveMaker = nextMoveMaker;
			return this;
			
		}
		/*****************************************************
		 * Sets the Pawn that be taken by the enPassant move 
		 * @param enPassantPawn
		 *****************************************************/
		public void setEnPassantPawn( Pawn enPassantPawn){
			this.enPassantPawn = enPassantPawn; 
		}
		
		/************************
		 * Creates the Board
		 * @return Board
		 ************************/
		public Board build(){
			return new Board(this);
		}
		
	}
//<-------------------------------------------------------------------------------------------------------------------------------------------------------->



}
