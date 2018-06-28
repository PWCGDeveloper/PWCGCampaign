package pwcg.aar.tabulate.combatreport;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.PilotStatusEventGenerator;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;

public class CombatReportTabulator 
{
    private Campaign campaign;
    private AARContext aarContext;
    
    private PilotStatusEventGenerator pilotStatusEventGenerator;
    private VictoryEventGenerator victoryEventGenerator;
    private AARCombatReportPanelData combatReportData = new AARCombatReportPanelData();
    
    public CombatReportTabulator (Campaign campaign, AARContext aarContext)
    {
        this.aarContext = aarContext;
        this.campaign = campaign;
        
        pilotStatusEventGenerator = new PilotStatusEventGenerator(campaign);
        victoryEventGenerator = new VictoryEventGenerator(campaign);
    }
        
    public AARCombatReportPanelData tabulateForAARCombatReportPanel() throws PWCGException
    {
        createCrewsInMission();
        createDeniedClaims();
        extractMissionHeader();
        createLossesForPilotsInMission();
        createVictoryEventsForSquadronMembersInMission();
        return combatReportData;
    }

    private void createDeniedClaims()
    {
        combatReportData.addClaimsDenied(aarContext.getReconciledInMissionData().getReconciledVictoryData().getPlayerClaimsDenied());        
    }

    private void createCrewsInMission() throws PWCGException
    {
        Map<Integer, SquadronMember> campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission().getSquadronMembers();
        Map<Integer, SquadronMember> squadronMembersInMission = SquadronMemberFilter.filterActiveAIAndPlayerAndAcesForSquadron(campaignMembersInMission, campaign.getDate(), campaign.getSquadronId());
        combatReportData.addPilotsInMission(squadronMembersInMission);
    }

    private void extractMissionHeader()
    {
        combatReportData.setMissionAARHeader(aarContext.getPreliminaryData().getPwcgMissionData().getMissionHeader());
    }

    private void createLossesForPilotsInMission() throws PWCGException
    {
        Map<Integer, PilotStatusEvent> pilotsLostInMission = pilotStatusEventGenerator.createPilotLossEvents(aarContext.getReconciledInMissionData().getPersonnelLosses());
        for (PilotStatusEvent pilotLostEvent : pilotsLostInMission.values())
        {
            if (pilotLostEvent.getPilot().getSquadronId() == campaign.getSquadronId())
            {
                combatReportData.addPilotLostInMission(pilotLostEvent);
            }
        }
    }

    private void createVictoryEventsForSquadronMembersInMission() throws PWCGException
    {
        Map<Integer, List<Victory>> victoryAwardByPilot = aarContext.getReconciledInMissionData().getReconciledVictoryData().getVictoryAwardsByPilot();
        List<VictoryEvent> victoriesInMission = victoryEventGenerator.createPilotVictoryEvents(victoryAwardByPilot);
        for (VictoryEvent victoryEvent : victoriesInMission)
        {
            if (victoryEvent.getPilot().getSquadronId() == campaign.getSquadronId())
            {
                combatReportData.addVictoryForSquadronMembers(victoryEvent);
            }
        }
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
