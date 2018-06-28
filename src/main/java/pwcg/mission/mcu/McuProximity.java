
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

public class McuProximity extends BaseFlightMcu
{
	private int distance = 5000;
	private int closer = 1;

	private List<Coalition> planeCoalitions = new ArrayList<Coalition>();

	public McuProximity(Coalition planeCoalition)
	{
		super();

		name = "Proximity";
		desc = "Proximity";

		planeCoalitions.add(planeCoalition);
	}

	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
		{
			writer.write("MCU_Proximity");
			writer.newLine();
			writer.write("{");
			writer.newLine();

			if (targets.size() == 0)
			{
				throw new PWCGIOException("No targets for proximity");
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
			Logger.logException(e);
			throw new PWCGIOException(e.getMessage());
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
