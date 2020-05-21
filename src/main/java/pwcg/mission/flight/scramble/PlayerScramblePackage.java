package pwcg.mission.flight.scramble;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightSpotterBuilder;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;
import pwcg.mission.target.TargetType;

public class PlayerScramblePackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    public PlayerScramblePackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.SCRAMBLE);
        List<IFlight> opposingFlights = makeOpposingScrambleFlights();

        this.targetDefinition = buildTargetDefintion(opposingFlights);
        
		PlayerScrambleFlight playerFlight = createPlayerFlight();
		linkScrambleOpposingFlights(playerFlight, opposingFlights);
        FlightSpotterBuilder.createSpotters(playerFlight, flightInformation);

		return playerFlight;
	}

    private List<IFlight> makeOpposingScrambleFlights() throws PWCGException
    {
        ScrambleOpposingFlightBuilder opposingFlightBuilder = new ScrambleOpposingFlightBuilder(flightInformation);
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        return opposingFlights;
    }
    
    private TargetDefinition buildTargetDefintion(List<IFlight> opposingFlights) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();

        if (opposingFlights.size() > 0)
        {
            IFlight enemyFlight = opposingFlights.get(0);
            MissionPoint enemyStartPoint = enemyFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_START);
            Coordinate targetLocationForPlayerFlight = enemyStartPoint.getPosition().copy();
            targetDefinition = new TargetDefinition(TargetType.TARGET_AIR, targetLocationForPlayerFlight, flightInformation.getCountry());
        }

        return targetDefinition;
    }

    private PlayerScrambleFlight createPlayerFlight() throws PWCGException
    {
        PlayerScrambleFlight playerFlight = new PlayerScrambleFlight (flightInformation, targetDefinition);
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
