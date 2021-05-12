package pwcg.mission.flight.escort;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class VirtualEscortFlightInformationBuilder
{

    public static FlightInformation buildVirtualEscortFlightInformation(Mission mission, Squadron friendlyFighterSquadron) throws PWCGException
    {
        Coordinate positionDoesntMatter = new Coordinate(0, 0, 0);
        
        FlightInformation virtualEscortFlightInformation = new FlightInformation(mission, NecessaryFlightType.VIRTUAL_ESCORT);
        virtualEscortFlightInformation.setFlightType(FlightTypes.ESCORT);
        virtualEscortFlightInformation.setCampaign(mission.getCampaign());
        virtualEscortFlightInformation.setSquadron(friendlyFighterSquadron);
        virtualEscortFlightInformation.setTargetSearchStartLocation(positionDoesntMatter);
        FlightPlaneBuilder.buildPlanes (virtualEscortFlightInformation);

        return virtualEscortFlightInformation;
    }
}
