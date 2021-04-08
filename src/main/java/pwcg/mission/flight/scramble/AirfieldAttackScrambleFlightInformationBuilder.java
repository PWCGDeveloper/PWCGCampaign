package pwcg.mission.flight.scramble;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class AirfieldAttackScrambleFlightInformationBuilder
{
    public static FlightInformation buildAiGroundAttackOpposingFlightInformation(Mission mission, Squadron opposingSquadron, Coordinate position) 
            throws PWCGException
    {
        FlightInformation groundAttackOpposingFlightInformation = new FlightInformation(mission);
        groundAttackOpposingFlightInformation.setFlightType(FlightTypes.SCRAMBLE);
        groundAttackOpposingFlightInformation.setMission(mission);
        groundAttackOpposingFlightInformation.setCampaign(mission.getCampaign());
        groundAttackOpposingFlightInformation.setSquadron(opposingSquadron);
        groundAttackOpposingFlightInformation.setPlayerFlight(false);
        groundAttackOpposingFlightInformation.setEscortForPlayerFlight(false);
        groundAttackOpposingFlightInformation.setEscortedByPlayerFlight(false);
        groundAttackOpposingFlightInformation.setScrambleOpposeFlight(false);
        groundAttackOpposingFlightInformation.setAiTriggeredTakeoff(true);
        groundAttackOpposingFlightInformation.setTargetSearchStartLocation(position);
        groundAttackOpposingFlightInformation.setAltitude(0);
        FlightPlaneBuilder.buildPlanes (groundAttackOpposingFlightInformation);

        return groundAttackOpposingFlightInformation;
    }
}
