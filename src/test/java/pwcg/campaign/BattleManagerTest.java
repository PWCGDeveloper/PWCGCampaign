package pwcg.campaign;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class BattleManagerTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }
    
    @Test
    public void getBattleTest () throws PWCGException
    {   
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);

    	BattleManager battleManager = PWCGContextManager.getInstance().getBattleManager();
    	
    	Battle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.FRANCE_MAP, new Coordinate(45000, 0, 220000), DateUtils.getDateYYYYMMDD("19160501"));
        assert (battle.getName().equals("Verdun"));
        assert (battle.getAggressorcountry() == Country.GERMANY);
        assert (battle.getDefendercountry() == Country.FRANCE);
    }

    @Test
    public void getBattleTestDateWrong () throws PWCGException
    {        
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);
        BattleManager battleManager = PWCGContextManager.getInstance().getBattleManager();
    	
    	Battle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.FRANCE_MAP, new Coordinate(45000, 0, 220000), DateUtils.getDateYYYYMMDD("19150501"));
        assert (battle == null);
    }

    @Test
    public void getBattleTestCoordinatesWrong () throws PWCGException
    {        
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);
        BattleManager battleManager = PWCGContextManager.getInstance().getBattleManager();
    	
    	Battle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.FRANCE_MAP, new Coordinate(45000, 0, 20000), DateUtils.getDateYYYYMMDD("19160501"));
        assert (battle == null);
    }    

    @Test
    public void getBattleTestMapWrong () throws PWCGException
    {        
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.CHANNEL_MAP);
        BattleManager battleManager = PWCGContextManager.getInstance().getBattleManager();
    	
    	Battle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.CHANNEL_MAP, new Coordinate(45000, 0, 220000), DateUtils.getDateYYYYMMDD("19160501"));
        assert (battle == null);
    }    

}
