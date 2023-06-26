/**
 * @author Shanika Perera
 *
 */

/**
 * The Baboon class represents a the Baboon Thread..
 */
public class Baboon extends Thread{

	//Unique ID of the Baboon
	private int id;
	
	//The side of the Canyon the Baboon is at currently.
	private CanyonSide current_side;
	
	// Number of trips a Baboon can perform. A trip is a movement from the beginning side to the opposite side plus movement back.
	//private int trips;
	
	public Baboon(int id, CanyonSide beginning_side)
	{
		this.id = id;
		this.current_side = beginning_side;
		//this.trips = trips;
	}
	
	public long GetId()
	{
		return this.id;
	}
	
	public CanyonSide GetCurrentSide()
	{
		return this.current_side;
	}
	
//	public int GetTrips()
//	{
//		return this.trips;
//	}
	
	/**
    *  Handle getting permission to get on the rope, move and get off the rope
    *   
    * @param   trip_id        Id of the current trip the Baboon is performing
    * @return
    */
	private void DoBaboonStuff(int trip_id)
	{
		System.out.println("Baboon " + this.id +" doing Baboon stuff...(trip "+ trip_id + ")");
		try {
			Thread.sleep(3000); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
    *  Handle getting permission to get on the rope, and updating the locks and counts.
    *
    * @param   get_on_side    Side of the Canyon from which the Baboon gets on to the rope.   
    * @param   trip_id        Id of the current trip the Baboon is performing
    * @return
    */
	private void GetOnRope(CanyonSide get_on_side, int trip_id) throws InterruptedException 
	{
		if(get_on_side == CanyonSide.WEST)
		{
			//get permission for eastward crossing
			RopeMonitor.EASTWARDMOVESEM.acquire();
			//start moving eastward
			System.out.println("Baboon " + this.id +" started moving east-ward(trip "+ trip_id + ") -->");
			//RopeMonitor.EASTWARDCOUNTSEM.acquire();
			RopeMonitor.EASTWARDBABOONCOUNT++;
			if(RopeMonitor.EASTWARDBABOONCOUNT == 1) {
				//block westward movement
				RopeMonitor.WESTWARDMOVESEM.acquire();
				System.out.println("xxx Blocking West-ward Crossing. xxx");
			}
			//RopeMonitor.EASTWARDCOUNTSEM.release();
			RopeMonitor.EASTWARDMOVESEM.release();
		}
		else if (get_on_side == CanyonSide.EAST)
		{
			Thread.sleep(500);//just to avoid happening at the same time
			//get permission for westward crossing
			RopeMonitor.WESTWARDMOVESEM.acquire();
			//start moving westward
			System.out.println("Baboon " + this.id +" started moving west-ward(trip "+ trip_id + ") <--");
			//RopeMonitor.WESTWARDCOUNTSEM.acquire();
			RopeMonitor.WESTWARDBABOONCOUNT++;
			if(RopeMonitor.WESTWARDBABOONCOUNT == 1) {
				//block eastward movement
				RopeMonitor.EASTWARDMOVESEM.acquire();
				System.out.println("xxx Blocking East-ward Crossing. xxx");
			}
			//RopeMonitor.WESTWARDCOUNTSEM.release();
			RopeMonitor.WESTWARDMOVESEM.release();
		}

	}
	
	/**
    *  Handle getting off the rope and updating locks and counts
    *
    * @param   get_off_side    Side of the Canyon from which the Baboon gets off the rope.   
    * @param   trip_id    	   Id of the current trip the Baboon is performing
    * @return
    */
	private void GetOffRope(CanyonSide get_off_side, int trip_id) throws InterruptedException 
	{
		
		if(get_off_side == CanyonSide.EAST)
		{
			System.out.println("Baboon " + this.id +" arrived at east end(trip "+ trip_id + ")");
			RopeMonitor.EASTWARDCOUNTSEM.acquire();
			RopeMonitor.EASTWARDBABOONCOUNT--;
			if(RopeMonitor.EASTWARDBABOONCOUNT == 0) {
				//unblocking westward movement
				RopeMonitor.WESTWARDMOVESEM.release();
				System.out.println("--- Unblocking West-ward Crossing. ---");
			}
			RopeMonitor.EASTWARDCOUNTSEM.release();
		}
		else if (get_off_side == CanyonSide.WEST)
		{
			System.out.println("Baboon " + this.id +" arrived at west end(trip "+ trip_id + ")");
			RopeMonitor.WESTWARDCOUNTSEM.acquire();
			RopeMonitor.WESTWARDBABOONCOUNT--;
			if(RopeMonitor.WESTWARDBABOONCOUNT == 0) {
				//unblocking eastward movement
				RopeMonitor.EASTWARDMOVESEM.release();
				System.out.println("--- Unblocking East-ward Crossing. ---");
			}
			RopeMonitor.WESTWARDCOUNTSEM.release();
		}
	}
	
	/**
    *  Handle the movement on rope - get on the rope, move and get off the rope
    *
    * @param  trip_id    Id of the current trip the Baboon is performing   
    * @return none  
    */
	private void CrossCanyon(int trip_id) throws InterruptedException
	{
		//CheckSide();
		//get on the rope
	    GetOnRope(this.current_side, trip_id);
	    
	    //move
		try {
			Thread.sleep(5000); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    //System.out.println("taking time for moving");
		
		//changing the current side of the Baboon as it is about to get off the rope
		this.current_side = this.current_side.GetOppositSide();
	     
	    //get off the rope
	    GetOffRope(this.current_side, trip_id);
	}
	
	/**
    * Handle entire Baboon life .
    *
    * @param  none   
    * @return none  
    */
	public void LiveBaboonLife()
	{
		for (int trip = 1; trip <= RopeMonitor.TRIPS; trip++) {
			//do Baboon stuff at the beginning side
			DoBaboonStuff(trip);  
			 
			// makes a request to move on the rope from the beginning side to the opposite side
		    try {
				CrossCanyon(trip);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		     
		    //do Baboon stuff at the opposite side
			DoBaboonStuff(trip);
		    
			// makes a request to return
		    try {
				CrossCanyon(trip);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
    public void run() 
	{
		LiveBaboonLife();
	}


}
