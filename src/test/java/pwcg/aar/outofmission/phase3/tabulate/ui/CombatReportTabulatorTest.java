package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AARTestSetup;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
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
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CombatReportTabulatorTest extends AARTestSetup
{
    @Mock private ReconciledVictoryData reconciledVictoryData;
    @Mock private SquadronMembers campaignMembersInMission;
    @Mock private PilotStatusEventGenerator pilotStatusEventGenerator;
    @Mock private PlaneStatusEventGenerator planeStatusEventGenerator;
    @Mock private VictoryEventGenerator victoryEventGenerator;

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

        List<ClaimDeniedEvent> claimsDenied = new ArrayList<>();
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        claimDenied.setPilotName("Any Pilot");
        claimsDenied.add(claimDenied);
        Mockito.when(reconciledVictoryData.getPlayerClaimsDenied()).thenReturn(claimsDenied);
        Mockito.when(reconciledInMissionData.getReconciledVictoryData()).thenReturn(reconciledVictoryData);
        Mockito.when(aarContext.getReconciledInMissionData()).thenReturn(reconciledInMissionData);

        List<VictoryEvent> victories = new ArrayList<>();
        VictoryEvent victoriesForPilot = new VictoryEvent(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        victoriesForPilot.setPilotName("Any Pilot");
        victories.add(victoriesForPilot);
        Mockito.when(victoryEventGenerator.createPilotVictoryEvents(Matchers.<Map<Integer, List<Victory>>>any())).thenReturn(victories);
        
        Map<Integer, PilotStatusEvent> pilotsLost = new HashMap<>();
        PilotStatusEvent pilotStatusEvent = new PilotStatusEvent(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        pilotStatusEvent.setPilotName("Any Pilot");
        pilotsLost.put(pilot1.getSerialNumber(), pilotStatusEvent);
        Mockito.when(pilotStatusEventGenerator.createPilotLossEvents(Matchers.<AARPersonnelLosses>any())).thenReturn(pilotsLost);
        
        
        Map<Integer, PlaneStatusEvent> planesLost = new HashMap<>();
        PlaneStatusEvent planeStatusEvent = new PlaneStatusEvent(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        planeStatusEvent.setPlaneSerialNumber(99999);
        planesLost.put(plane1.getSerialNumber(), planeStatusEvent);
        Mockito.when(planeStatusEventGenerator.createPlaneLossEvents(Matchers.<AAREquipmentLosses>any())).thenReturn(planesLost);

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
