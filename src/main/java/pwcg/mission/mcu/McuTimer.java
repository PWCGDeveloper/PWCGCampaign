package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class McuTimer extends BaseFlightMcu
{
	private int timer = 2;
	private int random = 100;

	public McuTimer ()
	{
		super();
	}
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public McuTimer copy ()
    {
        McuTimer clone = new McuTimer();
        
        super.clone(clone);
        
        clone.timer = this.timer;
        clone.random = this.random;
            
        return clone;
    }

	/* (non-Javadoc)
	 * @see rof.campaign.mcu.BaseFlightMcu#write(java.io.BufferedWriter)
	 */
	public void write(BufferedWriter writer) throws PWCGIOException
	{
        try
        {
    		writer.write("MCU_Timer");
    		writer.newLine();
    		writer.write("{");
    		writer.newLine();
    		
    		super.write(writer);
    		
    		writer.write("  Time  = " + timer + ";");
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
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {		
		this.timer = timer;
	}

	public int getRandom() {
		return random;
	}

	public void setRandom(int random) {
		this.random = random;
	}
}
