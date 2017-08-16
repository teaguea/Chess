package package1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
/************************************************************
 * The Board class represents the actual playing space that the chess
 * game is played on. The board is capable of keeping track of the White
 * Piece player, Black Piece player, and the player is able to move their 
 * piece. The Board class also calculates the pieces that are still in play
 * and calculates what moves are legal. 
 * Author: Aaron Teague
 * The builder pattern and general design of this class
 * was provided by the web site StackOverflow. And
 * I was provided help from chessprogramming.wikispaces.com/
 ************************************************************/
public class Board {

	private List<Space> playBoard;
	private Collection<Piece> whitePieces;
	private Collection<Piece> blackPieces;
	
	//the player who controls the white pieces
	private WhitePlayer whitePlayer;
	//the player who controls the black pieces
	private BlackPlayer blackPlayer;
	//the player who can move their piece
	private Player currentPlayer;
	//the pawn that can potentially used in an En Passan move
	private Pawn enPassantPawn;
	
	//These lists are used by the pieces to calculate if a certain is legal
	public final static List<Boolean> FIRST_COLUMN = initializeColumn(0);
	public final static List<Boolean> SECOND_COLUMN = initializeColumn(1);
	public final static List<Boolean> SEVENTH_COLUMN = initializeColumn(6);
	public final static List<Boolean> EIGHTH_COLUMN = initializeColumn(7);
	
