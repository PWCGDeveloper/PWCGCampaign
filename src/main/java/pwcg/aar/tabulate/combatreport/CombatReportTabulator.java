package pwcg.aar.tabulate.combatreport;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.PilotStatusEventGenerator;
import pwcg.aar.ui.events.PlaneStatusEventGenerator;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CombatReportTabulator 
{
    private Campaign campaign;
    private Squadron squadron;
    private AARContext aarContext;
    
    private PilotStatusEventGenerator pilotStatusEventGenerator;
    private PlaneStatusEventGenerator planeStatusEventGenerator;
    private VictoryEventGenerator victoryEventGenerator;
    private AARCombatReportPanelData combatReportPanelData = new AARCombatReportPanelData();
    
    public CombatReportTabulator (Campaign campaign, Squadron squadron, AARContext aarContext)
    {
        this.aarContext = aarContext;
        this.campaign = campaign;
        this.squadron = squadron;
        
        pilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        planeStatusEventGenerator = new PlaneStatusEventGenerator(campaign);
        victoryEventGenerator = new VictoryEventGenerator(campaign);
    }
        
    public AARCombatReportPanelData tabulateForAARCombatReportPanel() throws PWCGException
    {
        createCrewsInMission();
        createDeniedClaims();
        extractMissionHeader();
        createLossesForPilotsInMission();
        createLossesForEquipmentInMission();
        createVictoryEventsForSquadronMembersInMission();
        return combatReportPanelData;
    }

    private void createDeniedClaims()
    {
        combatReportPanelData.addClaimsDenied(aarContext.getReconciledMissionVictoryData().getPlayerClaimsDenied());        
    }

    private void createCrewsInMission() throws PWCGException
    {
        Map<Integer, SquadronMember> campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission().getSquadronMemberCollection();
        SquadronMembers squadronMembersInMission = SquadronMemberFilter.filterActiveAIAndPlayerAndAcesForSquadron(campaignMembersInMission, campaign.getDate(), squadron.getSquadronId());
        combatReportPanelData.addPilotsInMission(squadronMembersInMission);
    }

    private void extractMissionHeader()
    {
        combatReportPanelData.setMissionAARHeader(aarContext.getPreliminaryData().getPwcgMissionData().getMissionHeader());
    }

    private void createLossesForPilotsInMission() throws PWCGException
    {
        Map<Integer, PilotStatusEvent> pilotsLostInMission = pilotStatusEventGenerator.createPilotLossEvents(aarContext.getPersonnelLosses());
        for (PilotStatusEvent pilotLostEvent : pilotsLostInMission.values())
        {
            if (pilotLostEvent.getSquadronId() == squadron.getSquadronId())
            {
                combatReportPanelData.addPilotLostInMission(pilotLostEvent);
            }
        }
    }

    private void createLossesForEquipmentInMission() throws PWCGException
    {
        Map<Integer, PlaneStatusEvent> planesLostInMission = planeStatusEventGenerator.createPlaneLossEvents(aarContext.getEquipmentLosses());
        for (PlaneStatusEvent planeLostEvent : planesLostInMission.values())
        {
            if (planeLostEvent.getSquadronId() == squadron.getSquadronId())
            {
                combatReportPanelData.addPlaneLostInMission(planeLostEvent);
            }
        }
    }

    private void createVictoryEventsForSquadronMembersInMission() throws PWCGException
    {
        Map<Integer, List<Victory>> victoryAwardByPilot = aarContext.getReconciledMissionVictoryData().getVictoryAwardsByPilot();
        List<VictoryEvent> victoriesInMission = victoryEventGenerator.createPilotVictoryEvents(victoryAwardByPilot);
        for (VictoryEvent victoryEvent : victoriesInMission)
        {
            if (victoryEvent.getSquadronId() == squadron.getSquadronId())
            {
                combatReportPanelData.addVictoryForSquadronMembers(victoryEvent);
            }
        }
    }

    public void setPlaneStatusEventGenerator(PlaneStatusEventGenerator planeStatusEventGenerator)
    {
        this.planeStatusEventGenerator = planeStatusEventGenerator;
    }

    public void setPilotStatusEventGenerator(PilotStatusEventGenerator pilotStatusEventGenerator)
    {
        this.pilotStatusEventGenerator = pilotStatusEventGenerator;
    }

    public void setVictoryEventGenerator(VictoryEventGenerator victoryEventGenerator)
    {
        this.victoryEventGenerator = victoryEventGenerator;
    }
}
