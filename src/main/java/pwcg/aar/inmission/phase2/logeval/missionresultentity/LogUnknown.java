package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase3.reconcile.victories.UnknownVictoryAssignments;

public class LogUnknown extends LogAIEntity
{
    UnknownVictoryAssignments unknownVictoryAssignment = UnknownVictoryAssignments.UNKNOWN_ASSIGNMENT;
    
    public LogUnknown()
    {
        this.setId(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY);
    }

    public UnknownVictoryAssignments getUnknownVictoryAssignment()
    {
        return unknownVictoryAssignment;
    }

    public void setUnknownVictoryAssignment(UnknownVictoryAssignments unknownVictoryAssignment)
    {
        this.unknownVictoryAssignment = unknownVictoryAssignment;
    }
}
