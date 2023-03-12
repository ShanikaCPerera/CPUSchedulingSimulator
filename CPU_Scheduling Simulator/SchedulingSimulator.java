import java.util.Queue;
import java.util.PriorityQueue;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The SchedulingSimulator class simulates scheduling of processes in a simple uni-processor CPU.
 */
public class SchedulingSimulator {
	
    /**
    * A Priority Queue object to keep track of the Ready Queue of the processor.
    */
	public static Queue<Process> readyQueue = new PriorityQueue<Process>();

    /**
    * An integer to keep track of the Round Robin Quantum.
    * User can change this. Default value is 2.
    */
	public static int rndRobQuantum = 2;
	
	/**
    * Read the input file and generate the input process list.
    *
    * @param  file_path   the file path of the input file
    * @return             the input process list
    */
	public static List<Process> readInputProcessList(String file_path)
	  {
	 
		List<String> processStringlist = Collections.emptyList();
		List<Process> processList = new ArrayList<Process>();
	    String[] string_parts;
	    Process tempProcess;
    	char process_name;
    	int process_arrival = 0;
    	int process_priority = 0;
    	int process_burst = 0;
    	
    	//read the process information file
	    try
	    {
	    	processStringlist = Files.readAllLines(Paths.get(file_path), StandardCharsets.UTF_8);
	    }
	 
	    catch (IOException e)
	    {

	      e.printStackTrace();
	    }
    	
	    //printing the input file
	    System.out.println("Input file \n----------");
	    Iterator<String> itr = processStringlist.iterator();		
	    while (itr.hasNext())
	    {
	    	//printing the lines of input file
	    	System.out.println(itr.next());
	    }
		
	    //generating the process list
	    itr = processStringlist.iterator();
	    while (itr.hasNext())
	    {	
	    	string_parts = itr.next().split(" ");
	    	process_name = string_parts[0].charAt(0);
	    	process_arrival = Integer.parseInt(string_parts[1]);
	    	process_priority = Integer.parseInt(string_parts[2]);
	    	process_burst = Integer.parseInt(string_parts[3]);
	    	
	    	tempProcess = new Process(process_name, process_arrival, process_priority, process_burst);
	    	processList.add(tempProcess);
	    }

	    return processList;
	  }
	
    /**
    * Schedule a uni-processor CPU using preemptive-priority.
    *
    * @param  processList   the list of input process
    * @return               the list of Gantt Chart
    */
	public static List<Character> PreemptivePrioritySchedule(List<Process> processList)
	{
        int time = 0;
        int list_index = 0;
        boolean preempt = false;
        boolean roundRobin = false;
        Process newProcess = null;
        Process currentProcess = null;
        Process preemptProcess = null;
        Process completedProcess = null;
        Process nextProcess = null;
        
        List<Character> ganttChartList = new ArrayList<Character>(1);
        
        //start
        System.out.print("\nTime: "+time);
        
        while (time >= 0)
        {
			
        	//check for arrival    	
    		if(list_index < processList.size())
    			newProcess = processList.get(list_index);
        	
        	if( newProcess.getArrival() == time)
        	{
        		System.out.print(" <<< Arriving Process "+newProcess.getName()+" with priority="+newProcess.getPriority());
        		
        		// put process to the priority Queue
        		readyQueue.offer(newProcess);
        		
        		if(currentProcess != null && currentProcess.getPriority() > newProcess.getPriority())
        		{
        			preempt = true;
        		}
        		else if(currentProcess != null && currentProcess.getPriority() == newProcess.getPriority())
        		{
        			//switch to Round Robin
        			System.out.print("\n   ---Switching to Secondary[Round Robin with Q="+ rndRobQuantum + "]---");
        			roundRobin = true;
        			//check if the current process has completed RR quantum
        			if(isRoundRobinCompleted(rndRobQuantum, currentProcess.getName(), ganttChartList))
    					preempt = true;
        			else
        				preempt = false;
        		}
        		else 
        		{
        			preempt = false;
        		    roundRobin = false;
        		}
        		
        		//increase only if arrival happens
        		list_index++;
        	}
        	
        	//check I/O completion - TODO
        	
        	//check for preemption
        	if(preempt) 
        	{
        		//preempting
        		preemptProcess = currentProcess;
        		readyQueue.offer(preemptProcess);
        		System.out.print("\n   ---Preempting Process " + preemptProcess.getName()+"---");
        		//dispatching the next process
        		currentProcess = readyQueue.poll();
        		System.out.print("\n   ---Dispatching Process " + currentProcess.getName()+"---");
        		preempt = false;
        	}
        	
        	//dispatching the process in the queue
        	if(currentProcess == null && readyQueue.size() == 1) {
        		currentProcess = readyQueue.poll();
        	    System.out.print("\n   ---Dispatching Process " + currentProcess.getName()+"---");
        	}

        	//process running
        	if(currentProcess != null)
        	{
				currentProcess.run();
				//demo purpose
				System.out.print("\n" + currentProcess.getName()+" Running...");
				
				//add to Gantt chart
				ganttChartList.add(currentProcess.getName());
				
				//looking at the next item in queue 
				//if equal priority then switch to Round Robin
				nextProcess =  readyQueue.peek();
				if(nextProcess != null && currentProcess.getPriority() == nextProcess.getPriority())
				{
					if(!roundRobin)
						System.out.print("\n   ---Switching to Secondary[Round Robin with Q="+ rndRobQuantum + "]---");
					roundRobin = true;
				}	
				else
					roundRobin = false;
				
				//if in RR state AND current process has completed the quantum
				if(roundRobin && isRoundRobinCompleted(rndRobQuantum, currentProcess.getName(), ganttChartList))
					preempt = true;
				else
					preempt = false;
				
				if(currentProcess.isComplete())
				{
					//at completion, remove process from queue
					completedProcess = currentProcess;
					//update completed time of the completed process
					completedProcess.setCompleteTime(time+1);
					//dispatching a new process
					currentProcess = readyQueue.poll();
					//ending RR if one of the equal priority processes completes
					if(roundRobin)
						System.out.print("\n   ---Ending Secondary[Round Robin with Q="+ rndRobQuantum + "]---");
					roundRobin = false;
				}
				
        	}
        	
    		time++;
			System.out.print("\nTime: " + time);
			
			//demo purpose
			if(completedProcess != null && completedProcess.isComplete()) {
				System.out.print(" >>> Process " + completedProcess.getName() + " completed!");

				completedProcess = null;
				if (currentProcess != null)
					System.out.print("\n   ---Dispatching Process " + currentProcess.getName() + "---");
			}
			
			//no more processes in ready queue to run
			if(currentProcess == null)
				break;
        }
        
        return ganttChartList;
	}
	
