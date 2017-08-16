package package1;

//Establishes the white and black teams
public enum Team {
	WHITE {
 
		@Override
		public int getDirection() {
			
			return -1;
		}

		@Override
		public boolean Black() {
			
			return false;
		}

		@Override
		public boolean White() {
			
			return true;
		}

		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			
			return whitePlayer;
		}

		@Override
		public int getOpposingDirection() {
			
			return 1;
		}

		@Override
		public boolean pawnPromotionSpace(int coordinate) {
			
			return Board.FIRST_ROW.get(coordinate);
		}
		
	}, BLACK {
	
		@Override
		public int getDirection() {
			
			return 1;
		}

		@Override
		public boolean Black() {
			
			return true;
		}

		@Override
		public boolean White() {
			
			return false;
		}

		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			
			return blackPlayer;
		}

		@Override
		public int getOpposingDirection() {
			
			return -1;
		}

		@Override
		public boolean pawnPromotionSpace(int coordinate) {
			
			return Board.EIGHTH_ROW.get(coordinate);
		}
	};	
	public abstract int getDirection();
	public abstract int getOpposingDirection();
	public abstract boolean Black();
	public abstract boolean White();
	public abstract boolean pawnPromotionSpace(int coordinate); 
	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
