package pwcg.mission.flight.escort;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.plane.FlightPlaneBuilder;
import pwcg.mission.target.TargetDefinition;

public class EscortedByPlayerFlightInformationBuilder
{

    public static FlightInformation buildEscortedByPlayerFlightInformation(FlightInformation playerEscortFlightInformation, TargetDefinition playerEscortTargetDefinition, Squadron friendlyBomberSquadron) throws PWCGException
    {
        FlightInformation escortedFlightInformation = new FlightInformation(playerEscortFlightInformation.getMission(), NecessaryFlightType.PLAYER_ESCORTED);
        escortedFlightInformation.setFlightType(FlightTypes.BOMB);
        escortedFlightInformation.setCampaign(playerEscortFlightInformation.getCampaign());
        escortedFlightInformation.setSquadron(friendlyBomberSquadron);
        escortedFlightInformation.setTargetSearchStartLocation(playerEscortTargetDefinition.getPosition());
        FlightPlaneBuilder.buildPlanes (escortedFlightInformation);
        escortedFlightInformation.setAltitude(playerEscortFlightInformation.getAltitude() - 500);


        return escortedFlightInformation;
    }
}
