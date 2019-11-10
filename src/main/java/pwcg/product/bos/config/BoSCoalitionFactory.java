package pwcg.product.bos.config;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.ICoalitionFactory;

public class BoSCoalitionFactory implements ICoalitionFactory
{
    public Coalition getCoalitionBySide(Side side)
    {
        Coalition coalition = Coalition.COALITION_AXIS;
        if (side == Side.ALLIED)
        {
            coalition = Coalition.COALITION_ALLIED;
        }
        
        return coalition;
    }    

    public Coalition getFriendlyCoalition(ICountry country)
    {
        Coalition friendlyCoalition = Coalition.COALITION_AXIS;
        if (country.getSide() == Side.ALLIED)
        {
            friendlyCoalition = Coalition.COALITION_ALLIED;
        }
        
        return friendlyCoalition;
    }    

    public List<Coalition> getAllCoalitions()
    {
        List<Coalition> allCoalitions = new ArrayList<>();
        allCoalitions.add(Coalition.COALITION_AXIS);
        allCoalitions.add(Coalition.COALITION_ALLIED);
        return allCoalitions;
    }    

    public Coalition getEnemyCoalition(ICountry country)
    {
        Coalition enemyCoalition = Coalition.COALITION_AXIS;
        if (country.getSide() == Side.AXIS)
        {
            enemyCoalition = Coalition.COALITION_ALLIED;
        }
        
        return enemyCoalition;
    }    
}
