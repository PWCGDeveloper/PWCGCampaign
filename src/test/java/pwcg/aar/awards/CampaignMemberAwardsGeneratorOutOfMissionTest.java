package pwcg.aar.awards;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.inmission.phase3.reconcile.ReconciledInMissionData;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CampaignMembersOutOfMissionFinder.class})
public class CampaignMemberAwardsGeneratorOutOfMissionTest
{
    private Campaign campaign;
    
    @Mock
    AARContext aarContext;

    @Mock
    private AARPreliminaryData preliminaryData;

    @Mock
    ReconciledInMissionData reconciledInMissionData;
    
    @Mock
    private ReconciledVictoryData reconciledVictoryData;
    
    @Mock
    private CampaignUpdateData campaignUpdateData;
    
    @Mock
    private AARPersonnelLosses personnelLosses;
    
    @Mock
    private SquadronMembers campaignMembersInMission;

    @Mock
    private SquadronMembers campaignMembersOutOfMission;

    @Mock
    private PwcgMissionData pwcgMissionData;
     
    private List<SquadronMember> squadronMembersOutOfMission = new ArrayList<>();

    @Before
    public void setup() throws PWCGException
    {
        PowerMockito.mockStatic(CampaignMembersOutOfMissionFinder.class);

        squadronMembersOutOfMission.clear();

        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(CampaignMembersOutOfMissionFinder.getAllCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(campaignMembersOutOfMission);
        Mockito.when(campaignMembersOutOfMission.getSquadronMemberList()).thenReturn(squadronMembersOutOfMission);
    }

    @Test
    public void testMedalAwardedForVictories () throws PWCGException
    {             
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        CampaignPersonnelTestHelper.addVictories(aiSquadMember, campaign.getDate(), 20);
        squadronMembersOutOfMission.add(aiSquadMember);

        CampaignMemberAwardsGeneratorOutOfMission awardsGenerator = new CampaignMemberAwardsGeneratorOutOfMission(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards();
        
        assert (campaignMemberAwards.getCampaignMemberMedals().size() >= 1);
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(aiSquadMember.getSerialNumber()));
    }


    @Test
    public void testPromotionAwardedForVictoriesAndMissionsFlown () throws PWCGException
    {     
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        aiSquadMember.setMissionFlown(100);
        CampaignPersonnelTestHelper.addVictories(aiSquadMember, campaign.getDate(), 20);
        squadronMembersOutOfMission.add(aiSquadMember);

        CampaignMemberAwardsGeneratorOutOfMission awardsGenerator = new CampaignMemberAwardsGeneratorOutOfMission(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards();
        
        assert (campaignMemberAwards.getPromotions().size() == 1);
        assert (campaignMemberAwards.getPromotions().containsKey(aiSquadMember.getSerialNumber()));
    }
    
    @Test
    public void testMissionsFlownIncreased () throws PWCGException
    {     
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        squadronMembersOutOfMission.add(aiSquadMember);

        CampaignMemberAwardsGeneratorOutOfMission awardsGenerator = new CampaignMemberAwardsGeneratorOutOfMission(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards();
        
        Integer numMissionsFlown = campaignMemberAwards.getMissionsFlown().get(aiSquadMember.getSerialNumber());
        
        assert (numMissionsFlown > aiSquadMember.getMissionFlown());
    }


}
