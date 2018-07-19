package pwcg.mission.flight.intercept;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.OpposingFlightBuilder;
import pwcg.mission.ground.GroundUnitFactory;
import pwcg.mission.ground.unittypes.SpotLightGroup;

public class InterceptPackage extends FlightPackage
{
	protected Role opposingFlightRole = Role.ROLE_BOMB;
	protected FlightTypes opposingFlightType = FlightTypes.BOMB;
	
    public InterceptPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.INTERCEPT;
    }

    public Flight createPackage () throws PWCGException 
    {
        InterceptFlight interceptFlight = null;
		if (isPlayerFlight)
		{
	        Coordinate targetCoordinates = getTargetCoordinates();	        
	        interceptFlight = createInterceptFlight(campaign, targetCoordinates);            
            addOpposingFlight(interceptFlight, targetCoordinates);
	        addSpotLights(targetCoordinates, interceptFlight);
		}
		else
		{
		    InterceptAiCoordinateGenerator coordinateGenerator = new InterceptAiCoordinateGenerator(campaign, squadron);
		    Coordinate targetCoordinates = coordinateGenerator.createTargetCoordinates();
            interceptFlight = createInterceptFlight(campaign, targetCoordinates);
		}
		
        setNightFlight(interceptFlight);
		return interceptFlight;
	}

    protected Coordinate getTargetCoordinates() throws PWCGException
    {
        InterceptPlayerCoordinateGenerator coordinateGenerator = new InterceptPlayerCoordinateGenerator(campaign, mission, opposingFlightType, squadron);
        coordinateGenerator.createTargetCoordinates();	        
        Coordinate targetCoordinates = coordinateGenerator.getTargetCoordinates();
        return targetCoordinates;
    }

    protected void addOpposingFlight(InterceptFlight interceptFlight, Coordinate targetCoordinates) throws PWCGException
    {
        for (int i = 0; i < 2; ++i)
        {
            OpposingFlightBuilder opposingFlightBuilder = new OpposingFlightBuilder(mission, targetCoordinates, opposingFlightRole, opposingFlightType);
            List<InterceptOpposingFlight> interceptOpposingFlights = opposingFlightBuilder.buildOpposingFlights();
            for (Flight interceptOpposingFlight : interceptOpposingFlights)
            {
                interceptFlight.addLinkedUnit(interceptOpposingFlight);
            }
        }
    }

    protected InterceptFlight createInterceptFlight(Campaign campaign, Coordinate targetCoordinates) throws PWCGException
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
        
        FlightInformation flightInformation = createFlightInformation(targetCoordinates);
		InterceptFlight interceptFlight = new InterceptFlight (flightInformation, missionBeginUnit);

		interceptFlight.createUnitMission();
        return interceptFlight;
    }

    protected void setNightFlight(InterceptFlight interceptFlight)
    {
        if (squadron.determineIsNightSquadron())
        {
            interceptFlight.setNightFlight(true);
        }
    }

    protected void addSpotLights(Coordinate targetCoordinates, InterceptFlight interceptFlight) throws PWCGException
    {
        if (isPlayerFlight)
        {
            if (squadron.determineIsNightSquadron())
            {
                GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(campaign, targetCoordinates, interceptFlight.getCountry());
                SpotLightGroup spotLightGroup = groundUnitFactory.createSpotLightGroup();
                interceptFlight.addLinkedUnit(spotLightGroup);
            }
		}
    }
}
