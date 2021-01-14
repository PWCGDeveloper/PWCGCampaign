package pwcg.mission.flight.escort;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class EscortForPlayerFlightInformationBuilder
{

    public static FlightInformation buildEscortForPlayerFlightInformation(
            FlightInformation playerFlightInformation, 
            Squadron friendlyFighterSquadron,
            Coordinate rendezvous) throws PWCGException
    {
        FlightInformation escortFlightInformation = new FlightInformation(playerFlightInformation.getMission());
        escortFlightInformation.setFlightType(FlightTypes.ESCORT);
        escortFlightInformation.setMission(playerFlightInformation.getMission());
        escortFlightInformation.setCampaign(playerFlightInformation.getCampaign());
        escortFlightInformation.setSquadron(friendlyFighterSquadron);
        escortFlightInformation.setPlayerFlight(false);
        escortFlightInformation.setEscortForPlayerFlight(true);
        escortFlightInformation.setEscortedByPlayerFlight(false);
        escortFlightInformation.setTargetSearchStartLocation(rendezvous.copy());
        
        FlightPlaneBuilder.buildPlanes (escortFlightInformation);
        escortFlightInformation.calculateAltitude();
        
        return escortFlightInformation;
    }

}
