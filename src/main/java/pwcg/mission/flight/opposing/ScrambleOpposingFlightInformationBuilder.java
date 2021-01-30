package pwcg.mission.flight.opposing;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class ScrambleOpposingFlightInformationBuilder
{
    public static FlightInformation buildAiScrambleOpposingFlightInformation(Mission mission, Squadron opposingSquadron, Squadron playerSquadron, FlightTypes opposingFlightType) 
            throws PWCGException
    {
        FlightInformation scrambleOpposingFlightInformation = new FlightInformation(mission);
        scrambleOpposingFlightInformation.setFlightType(opposingFlightType);
        scrambleOpposingFlightInformation.setMission(mission);
        scrambleOpposingFlightInformation.setCampaign(mission.getCampaign());
        scrambleOpposingFlightInformation.setSquadron(opposingSquadron);
        scrambleOpposingFlightInformation.setPlayerFlight(false);
        scrambleOpposingFlightInformation.setEscortForPlayerFlight(false);
        scrambleOpposingFlightInformation.setEscortedByPlayerFlight(false);
        scrambleOpposingFlightInformation.setTargetSearchStartLocation(playerSquadron.determineCurrentPosition(mission.getCampaign().getDate()));
        FlightPlaneBuilder.buildPlanes (scrambleOpposingFlightInformation);
        scrambleOpposingFlightInformation.calculateAltitude();

        return scrambleOpposingFlightInformation;
    }
}
