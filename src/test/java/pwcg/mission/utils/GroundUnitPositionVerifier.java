package pwcg.mission.utils;

import org.junit.jupiter.api.Assertions;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitPositionDuplicateDetector;
import pwcg.mission.ground.org.GroundUnitCollection;

public class GroundUnitPositionVerifier
{
    public static void verifyGroundUnitPositionsAndAssert (Mission mission) throws PWCGException
    {
        verifyNoDUplicatePositions(mission);
        verifyProperBattlePlacement(mission);
    }

    private static void verifyNoDUplicatePositions(Mission mission) throws PWCGException
    {
        GroundUnitPositionDuplicateDetector duplicateDetector = new GroundUnitPositionDuplicateDetector();
        boolean noDuplicates = duplicateDetector.verifyMissionGroundUnitPositionsNotDuplicated(
                mission.getGroundUnitBuilder().getAllMissionGroundUnits(), mission.getGroundUnitBuilder().getAllMissionGroundUnits());
        Assertions.assertTrue (noDuplicates);
    }
    

    private static void verifyProperBattlePlacement(Mission mission) throws PWCGException
    {
        if (!mission.getGroundUnitBuilder().getAssaults().isEmpty())
        {
            boolean oneAssaultIsInBox = false;
            for (GroundUnitCollection assault : mission.getGroundUnitBuilder().getAssaults())
            {
                if (mission.getMissionBorders().isInBox(assault.getPosition()))
                {
                    oneAssaultIsInBox = true;
                }
            }
            assert(oneAssaultIsInBox);
        }
    }
}
