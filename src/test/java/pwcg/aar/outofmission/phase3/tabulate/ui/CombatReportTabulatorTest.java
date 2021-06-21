package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.AARTestSetup;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.tabulate.combatreport.CombatReportTabulator;
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
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.Silent.class) 
public class CombatReportTabulatorTest extends AARTestSetup
{
    @Mock private ReconciledMissionVictoryData reconciledVictoryData;
    @Mock private SquadronMembers campaignMembersInMission;
    @Mock private PilotStatusEventGenerator pilotStatusEventGenerator;
    @Mock private PlaneStatusEventGenerator planeStatusEventGenerator;
    @Mock private VictoryEventGenerator victoryEventGenerator;
    @Mock private Victory victory;

    private Map<Integer, SquadronMember> campaignMembersInMissionMap = new HashMap<>();

    @Before
    public void setupForTestEnvironment() throws PWCGException
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
        Mockito.when(preliminaryData.getPwcgMissionData()).thenReturn(pwcgMissionData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
        Mockito.when(campaignMembersInMission.getSquadronMemberCollection()).thenReturn(campaignMembersInMissionMap);
        
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        
        boolean isNewsWorthy = false;
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(campaign, "Any Plane", player.getSquadronId(), player.getSerialNumber(), campaign.getDate(), isNewsWorthy);

        List<ClaimDeniedEvent> claimsDenied = new ArrayList<>();
        claimsDenied.add(claimDenied);
        Mockito.when(reconciledVictoryData.getPlayerClaimsDenied()).thenReturn(claimsDenied);
        Mockito.when(aarContext.getReconciledMissionVictoryData()).thenReturn(reconciledVictoryData);

        List<VictoryEvent> victories = new ArrayList<>();
        isNewsWorthy = true;
        VictoryEvent victoryEvent = new VictoryEvent(campaign, victory, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), SerialNumber.AI_STARTING_SERIAL_NUMBER, campaign.getDate(), isNewsWorthy);        
        victories.add(victoryEvent);
        Mockito.when(victoryEventGenerator.createPilotVictoryEvents(ArgumentMatchers.<Map<Integer, List<Victory>>>any())).thenReturn(victories);
                
        isNewsWorthy = true;
        PilotStatusEvent pilotStatusEvent = new PilotStatusEvent(campaign, SquadronMemberStatus.STATUS_KIA, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), SerialNumber.AI_STARTING_SERIAL_NUMBER, campaign.getDate(), isNewsWorthy);

        Map<Integer, PilotStatusEvent> pilotsLost = new HashMap<>();
        pilotsLost.put(pilot1.getSerialNumber(), pilotStatusEvent);
        Mockito.when(pilotStatusEventGenerator.createPilotLossEvents(ArgumentMatchers.<AARPersonnelLosses>any())).thenReturn(pilotsLost);

        boolean isNewsworthy = true;
        LogPlane logPlane = new LogPlane(aarContext.getNextOutOfMissionEventSequenceNumber());
        logPlane.initializeFromOutOfMission(campaign, plane1, pilot1);
        
        PlaneStatusEvent planeStatusEvent = new PlaneStatusEvent(campaign, logPlane, PlaneStatus.STATUS_DESTROYED, isNewsworthy);

        Map<Integer, PlaneStatusEvent> planesLost = new HashMap<>();
        planesLost.put(plane1.getSerialNumber(), planeStatusEvent);
        Mockito.when(planeStatusEventGenerator.createPlaneLossEvents(ArgumentMatchers.<AAREquipmentLosses>any())).thenReturn(planesLost);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        CombatReportTabulator combatReportPanelEventTabulator = new CombatReportTabulator(campaign, squadron, aarContext);
        combatReportPanelEventTabulator.setPilotStatusEventGenerator(pilotStatusEventGenerator);
        combatReportPanelEventTabulator.setPlaneStatusEventGenerator(planeStatusEventGenerator);
        combatReportPanelEventTabulator.setVictoryEventGenerator(victoryEventGenerator);
        AARCombatReportPanelData combatReportPanelData = combatReportPanelEventTabulator.tabulateForAARCombatReportPanel();

        assert (combatReportPanelData.getClaimsDenied().size() == 1);
        assert (combatReportPanelData.getCrewsInMission().size() == 1);
        assert (combatReportPanelData.getMissionHeader().getMissionFileName().equals("MissionFileName"));
        assert (combatReportPanelData.getSquadronMembersLostInMission().size() == 1);
        assert (combatReportPanelData.getSquadronPlanesLostInMission().size() == 1);
        assert (combatReportPanelData.getVictoriesForSquadronMembersInMission().size() == 1);

    }

}
