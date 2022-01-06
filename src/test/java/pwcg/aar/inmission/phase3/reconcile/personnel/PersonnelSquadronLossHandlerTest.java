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
public class PersonnelSquadronLossHandlerTest
{
    private Campaign campaign;
    private List<LogCrewMember> crewMemberStatusList;

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
    }

    private void addCrewMember(Integer serialNumber, int status)
    {
        LogCrewMember squadronCrewMember = new LogCrewMember();
        squadronCrewMember.setSerialNumber(serialNumber);
        squadronCrewMember.setStatus(status);
        crewMemberStatusList.add(squadronCrewMember);
    }

    @Test
    public void testEverybodyKilled() throws PWCGException
    {
        CrewMember playerInFlight = campaign.findReferencePlayer();
        addCrewMember(playerInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        CrewMember SergentInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergent");
        addCrewMember(SergentInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        CrewMember corporalInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addCrewMember(corporalInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        CrewMember sltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sous Lieutenant");
        addCrewMember(sltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        addCrewMember(101064, CrewMemberStatus.STATUS_KIA);

        PersonnelLossHandler crewMemberLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = crewMemberLossInMissionHandler.crewMembersShotDown(crewMemberStatusList);

        Assertions.assertTrue (personnelLosses.getPersonnelKilled().size() == 5);
        Assertions.assertTrue (personnelLosses.getPersonnelCaptured().size() == 0);
        Assertions.assertTrue (personnelLosses.getPersonnelMaimed().size() == 0);
    }

    @Test
    public void testMixedStatusWithMaimed() throws PWCGException
    {
        CrewMember playerInFlight = campaign.findReferencePlayer();
        addCrewMember(playerInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);

        CrewMember sergentInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergent");
        addCrewMember(sergentInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);

        CrewMember corporalInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addCrewMember(corporalInFlight.getSerialNumber(), CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);

        CrewMember sltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sous Lieutenant");
        addCrewMember(sltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        addCrewMember(101064, CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);

        PersonnelLossHandler crewMemberLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = crewMemberLossInMissionHandler.crewMembersShotDown(crewMemberStatusList);

        Assertions.assertTrue (personnelLosses.getPersonnelKilled().size() == 1);
        Assertions.assertTrue (personnelLosses.getPersonnelCaptured().size() == 0);
        Assertions.assertTrue (personnelLosses.getPersonnelMaimed().size() == 2);
        Assertions.assertTrue (personnelLosses.getPersonnelWounded().size() == 2);
    }

    @Test
    public void testMixedStatusWithCaptured() throws PWCGException
    {
        CrewMember playerInFlight = campaign.findReferencePlayer();
        addCrewMember(playerInFlight.getSerialNumber(), CrewMemberStatus.STATUS_CAPTURED);

        CrewMember sergentInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergent");
        addCrewMember(sergentInFlight.getSerialNumber(), CrewMemberStatus.STATUS_ACTIVE);

        CrewMember corporalInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addCrewMember(corporalInFlight.getSerialNumber(), CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);

        CrewMember sltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sous Lieutenant");
        addCrewMember(sltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_CAPTURED);

        addCrewMember(101064, CrewMemberStatus.STATUS_CAPTURED);

        PersonnelLossHandler crewMemberLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = crewMemberLossInMissionHandler.crewMembersShotDown(crewMemberStatusList);

        Assertions.assertTrue (personnelLosses.getPersonnelKilled().size() == 0);
        Assertions.assertTrue (personnelLosses.getPersonnelCaptured().size() == 3);
        Assertions.assertTrue (personnelLosses.getPersonnelMaimed().size() == 1);
    }

}
