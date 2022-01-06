package pwcg.aar.awards;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.outofmission.phase1.elapsedtime.AARMissionsFlownUpdater;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARMissionsFlownUpdaterTest
{
    private Campaign campaign;
    private AARPersonnelAcheivements personnelAcheivements = new AARPersonnelAcheivements();
    
    @Mock
    private AARContext aarContext;

    @Mock
    private AARPreliminaryData preliminaryData;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);

        personnelAcheivements = new AARPersonnelAcheivements();
    }

    @Test
    public void testMissionsFlownIncreased() throws PWCGException
    {
        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);        

        CrewMembers nonPlayerCrewMembers = CrewMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());

        CrewMembers campaignMembersInMission = new CrewMembers();
        campaignMembersInMission.addToCrewMemberCollection(nonPlayerCrewMembers.getCrewMemberList().get(1));
        campaignMembersInMission.addToCrewMemberCollection(nonPlayerCrewMembers.getCrewMemberList().get(2));
        campaignMembersInMission.addToCrewMemberCollection(nonPlayerCrewMembers.getCrewMemberList().get(3));

        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
        
        int missionsFlownBefore1 = nonPlayerCrewMembers.getCrewMemberList().get(1).getBattlesFought();
        int missionsFlownBefore2 = nonPlayerCrewMembers.getCrewMemberList().get(2).getBattlesFought();
        int missionsFlownBefore3 = nonPlayerCrewMembers.getCrewMemberList().get(3).getBattlesFought();
        
        AARMissionsFlownUpdater missionsFlown = new AARMissionsFlownUpdater(campaign, aarContext);
        missionsFlown.updateMissionsFlown();

        Map<Integer, Integer> updatedMissionsFLown = personnelAcheivements.getMissionsFlown();
        
        assert(updatedMissionsFLown.containsKey(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber()));
        assert(updatedMissionsFLown.containsKey(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber()));
        assert(updatedMissionsFLown.containsKey(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber()));
        
        int missionsFlownAfter1 = updatedMissionsFLown.get(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber());
        int missionsFlownAfter2 = updatedMissionsFLown.get(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber());
        int missionsFlownAfter3 = updatedMissionsFLown.get(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber());

        Assertions.assertTrue ((missionsFlownAfter1 - missionsFlownBefore1) == 1);
        Assertions.assertTrue ((missionsFlownAfter2 - missionsFlownBefore2) == 1);
        Assertions.assertTrue ((missionsFlownAfter3 - missionsFlownBefore3) == 1);
    }

}
