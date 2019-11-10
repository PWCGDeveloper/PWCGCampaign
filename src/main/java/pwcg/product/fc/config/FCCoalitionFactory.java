package pwcg.product.fc.config;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.ICoalitionFactory;

public class FCCoalitionFactory implements ICoalitionFactory
{
    public Coalition getCoalitionBySide(Side side)
    {
        Coalition coalition = Coalition.COALITION_CENTRAL;
        if (side == Side.ALLIED)
        {
            coalition = Coalition.COALITION_ENTENTE;
        }
        
        return coalition;
    }    

    public Coalition getFriendlyCoalition(ICountry country)
    {
        Coalition friendlyCoalition = Coalition.COALITION_CENTRAL;
        if (country.getSide() == Side.ALLIED)
        {
            friendlyCoalition = Coalition.COALITION_ENTENTE;
        }
        
        return friendlyCoalition;
    }    

    public List<Coalition> getAllCoalitions()
    {
        List<Coalition> allCoalitions = new ArrayList<>();
        allCoalitions.add(Coalition.COALITION_CENTRAL);
        allCoalitions.add(Coalition.COALITION_ENTENTE);
        return allCoalitions;
    }    

    public Coalition getEnemyCoalition(ICountry country)
    {
        Coalition enemyCoalition = Coalition.COALITION_CENTRAL;
        if (country.getSide() == Side.AXIS)
        {
            enemyCoalition = Coalition.COALITION_ENTENTE;
        }
        
        return enemyCoalition;
    }    
}
