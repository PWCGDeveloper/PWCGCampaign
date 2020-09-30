package pwcg.mission.flight.escort;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class VirtualEscortFlightInformationBuilder
{

    public static IFlightInformation buildVirtualEscortFlightInformation(Mission mission, Squadron friendlyFighterSquadron) throws PWCGException
    {
        Coordinate positionDoesntMatter = new Coordinate(0, 0, 0);
        
        FlightInformation virtualEscortFlightInformation = new FlightInformation(mission);
        virtualEscortFlightInformation.setFlightType(FlightTypes.ESCORT);
        virtualEscortFlightInformation.setMission(mission);
        virtualEscortFlightInformation.setCampaign(mission.getCampaign());
        virtualEscortFlightInformation.setSquadron(friendlyFighterSquadron);
        virtualEscortFlightInformation.setPlayerFlight(false);
        virtualEscortFlightInformation.setEscortForPlayerFlight(false);
        virtualEscortFlightInformation.setEscortedByPlayerFlight(false);
        virtualEscortFlightInformation.setTargetSearchStartLocation(positionDoesntMatter);
        FlightPlaneBuilder.buildPlanes (virtualEscortFlightInformation);

        return virtualEscortFlightInformation;
    }
}
