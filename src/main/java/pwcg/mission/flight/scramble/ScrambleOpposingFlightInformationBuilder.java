package pwcg.mission.flight.scramble;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.FlightPlaneBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class ScrambleOpposingFlightInformationBuilder
{

    public static IFlightInformation buildAiScrambleOpposingFlightInformation(Squadron opposingSquadron, IFlightInformation playerFlightInformation, FlightTypes opposingFlightType) throws PWCGException
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
        buildSpecificTargetDefinition (scrambleOpposingFlightInformation, opposingFlightType);

        return scrambleOpposingFlightInformation;
    }

    public static void buildSpecificTargetDefinition (FlightInformation scrambleOpposingFlightInformation, FlightTypes opposingFlightType) throws PWCGException
    {
        if (FlightTypes.isBombingFlight(opposingFlightType))
        {
            TargetDefinition targetDefinition = ScrambleOpposeTargetDefinitionBuilder.buildScrambleOpposeTargetDefinitionAirToGround(scrambleOpposingFlightInformation, TargetType.TARGET_AIRFIELD);
            scrambleOpposingFlightInformation.setTargetDefinition(targetDefinition);
        }
        else
        {
            TargetDefinition targetDefinition = ScrambleOpposeTargetDefinitionBuilder.buildScrambleOpposeTargetDefinitionAirToAir(scrambleOpposingFlightInformation, TargetType.TARGET_AIRFIELD);
            scrambleOpposingFlightInformation.setTargetDefinition(targetDefinition);
         }
    }
}
