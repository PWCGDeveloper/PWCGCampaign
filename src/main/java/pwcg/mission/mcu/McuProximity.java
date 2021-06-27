
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class McuProximity extends BaseFlightMcu
{
	private int distance = 8000;
	private int closer = 1;

	private List<Coalition> planeCoalitions = new ArrayList<Coalition>();

	public McuProximity()
	{
		super();

		name = "Proximity";
		desc = "Proximity";
	}
	
	public void addCoalition(Coalition coalition)
	{
	    planeCoalitions.add(coalition);
	}

	public void write(BufferedWriter writer) throws PWCGException
	{
		try
		{
			writer.write("MCU_Proximity");
			writer.newLine();
			writer.write("{");
			writer.newLine();

			if (targets.size() == 0)
			{
				throw new PWCGException("No targets for proximity");
			}

			super.write(writer);

			writer.write("  Distance = " + distance + ";");
			writer.newLine();
			writer.write("  Closer = " + closer + ";");
			writer.newLine();

			CoalitionWriter.writePlaneCoalition(writer, planeCoalitions);

			writer.write("}");
			writer.newLine();
			writer.newLine();
		}
		catch (IOException e)
		{
			PWCGLogger.logException(e);
			throw new PWCGException(e.getMessage());
		}
	}

	public void setDistance(int distance)
	{
		this.distance = distance;
	}

	public void setCloser(int closer)
	{
		this.closer = closer;
	}
}
