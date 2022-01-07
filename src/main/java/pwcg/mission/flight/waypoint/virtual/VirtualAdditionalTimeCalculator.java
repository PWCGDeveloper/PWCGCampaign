package pwcg.mission.flight.waypoint.virtual;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class VirtualAdditionalTimeCalculator
{
    public int addDelayForPlayerDelay(Mission mission, IFlight flightToBeDelayed) throws PWCGException
    {
        int randomAdditionalTime = RandomNumberGenerator.getRandom(300);
        int additionalTime = 120 + randomAdditionalTime;
        return additionalTime;
    }
}
