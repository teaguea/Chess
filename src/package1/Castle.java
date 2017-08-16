package package1;
/**********************************************************************
 * The Castle class is an abstract that creates the basic
 * framework for the King Side Castle and the Queen Side Castle.
 * The Castle class stores the actual Rook that is involved with the
 * move. As well as the beginning and ending coordinates of the Rook
 * @author Aaron Teague
 *I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
 ***********************************************************************/

public abstract class Castle extends Move{
	
	private Rook castleRook;
	private int castleRookStart;
	private int castleRookEnd;

	Castle(Board board, Piece movedPiece, int endCoordinate, Rook castleRook, int castleRookStart, int castleRookEnd) {
		
		super(board, movedPiece, endCoordinate);
		this.castleRook = castleRook;
		this.castleRookStart = castleRookStart;
		this.castleRookEnd = castleRookEnd;

	}
	
	/***********************************************
	 * Gets the rook that is going to be castled
	 * @return the rook that is going to be castled
	 ***********************************************/
	public Rook getCastleRook(){
		return this.castleRook;
	}
	
	@Override
	public boolean isCastling(){
		return true;
	}
	
	@Override
	public Board execute() {
		Board.Builder builder = new Board.Builder();
		//Sets all the not moving pieces to the new board
		for(Piece piece: this.board.getCurrentPlayer().getAlivePieces()){
					
			//checks if the piece is the moving piece and not the castle rook
			if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
				 builder.setPiece(piece);
				}
		}
				
		//Sets all the opposing pieces on the new board
		for(Piece piece : this.board.getCurrentPlayer().getOpponent().getAlivePieces()){
					
			//No check is required because none of the opposing pieces are being moved at this point
			builder.setPiece(piece);
			
		}
		
		//Sets the King
		builder.setPiece(this.movedPiece.movePiece(this));
		//Set the Rook
		builder.setPiece(new Rook(this.castleRookEnd, this.castleRook.getTeam()));
		//Sets the who can move next
		builder.setNewPlayer(this.board.getCurrentPlayer().getOpponent().getTeam());
		return builder.build();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((castleRook == null) ? 0 : castleRook.hashCode());
		result = prime * result + castleRookEnd;
		result = prime * result + castleRookStart;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Castle other = (Castle) obj;
		if (castleRook == null) {
			if (other.castleRook != null)
				return false;
		} else if (!castleRook.equals(other.castleRook))
			return false;
		if (castleRookEnd != other.castleRookEnd)
			return false;
		if (castleRookStart != other.castleRookStart)
			return false;
		return true;
	}
	
	
	

}
