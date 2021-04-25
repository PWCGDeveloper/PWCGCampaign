
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;


public class McuCheckZone extends BaseFlightMcu
{
    private int zone = 15000;
	private int cylinder = 1;
	private int closer = 1;
	private List<Coalition> triggerCoalitions = new ArrayList<Coalition>();
	private List<Coalition> vehicleCoalitions = new ArrayList<Coalition>();
	
    public McuCheckZone (String name)
    {
        super();
        this.setName(name);
    }

    public void triggerCheckZoneByCoalitions (List<Coalition> coalitions)
    {
        if (!coalitions.isEmpty())
        {
            triggerCoalitions.clear();
            for (Coalition coalition: coalitions)
            {
                addPlaneCoalition(coalition);
            }
        }
    }
    
    public void triggerCheckZoneByCoalition (Coalition coalition)
    {
        triggerCoalitions.clear();
        addPlaneCoalition(coalition);
    }

    public void triggerCheckZoneByFlight (IFlight flight)
    {
        if (!flight.getFlightPlanes().getPlanes().isEmpty())
        {
            triggerCoalitions.clear();
            for (PlaneMcu plane: flight.getFlightPlanes().getPlanes())
            {
                this.setObject(plane.getLinkTrId());
            }
        }
    }

    public void triggerCheckZoneBySingleObject (int objectId)
    {
        triggerCoalitions.clear();
        this.setObject(objectId);
    }

	public void triggerCheckZoneByMultiplePlaneIds(List<Integer> planeIds) throws PWCGException
    {
        if (!planeIds.isEmpty())
        {
            triggerCoalitions.clear();
            for (int planeId : planeIds)
            {
                setObject(planeId);
            }
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
            
            if (objects.size() == 0)
            {
                CoalitionWriter.writePlaneCoalition(writer, triggerCoalitions);
                CoalitionWriter.writeVehicleCoalition(writer, vehicleCoalitions);
            }
            
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
        triggerCoalitions.add(coalition);
	}

    public List<Coalition> getTriggerCoalitions()
    {
        return triggerCoalitions;
    }
}
