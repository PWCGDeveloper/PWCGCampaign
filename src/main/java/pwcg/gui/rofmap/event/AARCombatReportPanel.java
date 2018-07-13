package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.util.Date;
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
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.AAREventPanel;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.mission.data.MissionHeader;

public class AARCombatReportPanel extends AAREventPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private AARCoordinator aarCoordinator;

    private CombatReportPanel campaignCombatReportGUI = null;

	public AARCombatReportPanel(Campaign campaign)
	{
        super();
        this.campaign = campaign;		
        this.shouldDisplay = true;
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanel()  
	{
        try
        {
            createCombatReportGUI();
            createPostCombatReportTabs();

        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
	
    private void createPostCombatReportTabs()
    {
        ImageResizingPanel postCombatPanel = new ImageResizingPanel(ContextSpecificImages.imagesMisc() + "PaperFull.jpg");
        postCombatPanel.setLayout(new BorderLayout());
        postCombatPanel.add(campaignCombatReportGUI, BorderLayout.CENTER);
        this.add(postCombatPanel, BorderLayout.CENTER);
    }

    private void createCombatReportGUI() throws PWCGException
    {
        CombatReport combatReport = startAARProcess ();
        
        campaignCombatReportGUI = new CombatReportPanel (combatReport);
        campaignCombatReportGUI.makeGUI();
    }

	private CombatReport startAARProcess() throws PWCGException 
	{                
        CombatReport combatReport = createCombatReport();

        String missionStatement = "";
		missionStatement += createCrewsInMissionReport();
        missionStatement += createFlownFromReport();
		missionStatement += createClaimStatusReport();
        missionStatement += createPilotLostReport();
        missionStatement += createEquipmentLostReport();
		
        combatReport.setHaReport(missionStatement);

        String narrativeStatement = "";
		combatReport.setNarrative(narrativeStatement);
		
		return combatReport;
	}

    private CombatReport createCombatReport() throws PWCGException
    {
        CombatReport combatReport = new CombatReport();
        combatReport.setSquadron(campaign.determineSquadron().determineDisplayName(campaign.getDate()));
        
        MissionHeader missionHeader = aarCoordinator.getAarContext().getUiCombatReportData().getCombatReportPanelData().getMissionHeader();
        
        Date combatReportDate = DateUtils.getDateYYYYMMDD(missionHeader.getDate());
        
        combatReport.setDate(combatReportDate);
        combatReport.setTime(missionHeader.getTime());
        
        SquadronMember player = campaign.getPlayer();
        combatReport.setPilot(player.getNameAndRank());
        
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
            victoryAppend += victoryEvent.getVictory().createVictoryDescription() + "\n\n";
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

	public void finished() 
	{
		try
		{
	        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
			campaignCombatReportGUI.writeCombatReport(campaign);
		}
		catch (Exception e)
		{	
			Logger.logException(e);
		}
	}

}
