
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class McuTakeoff extends BaseFlightMcu
{
	public McuTakeoff ()
	{
		super();
		
		name = "command Take off";
		desc = "command Take off ";
	}

	public void write(BufferedWriter writer) throws PWCGException
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
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}	
}
