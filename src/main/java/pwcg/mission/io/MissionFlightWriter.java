package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class MissionFlightWriter 
{
    private Mission mission = null;
    private Map<Integer, Squadron> includedSquadrons = new HashMap<>();

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
            writeFlight(writer, flight);
            for (IFlight linkedFlight : flight.getLinkedFlights().getLinkedFlights())
            {
                writeFlight(writer, linkedFlight);
            }
        }
    }

    private void writeFlight(BufferedWriter writer, IFlight flight) throws PWCGException
    {
        assert(!includedSquadrons.containsKey(flight.getSquadron().getSquadronId()));
        includedSquadrons.put(flight.getSquadron().getSquadronId(), flight.getSquadron());
        System.out.println("Writing flight: " + flight.getSquadron().getSquadronId());
        flight.write(writer);
    }
}
