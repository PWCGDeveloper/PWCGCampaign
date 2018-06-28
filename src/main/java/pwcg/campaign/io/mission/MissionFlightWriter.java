package pwcg.campaign.io.mission;

import java.io.BufferedWriter;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.Mission;
import pwcg.mission.Unit;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.group.PlaneCounter;

public class MissionFlightWriter 
{
    protected Mission mission = null;
	
	public MissionFlightWriter (Mission mission)
	{
		this.mission = mission;
	}
	
	public void writeFlights(BufferedWriter writer) throws PWCGException
	{
        writePlayerFlight(writer);
        if (!TestDriver.getInstance().isCreatePlayerOnly())
        {
            writeOtherFlights(writer);
            writePlaneCounters(writer);
        }
	}
	
    private void writePlayerFlight(BufferedWriter writer) throws PWCGException
    {
        mission.getMissionFlightBuilder().getPlayerFlight().write(writer);
        for (Unit linkedUnit : mission.getMissionFlightBuilder().getPlayerFlight().getLinkedUnits())
        {
            linkedUnit.write(writer);
        }
    }

    private void writeOtherFlights(BufferedWriter writer) throws PWCGException
    {
        for (Flight flight : mission.getMissionFlightBuilder().getMissionFlights())
        {
            flight.write(writer);
            
            for (Unit linkedUnit : flight.getLinkedUnits())
            {
                linkedUnit.write(writer);
            }
        }
    }

    private void writePlaneCounters(BufferedWriter writer) throws PWCGIOException
    {
        PlaneCounter alliedPlaneCounter = mission.getMissionPlaneCalculator().getAlliedPlaneCounter();
        alliedPlaneCounter.write(writer);

        PlaneCounter axisPlaneCounter = mission.getMissionPlaneCalculator().getAxisPlaneCounter();
        axisPlaneCounter.write(writer);
    }
}
