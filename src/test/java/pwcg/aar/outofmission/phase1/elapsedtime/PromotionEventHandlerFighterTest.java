package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.awards.PromotionEventHandler;
import pwcg.aar.awards.PromotionEventHandlerFighter;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberVictories;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.VictoryMaker;

@RunWith(MockitoJUnitRunner.class)
public class PromotionEventHandlerFighterTest
{
    private Campaign campaign;
    
    @Mock
    private SquadronMember squadronMember;
    
    @Mock
    private SquadronMemberVictories squadronMemberVictories;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.ESC_103_PROFILE);
        Mockito.when(squadronMember.getSquadronMemberVictories()).thenReturn(squadronMemberVictories);
    }

    @Test
    public void promoteCorporalToSergent () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(PromotionEventHandlerFighter.PilotRankMedVictories, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictories()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerFighter.PilotRankMedMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Corporal");

        PromotionEventHandlerFighter promotionEventHandlerFighter = new PromotionEventHandlerFighter();
        String promotion = promotionEventHandlerFighter.determineScoutPromotion(campaign, squadronMember);

        assert (promotion.equals("Sergent"));
    }

    @Test
    public void promoteSergent () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(PromotionEventHandlerFighter.PilotRankHighMinVictories, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictories()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerFighter.PilotRankHighMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Sergent");

        PromotionEventHandlerFighter promotionEventHandlerFighter = new PromotionEventHandlerFighter();
        String promotion = promotionEventHandlerFighter.determineScoutPromotion(campaign, squadronMember);

        assert (promotion.equals("Sous Lieutenant"));
    }

    @Test
    public void promoteSousLieutenant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(PromotionEventHandlerFighter.PilotRankExecVictories, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictories()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerFighter.PilotRankExecMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Sous Lieutenant");

        PromotionEventHandlerFighter promotionEventHandlerFighter = new PromotionEventHandlerFighter();
        String promotion = promotionEventHandlerFighter.determineScoutPromotion(campaign, squadronMember);

        assert (promotion.equals("Lieutenant"));
    }

    @Test
    public void promoteCapitaine () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(PromotionEventHandlerFighter.PilotRankCommandVictories, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictories()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerFighter.PilotRankCommandMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);

        PromotionEventHandlerFighter promotionEventHandlerFighter = new PromotionEventHandlerFighter();
        String promotion = promotionEventHandlerFighter.determineScoutPromotion(campaign, squadronMember);

        assert (promotion.equals("Capitaine"));
    }

    @Test
    public void promoteCapitaineFailNotPlayer () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(PromotionEventHandlerFighter.PilotRankCommandVictories, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictories()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerFighter.PilotRankCommandMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(false);

        PromotionEventHandlerFighter promotionEventHandlerFighter = new PromotionEventHandlerFighter();
        String promotion = promotionEventHandlerFighter.determineScoutPromotion(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughMissions () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(PromotionEventHandlerFighter.PilotRankCommandVictories, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictories()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerFighter.PilotRankCommandMinMissions-1);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);

        PromotionEventHandlerFighter promotionEventHandlerFighter = new PromotionEventHandlerFighter();
        String promotion = promotionEventHandlerFighter.determineScoutPromotion(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughictories () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(PromotionEventHandlerFighter.PilotRankCommandVictories-1, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictories()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(PromotionEventHandlerFighter.PilotRankCommandMinMissions);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);

        PromotionEventHandlerFighter promotionEventHandlerFighter = new PromotionEventHandlerFighter();
        String promotion = promotionEventHandlerFighter.determineScoutPromotion(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

}
