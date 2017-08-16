/*****************************************************************************
 * The Piece Class sets up basic the operations of chess piece object. A chess
 * piece object contains it's position, what team it's playing for, is it on 
 * first move, and what type it is.
 * @author Aaron Teague
 *I was helped by StackOverflow and chessprogramming.wikispaces.com/
 ****************************************************************************/

package package1;
import java.util.Collection;


public abstract class Piece {
	
	//The coordinate of the space that the piece uses 
	protected int piecePosition;
	
	//Indicates whether the piece is on the black or the white team.
	protected Team pieceTeam;
	
	//Indicates if the piece has first Move
	protected boolean First;
	
	//Indicates the type of piece
	protected PieceType pieceType;
	
	private final int cachedHashCode;
	
	/*************************************
	 * Returns the Team of the piece
	 * @return
	 *************************************/
	public Team getTeam(){
		return this.pieceTeam;
	}
	
	
	/************************************************************
	 * Creates the Piece object from the passed in variables
	 * @param piecePosition
	 * @param pieceTeam
	 * @param First
	 ***********************************************************/
	Piece(int piecePosition, Team pieceTeam, PieceType pieceType, boolean firstMove){
		this.piecePosition = piecePosition;
		this.pieceTeam = pieceTeam;
		this.First = firstMove;
		this.pieceType = pieceType; 
		this.cachedHashCode = calcHashCode();
	}
	
	
	
	private int calcHashCode() {
		int result = pieceType.hashCode();
        result = 31 * result + pieceTeam.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (First ? 1 : 0);
        return result;
	}


	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
        if (!(obj instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) obj;
        return piecePosition == otherPiece.piecePosition && pieceType == otherPiece.pieceType &&
        pieceTeam == otherPiece.pieceTeam && First == otherPiece.First;
	}


	/***************************************
	 *Checks if the piece has first move
	 *@return First
	 ***************************************/
	public boolean getFirst(){
		return this.First;
	}
	
	/***************************
	 * Gets the type of piece
	 * @return pieceType
	 ***************************/
	public PieceType getPieceType(){
		return this.pieceType;
	}
	
	//creates a list of legal moves
	public abstract Collection<Move> calcLegalMove(Board board);
	
	public abstract Piece movePiece(Move move);


	/***************************
	 * Gets the piece position
	 * @return piecePosition
	 ***************************/
	public Integer getPiecePosition() {
		
		return this.piecePosition;
	}

	
}
