package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.awards.PromotionEventHandler;
import pwcg.aar.awards.PromotionEventHandlerRecon;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class PromotionEventHandlerReconTest
{
    private Campaign campaign;
    
    @Mock
    private SquadronMember squadronMember;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        campaign = CampaignCache.makeCampaignForceCreation(SquadrontTestProfile.ESC_103_PROFILE);
    }

    @Test
    public void promoteCorporalToSergent () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerRecon.PilotRankMedMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Corporal");

        PromotionEventHandlerRecon promotionEventHandlerFighter = new PromotionEventHandlerRecon();
        String promotion = promotionEventHandlerFighter.determineReconPromotion(campaign, squadronMember);

        assert (promotion.equals("Sergent"));
    }

    @Test
    public void promoteSergent () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerRecon.PilotRankHighMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Sergent");

        PromotionEventHandlerRecon promotionEventHandlerFighter = new PromotionEventHandlerRecon();
        String promotion = promotionEventHandlerFighter.determineReconPromotion(campaign, squadronMember);

        assert (promotion.equals("Sous Lieutenant"));
    }

    @Test
    public void promoteSousLieutenant () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerRecon.PilotRankExecMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Sous Lieutenant");

        PromotionEventHandlerRecon promotionEventHandlerFighter = new PromotionEventHandlerRecon();
        String promotion = promotionEventHandlerFighter.determineReconPromotion(campaign, squadronMember);

        assert (promotion.equals("Lieutenant"));
    }

    @Test
    public void promoteCapitaine () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerRecon.PilotRankCommandMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);

        PromotionEventHandlerRecon promotionEventHandlerFighter = new PromotionEventHandlerRecon();
        String promotion = promotionEventHandlerFighter.determineReconPromotion(campaign, squadronMember);

        assert (promotion.equals("Capitaine"));
    }

    @Test
    public void promoteCapitaineFailNotPlayer () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerRecon.PilotRankCommandMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(false);

        PromotionEventHandlerRecon promotionEventHandlerFighter = new PromotionEventHandlerRecon();
        String promotion = promotionEventHandlerFighter.determineReconPromotion(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughMissions () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerRecon.PilotRankCommandMinMissions-1);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);

        PromotionEventHandlerRecon promotionEventHandlerFighter = new PromotionEventHandlerRecon();
        String promotion = promotionEventHandlerFighter.determineReconPromotion(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }
}
