package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.core.location.Coordinate;

public class LogDamage extends LogBase
{
    private LogAIEntity victor = new LogUnknown();
    private LogAIEntity victim = new LogUnknown();
    private Coordinate location;
    private double damageLevel = 0.0;

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

    public double getDamageLevel()
    {
        return damageLevel;
    }

    public void setDamageLevel(double damageLevel)
    {
        this.damageLevel = damageLevel;
    }
}
