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
public class PromotionEventHandlerBomberTest
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
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_321_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(crewMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(squadronMemberVictories);
        Mockito.when(squadronMemberVictories.getGroundVictoryPointTotal()).thenReturn(0);        
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.ATTACK);
        Mockito.when(squadron.getService()).thenReturn(BoSServiceManager.VVS);
    }

    @Test
    public void promoteSerzhantToLeyitenant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAirGroundVictories(5, campaign.getDate());

        Mockito.when(squadronMemberVictories.getGroundVictoryPointTotal()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(20);
        Mockito.when(crewMember.getRank()).thenReturn("Serzhant");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Leyitenant"));
    }

    @Test
    public void promoteLeyitenantToStarshyiLeyitenant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAirGroundVictories(15, campaign.getDate());

        Mockito.when(squadronMemberVictories.getGroundVictoryPointTotal()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(50);
        Mockito.when(crewMember.getRank()).thenReturn("Leyitenant");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Starshyi Leyitenant"));
    }

    @Test
    public void promoteStarshyiLeyitenantToKapitan () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAirGroundVictories(30, campaign.getDate());

        Mockito.when(squadronMemberVictories.getGroundVictoryPointTotal()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(80);
        Mockito.when(crewMember.getRank()).thenReturn("Starshyi Leyitenant");
        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Kapitan"));
    }

    @Test
    public void promoteKapitanToMajor () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAirGroundVictories(50, campaign.getDate());

        Mockito.when(squadronMemberVictories.getGroundVictoryPointTotal()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(110);
        Mockito.when(crewMember.getRank()).thenReturn("Kapitan");
        Mockito.when(crewMember.isPlayer()).thenReturn(true);
        Mockito.when(crewMember.getCompanyId()).thenReturn(SquadronTestProfile.REGIMENT_321_PROFILE.getCompanyId());

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Major"));
    }

    @Test
    public void promoteMajorFailNotPlayer () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAirGroundVictories(50, campaign.getDate());

        Mockito.when(squadronMemberVictories.getGroundVictoryPointTotal()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(110);
        Mockito.when(crewMember.getRank()).thenReturn("Kapitan");
        Mockito.when(crewMember.isPlayer()).thenReturn(false);
        Mockito.when(crewMember.getCompanyId()).thenReturn(SquadronTestProfile.REGIMENT_321_PROFILE.getCompanyId());

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteMajorFailNotEnoughMissions () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAirGroundVictories(50, campaign.getDate());

        Mockito.when(squadronMemberVictories.getGroundVictoryPointTotal()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(109);
        Mockito.when(crewMember.getRank()).thenReturn("Kapitan");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteMajorFailNotEnoughictories () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAirGroundVictories(49, campaign.getDate());

        Mockito.when(squadronMemberVictories.getGroundVictoryPointTotal()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(90);
        Mockito.when(crewMember.getRank()).thenReturn("Kapitan");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

}
