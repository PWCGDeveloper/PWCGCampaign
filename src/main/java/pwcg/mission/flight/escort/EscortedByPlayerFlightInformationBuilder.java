package pwcg.mission.flight.escort;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.FlightPlaneBuilder;
import pwcg.mission.target.TargetDefinition;

public class EscortedByPlayerFlightInformationBuilder
{

    public static IFlightInformation buildEscortedByPlayerFlightInformation(IFlightInformation playerEscortFlightInformation, TargetDefinition playerEscortTargetDefinition, Squadron friendlyBomberSquadron) throws PWCGException
    {
        FlightInformation escortedFlightInformation = new FlightInformation(playerEscortFlightInformation.getMission());
        escortedFlightInformation.setFlightType(FlightTypes.BOMB);
        escortedFlightInformation.setMission(playerEscortFlightInformation.getMission());
        escortedFlightInformation.setCampaign(playerEscortFlightInformation.getCampaign());
        escortedFlightInformation.setSquadron(friendlyBomberSquadron);
        escortedFlightInformation.setPlayerFlight(false);
        escortedFlightInformation.setEscortForPlayerFlight(false);
        escortedFlightInformation.setEscortedByPlayerFlight(true);
        escortedFlightInformation.setTargetSearchStartLocation(playerEscortTargetDefinition.getPosition());
        FlightPlaneBuilder.buildPlanes (escortedFlightInformation);
        escortedFlightInformation.setAltitude(playerEscortFlightInformation.getAltitude() - 500);


        return escortedFlightInformation;
    }
}
