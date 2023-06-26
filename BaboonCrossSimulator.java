/**
 * @author Shanika Perera
 *
 */

//import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import java.util.Scanner;

public class BaboonCrossSimulator {
	
	public static void main(String[] args) {
		int input_num;
		int input_trips;
        Baboon baboon;
        Baboon print_baboon;
        List<Baboon> baboonList = new ArrayList<Baboon>();
        Iterator<Baboon> itr = baboonList.iterator();
		
		
        System.out.println("--- Welcome to the Baboon Crossing Simulator! ---");
        //Scanner scanner = new Scanner(new InputStreamReader(System.in));
        //System.out.print("Please enter the number of baboons:");
        //input_num = scanner.nextInt();
        
        //using command line arguments
        input_num = Integer.parseInt(args[0]);
        input_trips = Integer.parseInt(args[1]);
        if (input_trips > 1)
        	RopeMonitor.TRIPS = input_trips;
        
        //Assigning the user entered number of Baboons to the West and East side alternatively.
        for(int i = 1; i<= input_num; i++)
        {
        	if (i%2 == 0)
        		baboon = new Baboon(i, CanyonSide.EAST);
        	else
        		baboon = new Baboon(i, CanyonSide.WEST);
        	
        	baboonList.add(baboon);
        }
        
        //Printing output for demo
        System.out.println("WEST\t\tEAST");
        itr = baboonList.iterator();
        while (itr.hasNext())
        {
        	print_baboon = itr.next();
        	if(print_baboon.GetCurrentSide() == CanyonSide.WEST)
        		System.out.print(print_baboon.GetId());
        }
        System.out.print("\t\t");
        itr = baboonList.iterator();
        while (itr.hasNext())
        {
        	print_baboon = itr.next();
        	if(print_baboon.GetCurrentSide() == CanyonSide.EAST)
        		System.out.print(print_baboon.GetId());
        }
        System.out.print("\n\n");
		
        //Running the Baboon threads
	    itr = baboonList.iterator();	    
	    while (itr.hasNext())
	    {
	    	//starting the Baboon threads
	    	itr.next().start();
	    }

	}
}
