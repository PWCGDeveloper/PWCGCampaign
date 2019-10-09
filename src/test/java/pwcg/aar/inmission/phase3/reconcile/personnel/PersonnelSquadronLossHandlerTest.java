package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class PersonnelSquadronLossHandlerTest
{
    private Campaign campaign;
    private List<LogPilot> pilotStatusList;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        campaign = CampaignCache.makeCampaignForceCreation(SquadrontTestProfile.ESC_103_PROFILE);
        
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
        SquadronMember playerInFlight = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().get(0);
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
        
        assert(personnelLosses.getPersonnelKilled().size() == 4);
        assert(personnelLosses.getPersonnelCaptured().size() == 0);
        assert(personnelLosses.getPersonnelMaimed().size() == 0);
    }

    @Test
    public void testMixedStatusWithMaimed() throws PWCGException
    {
        SquadronMember playerInFlight = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().get(0);
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
        
        assert(personnelLosses.getPersonnelKilled().size() == 1);
        assert(personnelLosses.getPersonnelCaptured().size() == 0);
        assert(personnelLosses.getPersonnelMaimed().size() == 1);
        assert(personnelLosses.getPersonnelWounded().size() == 2);
    }

    @Test
    public void testMixedStatusWithCaptured() throws PWCGException
    {
        SquadronMember playerInFlight = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().get(0);
        addPilot(playerInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_CAPTURED);

        SquadronMember sergentInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sergent");
        addPilot(sergentInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_ACTIVE);

        SquadronMember corporalInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        addPilot(corporalInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        
        SquadronMember sltInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sous Lieutenant");
        addPilot(sltInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_CAPTURED);

        addPilot(101064, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        
        PersonnelLossHandler pilotLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = pilotLossInMissionHandler.pilotsShotDown(pilotStatusList);
        
        assert(personnelLosses.getPersonnelKilled().size() == 0);
        assert(personnelLosses.getPersonnelCaptured().size() == 2);
        assert(personnelLosses.getPersonnelMaimed().size() == 1);
    }

}
