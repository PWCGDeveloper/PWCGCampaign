package pwcg.aar.awards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);        

        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(aarContext.getPersonnelLosses()).thenReturn(personnelLosses);
        Mockito.when(personnelLosses.crewMemberisWoundedToday(Mockito.any())).thenReturn(false);        
    }

    @Test
    public void testMedalAwardedForVictories () throws PWCGException
    {             
        
        CrewMembers aiCrewMembers = CrewMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        CampaignPersonnelTestHelper.addVictories(aiCrewMembers.getCrewMemberList().get(1), campaign.getDate(), 20);
        
        List<Victory> victoriesToday = new ArrayList<>();
        victoriesToday.add(victory1);
        victoriesToday.add(victory2);
        Map<Integer, List<Victory>> victoriesByAllCrewMembers = new HashMap<>();
        victoriesByAllCrewMembers.put(aiCrewMembers.getCrewMemberList().get(1).getSerialNumber(), victoriesToday);

        Map<Integer, CrewMember> crewMembersToEvaluate = new HashMap<>();
        crewMembersToEvaluate.put(aiCrewMembers.getCrewMemberList().get(1).getSerialNumber(), aiCrewMembers.getCrewMemberList().get(1));
        crewMembersToEvaluate.put(aiCrewMembers.getCrewMemberList().get(2).getSerialNumber(), aiCrewMembers.getCrewMemberList().get(2));
        crewMembersToEvaluate.put(aiCrewMembers.getCrewMemberList().get(3).getSerialNumber(), aiCrewMembers.getCrewMemberList().get(3));

        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(crewMembersToEvaluate.values()));
        
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().size() == 1);
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().containsKey(aiCrewMembers.getCrewMemberList().get(1).getSerialNumber()));
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(aiCrewMembers.getCrewMemberList().get(2).getSerialNumber()));
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(aiCrewMembers.getCrewMemberList().get(3).getSerialNumber()));
    }

    @Test
    public void testInjuredMemberssAwardedWoundBadge () throws PWCGException
    {            
        CrewMembers nonPlayerCrewMembers = CrewMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());

        Map<Integer, CrewMember> squadronMembersInjured = new HashMap<>();
        squadronMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(1));
        squadronMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(2));
        squadronMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(3));
        
        Mockito.when(personnelLosses.crewMemberisWoundedToday(nonPlayerCrewMembers.getCrewMemberList().get(2))).thenReturn(true);
         
        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(squadronMembersInjured.values()));
        
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().size() == 1);
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber()));
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber()));
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber()));
    }


    @Test
    public void testPromotionAwardedForVictoriesAndMissionsFlown () throws PWCGException
    {     
        CrewMembers nonPlayerCrewMembers = CrewMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());

        Map<Integer, CrewMember> squadronMembersInjured = new HashMap<>();
        squadronMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(1));
        squadronMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(2));
        squadronMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(3));
        
        CampaignPersonnelTestHelper.addVictories(nonPlayerCrewMembers.getCrewMemberList().get(3), campaign.getDate(), 20);
        nonPlayerCrewMembers.getCrewMemberList().get(3).setRank("Leutnant");
        nonPlayerCrewMembers.getCrewMemberList().get(3).setBattlesFought(150);

        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(squadronMembersInjured.values()));
                
        Assertions.assertTrue (campaignMemberAwards.getPromotions().size() == 1);
        Assertions.assertTrue (campaignMemberAwards.getPromotions().containsKey(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber()));
    }
}