	public final static List<Boolean> FIRST_ROW = initializeRow(0);
	public final static List<Boolean> SECOND_ROW = initializeRow(8);
	public final static List<Boolean> THIRD_ROW = initializeRow(16);
	public final static List<Boolean> FOURTH_ROW = initializeRow(24);
	public final static List<Boolean> FIFTH_ROW = initializeRow(32);
	public final static List<Boolean> SIXTH_ROW = initializeRow(40);
	public final static List<Boolean> SEVENTH_ROW = initializeRow(48);
	public final static List<Boolean> EIGHTH_ROW = initializeRow(56);
	
	
	/***********************************
	 * Creates the Board object
	 * @param builder
	 ***********************************/
	public Board(Builder builder){
		this.playBoard = createPlayBoard(builder);
		this.whitePieces = calculateAlivePieces(this.playBoard, Team.WHITE);
		this.blackPieces = calculateAlivePieces(this.playBoard, Team.BLACK);
		this.enPassantPawn = builder.enPassanPawn;
		Collection<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
		Collection<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);
		this.whitePlayer = new WhitePlayer(this, whiteLegalMoves,  blackLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteLegalMoves,  blackLegalMoves);
		this.currentPlayer = builder.newPlayer.choosePlayer(this.whitePlayer, this.blackPlayer);
		
	}
	
	/****************************************************
	 * A string method just used for debugging purposes.
	 ****************************************************/
	@Override
	public String toString(){
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i < 64; i++){
			String spaceText = this.playBoard.get(i).toString();
			builder.append(String.format("%3s", spaceText));
			if((i + 1) % 8 == 0){
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	/**************************************************
	 * Gets the player who is currently making a move
	 * @return this.currentPlayer
	 **************************************************/
	public Player getCurrentPlayer(){
		return this.currentPlayer;
		
	}
	
	
	/****************************************
	 * Gets the black pieces on the board
	 * @return the black pieces on the board
	 ****************************************/
	public Collection<Piece> getBlackPieces(){
		return this.blackPieces;
	}
	
	/**************************************
	 * Gets the white pieces on the board
	 * @return whitePieces
	 **************************************/
	public Collection<Piece> getWhitePieces(){
		return this.whitePieces;
	}
	
	/************************************************************************
	 * Calculates a list of all the legal moves of the pieces that are alive 
	 * on the board.
	 * @param pieces
	 * @return a list of all the legal moves of the pieces that are alive\
	 ***********************************************************************/
	private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
		List<Move> legalMoves = new ArrayList<>();
		
		//loops through the pieces on the board
		for(Piece piece : pieces){
			//Uses the piece's respective calculation function to calculate the moves and adds the moves to legalMoves
			legalMoves.addAll(piece.calcLegalMove(this));
		}
		
		return legalMoves;
	}

	/***********************************************************
	 * Finds and saves pieces on the board of a certain team
	 * @param playBoard
	 * @param team
	 * @return alivePieces
	 ***********************************************************/
	private static Collection<Piece> calculateAlivePieces(List<Space> playBoard,
			Team team) {
		List<Piece> alivePieces = new ArrayList<>();
		
		for(Space space: playBoard){
			if(space.isSpaceUsed()){
				Piece piece = space.getPiece();
				if(piece.getTeam() == team){
					alivePieces.add(piece);
				}
			}
		}
		
		return alivePieces;
	}

	/********************************************************************
	 * Returns the piece from the board that is on possibleEndCoordinate 
	 * @param possibleEndCoordinate
	 * @return The piece the is on the end point of the move
	 ********************************************************************/
	public Space getSpace(int possibleEndCoordinate) {
		
		return playBoard.get(possibleEndCoordinate);
	}
	
	/********************************************************************
	 * Creates an array of 64 spaces that will represent the chess board.
	 * Each space will be assigned a piece that is found in boardConfig. 
	 * @param builder
	 * @return Array of Space objects called spaces
	 ********************************************************************/
	private static List<Space> createPlayBoard(Builder builder) {
		List<Space> spaces = new ArrayList<>();
		
		for(int i = 0; i < 64; i++){
			spaces.add(Space.createSpace(i, builder.boardConfig.get(i)));
		}
		
		return spaces;
	}
	
	/********************************************************************
	 * Creates the board with all pieces in standard starting positions
	 * @return
	 ********************************************************************/
	public static Board createStartBoard(){
		Builder builder = new Builder();
		
		//Setting all initial black pieces
		builder.setPiece(new Pawn(15, Team.BLACK));
		builder.setPiece(new Pawn(14, Team.BLACK));
		builder.setPiece(new Pawn(13, Team.BLACK));
		builder.setPiece(new Pawn(12, Team.BLACK));
		builder.setPiece(new Pawn(11, Team.BLACK));
		builder.setPiece(new Pawn(10, Team.BLACK));
		builder.setPiece(new Pawn(9, Team.BLACK));
		builder.setPiece(new Pawn(8, Team.BLACK));
		builder.setPiece(new Rook(7, Team.BLACK));
		builder.setPiece(new Knight(6, Team.BLACK));
		builder.setPiece(new Bishop(5, Team.BLACK));
		builder.setPiece(new King(4, Team.BLACK));
		builder.setPiece(new Queen(3, Team.BLACK));
		builder.setPiece(new Bishop(2, Team.BLACK));
		builder.setPiece(new Knight(1, Team.BLACK));
		builder.setPiece(new Rook(0, Team.BLACK));
		
		//Setting all initial white pieces
		builder.setPiece(new Pawn(48, Team.WHITE));
		builder.setPiece(new Pawn(49, Team.WHITE));
		builder.setPiece(new Pawn(50, Team.WHITE));
		builder.setPiece(new Pawn(51, Team.WHITE));
		builder.setPiece(new Pawn(52, Team.WHITE));
		builder.setPiece(new Pawn(53, Team.WHITE));
		builder.setPiece(new Pawn(54, Team.WHITE));
		builder.setPiece(new Pawn(55, Team.WHITE));
		builder.setPiece(new Rook(56, Team.WHITE));
		builder.setPiece(new Knight(57, Team.WHITE));
		builder.setPiece(new Bishop(58, Team.WHITE));
		builder.setPiece(new Queen(59, Team.WHITE));
		builder.setPiece(new King(60, Team.WHITE));
		builder.setPiece(new Bishop(61, Team.WHITE));
		builder.setPiece(new Knight(62, Team.WHITE));
		builder.setPiece(new Rook(63, Team.WHITE));
		
		builder.setNewPlayer(Team.WHITE);
		
		return builder.build();
		
	}
	
	/*****************************
	 * @return The enPassantPawn.
	 *****************************/
	public Pawn getEnPassantPawn() {
		return this.enPassantPawn;
	}

	/***************************
	 * Returns the white player
	 * @return this.whitePlayer
	 ****************************/
	public Player getWhitePlayer() {
		
		return this.whitePlayer;
	}

	/***************************
	 * Returns the black player
	 * @return this.blackPlayer
	 ****************************/
	public Player getBlackPlayer() {
		
		return this.blackPlayer;
	}
	
	
	/****************************************************************
	 * I was specifically helped here by stackoverflow.com
	 * to use Guava to concatenate the white moves and black moves
	 * @return all the legal moves on the board
	 ****************************************************************/
	public Iterable<Move> getAllLegalMoves() {
		// returns a concatenated list of whitePlayer moves and blackPlayer moves
		return Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves());
	}
	
	
	/************************************************************************
	 * Checks if the coordinate is on the board
	 * @param possibleEndCoordinate
	 * @return true if the move remains on the board, false if it falls off
	 ***********************************************************************/
	public static boolean isValidSpaceCoordinate(int possibleEndCoordinate) {
		
		//checks if possibleEndCoordinate is valid coordinate 
		if(possibleEndCoordinate >= 0 && possibleEndCoordinate < 64){
		         return true;
		}
		
		return false;
	}
	
	public static class Builder {
		
		//Maps the space coordinate with the piece that is on the coordinate
		Map<Integer, Piece> boardConfig;
		
		//Represent the person who is about to move
		Team newPlayer;
		
		//Represents the pawn that can be captured by En Passant
		Pawn enPassanPawn;
		
		public Builder() {
			
			this.boardConfig = new HashMap<>();
			
		}
		
		/*************************************
		 * Sets the piece for the Builder object
		 * @param piece
		 * @returns Builder object
		 *************************************/
		public Builder setPiece(Piece piece){
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		
		/*******************************
		 * Sets which team gets to move
		 * @param team
		 * @return Builder object
		 *******************************/
		public Builder setNewPlayer(Team team){
			this.newPlayer = team;
			return this;
		}
		
		public Board build(){
			return new Board(this);
		}

		public void setEnPassantPawn(Pawn enPassanPawn) {
			this.enPassanPawn = enPassanPawn; 
			
		}
	}

	
	
	/****************************************************
	 * Takes an integer variable rowNum and set all the
	 * values in that row to True. The rest are set to false
	 * @param rowNum
	 * @return returns a boolean list. With selected column
	 * set to true.
	 ******************************************************/
	private static List<Boolean> initializeRow(int rowNum) {
		Boolean[] row = new Boolean[64];
		for(int i = 0; i < row.length; i++) {
            row[i] = false;
        }
		do{
			row[rowNum] = true;
			rowNum++;
			
		}while(rowNum % 8 != 0);
		
		return ImmutableList.copyOf(row);
	}

	/*************************************************************
	 * Takes an integer variable colNum and set all the
	 * values in that Column to True. The rest are set to false.
	 * @param colNum
	 * @return returns a boolean list. With selected column
	 * set to true.
	 ************************************************************/
	private static List<Boolean> initializeColumn(int colNum) {
		
		Boolean[] column = new Boolean[64];
        for(int i = 0; i < column.length; i++) {
            column[i] = false;
        }
        do {
            column[colNum] = true;
            colNum += 8;
        } while(colNum < 64);
        
        return ImmutableList.copyOf(column);
	}

	
	

}
