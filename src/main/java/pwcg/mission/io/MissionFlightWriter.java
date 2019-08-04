package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.List;

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
	    writeFlights(mission.getMissionFlightBuilder().getPlayerFlights(), writer);
        if (!TestDriver.getInstance().isCreatePlayerOnly())
        {
            writeFlights(mission.getMissionFlightBuilder().getAiFlights(), writer);
            writePlaneCounters(writer);
        }
	}

    private void writeFlights(List<Flight> flights, BufferedWriter writer) throws PWCGException
    {
        for (Flight flight : flights)
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
