package pwcg.mission.flight.intercept;

import java.util.ArrayList;
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
import pwcg.mission.ground.GroundUnitFactory;
import pwcg.mission.ground.unittypes.SpotLightGroup;

public class InterceptPackage extends FlightPackage
{	
    public InterceptPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.INTERCEPT;
    }

    public Flight createPackage () throws PWCGException 
    {
        Coordinate targetCoordinates = getPlayerTargetCoordinates();
        InterceptFlight interceptFlight = createInterceptFlight(campaign, targetCoordinates);            
        if (isPlayerFlight)
        {
            addOpposingFlights(interceptFlight, targetCoordinates);
        }        
		return interceptFlight;
	}

    protected Coordinate getTargetCoordinates() throws PWCGException 
    {
        Coordinate targetCoordinates;
        
        if (isPlayerFlight)
        {
            targetCoordinates = getPlayerTargetCoordinates();          
        }
        else
        {
            InterceptAiCoordinateGenerator coordinateGenerator = new InterceptAiCoordinateGenerator(campaign, squadron);
            targetCoordinates = coordinateGenerator.createTargetCoordinates();
        }
 
        return targetCoordinates;
    }
    
    private Coordinate getPlayerTargetCoordinates() throws PWCGException
    {
        InterceptPlayerCoordinateGenerator coordinateGenerator = new InterceptPlayerCoordinateGenerator(campaign, mission, squadron);
        coordinateGenerator.createTargetCoordinates();	        
        Coordinate targetCoordinates = coordinateGenerator.getTargetCoordinates();
        return targetCoordinates;
    }

    private void addOpposingFlights(InterceptFlight interceptFlight, Coordinate targetCoordinates) throws PWCGException
    {
        List<Role> opposingFlightRoles = generateOpposingFlightRole();
                
        OpposingFlightBuilder opposingFlightBuilder = new OpposingFlightBuilder(mission, squadron, targetCoordinates, opposingFlightRoles);
        List<InterceptOpposingFlight> interceptOpposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (Flight interceptOpposingFlight : interceptOpposingFlights)
        {
            interceptFlight.addLinkedUnit(interceptOpposingFlight);
        }
    }


    protected List<Role> generateOpposingFlightRole()
    {
        List<Role> opposingFlightRoles = new ArrayList<>();
        opposingFlightRoles.add(Role.ROLE_BOMB);
        opposingFlightRoles.add(Role.ROLE_ATTACK);
        opposingFlightRoles.add(Role.ROLE_DIVE_BOMB);
        return opposingFlightRoles;
    }

    private InterceptFlight createInterceptFlight(Campaign campaign, Coordinate targetCoordinates) throws PWCGException
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
        
        FlightInformation flightInformation = createFlightInformation(targetCoordinates);
		InterceptFlight interceptFlight = new InterceptFlight (flightInformation, missionBeginUnit);

		interceptFlight.createUnitMission();
        return interceptFlight;
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
