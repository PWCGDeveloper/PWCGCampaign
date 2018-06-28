package pwcg.aar.outofmission.phase3.tabulate.ui;

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
import pwcg.testutils.MissionEntityBuilder;

@RunWith(MockitoJUnitRunner.class)
public class InMissionSquadronPilotStatusEventGeneratorTest extends AARTestSetup
{	
    private Map<Integer, SquadronMember> squadronMembersKilledInMission = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersCapturedInMission = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersMaimedInMission = new HashMap<>();
    private Map<Integer, Ace> acesKilledMissionSquadronInMission = new HashMap<>();
	
    @Before
    public void setup() throws PWCGException
    {
        setupAARMocks();
        
        SquadronMember squaddie1 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie A", SerialNumber.AI_STARTING_SERIAL_NUMBER+1, SquadronMemberStatus.STATUS_KIA, campaign.getDate());
        SquadronMember squaddie2 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie B", SerialNumber.AI_STARTING_SERIAL_NUMBER+2, SquadronMemberStatus.STATUS_KIA, campaign.getDate());
        SquadronMember squaddie3 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie C", SerialNumber.AI_STARTING_SERIAL_NUMBER+3, SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate());
        SquadronMember squaddie4 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie D", SerialNumber.AI_STARTING_SERIAL_NUMBER+4, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate());

        squadronMembersKilledInMission.put(squaddie1.getSerialNumber(), squaddie1);
        squadronMembersKilledInMission.put(squaddie2.getSerialNumber(), squaddie2);
        squadronMembersCapturedInMission.put(squaddie3.getSerialNumber(), squaddie3);
        squadronMembersMaimedInMission.put(squaddie4.getSerialNumber(), squaddie4);

        Ace ace1 = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        squadronMembersCapturedInMission.put(ace1.getSerialNumber(), ace1);
        
        Mockito.when(personnelLossesInMissionData.getPersonnelKilled()).thenReturn(squadronMembersKilledInMission);
        Mockito.when(personnelLossesInMissionData.getPersonnelCaptured()).thenReturn(squadronMembersCapturedInMission);
        Mockito.when(personnelLossesInMissionData.getPersonnelMaimed()).thenReturn(squadronMembersMaimedInMission);
        Mockito.when(personnelLossesInMissionData.getAcesKilled()).thenReturn(acesKilledMissionSquadronInMission);
    }
    
	@Test
	public void testPilotAndPlayerKilled() throws PWCGException
	{
        Mockito.when(personnelLossesInMissionData.getPlayerStatus()).thenReturn(SquadronMemberStatus.STATUS_KIA);

        PilotStatusEventGenerator inMissionSquadronPilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        Map<Integer, PilotStatusEvent> pilotsLostInMission = inMissionSquadronPilotStatusEventGenerator.createPilotLossEvents(personnelLossesInMissionData);
        
        assert(pilotsLostInMission.size() == 6);
	}
    
	@Test
	public void testPilotAndPlayerWounded() throws PWCGException
	{
        Mockito.when(personnelLossesInMissionData.getPlayerStatus()).thenReturn(SquadronMemberStatus.STATUS_WOUNDED);

        PilotStatusEventGenerator inMissionSquadronPilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        Map<Integer, PilotStatusEvent> pilotsLostInMission = inMissionSquadronPilotStatusEventGenerator.createPilotLossEvents(personnelLossesInMissionData);
        
        assert(pilotsLostInMission.size() == 6);
	}
    
	@Test
	public void testPilotAndPlayerOK() throws PWCGException
	{
        Mockito.when(personnelLossesInMissionData.getPlayerStatus()).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);

        PilotStatusEventGenerator inMissionSquadronPilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        Map<Integer, PilotStatusEvent> pilotsLostInMission = inMissionSquadronPilotStatusEventGenerator.createPilotLossEvents(personnelLossesInMissionData);
        
        assert(pilotsLostInMission.size() == 5);
	}
}
