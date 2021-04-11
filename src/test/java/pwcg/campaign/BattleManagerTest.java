package pwcg.campaign;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.battle.Battle;
import pwcg.campaign.battle.BattleManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class BattleManagerTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
    }
    
    @Test
    public void getBattleTest () throws PWCGException
    {   
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);

    	BattleManager battleManager = PWCGContext.getInstance().getCurrentMap().getBattleManager();
    	
    	Battle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.ARRAS_MAP, new Coordinate(80000, 0, 80000), DateUtils.getDateYYYYMMDD("19171121"));
        assert (battle.getName().equals("Cambrai"));
        assert (battle.getAggressorcountry() == Country.BRITAIN);
        assert (battle.getDefendercountry() == Country.GERMANY);
    }

    @Test
    public void getBattleTestDateWrong () throws PWCGException
    {        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);
        BattleManager battleManager = PWCGContext.getInstance().getCurrentMap().getBattleManager();
    	
    	Battle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.ARRAS_MAP, new Coordinate(45000, 0, 220000), DateUtils.getDateYYYYMMDD("19180501"));
        assert (battle == null);
    }

    @Test
    public void getBattleTestCoordinatesWrong () throws PWCGException
    {        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);
        BattleManager battleManager = PWCGContext.getInstance().getCurrentMap().getBattleManager();
    	
    	Battle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.ARRAS_MAP, new Coordinate(45000, 0, 20000), DateUtils.getDateYYYYMMDD("19180501"));
        assert (battle == null);
    }    

    @Test
    public void getBattleTestMapWrong () throws PWCGException
    {        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);
        BattleManager battleManager = PWCGContext.getInstance().getCurrentMap().getBattleManager();
    	
    	Battle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.ARRAS_MAP, new Coordinate(45000, 0, 220000), DateUtils.getDateYYYYMMDD("19180501"));
        assert (battle == null);
    }    

}
