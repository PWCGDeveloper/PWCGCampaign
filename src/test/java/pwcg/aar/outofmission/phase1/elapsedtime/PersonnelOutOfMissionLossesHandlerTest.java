package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CampaignMembersOutOfMissionFinder.class})
public class PersonnelOutOfMissionLossesHandlerTest
{
    private Campaign campaign;

    @Mock private AARContext aarContext;
    @Mock private CampaignUpdateData campaignUpdateData;
    @Mock private AARPreliminaryData preliminaryData;
    @Mock private SquadronMembers squadronMembers;

    
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.ESC_103_PROFILE);
     
        PowerMockito.mockStatic(CampaignMembersOutOfMissionFinder.class);

        Mockito.when(aarContext.getNewDate()).thenReturn(DateUtils.getDateYYYYMMDD("19171001"));
        
        SquadronMembers possibleDeadGuys = SquadronMemberFilter.filterActiveAI(campaign.getPersonnelManager().getActiveCampaignMembers(), campaign.getDate());
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        
        Mockito.when(CampaignMembersOutOfMissionFinder.getAllCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(squadronMembers);
        Mockito.when(CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(squadronMembers);

        Mockito.when(squadronMembers.getSquadronMemberCollection()).thenReturn(possibleDeadGuys.getSquadronMemberCollection());
    }

    @Test
    public void testAcesKilled () throws PWCGException
    {     
        OutOfMissionLossHandler outOfMissionLossesHandler = new OutOfMissionLossHandler(campaign, aarContext);
        outOfMissionLossesHandler.lossesOutOfMission(new HashMap<Integer, SquadronMember>(), new HashMap<Integer, LogPlane>());
        AARPersonnelLosses lossesInMissionDataTotal = outOfMissionLossesHandler.getOutOfMissionPersonnelLosses();
        assert (lossesInMissionDataTotal.getAcesKilled().size() > 0);
    }

    @Test
    public void testAiKilled () throws PWCGException
    {     
        Map<Integer, SquadronMember> aiKilled = new HashMap<>();
        Map<Integer, SquadronMember> aiMaimed = new HashMap<>();
        Map<Integer, SquadronMember> aiCaptured = new HashMap<>();
        Map<Integer, SquadronMember> aiWounded = new HashMap<>();

        OutOfMissionLossHandler outOfMissionLossesHandler = new OutOfMissionLossHandler(campaign, aarContext);
        SquadronMembers allAiCampaignMembers = SquadronMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        outOfMissionLossesHandler.lossesOutOfMission(allAiCampaignMembers.getSquadronMemberCollection(), new HashMap<Integer, LogPlane>());

        AARPersonnelLosses lossesInMissionDataTotal = outOfMissionLossesHandler.getOutOfMissionPersonnelLosses();
        aiKilled.putAll(lossesInMissionDataTotal.getPersonnelKilled());
        aiMaimed.putAll(lossesInMissionDataTotal.getPersonnelCaptured());
        aiCaptured.putAll(lossesInMissionDataTotal.getPersonnelMaimed());
        aiWounded.putAll(lossesInMissionDataTotal.getPersonnelWounded());
        
        assert (aiKilled.size() > 0);
        assert (aiMaimed.size() > 0);
        assert (aiCaptured.size() > 0);
        assert (aiWounded.size() > 0);
    }
}
