package pwcg.aar.awards;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.inmission.phase3.reconcile.ReconciledInMissionData;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignMemberAwardsGeneratorInMissionTest
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
     
    private Map<Integer, SquadronMember> squadronMembersInMission = new HashMap<>();
    
    @Before
    public void setup() throws PWCGException
    {
        squadronMembersInMission.clear();

        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.ESC_103_PROFILE);
        
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
        Mockito.when(campaignMembersInMission.getSquadronMemberCollection()).thenReturn(squadronMembersInMission);
        Mockito.when(aarContext.getReconciledInMissionData()).thenReturn(reconciledInMissionData);
        Mockito.when(reconciledInMissionData.getReconciledVictoryData()).thenReturn(reconciledVictoryData);
        Mockito.when(aarContext.getCampaignUpdateData()).thenReturn(campaignUpdateData);
        Mockito.when(campaignUpdateData.getPersonnelLosses()).thenReturn(personnelLosses);
        Mockito.when(personnelLosses.getPersonnelKilled()).thenReturn(new HashMap<Integer, SquadronMember>());
        Mockito.when(personnelLosses.getPersonnelMaimed()).thenReturn(new HashMap<Integer, SquadronMember>());

    }

    @Test
    public void testMedalAwardedForVictories () throws PWCGException
    {             
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        CampaignPersonnelTestHelper.addVictories(aiSquadMember, campaign.getDate(), 20);
        squadronMembersInMission.put(aiSquadMember.getSerialNumber(), aiSquadMember);

        CampaignMemberAwardsGeneratorInMission awardsGenerator = new CampaignMemberAwardsGeneratorInMission(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards();
        
        assert (campaignMemberAwards.getCampaignMemberMedals().size() >= 1);
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(aiSquadMember.getSerialNumber()));
    }

    @Test
    public void testKilledMemberssAwardedWoundBadge () throws PWCGException
    {            
        SquadronMembers nonPlayerSquadronMembers = SquadronMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        Map<Integer, SquadronMember> squadronMembersKilled = new HashMap<>();
        squadronMembersKilled.put(nonPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(1));
        Mockito.when(personnelLosses.getPersonnelKilled()).thenReturn(squadronMembersKilled);

        Map<Integer, SquadronMember> squadronMembersMaimed = new HashMap<>();
        squadronMembersMaimed.put(nonPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(2));
        squadronMembersMaimed.put(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(3));
        Mockito.when(personnelLosses.getPersonnelMaimed()).thenReturn(squadronMembersMaimed);
        
        squadronMembersInMission.put(nonPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(1));
        squadronMembersInMission.put(nonPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(2));
        squadronMembersInMission.put(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(3));

        CampaignMemberAwardsGeneratorInMission awardsGenerator = new CampaignMemberAwardsGeneratorInMission(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards();
        
        assert (campaignMemberAwards.getCampaignMemberMedals().size() >= 1);
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber()));
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber()));
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber()));
    }


    @Test
    public void testPromotionAwardedForVictoriesAndMissionsFlown () throws PWCGException
    {     
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        CampaignPersonnelTestHelper.addVictories(aiSquadMember, campaign.getDate(), 20);
        aiSquadMember.setMissionFlown(100);
        squadronMembersInMission.put(aiSquadMember.getSerialNumber(), aiSquadMember);
        CampaignMemberAwardsGeneratorInMission awardsGenerator = new CampaignMemberAwardsGeneratorInMission(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards();
        
        assert (campaignMemberAwards.getPromotions().size() == 1);
        assert (campaignMemberAwards.getPromotions().containsKey(aiSquadMember.getSerialNumber()));
    }
    
    @Test
    public void testMissionsFlownIncreased () throws PWCGException
    {     
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        CampaignPersonnelTestHelper.addVictories(aiSquadMember, campaign.getDate(), 20);
        squadronMembersInMission.put(aiSquadMember.getSerialNumber(), aiSquadMember);

        CampaignMemberAwardsGeneratorInMission awardsGenerator = new CampaignMemberAwardsGeneratorInMission(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards();
        
        Integer numMissionsFlown = campaignMemberAwards.getMissionsFlown().get(aiSquadMember.getSerialNumber());
        
        assert (numMissionsFlown > aiSquadMember.getMissionFlown());
    }


}
