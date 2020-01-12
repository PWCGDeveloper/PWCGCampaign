
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;


public class McuCheckZone extends BaseFlightMcu
{
	private int zone = 20000;
	private int cylinder = 1;
	private int closer = 1;
	private List<Coalition> planeCoalitions = new ArrayList<Coalition>();
	private List<Coalition> vehicleCoalitions = new ArrayList<Coalition>();
	
    public McuCheckZone ()
    {
        super();
    }

    public void triggerCheckZoneByCoalitions (List<Coalition> coalitions)
    {
        planeCoalitions.clear();
        for (Coalition coalition: coalitions)
        {
            addPlaneCoalition(coalition);
        }
    }
    
    public void triggerCheckZoneByCoalition (Coalition coalition)
    {
        planeCoalitions.clear();
        addPlaneCoalition(coalition);
    }

    public void triggerCheckZoneByFlight (IFlight flight)
    {
        planeCoalitions.clear();
        for (PlaneMcu plane: flight.getFlightData().getFlightPlanes().getPlanes())
        {
            this.setObject(plane.getLinkTrId());
        }
    }

    public void triggerCheckZoneBySingleObject (int objectId)
    {
        planeCoalitions.clear();
        this.setObject(objectId);
    }

	public void triggerCheckZoneByMultipleObjects(List<Integer> playerPlaneIds) throws PWCGException 
    {       
		planeCoalitions.clear();
		for (int playerPlaneId : playerPlaneIds)
		{
			setObject(playerPlaneId);
		}
    }

	
	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
        {
            writer.write("MCU_CheckZone");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            super.write(writer);

            writer.write("  Zone = " + zone + ";");
            writer.newLine();
            writer.write("  Closer = " + closer + ";");
            writer.newLine();
            writer.write("  Cylinder = " + cylinder + ";");
            writer.newLine();
            
            CoalitionWriter.writePlaneCoalition(writer, planeCoalitions);
            CoalitionWriter.writeVehicleCoalition(writer, vehicleCoalitions);
            
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

	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

	public int getCylinder() {
		return cylinder;
	}

	public void setCylinder(int cylinder) {
		this.cylinder = cylinder;
	}

	public int getCloser() {
		return closer;
	}

	public void setCloser(int closer) {
		this.closer = closer;
	}

	private void addPlaneCoalition(Coalition coalition) 
	{
        planeCoalitions.add(coalition);
	}
}
