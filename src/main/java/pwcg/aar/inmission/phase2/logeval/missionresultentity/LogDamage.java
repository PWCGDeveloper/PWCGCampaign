package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.core.location.Coordinate;

public class LogDamage extends LogBase
{
    private LogAIEntity victor = new LogUnknown();
    private LogAIEntity victim = new LogUnknown();
    protected Coordinate location;

    public LogDamage(int sequenceNumber)
    {
        super(sequenceNumber);
    }

    public LogAIEntity getVictor()
    {
        return victor;
    }

    public void setVictor(LogAIEntity victor)
    {
        this.victor = victor;
    }

    public LogAIEntity getVictim()
    {
        return victim;
    }

    public void setVictim(LogAIEntity victim)
    {
        this.victim = victim;
    }

    public Coordinate getLocation()
    {
        return location;
    }

    public void setLocation(Coordinate location)
    {
        this.location = location;
    }
}
