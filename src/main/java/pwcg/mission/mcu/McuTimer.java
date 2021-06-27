package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class McuTimer extends BaseFlightMcu
{
	private double time = 0.1;
	private int random = 100;

	public McuTimer ()
	{
		super();
	}

    public McuTimer copy ()
    {
        McuTimer clone = new McuTimer();
        
        super.clone(clone);
        
        clone.time = this.time;
        clone.random = this.random;
            
        return clone;
    }

	public void write(BufferedWriter writer) throws PWCGException
	{
        try
        {
    		writer.write("MCU_Timer");
    		writer.newLine();
    		writer.write("{");
    		writer.newLine();
    		
    		super.write(writer);
    		
    		writer.write("  Time  = " + time + ";");
    		writer.newLine();
    		writer.write("  Random  = " + random + ";");
    		writer.newLine();
    
    		writer.write("}");
    		writer.newLine();
    		writer.newLine();
    		writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}
	
	public double getTime() {
		return time;
	}

	public void setTime(double timer) {		
		this.time = timer;
	}

	public int getRandom() {
		return random;
	}

	public void setRandom(int random) {
		this.random = random;
	}
}
