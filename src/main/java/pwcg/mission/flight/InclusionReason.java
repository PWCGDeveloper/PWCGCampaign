package pwcg.mission.flight;

import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

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
        Logger.log(LogLevel.DEBUG, "Inclusion   : ");
        Logger.log(LogLevel.DEBUG, "       Reason: " + reason);
        Logger.log(LogLevel.DEBUG, "       player contact: " + firstContactWithPlayer);
        Logger.log(LogLevel.DEBUG, "       enemy contact: " + firstContactWithEnemy);
    }

}
