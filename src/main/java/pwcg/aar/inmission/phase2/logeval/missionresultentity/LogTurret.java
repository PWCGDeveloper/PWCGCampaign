package pwcg.aar.inmission.phase2.logeval.missionresultentity;

public class LogTurret extends LogAIEntity
{
    private LogAIEntity parent;

    public LogTurret(int sequenceNumber)
    {
        super(sequenceNumber);
    }

    public LogAIEntity getParent() {
        return parent;
    }

    public void setParent(LogAIEntity parent) {
        this.parent = parent;
    }
}
