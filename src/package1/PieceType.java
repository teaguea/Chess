/************************************************************************
 * This Enum Class designates a letter
 * for a piece. Also a special isKing
 * isRook is used for the castling
 * Author: Aaron Teague
 * I was helped by StackOverflow and chessprogramming.wikispaces.com/
 ***********************************************************************/


package package1;

public enum PieceType{
	PAWN("P") {
		
		/***************************************
		 * Returns true if the piece is a King
		 * @return false, this is not a King
		 *************************************/
		@Override
		public boolean isKing() {
			
			return false;
		}

		/***************************************
		 * Returns true if the piece is a Rook
		 * @return false, this is not a Rook
		 *************************************/
		@Override
		public boolean isRook() {
			
			return false;
		}
	},
	KNIGHT("N") {
		
		/***************************************
		 * Returns true if the piece is a King
		 * @return false, this is not a King
		 *************************************/
		@Override
		public boolean isKing() {
			
			return false;
		}

		/***************************************
		 * Returns true if the piece is a Rook
		 * @return false, this is not a Rook
		 *************************************/
		@Override
		public boolean isRook() {
			
			return false;
		}
	},
	BISHOP("B") {
		
		/***************************************
		 * Returns true if the piece is a King
		 * @return false, this is not a King
		 *************************************/
		@Override
		public boolean isKing() {
			
			return false;
		}

		/***************************************
		 * Returns true if the piece is a Rook
		 * @return false, this is not a Rook
		 *************************************/
		@Override
		public boolean isRook() {
			
			return false;
		}
	},
	ROOK("R") {
		
		/***************************************
		 * Returns true if the piece is a King
		 * @return false, this is not a King
		 *************************************/
		@Override
		public boolean isKing() {
			
			return false;
		}

		/***************************************
		 * Returns true if the piece is a Rook
		 * @return true, this is a Rook
		 *************************************/
		@Override
		public boolean isRook() {
			
			return true;
		}
	},
	QUEEN("Q") {
		
		/***************************************
		 * Returns true if the piece is a King
		 * @return false, this is not a King
		 *************************************/
		@Override
		public boolean isKing() {
			
			return false;
		}

		/***************************************
		 * Returns true if the piece is a Rook
		 * @return false, this is not a Rook
		 *************************************/
		@Override
		public boolean isRook() {
			
			return false;
		}
	},
	KING("K") {
		
		/***************************************
		 * Returns true if the piece is a King
		 * @return true, this is a King
		 *************************************/
		@Override
		public boolean isKing() {
			
			return true;
		}

		/***************************************
		 * Returns true if the piece is a Rook
		 * @return false, this is not a Rook
		 *************************************/
		@Override
		public boolean isRook() {
			
			return false;
		}
	};
	
    private String pieceName;
	
    /****************************
     * Set's the piece's name
     * @param pieceName
     ****************************/
	PieceType(final String pieceName){
		this.pieceName = pieceName;
	}
	
	/***************************************
	 * Print's the piece's name
	 * @return pieceName, the piece's name
	 ***************************************/
	@Override
	public String toString() {
		return 	this.pieceName;
	}
	
	public abstract boolean isKing();

	public abstract boolean isRook();
	
}