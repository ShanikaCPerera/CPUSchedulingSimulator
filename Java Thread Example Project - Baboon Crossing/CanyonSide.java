/**
 * @author Shanika Perera
 *
 */

public enum CanyonSide {
	WEST,EAST;
	
	public CanyonSide GetOppositSide()
	{
		switch(this) {
	      case WEST:
	        return EAST;

	      case EAST:
	        return WEST;
	      default:
	          return null;
		}
	}
}
