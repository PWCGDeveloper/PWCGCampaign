package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AARTestSetup;
import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.aar.ui.events.PilotStatusEventGenerator;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.MissionEntityBuilder;

@RunWith(MockitoJUnitRunner.class)
public class InMissionSquadronPilotStatusEventGeneratorTest extends AARTestSetup
{	
    private Map<Integer, SquadronMember> squadronMembersKilledInMission = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersCapturedInMission = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersMaimedInMission = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersWoundedInMission = new HashMap<>();
    private Map<Integer, Ace> acesKilledMissionSquadronInMission = new HashMap<>();
	
    @Before
    public void setup() throws PWCGException
    {
        squadronMembersKilledInMission = new HashMap<>();
        squadronMembersCapturedInMission = new HashMap<>();
        squadronMembersMaimedInMission = new HashMap<>();
        squadronMembersWoundedInMission = new HashMap<>();
        acesKilledMissionSquadronInMission = new HashMap<>();

        setupAARMocks();
        
        SquadronMember squaddie1 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie A", SerialNumber.AI_STARTING_SERIAL_NUMBER+1, SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        SquadronMember squaddie2 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie B", SerialNumber.AI_STARTING_SERIAL_NUMBER+2, SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        SquadronMember squaddie3 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie C", SerialNumber.AI_STARTING_SERIAL_NUMBER+3, SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        SquadronMember squaddie4 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie D", SerialNumber.AI_STARTING_SERIAL_NUMBER+4, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);

        squadronMembersKilledInMission.put(squaddie1.getSerialNumber(), squaddie1);
        squadronMembersKilledInMission.put(squaddie2.getSerialNumber(), squaddie2);
        squadronMembersCapturedInMission.put(squaddie3.getSerialNumber(), squaddie3);
        squadronMembersMaimedInMission.put(squaddie4.getSerialNumber(), squaddie4);

        Ace ace1 = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        squadronMembersCapturedInMission.put(ace1.getSerialNumber(), ace1);
        
        Mockito.when(personnelLossesInMissionData.getPersonnelKilled()).thenReturn(squadronMembersKilledInMission);
        Mockito.when(personnelLossesInMissionData.getPersonnelCaptured()).thenReturn(squadronMembersCapturedInMission);
        Mockito.when(personnelLossesInMissionData.getPersonnelMaimed()).thenReturn(squadronMembersMaimedInMission);
        Mockito.when(personnelLossesInMissionData.getPersonnelWounded()).thenReturn(squadronMembersWoundedInMission);
        Mockito.when(personnelLossesInMissionData.getAcesKilled()).thenReturn(acesKilledMissionSquadronInMission);
    }
    
	@Test
	public void testPilotAndPlayerKilled() throws PWCGException
	{
        SquadronMember player = MissionEntityBuilder.makeSquadronMemberWithStatus("PLayer Pilot", SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1, SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        squadronMembersKilledInMission.put(player.getSerialNumber(), player);

        PilotStatusEventGenerator inMissionSquadronPilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        Map<Integer, PilotStatusEvent> pilotsLostInMission = inMissionSquadronPilotStatusEventGenerator.createPilotLossEvents(personnelLossesInMissionData);
        
        assert(pilotsLostInMission.size() == 6);
	}
    
    @Test
    public void testPilotAndPlayerMaimed() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        SquadronMember player = MissionEntityBuilder.makeSquadronMemberWithStatus("PLayer Pilot", SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        squadronMembersMaimedInMission.put(player.getSerialNumber(), player);

        PilotStatusEventGenerator inMissionSquadronPilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        Map<Integer, PilotStatusEvent> pilotsLostInMission = inMissionSquadronPilotStatusEventGenerator.createPilotLossEvents(personnelLossesInMissionData);
        
        assert(pilotsLostInMission.size() == 6);
    }
    
    @Test
    public void testPilotAndPlayerWounded() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 14);
        SquadronMember player = MissionEntityBuilder.makeSquadronMemberWithStatus("PLayer Pilot", SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1, SquadronMemberStatus.STATUS_WOUNDED, campaign.getDate(), returnDate);
        squadronMembersWoundedInMission.put(player.getSerialNumber(), player);

        PilotStatusEventGenerator inMissionSquadronPilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        Map<Integer, PilotStatusEvent> pilotsLostInMission = inMissionSquadronPilotStatusEventGenerator.createPilotLossEvents(personnelLossesInMissionData);
        
        assert(pilotsLostInMission.size() == 5);
    }
    
	@Test
	public void testPilotAndPlayerOK() throws PWCGException
	{
        PilotStatusEventGenerator inMissionSquadronPilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        Map<Integer, PilotStatusEvent> pilotsLostInMission = inMissionSquadronPilotStatusEventGenerator.createPilotLossEvents(personnelLossesInMissionData);
        
        assert(pilotsLostInMission.size() == 5);
	}
}
