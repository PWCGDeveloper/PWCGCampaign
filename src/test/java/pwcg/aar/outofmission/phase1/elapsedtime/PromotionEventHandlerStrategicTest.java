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
import pwcg.aar.awards.PromotionEventHandlerStrategic;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class PromotionEventHandlerStrategicTest
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
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerStrategic.PilotRankMedMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Corporal");

        PromotionEventHandlerStrategic promotionEventHandlerFighter = new PromotionEventHandlerStrategic();
        String promotion = promotionEventHandlerFighter.determineStrategicPromotion(campaign, squadronMember);

        assert (promotion.equals("Sergent"));
    }

    @Test
    public void promoteSergent () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerStrategic.PilotRankHighMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Sergent");

        PromotionEventHandlerStrategic promotionEventHandlerFighter = new PromotionEventHandlerStrategic();
        String promotion = promotionEventHandlerFighter.determineStrategicPromotion(campaign, squadronMember);

        assert (promotion.equals("Sous Lieutenant"));
    }

    @Test
    public void promoteSousLieutenant () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerStrategic.PilotRankExecMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Sous Lieutenant");

        PromotionEventHandlerStrategic promotionEventHandlerFighter = new PromotionEventHandlerStrategic();
        String promotion = promotionEventHandlerFighter.determineStrategicPromotion(campaign, squadronMember);

        assert (promotion.equals("Lieutenant"));
    }

    @Test
    public void promoteCapitaine () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerStrategic.PilotRankCommandMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);

        PromotionEventHandlerStrategic promotionEventHandlerFighter = new PromotionEventHandlerStrategic();
        String promotion = promotionEventHandlerFighter.determineStrategicPromotion(campaign, squadronMember);

        assert (promotion.equals("Capitaine"));
    }

    @Test
    public void promoteCapitaineFailNotPlayer () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerStrategic.PilotRankCommandMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(false);

        PromotionEventHandlerStrategic promotionEventHandlerFighter = new PromotionEventHandlerStrategic();
        String promotion = promotionEventHandlerFighter.determineStrategicPromotion(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughMissions () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerStrategic.PilotRankCommandMinMissions-1);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);

        PromotionEventHandlerStrategic promotionEventHandlerFighter = new PromotionEventHandlerStrategic();
        String promotion = promotionEventHandlerFighter.determineStrategicPromotion(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }
}
