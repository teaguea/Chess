/*****************************************************************************
 * The UsedSpace class can indicate it is indeed a used space and can return
 * the piece that is using the space.
 * @author Aaron Teague
 * I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
 ****************************************************************************/
package package1;

public final class UsedSpace extends Space {
	//The chess piece that is using the space
	Piece pieceOnSpace;
	
	/*************************************
	 * Creates the space that is being used by
	 * a piece.
	**************************************/
	UsedSpace(int spaceCoordinate, Piece pieceOnSpace){
		super(spaceCoordinate);
		this.pieceOnSpace = pieceOnSpace;
	}

	@Override
	/***********************************
	 * Since the space is being used the
	 * method returns a true value.
	 ***********************************/
	public boolean isSpaceUsed() {

		return true;
	}

	@Override
	/*******************************************
	 * Returns the piece that is currently using
	 * the space.
	 *******************************************/
	public Piece getPiece() {
		return this.pieceOnSpace;
	}
	
	@Override
	public String toString(){
		return getPiece().getTeam().Black() ? getPiece().toString().toLowerCase() :
			getPiece().toString();
	}
}
