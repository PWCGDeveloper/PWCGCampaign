package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.aar.inmission.phase2.logeval.AARDamageStatus;
import pwcg.core.location.Coordinate;

public class LogVictory extends LogBase
{
    private LogAIEntity victor = new LogUnknown();
    private LogAIEntity victim = new LogUnknown();
    private AARDamageStatus damageInformation;
    private Coordinate location;
    private boolean confirmed = false;
    
    public LogVictory(int sequenceNumber)
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

    public boolean isConfirmed()
    {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed)
    {
        this.confirmed = confirmed;
    }

    public boolean didCrewMemberDamagePlane(String victorId)
    {
        if(damageInformation == null)
        {
            return false;
        }
        return damageInformation.didCrewMemberDamagePlane(victorId);
    }

    public void setDamageInformation(AARDamageStatus damageInformation)
    {
        this.damageInformation = damageInformation;
    }
}
