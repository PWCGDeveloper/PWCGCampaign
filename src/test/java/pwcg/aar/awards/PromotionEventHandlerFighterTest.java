package pwcg.aar.awards;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberVictories;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.VictoryMaker;

@RunWith(MockitoJUnitRunner.class)
public class PromotionEventHandlerFighterTest
{
    private Campaign campaign;
    
    @Mock private ArmedService service;
    @Mock private Squadron squadron;
    @Mock private SquadronMember squadronMember;
    @Mock private SquadronMemberVictories squadronMemberVictories;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        Mockito.when(squadronMember.getSquadronMemberVictories()).thenReturn(squadronMemberVictories);
        Mockito.when(squadronMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineSquadronPrimaryRole(Mockito.any())).thenReturn(Role.ROLE_FIGHTER);
    }

    @Test
    public void promoteCorporalToSergent () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(1, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(20);
        Mockito.when(squadronMember.getRank()).thenReturn("Corporal");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Sergent"));
    }

    @Test
    public void promoteSergent () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(3, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(40);
        Mockito.when(squadronMember.getRank()).thenReturn("Sergent");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Sous Lieutenant"));
    }

    @Test
    public void promoteSousLieutenant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(8, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(70);
        Mockito.when(squadronMember.getRank()).thenReturn("Sous Lieutenant");
        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Lieutenant"));
    }

    @Test
    public void promoteCapitaine () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(12, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(90);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Capitaine"));
    }

    @Test
    public void promoteCapitaineFailNotPlayer () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(10, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(90);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(false);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughMissions () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(10, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(89);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughictories () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(9, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(90);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

}
