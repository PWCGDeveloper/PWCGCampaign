package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class MissionFlightWriter 
{
    private Mission mission = null;

	public MissionFlightWriter (Mission mission)
	{
		this.mission = mission;
	}
	
	public void writeFlights(BufferedWriter writer) throws PWCGException
	{
	    writeFlights(mission.getFlights().getPlayerFlights(), writer);
        if (!TestDriver.getInstance().isCreatePlayerOnly())
        {
            writeFlights(mission.getFlights().getAiFlights(), writer);
        }
	}

    private void writeFlights(List<IFlight> flights, BufferedWriter writer) throws PWCGException
    {
        for (IFlight flight : flights)
        {
            writeFlight(writer, flight);
        }
    }

    private void writeFlight(BufferedWriter writer, IFlight flight) throws PWCGException
    {
        flight.write(writer);
    }
}
