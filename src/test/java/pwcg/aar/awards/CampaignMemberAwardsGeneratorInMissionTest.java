package pwcg.aar.awards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.inmission.phase3.reconcile.ReconciledInMissionData;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;
import pwcg.testutils.CampaignPersonnelTestHelper;

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
    private Map<Integer, SquadronMember> squadronMembersOutOfMission = new HashMap<>();
    
    @Before
    public void setup() throws PWCGException
    {
        squadronMembersInMission.clear();

        PWCGContextManager.setRoF(true);
        campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_103_PROFILE);
        
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
        Mockito.when(campaignMembersInMission.getSquadronMembers()).thenReturn(squadronMembersInMission);
        Mockito.when(preliminaryData.getCampaignMembersOutOfMission()).thenReturn(campaignMembersOutOfMission);
        Mockito.when(campaignMembersOutOfMission.getSquadronMembers()).thenReturn(squadronMembersOutOfMission);
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
        Map<Integer, SquadronMember> nonPlayerSquadronMembersMap = campaign.getPersonnelManager().getAllNonAceCampaignMembers();
        List<SquadronMember>nonPlayerSquadronMembers = new ArrayList<>(nonPlayerSquadronMembersMap.values());
        Map<Integer, SquadronMember> squadronMembersKilled = new HashMap<>();
        squadronMembersKilled.put(nonPlayerSquadronMembers.get(1).getSerialNumber(), nonPlayerSquadronMembers.get(1));
        Mockito.when(personnelLosses.getPersonnelKilled()).thenReturn(squadronMembersKilled);

        Map<Integer, SquadronMember> squadronMembersMaimed = new HashMap<>();
        squadronMembersMaimed.put(nonPlayerSquadronMembers.get(2).getSerialNumber(), nonPlayerSquadronMembers.get(2));
        squadronMembersMaimed.put(nonPlayerSquadronMembers.get(3).getSerialNumber(), nonPlayerSquadronMembers.get(3));
        Mockito.when(personnelLosses.getPersonnelMaimed()).thenReturn(squadronMembersMaimed);
        
        squadronMembersInMission.put(nonPlayerSquadronMembers.get(1).getSerialNumber(), nonPlayerSquadronMembers.get(1));
        squadronMembersInMission.put(nonPlayerSquadronMembers.get(2).getSerialNumber(), nonPlayerSquadronMembers.get(2));
        squadronMembersInMission.put(nonPlayerSquadronMembers.get(3).getSerialNumber(), nonPlayerSquadronMembers.get(3));

        CampaignMemberAwardsGeneratorInMission awardsGenerator = new CampaignMemberAwardsGeneratorInMission(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards();
        
        assert (campaignMemberAwards.getCampaignMemberMedals().size() >= 1);
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.get(1).getSerialNumber()));
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.get(2).getSerialNumber()));
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.get(3).getSerialNumber()));
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
