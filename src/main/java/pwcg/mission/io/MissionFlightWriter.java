package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

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
        }
	}

    private void writeFlights(List<IFlight> flights, BufferedWriter writer) throws PWCGException
    {
        for (IFlight flight : flights)
        {
            flight.write(writer);
            for (IFlight linkedFlight : flight.getLinkedFlights().getLinkedFlights())
            {
                linkedFlight.write(writer);
            }
        }
    }
}
