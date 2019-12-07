package pwcg.mission.ground.org;

import pwcg.core.utils.RandomNumberGenerator;

public class GroundUnitNumberCalculator
{
    public static int calcNumUnits(int minRequested, int maxRequested)
    {

        if (maxRequested < minRequested)
        {
            maxRequested = minRequested;
        }
        
        int numUnits = minRequested + (RandomNumberGenerator.getRandom(maxRequested - minRequested));
        return numUnits;
    }
}
