package pwcg.mission.flight.lonewolf;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.GeneralTargetLocationGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.patrol.PatrolPackage;

public class LoneWolfPackage extends PatrolPackage
{
    public LoneWolfPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.LONE_WOLF;
    }

    public Flight createPackage () throws PWCGException 
	{
        Coordinate targetGeneralLocation = GeneralTargetLocationGenerator.createTargetGeneralLocation(campaign, mission, squadron);
		Coordinate targetWaypoint = getTargetWaypoint(
		                mission, 
		                targetGeneralLocation, 
		                squadron.determineSquadronCountry(campaign.getDate()));
		
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
        
        FlightInformation flightInformation = createFlightInformation(targetWaypoint);
		LoneWolfFlight patrol = new LoneWolfFlight (flightInformation, missionBeginUnit);
		patrol.createUnitMission();		
		return patrol;
	}

}
