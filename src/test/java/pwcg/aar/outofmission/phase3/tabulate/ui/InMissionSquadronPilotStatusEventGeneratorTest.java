package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.aar.ui.events.CrewMemberStatusEventGenerator;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.MissionEntityBuilder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class InMissionSquadronCrewMemberStatusEventGeneratorTest extends AARTestSetup
{	
    private Map<Integer, CrewMember> squadronMembersKilledInMission = new HashMap<>();
    private Map<Integer, CrewMember> squadronMembersCapturedInMission = new HashMap<>();
    private Map<Integer, CrewMember> squadronMembersMaimedInMission = new HashMap<>();
    private Map<Integer, CrewMember> squadronMembersWoundedInMission = new HashMap<>();
    private Map<Integer, CrewMember> acesKilledMissionSquadronInMission = new HashMap<>();
	
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        squadronMembersKilledInMission = new HashMap<>();
        squadronMembersCapturedInMission = new HashMap<>();
        squadronMembersMaimedInMission = new HashMap<>();
        squadronMembersWoundedInMission = new HashMap<>();
        acesKilledMissionSquadronInMission = new HashMap<>();

        setupAARMocks();
        
        CrewMember squaddie1 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie A", SerialNumber.AI_STARTING_SERIAL_NUMBER+1, CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        CrewMember squaddie2 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie B", SerialNumber.AI_STARTING_SERIAL_NUMBER+2, CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        CrewMember squaddie3 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie C", SerialNumber.AI_STARTING_SERIAL_NUMBER+3, CrewMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        CrewMember squaddie4 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie D", SerialNumber.AI_STARTING_SERIAL_NUMBER+4, CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);

        squadronMembersKilledInMission.put(squaddie1.getSerialNumber(), squaddie1);
        squadronMembersKilledInMission.put(squaddie2.getSerialNumber(), squaddie2);
        squadronMembersCapturedInMission.put(squaddie3.getSerialNumber(), squaddie3);
        squadronMembersMaimedInMission.put(squaddie4.getSerialNumber(), squaddie4);

        TankAce ace1 = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        squadronMembersCapturedInMission.put(ace1.getSerialNumber(), ace1);
        
        Mockito.when(personnelLosses.getPersonnelKilled()).thenReturn(squadronMembersKilledInMission);
        Mockito.when(personnelLosses.getPersonnelCaptured()).thenReturn(squadronMembersCapturedInMission);
        Mockito.when(personnelLosses.getPersonnelMaimed()).thenReturn(squadronMembersMaimedInMission);
        Mockito.when(personnelLosses.getPersonnelWounded()).thenReturn(squadronMembersWoundedInMission);
        Mockito.when(personnelLosses.getAcesKilled(campaign)).thenReturn(acesKilledMissionSquadronInMission);
    }
    
	@Test
	public void testCrewMemberAndPlayerKilled() throws PWCGException
	{
        CrewMember player = MissionEntityBuilder.makeCrewMemberWithStatus("PLayer CrewMember", SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1, CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        squadronMembersKilledInMission.put(player.getSerialNumber(), player);

        CrewMemberStatusEventGenerator inMissionSquadronCrewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = inMissionSquadronCrewMemberStatusEventGenerator.createCrewMemberLossEvents(personnelLosses);
        
        assert(crewMembersLostInMission.size() == 6);
	}
    
    @Test
    public void testCrewMemberAndPlayerMaimed() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        CrewMember player = MissionEntityBuilder.makeCrewMemberWithStatus("PLayer CrewMember", SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1, CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        squadronMembersMaimedInMission.put(player.getSerialNumber(), player);

        CrewMemberStatusEventGenerator inMissionSquadronCrewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = inMissionSquadronCrewMemberStatusEventGenerator.createCrewMemberLossEvents(personnelLosses);
        
        assert(crewMembersLostInMission.size() == 6);
    }
    
    @Test
    public void testCrewMemberAndPlayerWounded() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 14);
        CrewMember player = MissionEntityBuilder.makeCrewMemberWithStatus("PLayer CrewMember", SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1, CrewMemberStatus.STATUS_WOUNDED, campaign.getDate(), returnDate);
        squadronMembersWoundedInMission.put(player.getSerialNumber(), player);

        CrewMemberStatusEventGenerator inMissionSquadronCrewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = inMissionSquadronCrewMemberStatusEventGenerator.createCrewMemberLossEvents(personnelLosses);
        
        assert(crewMembersLostInMission.size() == 5);
    }
    
	@Test
	public void testCrewMemberAndPlayerOK() throws PWCGException
	{
        CrewMemberStatusEventGenerator inMissionSquadronCrewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = inMissionSquadronCrewMemberStatusEventGenerator.createCrewMemberLossEvents(personnelLosses);
        
        assert(crewMembersLostInMission.size() == 5);
	}
}
