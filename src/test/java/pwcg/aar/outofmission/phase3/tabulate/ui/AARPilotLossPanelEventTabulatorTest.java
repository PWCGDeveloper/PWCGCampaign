package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.AARTestSetup;
import pwcg.aar.data.AARContext;
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

@RunWith(MockitoJUnitRunner.Silent.class) 
public class AARPilotLossPanelEventTabulatorTest extends AARTestSetup
{
    private Map<Integer, SquadronMember> squadronMembersKilled = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersCaptured = new HashMap<>();
    private Map<Integer, SquadronMember> squadronMembersMaimed = new HashMap<>();
    private Map<Integer, SquadronMember> acesKilledMissionSquadron = new HashMap<>();
    
    @Mock
    private AARContext aarContext;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        setupAARMocks();
        
        squadronMembersKilled.clear();
        squadronMembersCaptured.clear();
        squadronMembersMaimed.clear();
        acesKilledMissionSquadron.clear();
 
        Mockito.when(personnelLosses.getPersonnelKilled()).thenReturn(squadronMembersKilled);
        Mockito.when(personnelLosses.getPersonnelCaptured()).thenReturn(squadronMembersCaptured);
        Mockito.when(personnelLosses.getPersonnelMaimed()).thenReturn(squadronMembersMaimed);
        Mockito.when(personnelLosses.getAcesKilled()).thenReturn(acesKilledMissionSquadron);
        
        Mockito.when(aarContext.getPersonnelLosses()).thenReturn(personnelLosses);
    }

    @Test
    public void testMergedSquadronMembersLost() throws PWCGException 
    {
        SquadronMember squaddie1 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie A", SerialNumber.AI_STARTING_SERIAL_NUMBER+1, SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        SquadronMember squaddie2 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie B", SerialNumber.AI_STARTING_SERIAL_NUMBER+2, SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        SquadronMember squaddie3 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie C", SerialNumber.AI_STARTING_SERIAL_NUMBER+3, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        Ace ace1 = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());

        squadronMembersKilled.put(squaddie1.getSerialNumber(), squaddie1);
        squadronMembersCaptured.put(squaddie2.getSerialNumber(), squaddie2);
        squadronMembersMaimed.put(squaddie3.getSerialNumber(), squaddie3);
        acesKilledMissionSquadron.put(ace1.getSerialNumber(), ace1);

        SquadronMember squaddie4 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie D", SerialNumber.AI_STARTING_SERIAL_NUMBER+4, SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        SquadronMember squaddie5 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie E", SerialNumber.AI_STARTING_SERIAL_NUMBER+5, SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);        
        SquadronMember squaddie6 = MissionEntityBuilder.makeSquadronMemberWithStatus("Squaddie F", SerialNumber.AI_STARTING_SERIAL_NUMBER+6, SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        Ace ace2 = MissionEntityBuilder.makeDeadAceWithVictories("Ace B", SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());

        squadronMembersKilled.put(squaddie4.getSerialNumber(), squaddie4);
        squadronMembersCaptured.put(squaddie5.getSerialNumber(), squaddie5);
        squadronMembersMaimed.put(squaddie6.getSerialNumber(), squaddie6);
        acesKilledMissionSquadron.put(ace2.getSerialNumber(), ace2);
        
        PilotLossPanelEventTabulator pilotLossPanelEventTabulator = new PilotLossPanelEventTabulator(campaign, aarContext);
        AARPilotLossPanelData pilotLossPanelData = pilotLossPanelEventTabulator.tabulateForAARPilotLossPanel();
        
        assert(pilotLossPanelData.getSquadMembersLost().size() == 8);
    }
}
