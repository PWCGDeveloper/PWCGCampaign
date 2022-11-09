package pwcg.mission.utils;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetType;

public class MissionInformationUtils
{

    public static boolean verifyFlightTargets(Mission mission, TargetType expectedTargetType, Side side) throws PWCGException
    {
        boolean bombFlightTargetFound = MissionInformationUtils.verifyFlightTypeTargets(mission, FlightTypes.BOMB, expectedTargetType, Side.AXIS);
        boolean diveBombFlightTargetFound = MissionInformationUtils.verifyFlightTypeTargets(mission, FlightTypes.DIVE_BOMB, expectedTargetType, Side.AXIS);
        boolean groundAttackFlightTargetFound = MissionInformationUtils.verifyFlightTypeTargets(mission, FlightTypes.GROUND_ATTACK, expectedTargetType, Side.AXIS);
        boolean isTargetTypeTargeted = (bombFlightTargetFound || diveBombFlightTargetFound || groundAttackFlightTargetFound);

        return isTargetTypeTargeted;
    }

    private static boolean verifyFlightTypeTargets(Mission mission, FlightTypes expectedFlightType, TargetType expectedTargetType, Side side) throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAiFlightsForSide(side))
        {
            if (flight.getFlightType() == expectedFlightType)
            {
                if (flight.getTargetDefinition().getTargetType() != expectedTargetType)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean verifyFlightTypeInMission(Mission mission, FlightTypes flightType, Side side) throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getFlightsForSide(side))
        {
            if (flight.getFlightInformation().getFlightType() == flightType)
            {
                return true;
            }
        }
        return false;
    }

}
