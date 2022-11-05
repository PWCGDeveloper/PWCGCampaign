package pwcg.mission.mcu.effect;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.McuMissionStart;
import pwcg.mission.mcu.McuTimer;

public class FirePotSeries extends Effect
{
    private McuMissionStart missionBegin = null;
    private List<FirePotPair> firePotPairs = new ArrayList<>();
    private McuTimer fireTriggerTimer = new McuTimer();;

    public static final int NUM_FIRE_POT_PAIRS = 5;

    public FirePotSeries()
    {
    }

    public void createSeries(FrontMapIdentifier mapIdentifier, Coordinate startPosition, double orientationDownRunway, double distanceBetween) throws PWCGException
    {
        missionBegin = new McuMissionStart();
        missionBegin.setPosition(startPosition.copy());
        missionBegin.setMissionBeginTarget(fireTriggerTimer.getIndex());
        
        fireTriggerTimer.setTime(1);
        fireTriggerTimer.setPosition(startPosition.copy());

        makeFirePotPairs(mapIdentifier, startPosition, orientationDownRunway, distanceBetween);
    }

    private void makeFirePotPairs(FrontMapIdentifier mapIdentifier, Coordinate startPosition, double orientationDownRunway, double distanceBetween) throws PWCGException
    {        
        for (int i = 0; i < NUM_FIRE_POT_PAIRS; ++i)
        {
            double metersAhead = (i + 1) * distanceBetween;
            
            Coordinate firePotPosition = MathUtils.calcNextCoord(mapIdentifier, startPosition, orientationDownRunway, metersAhead);
            firePotPosition.setYPos(0.0);

            double orientationAcrossRunway = MathUtils.adjustAngle(orientationDownRunway, 90);
            McuTimer pairTriggerSource = getSourceTimer();
            FirePotPair firePotPair  = new FirePotPair(pairTriggerSource);
            firePotPair.createSeries(mapIdentifier, firePotPosition, orientationAcrossRunway, 120.0);
            firePotPairs.add(firePotPair);
        }
    }

    private McuTimer getSourceTimer()
    {
        McuTimer pairTriggerSource = null;
        if (firePotPairs.size() == 0)
        {
            pairTriggerSource = fireTriggerTimer;
        }
        else
        {
            int index = firePotPairs.size() - 1;
            pairTriggerSource = firePotPairs.get(index).getFirePotTriggerTimer();
        }
        return pairTriggerSource;
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        missionBegin.write(writer);
        fireTriggerTimer.write(writer);
        for (FirePotPair firePotPair : firePotPairs)
        {
            firePotPair.write(writer);
        }
    }
}
