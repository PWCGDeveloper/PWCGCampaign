package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.playerunit.PlayerUnit;

public class MissionUnitWriter 
{
    private Mission mission = null;

	public MissionUnitWriter (Mission mission)
	{
		this.mission = mission;
	}
	
	public void writeFlights(BufferedWriter writer) throws PWCGException
	{
        writePlayerUnits(mission.getUnits().getPlayerUnits(), writer);
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

    private void writePlayerUnits(List<PlayerUnit> units, BufferedWriter writer) throws PWCGException
    {
        for (PlayerUnit unit : units)
        {
            writePlayerUnit(writer, unit);
        }
    }

    private void writePlayerUnit(BufferedWriter writer, PlayerUnit unit) throws PWCGException
    {
        unit.write(writer);
    }
}
