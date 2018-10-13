package pwcg.gui.display.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.VictoryDescription;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;

public class CombatReportBuilder
{
    private Campaign campaign;
    private SquadronMember reportSquadronMember;
    private AARCoordinator aarCoordinator;
    private CombatReport combatReport = new CombatReport();

    public CombatReportBuilder(Campaign campaign, SquadronMember reportSquadronMember, AARCoordinator aarCoordinator)
    {
        this.campaign = campaign;
        this.reportSquadronMember = reportSquadronMember;
        this.aarCoordinator = aarCoordinator;
    }
    
    public CombatReport createCombatReport() throws PWCGException 
    {                
        createCombatReportHeader();

        String missionStatement = createCombatReportMissionStatement();
        combatReport.setHaReport(missionStatement);

        String narrativeStatement = "";
        combatReport.setNarrative(narrativeStatement);
        
        return combatReport;
    }

    
    private CombatReport createCombatReportHeader() throws PWCGException
    {
        combatReport.setPilotSerialNumber(reportSquadronMember.getSerialNumber());
        combatReport.setReportPilotName(reportSquadronMember.getNameAndRank());
        combatReport.setSquadron(reportSquadronMember.determineSquadron().determineDisplayName(campaign.getDate()));
        
        MissionHeader missionHeader = aarCoordinator.getAarContext().findUiCombatReportDataForSquadron(reportSquadronMember.getSquadronId()).
                        getCombatReportPanelData().getMissionHeader();
        Date combatReportDate = DateUtils.getDateYYYYMMDD(missionHeader.getDate());
        
        combatReport.setDate(combatReportDate);
        combatReport.setTime(missionHeader.getTime());
        
        if (campaign.isCoop())
        {
            setPilotsForCoop();            
        }
        else
        {
            setPilotsForSinglePlayer();
        }
        
        combatReport.setType(missionHeader.getAircraftType());
        combatReport.setDuty(missionHeader.getDuty());

        int altitude = missionHeader.getAltitude();
        String altString = " meters";
        
        ICountry personnelCountry = reportSquadronMember.determineSquadron().getCountry();
        if (personnelCountry.isCountry(Country.BRITAIN))
        {
            altitude = altitude * 3;
            altString = " ft";
        }

        combatReport.setAltitude(altitude + altString);
        return combatReport;
    }

    private void setPilotsForSinglePlayer() throws PWCGException
    {
        combatReport.setReportPilotName(reportSquadronMember.getNameAndRank());
    }   

    private void setPilotsForCoop()
    {
        SquadronMembers campaignMembersInMission = aarCoordinator.getAarContext().getPreliminaryData().getCampaignMembersInMission();
        for (SquadronMember squadronMember : campaignMembersInMission.getSquadronMemberList())
        {
            if (squadronMember.getSquadronId() == reportSquadronMember.getSquadronId())
            {
                combatReport.addFlightPilot(squadronMember.getNameAndRank());
            }
        }
    }   

    private String createCombatReportMissionStatement() throws PWCGException
    {
        String missionStatement = "";
        missionStatement += createCrewsInMissionReport();
        missionStatement += createFlownFromReport();
        missionStatement += createClaimStatusReport();
        missionStatement += createPilotLostReport();
        missionStatement += createEquipmentLostReport();
        return missionStatement;
    }


    private String createFlownFromReport() throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        
        String flownFromStatement = "\n";

        flownFromStatement += " The mission was flown from " + reportSquadronMember.determineSquadron().determineCurrentAirfieldName(campaign.getDate()) + " aerodrome.\n";

        flownFromStatement += "\n";
        return flownFromStatement;
    }

    private String createPilotLostReport() throws PWCGException
    {
        Map<Integer, PilotStatusEvent> squadronMembersLostInMission = aarCoordinator.getAarContext()
                        .findUiCombatReportDataForSquadron(reportSquadronMember.getSquadronId()).
                        getCombatReportPanelData().getSquadronMembersLostInMission();

        String pilotsLostStatement = "";
        String pilotsLostAppend = "";            
        for (PilotStatusEvent pilotLostEvent : squadronMembersLostInMission.values())
        {
            pilotsLostAppend += "    " + 
                            pilotLostEvent.getPilotName() + ": " + 
                            SquadronMemberStatus.pilotStatusToStatusDescription(pilotLostEvent.getStatus()) + "\n";
        }
        
        if (pilotsLostAppend.length() > 0)
        {
            pilotsLostStatement = "Pilots lost: \n" + pilotsLostAppend;
        }

        return pilotsLostStatement;
    }


    private String createEquipmentLostReport() throws PWCGException
    {
        Map<Integer, PlaneStatusEvent> squadronPlanesLostInMission = aarCoordinator.getAarContext()
                    .findUiCombatReportDataForSquadron(reportSquadronMember.getSquadronId()).
                    getCombatReportPanelData().getSquadronPlanesLostInMission();

        String planesLostStatement = "";
        String planesLostAppend = "";        
        for (PlaneStatusEvent planeLostEvent :squadronPlanesLostInMission.values())
        {
            EquippedPlane lostPlane = campaign.getEquipmentManager().getPlaneFromAnySquadron(planeLostEvent.getPlaneSerialNumber());
            planesLostAppend += "    " + lostPlane.getDisplayName() + ": " + lostPlane.getDisplayMarkings() + "\n";
        }
        
        if (planesLostAppend.length() > 0)
        {
            planesLostStatement = "Aircraft lost: \n" + planesLostAppend;
        }

        return planesLostStatement;
    }
    
    private String createCrewsInMissionReport() throws PWCGException
    {        
        Map<Integer, SquadronMember> pilotsInMission = aarCoordinator.getAarContext().
                        findUiCombatReportDataForSquadron(reportSquadronMember.getSquadronId()).getCombatReportPanelData().getCrewsInMission();

        String missionStatement;
        missionStatement = "This mission was flown by:\n";
        for (SquadronMember pilotSquadronMember : pilotsInMission.values())
        {
            if (pilotSquadronMember != null)
            {
                missionStatement += pilotSquadronMember.getNameAndRank();
                missionStatement += "\n";
            }

        }
        return missionStatement;
    }

    private String createClaimStatusReport() throws PWCGException
    {
        List<VictoryEvent> victoryEvents = aarCoordinator.getAarContext().
                        findUiCombatReportDataForSquadron(reportSquadronMember.getSquadronId()).getCombatReportPanelData().getVictoriesForSquadronMembersInMission();

        String claimStatusStatement = "";
        String victoryAppend = "";
        for (VictoryEvent victoryEvent : victoryEvents)
        {            
            VictoryDescription victoryDescription = new VictoryDescription(campaign, victoryEvent.getVictory());
            String victoryDescriptionText = victoryDescription.createVictoryDescription();
            victoryAppend += victoryDescriptionText + "\n\n";
        }
        
        
        List<ClaimDeniedEvent> claimDeniedEvents = aarCoordinator.getAarContext().
                        findUiCombatReportDataForSquadron(reportSquadronMember.getSquadronId()).getCombatReportPanelData().getClaimsDenied();

        for (ClaimDeniedEvent claimDeniedEvent : claimDeniedEvents)
        {
            victoryAppend += "    Claim denied for " + claimDeniedEvent.getType() + "\n\n";               
        }

        if (victoryAppend.length() > 0)
        {
            claimStatusStatement = "Claim Status: \n" + victoryAppend;
        }

        claimStatusStatement += "\n";

        return claimStatusStatement; 
    }

}
