package pwcg.mission.flight.escort;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class EscortForPlayerFlightInformationBuilder
{

    public static FlightInformation buildEscortForPlayerFlightInformation(
            FlightInformation playerFlightInformation, 
            Squadron friendlyFighterSquadron,
            Coordinate rendezvous) throws PWCGException
    {
        FlightInformation escortFlightInformation = new FlightInformation(playerFlightInformation.getMission(), NecessaryFlightType.PLAYER_ESCORT);
        escortFlightInformation.setFlightType(FlightTypes.ESCORT);
        escortFlightInformation.setCampaign(playerFlightInformation.getCampaign());
        escortFlightInformation.setSquadron(friendlyFighterSquadron);
        escortFlightInformation.setTargetSearchStartLocation(rendezvous.copy());
        
        FlightPlaneBuilder.buildPlanes (escortFlightInformation);
        escortFlightInformation.calculateAltitude();
        
        return escortFlightInformation;
    }

}
