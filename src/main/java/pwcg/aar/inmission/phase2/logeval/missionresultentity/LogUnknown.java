package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.UnknownVictoryAssignments;
import pwcg.core.logfiles.LogParser;

public class LogUnknown extends LogAIEntity
{
    private UnknownVictoryAssignments unknownVictoryAssignment = UnknownVictoryAssignments.UNKNOWN_ASSIGNMENT;
    private static final int unknownSequenceNumber = -1;
    
    public LogUnknown()
    {
        super(unknownSequenceNumber);
        this.setId(LogParser.UNKNOWN_MISSION_LOG_ENTITY);
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
