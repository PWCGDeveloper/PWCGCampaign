package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonnelSquadronLossHandlerTest
{
    private Campaign campaign;
    private List<LogPilot> pilotStatusList;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        pilotStatusList = new ArrayList<>();
    }

    private void addPilot(Integer serialNumber, int status)
    {
        LogPilot squadronCrewMember = new LogPilot();
        squadronCrewMember.setSerialNumber(serialNumber);
        squadronCrewMember.setStatus(status);
        pilotStatusList.add(squadronCrewMember);
    }

    @Test
    public void testEverybodyKilled() throws PWCGException
    {
        SquadronMember playerInFlight = campaign.findReferencePlayer();
        addPilot(playerInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_KIA);

        SquadronMember SergentInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sergent");
        addPilot(SergentInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_KIA);

        SquadronMember corporalInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        addPilot(corporalInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_KIA);

        SquadronMember sltInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sous Lieutenant");
        addPilot(sltInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_KIA);

        addPilot(101064, SquadronMemberStatus.STATUS_KIA);

        PersonnelLossHandler pilotLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = pilotLossInMissionHandler.pilotsShotDown(pilotStatusList);

        Assertions.assertTrue (personnelLosses.getPersonnelKilled().size() == 5);
        Assertions.assertTrue (personnelLosses.getPersonnelCaptured().size() == 0);
        Assertions.assertTrue (personnelLosses.getPersonnelMaimed().size() == 0);
    }

    @Test
    public void testMixedStatusWithMaimed() throws PWCGException
    {
        SquadronMember playerInFlight = campaign.findReferencePlayer();
        addPilot(playerInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_WOUNDED);

        SquadronMember sergentInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sergent");
        addPilot(sergentInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_WOUNDED);

        SquadronMember corporalInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        addPilot(corporalInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);

        SquadronMember sltInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sous Lieutenant");
        addPilot(sltInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_KIA);

        addPilot(101064, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);

        PersonnelLossHandler pilotLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = pilotLossInMissionHandler.pilotsShotDown(pilotStatusList);

        Assertions.assertTrue (personnelLosses.getPersonnelKilled().size() == 1);
        Assertions.assertTrue (personnelLosses.getPersonnelCaptured().size() == 0);
        Assertions.assertTrue (personnelLosses.getPersonnelMaimed().size() == 2);
        Assertions.assertTrue (personnelLosses.getPersonnelWounded().size() == 2);
    }

    @Test
    public void testMixedStatusWithCaptured() throws PWCGException
    {
        SquadronMember playerInFlight = campaign.findReferencePlayer();
        addPilot(playerInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_CAPTURED);

        SquadronMember sergentInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sergent");
        addPilot(sergentInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_ACTIVE);

        SquadronMember corporalInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        addPilot(corporalInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);

        SquadronMember sltInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sous Lieutenant");
        addPilot(sltInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_CAPTURED);

        addPilot(101064, SquadronMemberStatus.STATUS_CAPTURED);

        PersonnelLossHandler pilotLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = pilotLossInMissionHandler.pilotsShotDown(pilotStatusList);

        Assertions.assertTrue (personnelLosses.getPersonnelKilled().size() == 0);
        Assertions.assertTrue (personnelLosses.getPersonnelCaptured().size() == 3);
        Assertions.assertTrue (personnelLosses.getPersonnelMaimed().size() == 1);
    }

}
