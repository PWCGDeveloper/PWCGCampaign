package pwcg.aar.inmission.phase2.logeval.missionresultentity;

public class LogTurret extends LogAIEntity
{
    private LogAIEntity parent;

    public LogTurret()
    {
    }

    public LogAIEntity getParent() {
        return parent;
    }

    public void setParent(LogAIEntity parent) {
        this.parent = parent;
    }
}
