package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.waypoint.WaypointPriority;

public class McuForceComplete extends BaseFlightMcu
{
	private WaypointPriority priority = WaypointPriority.PRIORITY_LOW;
	private int emergencyOrdnanceDrop = 0;

	public McuForceComplete (WaypointPriority priority, int emergencyOrdnanceDrop)
	{
		super();
        this.priority = priority;
        this.emergencyOrdnanceDrop = emergencyOrdnanceDrop;
	}

	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
        {
            writer.write("MCU_CMD_ForceComplete");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
            
            writer.write("  Priority = " + priority.getPriorityValue() + ";");
            writer.write("  EmergencyOrdnanceDrop = " + emergencyOrdnanceDrop + ";");
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
}
