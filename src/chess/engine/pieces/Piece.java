package chess.engine.pieces;

import java.util.Collection;

import chess.engine.board.Board;
import chess.engine.board.Move;

/************************************************************
 * Abstract Parent Class that all the piece in the game will
 * inherent from
 * @author Aaron Teague
 * I was helped by the sites www.stackoverflow.com, www.stackexchange.com,
 * and chessprogramming.wikispaces.com
 ************************************************************/

public abstract class Piece {
	
	//The position the Piece is located
	protected final int piecePosition;
	//The Value that represents if the piece is black or white
	protected final Team pieceTeam;
	//The Value that indicates if it's the Piece's first move
	protected final boolean isFirstMove;
	//Identifies the piece's type
	protected final PieceType pieceType;
	private final int cachedHashCode;
	
	/***********************************************************************
	 * Creates the piece object from the piecePosition and pieceTeam values
	 * that are passed in.
	 * @param piecePosition
	 * @param pieceTeam
	 ***********************************************************************/
	Piece(final PieceType pieceType, final int piecePosition, final Team pieceTeam, final boolean isFirstMove) {
		this.piecePosition = piecePosition;
		this.pieceTeam = pieceTeam;
		this.isFirstMove = isFirstMove;
		this.pieceType = pieceType;
		this.cachedHashCode = computeHashCode();
	}
	
	private int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceTeam.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFirstMove ? 1:0);
		return result;
	}

	@Override
	public boolean equals(final Object other){
		if(this == other)
			return true;
		
		if(!(other instanceof Piece))
			return false;
		
		final Piece otherPiece = (Piece) other;
		return this.piecePosition == otherPiece.getPiecePosition() && this.pieceType == otherPiece.getPieceType() &&
			   this.pieceTeam == otherPiece.getPieceTeam() && this.isFirstMove == otherPiece.isFirstMove;
	}
	
	@Override
	public int hashCode() {
		return this.cachedHashCode;
		
	}
	
	/******************************************
	 * Returns the coordinate of the Piece on 
	 * the board
	 * @return int piecePosition
	 ******************************************/
	public int getPiecePosition(){
		return this.piecePosition;
	}
	
	/*****************************************
	 * Returns the team the piece belongs to.
	 * @return Team pieceTeam
	 *****************************************/
	public Team getPieceTeam() {
		return this.pieceTeam;
	}
	
	/**********************************************************
	 * Return a boolean value to indicate if it's the piece's
	 * first move.
	 * @return boolean value isFirstMove
	 **********************************************************/
	public boolean isFirstMove(){
		return this.isFirstMove;
	}
	
	/*********************************
	 * Returns the type of the piece
	 * @return PieceType pieceType
	 ********************************/
	public PieceType getPieceType(){
		return this.pieceType;
	}
	
	/****************************************
	 * Returns the Piece's value
	 * @return int pieceValue
	 ****************************************/
	public int getPieceValue(){
		return this.pieceType.getPieceValue();
	}
	
	/*************************************************************
	 * An abstract method that different piece will implement to
	 * calculate it's legal moves
	 * @param board
	 * @return
	 **************************************************************/
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	
	/****************************************************
	 * Creates the new piece with an updated coordinate
	 * @param move
	 * @return Piece
	 ****************************************************/
	public abstract Piece movePiece(Move move);
	
//<--------------------------------------------------------------------------------------------------------------------------------------------------------->	
	public enum PieceType{
		
		PAWN("P", 100) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KNIGHT("N", 300) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP("B", 300) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK("R", 500) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN("Q", 900) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING("K", 10000) {
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		};
		
		private String pieceName;
		private int pieceValue;
		
		PieceType(final String pieceName, final int pieceValue){
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}
		
		@Override
		public String toString(){
			return this.pieceName;
		}
		
		public int getPieceValue() {
			return this.pieceValue;
		}
		
		
		/*************************************
		 * Checks if the piece is a King type
		 * @return boolean true/false
		 *************************************/
		public abstract boolean isKing();

		/*************************************
		 * Checks if the piece is a Rook type
		 * @return boolean true/false
		 ************************************/
		public abstract boolean isRook();
		
	}
//<--------------------------------------------------------------------------------------------------------------------------------------------------------->	

}
