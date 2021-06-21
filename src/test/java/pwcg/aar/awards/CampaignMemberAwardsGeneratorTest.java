package pwcg.aar.awards;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.outofmission.phase2.awards.CampaignMemberAwardsGenerator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignMemberAwardsGeneratorTest
{
    private Campaign campaign;
    
    @Mock
    AARContext aarContext;
    
    @Mock
    private ReconciledMissionVictoryData reconciledVictoryData;
    
    @Mock
    private AARPersonnelLosses personnelLosses;
         
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        
        Mockito.when(aarContext.getPersonnelLosses()).thenReturn(personnelLosses);
        Mockito.when(personnelLosses.pilotisWoundedToday(Mockito.any())).thenReturn(false);
    }

    @Test
    public void testMedalAwardedForVictories () throws PWCGException
    {             
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        CampaignPersonnelTestHelper.addVictories(aiSquadMember, campaign.getDate(), 20);

        CampaignMemberAwardsGenerator awardsGenerator = new CampaignMemberAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.generateAwards(aiSquadMember, 1);
        
        assert (campaignMemberAwards.getCampaignMemberMedals().size() >= 1);
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(aiSquadMember.getSerialNumber()));
    }

    @Test
    public void testInjuredMembersAwardedWoundBadge () throws PWCGException
    {            
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");

        Mockito.when(personnelLosses.pilotisWoundedToday(Mockito.any())).thenReturn(true);
         
        CampaignMemberAwardsGenerator awardsGenerator = new CampaignMemberAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.generateAwards(aiSquadMember, 1);
        
        assert (campaignMemberAwards.getCampaignMemberMedals().size() >= 1);
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(aiSquadMember.getSerialNumber()));
    }

    @Test
    public void testPromotionAwardedForVictoriesAndMissionsFlown () throws PWCGException
    {     
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        CampaignPersonnelTestHelper.addVictories(aiSquadMember, campaign.getDate(), 20);
        aiSquadMember.setMissionFlown(100);

        CampaignMemberAwardsGenerator awardsGenerator = new CampaignMemberAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.generateAwards(aiSquadMember, 1);
        
        assert (campaignMemberAwards.getPromotions().size() == 1);
        assert (campaignMemberAwards.getPromotions().containsKey(aiSquadMember.getSerialNumber()));
    }
}
