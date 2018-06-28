package pwcg.mission.flight.transport;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;

public class TransportPackage extends FlightPackage
{
    public TransportPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.TRANSPORT;
    }

    @Override
    public Flight createPackage() throws PWCGException
	{
        TransportFlight transportFlight = makeTransportFlight();
        return transportFlight;
	}
    
    private TransportFlight makeTransportFlight() throws PWCGException
    {
        TransportReferenceLocationSelector transportReferenceLocationSelector = new TransportReferenceLocationSelector(campaign, squadron);
        Coordinate targetCoordinates = transportReferenceLocationSelector.getTargetCoordinate();
        
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
            
        TransportFlight transportFlight = new TransportFlight ();
        transportFlight.initialize(mission, campaign, FlightTypes.TRANSPORT, targetCoordinates, squadron, missionBeginUnit, isPlayerFlight);
        transportFlight.createUnitMission();

        return transportFlight;
    }
}
