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

import pwcg.aar.outofmission.phase2.awards.PromotionEventHandler;
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
public class PromotionEventHandlerLuftwaffeFighterTest
{
    private Campaign campaign;
    
    @Mock private ArmedService service;
    @Mock private Squadron squadron;
    @Mock private SquadronMember squadronMember;
    @Mock private SquadronMemberVictories squadronMemberVictories;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_52_PROFILE_STALINGRAD);
        Mockito.when(squadronMember.getSquadronMemberVictories()).thenReturn(squadronMemberVictories);
        Mockito.when(squadronMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineSquadronPrimaryRole(Mockito.any())).thenReturn(Role.ROLE_FIGHTER);
    }

    @Test
    public void promoteLeutnant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(3, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(40);
        Mockito.when(squadronMember.getRank()).thenReturn("Oberfeldwebel");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Leutnant"));
    }

    @Test
    public void promoteOberleutnant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(15, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(90);
        Mockito.when(squadronMember.getRank()).thenReturn("Leutnant");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Oberleutnant"));
    }

    @Test
    public void promoteHauptmann () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(25, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(120);
        Mockito.when(squadronMember.getRank()).thenReturn("Oberleutnant");
        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Hauptmann"));
    }

    @Test
    public void promoteMajor () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(35, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(150);
        Mockito.when(squadronMember.getRank()).thenReturn("Hauptmann");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.JG_52_PROFILE_STALINGRAD.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Major"));
    }

    @Test
    public void promoteMajorFailNotPlayer () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(35, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(150);
        Mockito.when(squadronMember.getRank()).thenReturn("Hauptmann");
        Mockito.when(squadronMember.isPlayer()).thenReturn(false);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.JG_52_PROFILE_STALINGRAD.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

    @Test
    public void promoteMajorFailNotEnoughMissions () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(35, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(149);
        Mockito.when(squadronMember.getRank()).thenReturn("Hauptmann");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.JG_52_PROFILE_STALINGRAD.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughictories () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(34, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(150);
        Mockito.when(squadronMember.getRank()).thenReturn("Hauptmann");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.JG_52_PROFILE_STALINGRAD.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals(PromotionEventHandler.NO_PROMOTION));
    }

}
