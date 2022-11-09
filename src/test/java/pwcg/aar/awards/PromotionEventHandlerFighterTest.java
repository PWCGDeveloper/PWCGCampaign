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
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.promotion.PromotionArbitrator;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberVictories;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.country.FCServiceManager;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.VictoryMaker;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PromotionEventHandlerFighterTest
{
    private Campaign campaign;
    
    @Mock private ArmedService service;
    @Mock private Squadron squadron;
    @Mock private SquadronMember squadronMember;
    @Mock private SquadronMemberVictories squadronMemberVictories;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.ESC_103_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(squadronMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadronMember.getSquadronMemberVictories()).thenReturn(squadronMemberVictories);
        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(0);        
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.FIGHTER);
        Mockito.when(squadron.getService()).thenReturn(FCServiceManager.LAVIATION_MILITAIRE);
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

        Assertions.assertTrue (promotion.equals("Sergent"));
    }

    @Test
    public void promoteSergent () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(3, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(50);
        Mockito.when(squadronMember.getRank()).thenReturn("Sergent");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        Assertions.assertTrue (promotion.equals("Sous Lieutenant"));
    }

    @Test
    public void promoteSousLieutenant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(7, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(80);
        Mockito.when(squadronMember.getRank()).thenReturn("Sous Lieutenant");
        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        Assertions.assertTrue (promotion.equals("Lieutenant"));
    }

    @Test
    public void promoteCapitaine () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(15, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(110);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        Assertions.assertTrue (promotion.equals("Capitaine"));
    }

    @Test
    public void promoteCapitaineFailNotPlayer () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(15, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(110);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(false);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughMissions () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(15, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(109);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughictories () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(14, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(90);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

}
