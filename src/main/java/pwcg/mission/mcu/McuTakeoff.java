
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

public class McuTakeoff extends BaseFlightMcu
{
	public McuTakeoff ()
	{
		super();
		
		name = "command Take off";
		desc = "command Take off ";
	}

	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
        {
            writer.write("MCU_CMD_TakeOff");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}	
}
