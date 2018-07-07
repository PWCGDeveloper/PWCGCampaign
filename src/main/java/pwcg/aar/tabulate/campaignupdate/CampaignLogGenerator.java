package pwcg.aar.tabulate.campaignupdate;

import java.util.ArrayList;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARLogEvents;
import pwcg.aar.outofmission.phase1.elapsedtime.ElapsedTimeEventGenerator;
import pwcg.aar.outofmission.phase1.elapsedtime.ElapsedTimeEvents;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class CampaignLogGenerator
{
	private Campaign campaign;
	private AARContext aarContext;
	private AARLogEvents campaignLogEvents = new AARLogEvents();
	
	public CampaignLogGenerator(Campaign campaign, AARContext aarContext)
	{
		this.campaign = campaign;
		this.aarContext = aarContext;
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

    private void createElapsedTimeEvents() throws PWCGException
	{
    	if (aarContext.getNewDate() != null)
    	{
	    	ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);	
	    	ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
	    	
	    	if (elapsedTimeEvents.getSquadronMoveEvent() != null)
	    	{
	    		campaignLogEvents.addEvent(elapsedTimeEvents.getSquadronMoveEvent());
	    	}
	    	
	    	if (elapsedTimeEvents.getEndOfWarEvent() != null)
	    	{
	    		campaignLogEvents.addEvent(elapsedTimeEvents.getEndOfWarEvent());
	    	}
    	}
	}

	private void createPilotVictoryEvents() throws PWCGException
    {
        campaignLogEvents.addEvents(aarContext.getUiCombatReportData().getCombatReportPanelData().getVictoriesForSquadronMembersInMission());
    }
	
    private void createPilotMedalEvents() throws PWCGException
    {
        campaignLogEvents.addEvents(aarContext.getUiDebriefData().getMedalPanelData().getMedalsAwarded());
    }
	
    private void createPilotPromotionEvents() throws PWCGException
    {
        campaignLogEvents.addEvents(aarContext.getUiDebriefData().getPromotionPanelData().getPromotionEventsDuringElapsedTime());
    }

    private void createPilotLossEvents() throws PWCGException
    {
        campaignLogEvents.addEvents(new ArrayList<PilotStatusEvent>(aarContext.getUiDebriefData().getPilotLossPanelData().getSquadMembersLost().values()));
    }
    
    private void createEquipmentLossEvents()
    {
        campaignLogEvents.addEvents(new ArrayList<PlaneStatusEvent>(aarContext.getUiDebriefData().getEquipmentLossPanelData().getEquipmentLost().values()));
    }

    private void createTransfersEvents() throws PWCGException
    {
        campaignLogEvents.addEvents(aarContext.getUiDebriefData().getTransferPanelData().getTransferOutOfSquadron());
        campaignLogEvents.addEvents(aarContext.getUiDebriefData().getTransferPanelData().getTransferIntoSquadron());
    }

}
