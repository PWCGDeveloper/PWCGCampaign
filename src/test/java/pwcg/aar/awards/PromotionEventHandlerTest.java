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
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.VictoryMaker;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PromotionEventHandlerTest
{
    private Campaign campaign;
    
    @Mock private CrewMember crewMember;
    @Mock private Company squadron;
    @Mock private CrewMemberVictories squadronMemberVictories;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(squadronMemberVictories);
    }

    @Test
    public void promoteCorporalToSergentRecon () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(30);
        Mockito.when(crewMember.getRank()).thenReturn("Corporal");
        Mockito.when(crewMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(ArgumentMatchers.<Date>any())).thenReturn(PwcgRoleCategory.RECON);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Sergent"));
    }

    @Test
    public void promoteCorporalToSergentFighter () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(1, campaign.getDate());
        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());

        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(20);
        Mockito.when(crewMember.getRank()).thenReturn("Corporal");
        Mockito.when(crewMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(ArgumentMatchers.<Date>any())).thenReturn(PwcgRoleCategory.FIGHTER);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Sergent"));
    }

    @Test
    public void promoteCorporalToSergentFailedDueToDifferentRole () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(10);
        Mockito.when(crewMember.getRank()).thenReturn("Corporal");
        Mockito.when(crewMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(ArgumentMatchers.<Date>any())).thenReturn(PwcgRoleCategory.RECON);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

}
