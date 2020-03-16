
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;




public class McuCounter extends BaseFlightMcu
{
	private int counter = 1;
	private int dropcount  = 1;

	public McuCounter ()
	{
		super();
	}

	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
        {
            writer.write("MCU_Counter");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("  Counter = " + counter + ";");
            writer.newLine();
            writer.write("  Dropcount = " + dropcount + ";");
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

    public int getCounter()
    {
        return this.counter;
    }

    public void setCounter(int counter)
    {
        this.counter = counter;
    }

    public int getDropcount()
    {
        return this.dropcount;
    }

    public void setDropcount(int dropcount)
    {
        this.dropcount = dropcount;
    }
}
