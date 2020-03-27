package pwcg.mission.flight.scramble;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightSpotterBuilder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;

public class PlayerScramblePackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public PlayerScramblePackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
	{        		
        List<IFlight> opposingFlights = makeLinkedScrambleFlights();
        changeTargetLocationForPlayerFlight(opposingFlights);
		PlayerScrambleFlight playerFlight = createPlayerFlight();
		linkScrambleOpposingFlights(playerFlight, opposingFlights);
        FlightSpotterBuilder.createSpotters(playerFlight, flightInformation);

		return playerFlight;
	}

    private List<IFlight> makeLinkedScrambleFlights() throws PWCGException
    {
        ScrambleOpposingFlightBuilder opposingFlightBuilder = new ScrambleOpposingFlightBuilder(flightInformation);
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        return opposingFlights;
    }
    
    private void changeTargetLocationForPlayerFlight(List<IFlight> opposingFlights) throws PWCGException
    {
        if (opposingFlights.size() > 0)
        {
            IFlight enemyFlight = opposingFlights.get(0);
            MissionPoint enemyStartPoint = enemyFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_START);
            Coordinate targetLocationForPlayerFlight = enemyStartPoint.getPosition().copy();
            flightInformation.getTargetDefinition().setTargetPosition(targetLocationForPlayerFlight);
        }
    }

    private PlayerScrambleFlight createPlayerFlight() throws PWCGException
    {
        PlayerScrambleFlight playerFlight = new PlayerScrambleFlight (flightInformation);
        playerFlight.createFlight();
        return playerFlight;
    }
    
    private void linkScrambleOpposingFlights(IFlight playerFlight, List<IFlight> opposingFlights)
    {
        for (IFlight opposingFlight: opposingFlights)
        {
            playerFlight.getLinkedFlights().addLinkedFlight(opposingFlight);
        }
    }
}
