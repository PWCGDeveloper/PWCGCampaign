
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.waypoint.WaypointPriority;

public class McuCover extends BaseFlightMcu
{
	private int coverGroup = 1;
	private WaypointPriority priority = WaypointPriority.PRIORITY_LOW;

	public McuCover ()
	{
		super();
		
		name = "Cover";
		desc = "Cover";
	}
	
	public void write(BufferedWriter writer) throws PWCGException
	{
		try
        {
            writer.write("MCU_CMD_Cover");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("  CoverGroup = " + coverGroup + ";");
            writer.newLine();
            writer.write("  Priority = " + priority.getPriorityValue() + ";");
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

}