    /**
    * Check whether a process has completed the Round Robin quantum. 
    *
    * @param  quantum        the Round Robin quantum
    * @param  process_name   the name of the process to check
    * @param  ganttChart     the list of Gantt Chart
    * @return                a boolean status of completion
    */
	public static boolean isRoundRobinCompleted(int quantum, char process_name, List<Character> ganttChart)
	{
		int j = 0;
		for(int i = ganttChart.size()-1; i >= ganttChart.size()-quantum ; i--)
        {
			if(ganttChart.get(i) == process_name)
				j++;
        }
		if(j == quantum)
			return true;
		
		return false;
	}
	
    /**
    * Print the Turn Around time of each process and the overall average.
    *
    * @param  processList   the list of input processes
    * @return               none
    */
	public static void printTurnAroundTimeStat(List<Process> processList)
	{
		int total =0;
		int temp_turn =0;
		
		for(int i = 0; i < processList.size(); i++) {
			temp_turn = processList.get(i).getTurnAroundTime();
			System.out.print("\nTurn Around Time of Process " + processList.get(i).getName() + ": " + temp_turn);
			total = total + temp_turn;
		}
		float avg = (float)total/processList.size();
		System.out.printf("\nAverage Turn Around Time: %1.2f", avg);
	}

	public static void main(String[] args) {
		
        List<Process> processList = new ArrayList<Process>();
	    List<Character> ganttChart = Collections.emptyList();
	    String input_file_path = "C:\\Users\\user\\eclipse-workspace\\SchedulingSimulator\\src\\input.txt";
	    //user can change this before running the program
	    rndRobQuantum = 2;
	    
	    //getting the input process list
	    processList = readInputProcessList(input_file_path);
	    
	    //emptying the Ready Queue
        readyQueue.clear();
        	
        //scheduling the CPU
        ganttChart = PreemptivePrioritySchedule(processList);
        
        //printing the Gantt chart
        System.out.print("\n\nGantt Chart: |");
		for(int i = 0; i < ganttChart.size(); i++) {
			System.out.print(ganttChart.get(i) + "|");
		}
		
		//printing Turn Around Time Statistics
		System.out.print("\n\nTurn Around Time Statistics\n----------------------------");
		printTurnAroundTimeStat(processList);

	}

}
