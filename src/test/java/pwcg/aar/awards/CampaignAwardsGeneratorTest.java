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
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;

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
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.ESC_103_PROFILE);        

        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(aarContext.getPersonnelLosses()).thenReturn(personnelLosses);
        Mockito.when(personnelLosses.pilotisWoundedToday(Mockito.any())).thenReturn(false);        
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

        Map<Integer, SquadronMember> pilotsToEvaluate = new HashMap<>();
        pilotsToEvaluate.put(aiPilots.getSquadronMemberList().get(1).getSerialNumber(), aiPilots.getSquadronMemberList().get(1));
        pilotsToEvaluate.put(aiPilots.getSquadronMemberList().get(2).getSerialNumber(), aiPilots.getSquadronMemberList().get(2));
        pilotsToEvaluate.put(aiPilots.getSquadronMemberList().get(3).getSerialNumber(), aiPilots.getSquadronMemberList().get(3));

        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(pilotsToEvaluate.values()));
        
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().size() == 1);
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().containsKey(aiPilots.getSquadronMemberList().get(1).getSerialNumber()));
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(aiPilots.getSquadronMemberList().get(2).getSerialNumber()));
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(aiPilots.getSquadronMemberList().get(3).getSerialNumber()));
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
        
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().size() == 1);
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber()));
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber()));
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber()));
    }


    @Test
    public void testPromotionAwardedForVictoriesAndMissionsFlown () throws PWCGException
    {     
        SquadronMembers nonPlayerSquadronMembers = SquadronMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembersForCountry(Country.GERMANY), campaign.getDate());
        SquadronMembers germanPlayerSquadronMembers = SquadronMemberFilter.filterActiveAIForSquadron(nonPlayerSquadronMembers.getSquadronMemberCollection(), campaign.getDate(), 401016);

        Map<Integer, SquadronMember> squadronMembersToBeEvaluated = new HashMap<>();
        squadronMembersToBeEvaluated.put(germanPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber(), germanPlayerSquadronMembers.getSquadronMemberList().get(1));
        squadronMembersToBeEvaluated.put(germanPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber(), germanPlayerSquadronMembers.getSquadronMemberList().get(2));
        squadronMembersToBeEvaluated.put(germanPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber(), germanPlayerSquadronMembers.getSquadronMemberList().get(3));
        
        CampaignPersonnelTestHelper.addVictories(germanPlayerSquadronMembers.getSquadronMemberList().get(3), campaign.getDate(), 31);
        germanPlayerSquadronMembers.getSquadronMemberList().get(3).setRank("Leutnant");
        germanPlayerSquadronMembers.getSquadronMemberList().get(3).setMissionFlown(150);

        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(squadronMembersToBeEvaluated.values()));
                
        Assertions.assertTrue (campaignMemberAwards.getPromotions().size() == 1);
        Assertions.assertTrue (campaignMemberAwards.getPromotions().containsKey(germanPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber()));
    }
}
