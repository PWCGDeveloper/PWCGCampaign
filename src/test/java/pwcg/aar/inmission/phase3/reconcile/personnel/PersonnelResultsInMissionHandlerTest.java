package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonnelResultsInMissionHandlerTest
{
    private Campaign campaign;
    private List<LogCrewMember> crewMemberStatusList;
    
    @Mock
    private AARMissionEvaluationData evaluationData;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        crewMemberStatusList = new ArrayList<>();
        Mockito.when(evaluationData.getCrewMembersInMission()).thenReturn(crewMemberStatusList);
    }

    @Test
    public void testMixedStatusWithMaimed() throws PWCGException
    {
        createCrewMembersInMission();
        createAcesInMission();
        
        PersonnelLossesInMissionHandler inMissionHandler = new PersonnelLossesInMissionHandler(campaign, evaluationData);
        inMissionHandler.personellChanges();
        
        assert(inMissionHandler.personellChanges().getPersonnelKilled().size() == 3);
        assert(inMissionHandler.personellChanges().getPersonnelCaptured().size() == 1);
        assert(inMissionHandler.personellChanges().getPersonnelMaimed().size() == 1);
        assert(inMissionHandler.personellChanges().getPersonnelWounded().size() == 2);
        assert(inMissionHandler.personellChanges().getAcesKilled(campaign).size() == 2);
    }

    private void createCrewMembersInMission() throws PWCGException
    {
        CrewMember playerInFlight = campaign.findReferencePlayer();
        addSquadronCrewMember(playerInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);

        CrewMember sergentInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergent");
        addSquadronCrewMember(sergentInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);

        CrewMember corporalInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addSquadronCrewMember(corporalInFlight.getSerialNumber(), CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        
        CrewMember sltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sous Lieutenant");
        addSquadronCrewMember(sltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);
        
        CrewMember ltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Lieutenant");
        addSquadronCrewMember(ltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_CAPTURED);
    }
    
    private void createAcesInMission() throws PWCGException
    {
        LogCrewMember wernerVoss = new LogCrewMember();
        wernerVoss.setSerialNumber(101175);
        wernerVoss.setStatus(CrewMemberStatus.STATUS_KIA);
        
        LogCrewMember georgesGuynemer = new LogCrewMember();
        georgesGuynemer.setSerialNumber(101064);
        georgesGuynemer.setStatus(CrewMemberStatus.STATUS_KIA);

        crewMemberStatusList.add(wernerVoss);
        crewMemberStatusList.add(georgesGuynemer);
    }
    
    private void addSquadronCrewMember(int serialNumber, int status)
    {
        LogCrewMember squadronCrewMember = new LogCrewMember();
        squadronCrewMember.setSerialNumber(serialNumber);
        squadronCrewMember.setStatus(status);
        crewMemberStatusList.add(squadronCrewMember);
    }
}
