package pwcg.mission.flight;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class FlightInformationFactory
{
    public static FlightInformation buildFlightInformation(Mission mission, ICountry country, FlightTypes flightType, PlaneType planeType) throws PWCGException
    {
        FlightInformation aFlightInformation = new FlightInformation(mission);
        aFlightInformation.setFlightType(flightType);
        aFlightInformation.setCampaign(mission.getCampaign());
        aFlightInformation.setTargetSearchStartLocation(mission.getMissionBorders().getCenter());
        FlightPlaneBuilder.buildPlanes (aFlightInformation);
        aFlightInformation.calculateAltitude();
        
        return aFlightInformation;
    }
}
