package pwcg.campaign;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.battle.Battle;
import pwcg.campaign.battle.BattleManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class BattleManagerTest
{
    @Mock Campaign campaign;
    @Mock SquadronMember referencePlayer;
    @Mock ConfigManagerCampaign configManager;

    public BattleManagerTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
    }
    
    @Test
    public void getBattleTest () throws PWCGException
    {   
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19171121"));
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.ARRAS_MAP);
        Mockito.when(campaign.getReferencePlayer()).thenReturn(referencePlayer);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey)).thenReturn(60);
        
        Mockito.when(referencePlayer.getSquadronId()).thenReturn(302056);

    	BattleManager battleManager = PWCGContext.getInstance().getMap(FrontMapIdentifier.ARRAS_MAP).getBattleManager();    	
    	Battle battle = battleManager.getBattleForCampaign(campaign, new Coordinate(80000, 0, 80000));
        Assertions.assertTrue (battle.getName().equals("Cambrai"));
        Assertions.assertTrue (battle.getAggressorcountry() == Country.BRITAIN);
        Assertions.assertTrue (battle.getDefendercountry() == Country.GERMANY);
    }

    @Test
    public void getBattleTestDateWrong () throws PWCGException
    {        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19171231"));

        BattleManager battleManager = PWCGContext.getInstance().getMap(FrontMapIdentifier.ARRAS_MAP).getBattleManager();
        Battle battle = battleManager.getBattleForCampaign(campaign, new Coordinate(80000, 0, 80000));    	
        Assertions.assertNull (battle);
    }

    @Test
    public void getBattleTestCoordinatesWrong () throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19171121"));
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.ARRAS_MAP);
        Mockito.when(campaign.getReferencePlayer()).thenReturn(referencePlayer);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey)).thenReturn(20);
        
        Mockito.when(referencePlayer.getSquadronId()).thenReturn(302056);

        BattleManager battleManager = PWCGContext.getInstance().getMap(FrontMapIdentifier.ARRAS_MAP).getBattleManager();        
        Battle battle = battleManager.getBattleForCampaign(campaign, new Coordinate(40000, 0, 40000));
        Assertions.assertNull (battle);
    }    

}
