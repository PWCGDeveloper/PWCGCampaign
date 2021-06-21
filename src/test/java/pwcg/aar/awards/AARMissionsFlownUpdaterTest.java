package pwcg.aar.awards;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.outofmission.phase1.elapsedtime.AARMissionsFlownUpdater;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AARMissionsFlownUpdaterTest
{
    private Campaign campaign;
    private AARPersonnelAcheivements personnelAcheivements = new AARPersonnelAcheivements();
    
    @Mock
    private AARContext aarContext;

    @Mock
    private AARPreliminaryData preliminaryData;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);

        personnelAcheivements = new AARPersonnelAcheivements();

        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);        
    }

    @Test
    public void testMissionsFlownIncreased() throws PWCGException
    {
        SquadronMembers nonPlayerSquadronMembers = SquadronMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());

        SquadronMembers campaignMembersInMission = new SquadronMembers();
        campaignMembersInMission.addToSquadronMemberCollection(nonPlayerSquadronMembers.getSquadronMemberList().get(1));
        campaignMembersInMission.addToSquadronMemberCollection(nonPlayerSquadronMembers.getSquadronMemberList().get(2));
        campaignMembersInMission.addToSquadronMemberCollection(nonPlayerSquadronMembers.getSquadronMemberList().get(3));

        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
        
        int missionsFlownBefore1 = nonPlayerSquadronMembers.getSquadronMemberList().get(1).getMissionFlown();
        int missionsFlownBefore2 = nonPlayerSquadronMembers.getSquadronMemberList().get(2).getMissionFlown();
        int missionsFlownBefore3 = nonPlayerSquadronMembers.getSquadronMemberList().get(3).getMissionFlown();
        
        AARMissionsFlownUpdater missionsFlown = new AARMissionsFlownUpdater(campaign, aarContext);
        missionsFlown.updateMissionsFlown();

        Map<Integer, Integer> updatedMissionsFLown = personnelAcheivements.getMissionsFlown();
        
        assert(updatedMissionsFLown.containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber()));
        assert(updatedMissionsFLown.containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber()));
        assert(updatedMissionsFLown.containsKey(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber()));
        
        int missionsFlownAfter1 = updatedMissionsFLown.get(nonPlayerSquadronMembers.getSquadronMemberList().get(1).getSerialNumber());
        int missionsFlownAfter2 = updatedMissionsFLown.get(nonPlayerSquadronMembers.getSquadronMemberList().get(2).getSerialNumber());
        int missionsFlownAfter3 = updatedMissionsFLown.get(nonPlayerSquadronMembers.getSquadronMemberList().get(3).getSerialNumber());

        assert ((missionsFlownAfter1 - missionsFlownBefore1) == 1);
        assert ((missionsFlownAfter2 - missionsFlownBefore2) == 1);
        assert ((missionsFlownAfter3 - missionsFlownBefore3) == 1);
    }

}
