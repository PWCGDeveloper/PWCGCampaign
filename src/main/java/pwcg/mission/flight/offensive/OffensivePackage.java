package pwcg.mission.flight.offensive;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.GeneralTargetLocationGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.waypoint.TargetLocationFinder;

public class OffensivePackage extends FlightPackage
{
    private static double OFFENSIVE_RADIUS = 150000.0;

    public OffensivePackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.OFFENSIVE;
    }

    public Flight createPackage () throws PWCGException 
	{
        Coordinate targetWaypoint = getOffensivePatrolCoordinate();
        OffensiveFlight offensive = createFlight(targetWaypoint);
		return offensive;
	}

    private Coordinate getOffensivePatrolCoordinate() throws PWCGException
    {
        Coordinate targetGeneralLocation = GeneralTargetLocationGenerator.createTargetGeneralLocation(campaign, mission, squadron);        
        Side enemySide = squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide();
        Coordinate targetWaypoint = getTargetWaypoint(targetGeneralLocation, enemySide);
        return targetWaypoint;
    }

    private OffensiveFlight createFlight(Coordinate targetWaypoint) throws PWCGException
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
		OffensiveFlight offensive = new OffensiveFlight ();
		offensive.initialize(mission, campaign, targetWaypoint, squadron, missionBeginUnit, isPlayerFlight);
		offensive.createUnitMission();
        return offensive;
    }

	protected Coordinate getTargetWaypoint(Coordinate referenceCoordinate, Side targetSide) throws PWCGException 
	{
        Side enemySide = squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide();
        TargetLocationFinder targetLocationFinder = new TargetLocationFinder(campaign, enemySide, referenceCoordinate, OFFENSIVE_RADIUS);
        Coordinate pickupLocation = targetLocationFinder.createTargetCoordinates();
		return pickupLocation;
	}

}
