package pwcg.aar.awards;

import java.util.Date;

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
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.promotion.PromotionArbitrator;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberVictories;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class PromotionEventHandlerReconTest
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
        Mockito.when(squadronMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.RECON);
        Mockito.when(squadronMember.getSquadronMemberVictories()).thenReturn(squadronMemberVictories);
        Mockito.when(squadronMemberVictories.getGroundVictoryPointTotal()).thenReturn(0);
        
    }

    @Test
    public void promoteCorporalToSergent () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(30);
        Mockito.when(squadronMember.getRank()).thenReturn("Corporal");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Sergent"));
    }

    @Test
    public void promoteSergent () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(60);
        Mockito.when(squadronMember.getRank()).thenReturn("Sergent");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Sous Lieutenant"));
    }

    @Test
    public void promoteSousLieutenant () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(110);
        Mockito.when(squadronMember.getRank()).thenReturn("Sous Lieutenant");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Lieutenant"));
    }

    @Test
    public void promoteCapitaine () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(150);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(true);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals("Capitaine"));
    }

    @Test
    public void promoteCapitaineFailNotPlayer () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(150);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(squadronMember.isPlayer()).thenReturn(false);
        Mockito.when(squadronMember.getSquadronId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughMissions () throws PWCGException
    {     
        Mockito.when(squadronMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.getMissionFlown()).thenReturn(109);
        Mockito.when(squadronMember.getRank()).thenReturn("Lieutenant");

        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);

        assert (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }
}
