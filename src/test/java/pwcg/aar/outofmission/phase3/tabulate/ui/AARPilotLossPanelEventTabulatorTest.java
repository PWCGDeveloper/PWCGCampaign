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
import pwcg.aar.tabulate.debrief.PilotLossPanelEventTabulator;
import pwcg.aar.ui.display.model.AARPilotLossPanelData;
import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.MissionEntityBuilder;

@RunWith(MockitoJUnitRunner.class)
public class AARPilotLossPanelEventTabulatorTest extends AARTestSetup
{
    private Map<Integer, SquadronMember> squadronMembersKilledInMission = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersCapturedInMission = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersMaimedInMission = new HashMap<>();
    private Map<Integer, Ace> acesKilledMissionSquadronInMission = new HashMap<>();

    private Map<Integer, SquadronMember> squadronMembersKilledOutMission = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersCapturedOutMission = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersMaimedOutMission = new HashMap<>();
    private Map<Integer, Ace> acesKilledMissionSquadronOutMission = new HashMap<>();

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        setupAARMocks();
        
        squadronMembersKilledInMission.clear();
        squadronMembersCapturedInMission.clear();
        squadronMembersMaimedInMission.clear();
        acesKilledMissionSquadronInMission.clear();
        
        squadronMembersKilledOutMission.clear();
        squadronMembersCapturedOutMission.clear();
        squadronMembersMaimedOutMission.clear();
        acesKilledMissionSquadronOutMission.clear();
        
        Mockito.when(personnelLossesInMissionData.getPersonnelKilled()).thenReturn(squadronMembersKilledInMission);
        Mockito.when(personnelLossesInMissionData.getPersonnelCaptured()).thenReturn(squadronMembersCapturedInMission);
        Mockito.when(personnelLossesInMissionData.getPersonnelMaimed()).thenReturn(squadronMembersMaimedInMission);
        Mockito.when(personnelLossesInMissionData.getAcesKilled()).thenReturn(acesKilledMissionSquadronInMission);

        Mockito.when(personnelLossesOutOfMissionData.getPersonnelKilled()).thenReturn(squadronMembersKilledOutMission);
        Mockito.when(personnelLossesOutOfMissionData.getPersonnelCaptured()).thenReturn(squadronMembersCapturedOutMission);
        Mockito.when(personnelLossesOutOfMissionData.getPersonnelMaimed()).thenReturn(squadronMembersMaimedOutMission);
        Mockito.when(personnelLossesOutOfMissionData.getAcesKilled()).thenReturn(acesKilledMissionSquadronOutMission);
    }

    @Test
    public void testMergedSquadronMembersLost() throws PWCGException 
    {
        SquadronMember squaddie1 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie A", SerialNumber.AI_STARTING_SERIAL_NUMBER+1, SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        SquadronMember squaddie2 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie B", SerialNumber.AI_STARTING_SERIAL_NUMBER+2, SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        SquadronMember squaddie3 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie C", SerialNumber.AI_STARTING_SERIAL_NUMBER+3, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        Ace ace1 = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());

        squadronMembersKilledOutMission.put(squaddie1.getSerialNumber(), squaddie1);
        squadronMembersCapturedOutMission.put(squaddie2.getSerialNumber(), squaddie2);
        squadronMembersMaimedOutMission.put(squaddie3.getSerialNumber(), squaddie3);
        acesKilledMissionSquadronOutMission.put(ace1.getSerialNumber(), ace1);

        SquadronMember squaddie4 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie D", SerialNumber.AI_STARTING_SERIAL_NUMBER+4, SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        SquadronMember squaddie5 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie E", SerialNumber.AI_STARTING_SERIAL_NUMBER+5, SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);        
        SquadronMember squaddie6 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie F", SerialNumber.AI_STARTING_SERIAL_NUMBER+6, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        Ace ace2 = MissionEntityBuilder.makeDeadAceWithVictories("Ace B", SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());

        squadronMembersKilledOutMission.put(squaddie4.getSerialNumber(), squaddie4);
        squadronMembersCapturedOutMission.put(squaddie5.getSerialNumber(), squaddie5);
        squadronMembersMaimedOutMission.put(squaddie6.getSerialNumber(), squaddie6);
        acesKilledMissionSquadronOutMission.put(ace2.getSerialNumber(), ace2);
        
        PilotLossPanelEventTabulator pilotLossPanelEventTabulator = new PilotLossPanelEventTabulator(campaign, aarContext);
        AARPilotLossPanelData pilotLossPanelData = pilotLossPanelEventTabulator.tabulateForAARPilotLossPanel();
        
        assert(pilotLossPanelData.getSquadMembersLost().size() == 8);
    }
}
