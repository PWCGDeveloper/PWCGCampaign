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
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonnelResultsInMissionHandlerTest
{
    private Campaign campaign;
    private List<LogPilot> pilotStatusList;
    
    @Mock
    private AARMissionEvaluationData evaluationData;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.ESC_103_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        pilotStatusList = new ArrayList<>();
        Mockito.when(evaluationData.getPilotsInMission()).thenReturn(pilotStatusList);
    }

    @Test
    public void testMixedStatusWithMaimed() throws PWCGException
    {
        createSquadronMembersInMission();
        createAcesInMission();
        
        PersonnelLossesInMissionHandler inMissionHandler = new PersonnelLossesInMissionHandler(campaign, evaluationData);
        inMissionHandler.personellChanges();
        
        assert(inMissionHandler.personellChanges().getPersonnelKilled().size() == 3);
        assert(inMissionHandler.personellChanges().getPersonnelCaptured().size() == 1);
        assert(inMissionHandler.personellChanges().getPersonnelMaimed().size() == 1);
        assert(inMissionHandler.personellChanges().getPersonnelWounded().size() == 2);
        assert(inMissionHandler.personellChanges().getAcesKilled(campaign).size() == 2);
    }

    private void createSquadronMembersInMission() throws PWCGException
    {
        SquadronMember playerInFlight = campaign.findReferencePlayer();
        addSquadronPilot(playerInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_WOUNDED);

        SquadronMember sergentInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sergent");
        addSquadronPilot(sergentInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_WOUNDED);

        SquadronMember corporalInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        addSquadronPilot(corporalInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        
        SquadronMember sltInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sous Lieutenant");
        addSquadronPilot(sltInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_KIA);
        
        SquadronMember ltInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Lieutenant");
        addSquadronPilot(ltInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_CAPTURED);
    }
    
    private void createAcesInMission() throws PWCGException
    {
        LogPilot wernerVoss = new LogPilot();
        wernerVoss.setSerialNumber(101175);
        wernerVoss.setStatus(SquadronMemberStatus.STATUS_KIA);
        
        LogPilot georgesGuynemer = new LogPilot();
        georgesGuynemer.setSerialNumber(101064);
        georgesGuynemer.setStatus(SquadronMemberStatus.STATUS_KIA);

        pilotStatusList.add(wernerVoss);
        pilotStatusList.add(georgesGuynemer);
    }
    
    private void addSquadronPilot(int serialNumber, int status)
    {
        LogPilot squadronCrewMember = new LogPilot();
        squadronCrewMember.setSerialNumber(serialNumber);
        squadronCrewMember.setStatus(status);
        pilotStatusList.add(squadronCrewMember);
    }
}
