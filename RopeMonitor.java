/**
 * @author Shanika Perera
 *
 */

import java.util.concurrent.Semaphore;

/**
 * The RopeMonitor class maintains monitor attributes.
 */
public class RopeMonitor {
	
    /**
    * An integer to keep track of the number of Baboons who moves east-ward.
    */
	public static int EASTWARDBABOONCOUNT = 0;
	
    /**
    * An integer to keep track of the number of Baboons who moves west-ward.
    */
	public static int WESTWARDBABOONCOUNT = 0;
	
   /**
    * An integer to keep track of the number of trips a baboon and perform.
    * A trip is from the beginning side to the other side and coming back to the beginning side
    */
	public static int TRIPS = 1;
	
    /**
    * A Semaphore to give permission for updates on EastwardBaboonCount attribute. 
    */
	public static Semaphore EASTWARDCOUNTSEM = new Semaphore(1);
	
    /**
    * A Semaphore to give permission for updates on WestwardBaboonCount attribute. 
    */
	public static Semaphore WESTWARDCOUNTSEM = new Semaphore(1);
	
    /**
    * A Semaphore to give permission for Eastward movement. 
    */
	public static Semaphore EASTWARDMOVESEM = new Semaphore(1);
	
    /**
    * A Semaphore to give permission for Westward movement. 
    */
	public static Semaphore WESTWARDMOVESEM = new Semaphore(1);

}
