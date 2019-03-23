package pwcg.mission.mcu;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;

public enum Coalition
{
    COALITION_ALLIED(1),
    COALITION_AXIS(2);

    private int coalitionValue;

    Coalition(int coalitionValue) 
    {
        this.coalitionValue = coalitionValue;
    }

    public static Coalition getCoalitionBySide(Side side)
    {
        Coalition coalition = COALITION_AXIS;
        if (side == Side.ALLIED)
        {
        	coalition = COALITION_ALLIED;
        }
        
        return coalition;
    }    

    public static Coalition getFriendlyCoalition(ICountry country)
    {
        Coalition friendlyCoalition = COALITION_AXIS;
        if (country.getSide() == Side.ALLIED)
        {
            friendlyCoalition = COALITION_ALLIED;
        }
        
        return friendlyCoalition;
    }    

    public static List<Coalition> getAllCoalitions()
    {
        List<Coalition> allCoalitions = new ArrayList<>();
        allCoalitions.add(COALITION_AXIS);
        allCoalitions.add(COALITION_ALLIED);
        return allCoalitions;
    }    

    public static Coalition getEnemyCoalition(ICountry country)
    {
        Coalition enemyCoalition = COALITION_AXIS;
        if (country.getSide() == Side.AXIS)
        {
            enemyCoalition = COALITION_ALLIED;
        }
        
        return enemyCoalition;
    }    

    public int getCoalitionValue() 
    {
        return coalitionValue;
    }
}
