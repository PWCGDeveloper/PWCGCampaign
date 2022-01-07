package pwcg.aar.awards;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.outofmission.phase2.awards.PromotionEventHandler;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberVictories;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.promotion.PromotionArbitrator;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.VictoryMaker;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PromotionEventHandlerLuftwaffeFighterTest
{
    private Campaign campaign;
    
    @Mock private ArmedService service;
    @Mock private Company squadron;
    @Mock private CrewMember crewMember;
    @Mock private CrewMemberVictories squadronMemberVictories;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_52_PROFILE_STALINGRAD);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(crewMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(squadronMemberVictories);
        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(0);        
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.FIGHTER);
        Mockito.when(squadron.getService()).thenReturn(BoSServiceManager.LUFTWAFFE);
    }

    @Test
    public void promoteLeutnant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(5, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(30);
        Mockito.when(crewMember.getRank()).thenReturn("Oberfeldwebel");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Leutnant"));
    }

    @Test
    public void promoteOberleutnant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(15, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(60);
        Mockito.when(crewMember.getRank()).thenReturn("Leutnant");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Oberleutnant"));
    }

    @Test
    public void promoteHauptmann () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(30, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(110);
        Mockito.when(crewMember.getRank()).thenReturn("Oberleutnant");
        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Hauptmann"));
    }

    @Test
    public void promoteMajor () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(50, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(150);
        Mockito.when(crewMember.getRank()).thenReturn("Hauptmann");
        Mockito.when(crewMember.isPlayer()).thenReturn(true);
        Mockito.when(crewMember.getCompanyId()).thenReturn(SquadronTestProfile.JG_52_PROFILE_STALINGRAD.getCompanyId());

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Major"));
    }

    @Test
    public void promoteMajorFailNotPlayer () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(50, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(150);
        Mockito.when(crewMember.getRank()).thenReturn("Hauptmann");
        Mockito.when(crewMember.isPlayer()).thenReturn(false);
        Mockito.when(crewMember.getCompanyId()).thenReturn(SquadronTestProfile.JG_52_PROFILE_STALINGRAD.getCompanyId());

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteMajorFailNotEnoughMissions () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(50, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(149);
        Mockito.when(crewMember.getRank()).thenReturn("Hauptmann");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteMajorFailNotEnoughictories () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(49, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(150);
        Mockito.when(crewMember.getRank()).thenReturn("Hauptmann");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

}
