/**
 * The Process class represents a Process that can run on a CPU.
 */
public class Process implements Comparable<Process>{
	private char name;
	private int arrival;
	private int priority;
	private int cpu_burst;
	private boolean complete;
	private int complete_time;
	
	Process(char name, int arrival, int priority, int cpu_burst) 
	{  
		this.name = name;
		this.arrival = arrival;
		this.priority = priority;
		this.cpu_burst = cpu_burst;
		this.complete = false;
		this.complete_time = 0;
	}
	
	public char getName() 
	{
		return this.name;
	}
	
	public int getArrival() 
	{
		return this.arrival;
	}
	
	public int getPriority() 
	{
		return this.priority;
	}
	
	public int getCpuBurst() 
	{
		return this.cpu_burst;
	}
	
	public boolean isComplete() 
	{
		return this.complete;
	}
	
	public int getCompletion() 
	{
		return this.complete_time;
	}
	
	public int getTurnAroundTime()
	{
		return this.complete_time - this.arrival;
	}
	
	public boolean isRoundRobbinCompleted()
	{
		return true;
	}
	
	public void setArrival(int arrival) 
	{
		this.arrival = arrival;
	}
	
	public void setPriority(int priority) 
	{
		this.priority = priority;
	}
	
	public void setCpuBurst(int cpu_burst) 
	{
		this.cpu_burst = cpu_burst;
	}
	
	private void setComplete(boolean complete) 
	{
		this.complete = complete;
	}
	
	public void setCompleteTime(int complete_time) 
	{
		this.complete_time = complete_time;
	}
	
    /**
    * Run the process in CPU and reduce the cpu burst time by 1 after each run.
    *
    * @param     none
    * @return    none
    */
	public void run() 
	{
		//reduce 1 cup burst from the total
		this.cpu_burst = this.cpu_burst-1;
		if(this.cpu_burst==0)
			this.setComplete(true);
	}

	@Override
	public int compareTo(Process p) {
		if (priority >= p.getPriority())//FCFS
			return 1;
		else if (priority < p.getPriority())//rearranged according to priority
			return -1;
		return 0;
	}

}
