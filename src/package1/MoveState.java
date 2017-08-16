package package1;
/*******************************************************************************
 * This is an enum that distinguishes if is finished or has been completed.
 * Is illegal and therefore can't be completed or is in check.
 * @author Aaron Teague
 * I was helped by stackoverflow.com and chessprogramming.wikispaces.com/
 *******************************************************************************/

public enum MoveState {
	
	FINISHED {
		/*************************************************
		 *@return true, because the enum value is FINISHED 
		 *************************************************/
		@Override
		boolean isFinished() {
			
			return true;
		}
	},
	ILLEGAL {
		/****************************************************
		 * @return false, because the enum value is ILLEGAL 
		 ****************************************************/
		@Override
		boolean isFinished() {
			
			return false;
		}
	},
	
	PLAYER_CHECK {
		/*******************************************************
		 * @return false, because the enum value is PLAYER_CHECK 
		 *******************************************************/
		@Override
		boolean isFinished() {
			
			return false;
		}
	};	
	
          abstract boolean isFinished();

}
