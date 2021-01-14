package pwcg.mission.flight.scramble;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class ScrambleOpposingFlightInformationBuilder
{

    public static FlightInformation buildAiScrambleOpposingFlightInformation(Squadron opposingSquadron, FlightInformation playerFlightInformation, FlightTypes opposingFlightType) throws PWCGException
    {
        FlightInformation scrambleOpposingFlightInformation = new FlightInformation(playerFlightInformation.getMission());
        scrambleOpposingFlightInformation.setFlightType(opposingFlightType);
        scrambleOpposingFlightInformation.setMission(playerFlightInformation.getMission());
        scrambleOpposingFlightInformation.setCampaign(playerFlightInformation.getCampaign());
        scrambleOpposingFlightInformation.setSquadron(opposingSquadron);
        scrambleOpposingFlightInformation.setPlayerFlight(false);
        scrambleOpposingFlightInformation.setEscortForPlayerFlight(false);
        scrambleOpposingFlightInformation.setEscortedByPlayerFlight(false);
        scrambleOpposingFlightInformation.setTargetSearchStartLocation(playerFlightInformation.getFlightHomePosition());
        FlightPlaneBuilder.buildPlanes (scrambleOpposingFlightInformation);
        scrambleOpposingFlightInformation.calculateAltitude();

        return scrambleOpposingFlightInformation;
    }
}
