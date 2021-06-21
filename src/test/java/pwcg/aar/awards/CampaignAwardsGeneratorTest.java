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
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.outofmission.phase2.awards.CampaignAwardsGenerator;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignAwardsGeneratorTest
{
    private Campaign campaign;
    
    @Mock
    AARContext aarContext;
    
    @Mock
    private ReconciledMissionVictoryData reconciledVictoryData;
    
    @Mock
    private AARPersonnelLosses personnelLosses;
    
    @Mock
    private Victory victory1;
    
    @Mock
    private Victory victory2;
    
    @Mock
    private AARPersonnelAcheivements personnelAcheivements;

    @Mock
    private PwcgMissionData pwcgMissionData;
         
    @Before
    public void setup() throws PWCGException
    {        
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        
        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(aarContext.getPersonnelLosses()).thenReturn(personnelLosses);

        Mockito.when(personnelAcheivements.getVictoriesByPilot()).thenReturn(new HashMap<>());
        Mockito.when(personnelLosses.pilotisWoundedToday(Mockito.any())).thenReturn(false);        

        Mockito.when(victory1.getDate()).thenReturn(campaign.getDate());
        Mockito.when(victory2.getDate()).thenReturn(campaign.getDate());

    }

    @Test
    public void testMedalAwardedForVictories () throws PWCGException
    {             
        SquadronMembers aiPilots = SquadronMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        CampaignPersonnelTestHelper.addVictories(aiPilots.getSquadronMemberList().get(1), campaign.getDate(), 20);
        
        List<Victory> victoriesToday = new ArrayList<>();
        victoriesToday.add(victory1);
        victoriesToday.add(victory2);
        Map<Integer, List<Victory>> victoriesByAllPilots = new HashMap<>();
        victoriesByAllPilots.put(aiPilots.getSquadronMemberList().get(1).getSerialNumber(), victoriesToday);
        Mockito.when(personnelAcheivements.getVictoriesByPilot()).thenReturn(victoriesByAllPilots);

        Map<Integer, SquadronMember> pilotsToEvaluate = new HashMap<>();
        pilotsToEvaluate.put(aiPilots.getSquadronMemberList().get(1).getSerialNumber(), aiPilots.getSquadronMemberList().get(1));
        pilotsToEvaluate.put(aiPilots.getSquadronMemberList().get(2).getSerialNumber(), aiPilots.getSquadronMemberList().get(2));
        pilotsToEvaluate.put(aiPilots.getSquadronMemberList().get(3).getSerialNumber(), aiPilots.getSquadronMemberList().get(3));

        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(pilotsToEvaluate.values()));
        
        assert (campaignMemberAwards.getCampaignMemberMedals().size() == 1);
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(aiPilots.getSquadronMemberList().get(1).getSerialNumber()));
        assert (!campaignMemberAwards.getCampaignMemberMedals().containsKey(aiPilots.getSquadronMemberList().get(2).getSerialNumber()));
        assert (!campaignMemberAwards.getCampaignMemberMedals().containsKey(aiPilots.getSquadronMemberList().get(3).getSerialNumber()));
    }

    @Test
    public void testInjuredMemberssAwardedWoundBadge () throws PWCGException
    {            
        SquadronMembers nonPlayerSquadronMembers = SquadronMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());

        Map<Integer, SquadronMember> squadronMembersInjured = new HashMap<>();
        squadronMembersInjured.put(nonPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(1));
        squadronMembersInjured.put(nonPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(2));
        squadronMembersInjured.put(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(3));
        
        Mockito.when(personnelLosses.pilotisWoundedToday(nonPlayerSquadronMembers.getSquadronMemberList().get(2))).thenReturn(true);
         
        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(squadronMembersInjured.values()));
        
        assert (campaignMemberAwards.getCampaignMemberMedals().size() == 1);
        assert (!campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber()));
        assert (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber()));
        assert (!campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber()));
    }


    @Test
    public void testPromotionAwardedForVictoriesAndMissionsFlown () throws PWCGException
    {     
        SquadronMembers nonPlayerSquadronMembers = SquadronMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());

        Map<Integer, SquadronMember> squadronMembersInjured = new HashMap<>();
        squadronMembersInjured.put(nonPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(1));
        squadronMembersInjured.put(nonPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(2));
        squadronMembersInjured.put(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber(), nonPlayerSquadronMembers.getSquadronMemberList().get(3));
        
        CampaignPersonnelTestHelper.addVictories(nonPlayerSquadronMembers.getSquadronMemberList().get(3), campaign.getDate(), 20);
        nonPlayerSquadronMembers.getSquadronMemberList().get(3).setMissionFlown(100);

        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(squadronMembersInjured.values()));
                
        assert (campaignMemberAwards.getPromotions().size() == 1);
        assert (campaignMemberAwards.getPromotions().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber()));
    }
}
