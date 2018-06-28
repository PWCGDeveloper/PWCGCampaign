package pwcg.aar.inmission.phase2.logeval.victory;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.UnknownVictoryAssignments;

public class AARRandomAssignment 
{
    private AARLogEventData logEventData;
    private AARRandomAssignmentCalculator randomAssignmentCalculator;
    
    public AARRandomAssignment(
                    AARLogEventData logEventData,
                    AARRandomAssignmentCalculator randomAssignmentCalculator)
    {
        this.logEventData = logEventData;
        this.randomAssignmentCalculator = randomAssignmentCalculator;
    }

    public LogUnknown markForRandomAssignment(LogVictory victoryResult)
    {
        if (randomAssignmentCalculator.shouldBeMarkedForRandomAssignment(logEventData.getChronologicalATypes(), victoryResult.getVictim().getId()))
        {
            return createRandomAssignmentEntity(victoryResult);
        }
        
        return null;
    }


    private LogUnknown createRandomAssignmentEntity(LogVictory victoryResult)
    {
        
        LogUnknown markedForAssignment = new LogUnknown();
        markedForAssignment.setUnknownVictoryAssignment(UnknownVictoryAssignments.RANDOM_ASSIGNMENT);
        return markedForAssignment;
    }
}

