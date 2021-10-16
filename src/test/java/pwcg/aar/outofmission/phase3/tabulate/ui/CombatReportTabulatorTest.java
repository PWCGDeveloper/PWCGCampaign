package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.data.AARContextEventSequence;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.tabulate.combatreport.AARCombatReportTabulator;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.PilotStatusEventGenerator;
import pwcg.aar.ui.events.PlaneStatusEventGenerator;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CombatReportTabulatorTest extends AARTestSetup
{
    @Mock private SquadronMembers campaignMembersInMission;
    @Mock private PilotStatusEventGenerator pilotStatusEventGenerator;
    @Mock private PlaneStatusEventGenerator planeStatusEventGenerator;
    @Mock private VictoryEventGenerator victoryEventGenerator;
    @Mock private AARMissionEvaluationData missionEvaluationData;
    @Mock private AARPersonnelAcheivements personnelAcheivements;
    @Mock private Victory victory;

    private Map<Integer, SquadronMember> campaignMembersInMissionMap = new HashMap<>();

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        setupAARMocks();
    }
    
    @Test
    public void combatReportTabulationTest () throws PWCGException
    {             
        List<SquadronMember> crews = new ArrayList<>();
        crews.add(pilot1);
        
        campaignMembersInMissionMap.put(pilot1.getSerialNumber(), pilot1);

        Mockito.when(missionHeader.getMissionFileName()).thenReturn("MissionFileName");
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(aarContext.getMissionEvaluationData()).thenReturn(missionEvaluationData);
        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(preliminaryData.getPwcgMissionData()).thenReturn(pwcgMissionData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
        Mockito.when(campaignMembersInMission.getSquadronMemberCollection()).thenReturn(campaignMembersInMissionMap);
        Mockito.when(missionEvaluationData.wasPilotInMission(Mockito.anyInt())).thenReturn(true);
        
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        
        boolean isNewsWorthy = false;
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(campaign, "Any Plane", player.getSquadronId(), player.getSerialNumber(), campaign.getDate(), isNewsWorthy);

        List<ClaimDeniedEvent> claimsDenied = new ArrayList<>();
        claimsDenied.add(claimDenied);
        Mockito.when(aarContext.getPersonnelAcheivements().getPlayerClaimsDenied()).thenReturn(claimsDenied);

        isNewsWorthy = true;
        List<VictoryEvent> victories = new ArrayList<>();
        VictoryEvent victoryEvent = new VictoryEvent(campaign, victory, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), pilot1.getSerialNumber(), campaign.getDate(), isNewsWorthy);        
        victories.add(victoryEvent);
        VictoryEvent victoryEventNotFromSquadron = new VictoryEvent(campaign, victory, SquadronTestProfile.ESC_3_PROFILE.getSquadronId(), pilot2.getSerialNumber(), campaign.getDate(), isNewsWorthy);        
        victories.add(victoryEventNotFromSquadron);
        Mockito.when(victoryEventGenerator.createPilotVictoryEvents(ArgumentMatchers.<Map<Integer, List<Victory>>>any())).thenReturn(victories);
                
        isNewsWorthy = true;
        PilotStatusEvent pilotStatusEvent = new PilotStatusEvent(campaign, SquadronMemberStatus.STATUS_KIA, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), pilot1.getSerialNumber(), campaign.getDate(), isNewsWorthy);
        PilotStatusEvent pilotStatusEventNotFromSquadron = new PilotStatusEvent(campaign, SquadronMemberStatus.STATUS_KIA, SquadronTestProfile.ESC_3_PROFILE.getSquadronId(), pilot2.getSerialNumber(), campaign.getDate(), isNewsWorthy);
        Map<Integer, PilotStatusEvent> pilotsLost = new HashMap<>();
        pilotsLost.put(pilot1.getSerialNumber(), pilotStatusEvent);
        pilotsLost.put(pilot2.getSerialNumber(), pilotStatusEventNotFromSquadron);
        Mockito.when(pilotStatusEventGenerator.createPilotLossEvents(ArgumentMatchers.<AARPersonnelLosses>any())).thenReturn(pilotsLost);

        boolean isNewsworthy = true;
        LogPlane logPlane = new LogPlane(AARContextEventSequence.getNextOutOfMissionEventSequenceNumber());
        logPlane.initializeFromOutOfMission(campaign, plane1, pilot1);
        PlaneStatusEvent planeStatusEvent = new PlaneStatusEvent(campaign, logPlane, PlaneStatus.STATUS_DESTROYED, isNewsworthy);

        LogPlane logPlaneNotFromSquadron = new LogPlane(AARContextEventSequence.getNextOutOfMissionEventSequenceNumber());
        logPlaneNotFromSquadron.initializeFromOutOfMission(campaign, plane2, pilot2);
        PlaneStatusEvent planeStatusEventNotFromSquadron = new PlaneStatusEvent(campaign, logPlane, PlaneStatus.STATUS_DESTROYED, isNewsworthy);

        Map<Integer, PlaneStatusEvent> planesLost = new HashMap<>();
        planesLost.put(plane1.getSerialNumber(), planeStatusEvent);
        planesLost.put(plane2.getSerialNumber(), planeStatusEventNotFromSquadron);
        Mockito.when(planeStatusEventGenerator.createPlaneLossEvents(ArgumentMatchers.<AAREquipmentLosses>any())).thenReturn(planesLost);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        AARCombatReportTabulator combatReportPanelEventTabulator = new AARCombatReportTabulator(campaign, squadron, aarContext);
        combatReportPanelEventTabulator.setPilotStatusEventGenerator(pilotStatusEventGenerator);
        combatReportPanelEventTabulator.setPlaneStatusEventGenerator(planeStatusEventGenerator);
        combatReportPanelEventTabulator.setVictoryEventGenerator(victoryEventGenerator);
        AARCombatReportPanelData combatReportPanelData = combatReportPanelEventTabulator.tabulateForAARCombatReportPanel();

        Assertions.assertTrue (combatReportPanelData.getClaimsDenied().size() == 1);
        Assertions.assertTrue (combatReportPanelData.getCrewsInMission().size() == 1);
        Assertions.assertTrue (combatReportPanelData.getMissionHeader().getMissionFileName().equals("MissionFileName"));
        Assertions.assertTrue (combatReportPanelData.getSquadronMembersLostInMission().size() == 1);
        Assertions.assertTrue (combatReportPanelData.getSquadronPlanesLostInMission().size() == 1);
        Assertions.assertTrue (combatReportPanelData.getVictoriesForSquadronMembersInMission().size() == 1);

    }

}
