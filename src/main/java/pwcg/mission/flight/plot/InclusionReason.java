package pwcg.mission.flight.plot;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class InclusionReason
{
    private String reason = "";
    private int firstContactWithPlayer = -1;
    private int firstContactWithEnemy = -1;
    
    public String getReason()
    {
        return this.reason;
    }
    
    public void setReason(String reason)
    {
        this.reason = reason;
    }
    
    public int getFirstContactWithPlayer()
    {
        return this.firstContactWithPlayer;
    }
    
    public void setFirstContactWithPlayer(int firstContactWithPlayer)
    {
        this.firstContactWithPlayer = firstContactWithPlayer;
    }
    
    public int getFirstContactWithEnemy()
    {
        return this.firstContactWithEnemy;
    }
    
    public void setFirstContactWithEnemy(int firstContactWithEnemy)
    {
        this.firstContactWithEnemy = firstContactWithEnemy;
    }
    

    /**
     * @return
     */
    public void dump()
    {
        PWCGLogger.log(LogLevel.DEBUG, "Inclusion   : ");
        PWCGLogger.log(LogLevel.DEBUG, "       Reason: " + reason);
        PWCGLogger.log(LogLevel.DEBUG, "       player contact: " + firstContactWithPlayer);
        PWCGLogger.log(LogLevel.DEBUG, "       enemy contact: " + firstContactWithEnemy);
    }

}
