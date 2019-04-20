package pwcg.aar.tabulate.campaignupdate;

import java.util.ArrayList;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARLogEvents;
import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.outofmission.phase1.elapsedtime.ElapsedTimeEventGenerator;
import pwcg.aar.outofmission.phase1.elapsedtime.ElapsedTimeEvents;
import pwcg.aar.tabulate.combatreport.UICombatReportData;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class CampaignLogGenerator
{
	private Campaign campaign;
    private AARContext aarContext;
    private AARLogEvents campaignLogEvents;
	
	public CampaignLogGenerator(Campaign campaign, AARContext aarContext)
	{
		this.campaign = campaign;
        this.aarContext = aarContext;
		this.campaignLogEvents = new AARLogEvents(campaign);
	}
	
	public AARLogEvents createCampaignLogEventsForCampaignUpdate() throws PWCGException
	{
		createPilotVictoryEvents();
		createPilotMedalEvents();
		createPilotPromotionEvents();
        createPilotLossEvents();
        createEquipmentLossEvents();
		createTransfersEvents();
		createElapsedTimeEvents();
		
		return campaignLogEvents;
	}

	private void createPilotVictoryEvents() throws PWCGException
    {
	    for (UICombatReportData combatReportData : aarContext.getAarTabulatedData().getUiCombatReportData())
	    {
	        campaignLogEvents.addEvents(combatReportData.getCombatReportPanelData().getVictoriesForSquadronMembersInMission());	        
	    }
    }
	
    private void createPilotMedalEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getAarTabulatedData().getUiDebriefData();
        campaignLogEvents.addEvents(debriefData.getMedalPanelData().getMedalsAwarded());
    }
	
    private void createPilotPromotionEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getAarTabulatedData().getUiDebriefData();
        campaignLogEvents.addEvents(debriefData.getPromotionPanelData().getPromotionEventsDuringElapsedTime());
    }

    private void createPilotLossEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getAarTabulatedData().getUiDebriefData();
        campaignLogEvents.addEvents(new ArrayList<PilotStatusEvent>(debriefData.getPilotLossPanelData().getSquadMembersLost().values()));
    }
    
    private void createEquipmentLossEvents()
    {
        UIDebriefData debriefData = aarContext.getAarTabulatedData().getUiDebriefData();
        campaignLogEvents.addEvents(new ArrayList<PlaneStatusEvent>(debriefData.getEquipmentLossPanelData().getEquipmentLost().values()));
    }

    private void createTransfersEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getAarTabulatedData().getUiDebriefData();
        campaignLogEvents.addEvents(debriefData.getTransferPanelData().getTransfers());
    }

    private void createElapsedTimeEvents() throws PWCGException
    {
        if (aarContext.getNewDate() != null)
        {
            ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);  
            ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
            for (SquadronMoveEvent squadronMoveEvent : elapsedTimeEvents.getSquadronMoveEvents())
            {
                campaignLogEvents.addEvent(squadronMoveEvent);
            }

            if (elapsedTimeEvents.getEndOfWarEvent() != null)
            {
                campaignLogEvents.addEvent(elapsedTimeEvents.getEndOfWarEvent());
            }
        }
    }
}
