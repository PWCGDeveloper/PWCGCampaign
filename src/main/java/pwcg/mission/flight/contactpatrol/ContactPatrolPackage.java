package pwcg.mission.flight.contactpatrol;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;

public class ContactPatrolPackage extends FlightPackage
{
    public ContactPatrolPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.CONTACT_PATROL;
    }

    public Flight createPackage () throws PWCGException 
	{
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        ContactPatrolFlight contactPatrol = createFlight(groundUnitCollection);
		return contactPatrol;
	}

    private ContactPatrolFlight createFlight(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide());
        
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
                
        ContactPatrolFlight contactPatrol = new ContactPatrolFlight ();
 		contactPatrol.initialize(mission, campaign, targetCoordinates, squadron, missionBeginUnit, isPlayerFlight);
        contactPatrol.createUnitMission();
        contactPatrol.linkGroundUnitsToFlight(groundUnitCollection);
        return contactPatrol;
    }	
}
