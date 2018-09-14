package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
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
    private AARCoordinator aarCoordinator;
    private CombatReport combatReport = new CombatReport();

    public CombatReportBuilder(Campaign campaign, AARCoordinator aarCoordinator)
    {
        this.campaign = campaign;
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
        combatReport.setSquadron(campaign.determineSquadron().determineDisplayName(campaign.getDate()));
        
        MissionHeader missionHeader = aarCoordinator.getAarContext().getUiCombatReportData().getCombatReportPanelData().getMissionHeader();
        
        Date combatReportDate = DateUtils.getDateYYYYMMDD(missionHeader.getDate());
        
        combatReport.setDate(combatReportDate);
        combatReport.setTime(missionHeader.getTime());
        
        if (campaign.getCampaignData().isCoop())
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
        
        ICountry personnelCountry = campaign.determineCountry();
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
        SquadronMember player = campaign.getPlayers().get(0);
        combatReport.setPilot(player.getNameAndRank());
    }   

    private void setPilotsForCoop()
    {
        List<String> squadroMemberInMissionNames = new ArrayList<>();
        SquadronMembers campaignMembersInMission = aarCoordinator.getAarContext().getPreliminaryData().getCampaignMembersInMission();
        for (SquadronMember squadronMember : campaignMembersInMission.getSquadronMemberList())
        {
            if (squadronMember.getSquadronId() == campaign.getSquadronId())
            {
                squadroMemberInMissionNames.add(squadronMember.getNameAndRank());
            }
        }
        combatReport.setPilots(squadroMemberInMissionNames);
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
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        
        String flownFromStatement = "\n";

        flownFromStatement += " The mission was flown from " + campaign.determineSquadron().determineCurrentAirfieldName(campaign.getDate()) + " aerodrome.\n";

        flownFromStatement += "\n";
        return flownFromStatement;
    }

    private String createPilotLostReport() throws PWCGException
    {
        String pilotsLostStatement = "";
        String pilotsLostAppend = "";
        for (PilotStatusEvent pilotLostEvent : aarCoordinator.getAarContext().getUiCombatReportData().getCombatReportPanelData().getSquadronMembersLostInMission().values())
        {
            pilotsLostAppend += "    " + 
                            pilotLostEvent.getPilot().getNameAndRank() + ": " + 
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
        String planesLostStatement = "";
        String planesLostAppend = "";
        for (PlaneStatusEvent planeLostEvent : aarCoordinator.getAarContext().getUiCombatReportData().getCombatReportPanelData().getSquadronPlanesLostInMission().values())
        {
            planesLostAppend += "    " + planeLostEvent.getPlane().getDisplayName() + ": " + planeLostEvent.getPlane().getSerialNumber() + "\n";
        }
        
        if (planesLostAppend.length() > 0)
        {
            planesLostStatement = "Aircraft lost: \n" + planesLostAppend;
        }

        return planesLostStatement;
    }
    
    private String createCrewsInMissionReport() throws PWCGException
    {
        String missionStatement;
        Map<Integer, SquadronMember> pilotsInMission = aarCoordinator.getAarContext().getUiCombatReportData().getCombatReportPanelData().getCrewsInMission();
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
        String claimStatusStatement = "";
        String victoryAppend = "";

        for (VictoryEvent victoryEvent :aarCoordinator.getAarContext().getUiCombatReportData().getCombatReportPanelData().getVictoriesForSquadronMembersInMission())
        {            
            VictoryDescription victoryDescription = new VictoryDescription(campaign, victoryEvent.getVictory());
            String victoryDescriptionText = victoryDescription.createVictoryDescription();
            victoryAppend += victoryDescriptionText + "\n\n";
        }
        
        
        for (ClaimDeniedEvent claimDeniedEvent : aarCoordinator.getAarContext().getUiCombatReportData().getCombatReportPanelData().getClaimsDenied())
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
