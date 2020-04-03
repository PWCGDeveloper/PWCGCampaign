package pwcg.mission.flight.escort;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.FlightPlaneBuilder;
import pwcg.mission.target.TargetDefinitionBuilderFactory;

public class EscortedByPlayerFlightInformationBuilder
{

    public static IFlightInformation buildEscortedByPlayerFlightInformation(IFlightInformation escortFlightInformation, Squadron friendlyBomberSquadron) throws PWCGException
    {
        FlightInformation escortedFlightInformation = new FlightInformation(escortFlightInformation.getMission());
        escortedFlightInformation.setFlightType(FlightTypes.BOMB);
        escortedFlightInformation.setMission(escortFlightInformation.getMission());
        escortedFlightInformation.setCampaign(escortFlightInformation.getCampaign());
        escortedFlightInformation.setSquadron(friendlyBomberSquadron);
        escortedFlightInformation.setPlayerFlight(false);
        escortedFlightInformation.setEscortForPlayerFlight(false);
        escortedFlightInformation.setEscortedByPlayerFlight(true);
        escortedFlightInformation.setTargetSearchStartLocation(escortFlightInformation.getTargetPosition());
        TargetDefinitionBuilderFactory.buildTargetDefinition (escortedFlightInformation);
        FlightPlaneBuilder.buildPlanes (escortedFlightInformation);
        escortedFlightInformation.setAltitude(escortFlightInformation.getAltitude() - 500);


        return escortedFlightInformation;
    }
}
