package pwcg.aar.tabulate.campaignupdate;

import java.util.ArrayList;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARLogEvents;
import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEventGenerator;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEvents;
import pwcg.aar.ui.display.model.AARElapsedTimeCombatResultsData;
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
    private AARElapsedTimeCombatResultsData elapsedTimeCombatResultsData;
	
	public CampaignLogGenerator(Campaign campaign, AARContext aarContext, AARElapsedTimeCombatResultsData elapsedTimeCombatResultsData)
	{
		this.campaign = campaign;
        this.aarContext = aarContext;
        this.elapsedTimeCombatResultsData = elapsedTimeCombatResultsData;
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
        campaignLogEvents.addEvents(elapsedTimeCombatResultsData.getVictories());          
    }

    private void createPilotLossEvents() throws PWCGException
    {
        campaignLogEvents.addEvents(new ArrayList<PilotStatusEvent>(elapsedTimeCombatResultsData.getPilotsLost()));
    }
    
    private void createEquipmentLossEvents()
    {
        campaignLogEvents.addEvents(new ArrayList<PlaneStatusEvent>(elapsedTimeCombatResultsData.getPlanesLost()));
    }
    
    private void createPilotMedalEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getUiDebriefData();
        campaignLogEvents.addEvents(debriefData.getMedalPanelData().getMedalsAwarded());
    }
    
    private void createPilotPromotionEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getUiDebriefData();
        campaignLogEvents.addEvents(debriefData.getPromotionPanelData().getPromotionEventsDuringElapsedTime());
    }

    private void createTransfersEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getUiDebriefData();
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
