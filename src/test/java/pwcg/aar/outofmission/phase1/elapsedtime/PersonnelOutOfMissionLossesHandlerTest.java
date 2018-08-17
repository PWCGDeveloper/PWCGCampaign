package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;

@RunWith(MockitoJUnitRunner.class)
public class PersonnelOutOfMissionLossesHandlerTest
{
    private Campaign campaign;

    @Mock
    private AARContext aarContext;

    @Mock
    private CampaignUpdateData campaignUpdateData;

    @Mock
    private AARPreliminaryData preliminaryData;

    @Mock
    private SquadronMembers squadronMembers;

    
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_103_PROFILE);
        
        Mockito.when(aarContext.getCampaignUpdateData()).thenReturn(campaignUpdateData);
        Mockito.when(aarContext.getNewDate()).thenReturn(DateUtils.getDateYYYYMMDD("19171001"));
        
        SquadronMembers possibleDeadGuys = SquadronMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(preliminaryData.getCampaignMembersOutOfMission()).thenReturn(squadronMembers);
        Mockito.when(squadronMembers.getSquadronMemberCollection()).thenReturn(possibleDeadGuys.getSquadronMemberCollection());
    }

    @Test
    public void testAcesKilled () throws PWCGException
    {     
        OutOfMissionLossHandler outOfMissionLossesHandler = new OutOfMissionLossHandler(campaign, aarContext);
        outOfMissionLossesHandler.lossesOutOfMission(new HashMap<Integer, SquadronMember>(), new HashMap<Integer, EquippedPlane>());
        AARPersonnelLosses lossesInMissionDataTotal = outOfMissionLossesHandler.getOutOfMissionPersonnelLosses();
        assert (lossesInMissionDataTotal.getAcesKilled().size() > 0);
    }

    @Test
    public void testAiKilled () throws PWCGException
    {     
        Map<Integer, SquadronMember> aiKilled = new HashMap<>();
        Map<Integer, SquadronMember> aiMaimed = new HashMap<>();
        Map<Integer, SquadronMember> aiCaptured = new HashMap<>();

        OutOfMissionLossHandler outOfMissionLossesHandler = new OutOfMissionLossHandler(campaign, aarContext);
        SquadronMembers nonPlayerSquadronMembers = SquadronMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        outOfMissionLossesHandler.lossesOutOfMission(nonPlayerSquadronMembers.getSquadronMemberCollection(), new HashMap<Integer, EquippedPlane>());

        AARPersonnelLosses lossesInMissionDataTotal = outOfMissionLossesHandler.getOutOfMissionPersonnelLosses();
        aiKilled.putAll(lossesInMissionDataTotal.getPersonnelKilled());
        aiMaimed.putAll(lossesInMissionDataTotal.getPersonnelCaptured());
        aiCaptured.putAll(lossesInMissionDataTotal.getPersonnelMaimed());
        
        assert (aiKilled.size() > 0);
        assert (aiMaimed.size() > 0);
        assert (aiCaptured.size() > 0);
    }
}
