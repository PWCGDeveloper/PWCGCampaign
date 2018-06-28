
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.Plane;


public class McuCheckZone extends BaseFlightMcu
{
	private int zone = 5000;
	private int cylinder = 1;
	private int closer = 1;
	private List<Coalition> planeCoalitions = new ArrayList<Coalition>();
	private List<Coalition> vehicleCoalitions = new ArrayList<Coalition>();
	
    /**
     * @param coalition
     */
    public McuCheckZone (Coalition coalition)
    {
        super();

        setPlaneCoalition(coalition);
    }
    

    /**
     * @param objectId
     */
    public McuCheckZone (int objectId)
    {
        super();
        
        this.setObject(objectId);
    }

	public void setCheckZoneForPlayer(Mission mission) 
    {       
        if (mission != null)
        {
            Flight myFlight = mission.getMissionFlightBuilder().getPlayerFlight();
            if (myFlight != null)
            {
                List<Plane> planes = myFlight.getPlanes();
                if (planes != null)
                {
                    Plane plane = myFlight.getPlanes().get(0);
                    if (plane != null)
                    {
                        planeCoalitions.clear();;
                        setObject(plane.getEntity().getIndex());
                    }
                }
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

	private void setPlaneCoalition(Coalition coalition) 
	{
        planeCoalitions.add(coalition);
	}
}
