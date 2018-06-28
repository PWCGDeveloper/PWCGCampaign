
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.mission.flight.waypoint.WaypointGoal;
import pwcg.mission.flight.waypoint.WaypointPriority;

public class McuCover extends BaseFlightMcu
{
	private int coverGroup = 1;
	private WaypointPriority priority = WaypointPriority.PRIORITY_LOW;
	private WaypointGoal goalType = WaypointGoal.GOAL_DEFAULT;

	public McuCover ()
	{
		super();
		
		name = "Cover";
		desc = "Cover";
	}
	
	public void write(BufferedWriter writer) throws PWCGIOException
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
            writeMCUGoal(writer, goalType.getGoal());

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
