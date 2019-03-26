package pwcg.mission.flight.patrol;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.GeneralTargetLocationGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;

public class PatrolPackage extends FlightPackage
{
    public PatrolPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.PATROL;
    }

    public Flight createPackage () throws PWCGException 
    {
        Coordinate targetGeneralLocation = GeneralTargetLocationGenerator.createTargetGeneralLocation(campaign, mission, squadron);
		Coordinate targetWaypoint = getTargetWaypoint(
		                mission, 
		                targetGeneralLocation, 
		                squadron.determineSquadronCountry(campaign.getDate()));
		
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());        
        FlightInformation flightInformation = createFlightInformation(targetWaypoint);
		PatrolFlight patrol = new PatrolFlight (flightInformation, missionBeginUnit);
		patrol.createUnitMission();
		
		return patrol;
	}

	protected Coordinate getTargetWaypoint(Mission mission, Coordinate referenceCoordinate, ICountry country) 
	                throws PWCGException 
	{
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        double radius = productSpecific.getInitialTargetRadiusFromGeneralTargetLocation(FlightTypes.PATROL);
        Side friendlySide = country.getSide();
        FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        FrontLinePoint targetFrontLinePoint = frontLinesForMap.findCloseFrontPositionForSide(referenceCoordinate, radius, friendlySide);

		return targetFrontLinePoint.getPosition();
	}

}
