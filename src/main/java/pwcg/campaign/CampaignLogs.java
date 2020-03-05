package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.aar.ui.events.model.AAREvent;
import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.aar.ui.events.model.AircraftIntroducedEvent;
import pwcg.aar.ui.events.model.AircraftRetiredEvent;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.aar.ui.events.model.MedalEvent;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PromotionEvent;
import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.VictoryDescription;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class CampaignLogs
{
	private Map<String, CampaignLog> campaignLogsByDate = new TreeMap<>();

	public CampaignLogs()
	{
	}

	public void parseEventsToCampaignLogs(Campaign campaign, Map<Integer, AAREvent> eventList) throws PWCGException
	{
		for (AAREvent event : eventList.values())
		{
			if (event instanceof AceKilledEvent)
			{
				addAceKilledToCampaignLogs(event);
			}
			else if (event instanceof AircraftIntroducedEvent)
			{
				addAircraftIntroducedToCampaignLogs(event);
			}
			else if (event instanceof AircraftRetiredEvent)
			{
				addAircraftRetiredToCampaignLogs(event);
			}
			else if (event instanceof ClaimDeniedEvent)
			{
				addClaimDeniedToCampaignLogs(campaign, event);
			}
			else if (event instanceof MedalEvent)
			{
				addMedalToCampaignLogs(event);
			}
			else if (event instanceof SquadronMoveEvent)
			{
				addSquadronMoveToCampaignLogs(campaign, event);
			}
			else if (event instanceof PromotionEvent)
			{
				addPromotionToCampaignLogs(event);
			}
			else if (event instanceof PilotStatusEvent)
			{
				addPilotlostToCampaignLogs(event);
			}
			else if (event instanceof TransferEvent)
			{
				addTransferToCampaignLogs(event);
			}
			else if (event instanceof VictoryEvent)
			{
				addVictoryToCampaignLogs(campaign, event);
			}
		}
	}

	private void addAircraftIntroducedToCampaignLogs(AAREvent event)
	{
		AircraftIntroducedEvent logEvent = (AircraftIntroducedEvent) event;
		String planeName = logEvent.getAircraft();
		String logEntry = "The " + planeName + " has been introduced to the squadron inventory";
		addCampaignLog(logEvent.getDate(), logEntry, Squadron.SQUADRON_ID_ANY);
	}

	private void addAircraftRetiredToCampaignLogs(AAREvent event)
	{
		AircraftRetiredEvent logEvent = (AircraftRetiredEvent) event;
		String planeName = logEvent.getAircraft();
		String logEntry = "The " + planeName + " has been retired from the squadron inventory";
		addCampaignLog(logEvent.getDate(), logEntry, Squadron.SQUADRON_ID_ANY);
	}

	private void addClaimDeniedToCampaignLogs(Campaign campaign, AAREvent event) throws PWCGException
	{
		ClaimDeniedEvent logEvent = (ClaimDeniedEvent) event;
		String planeName = logEvent.getType();
		String logEntry = "A claim for a  " + planeName + ", made by " + logEvent.getPilotName() + ",  has been denied";
		addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
	}

	private void addMedalToCampaignLogs(AAREvent event)
	{
		MedalEvent logEvent = (MedalEvent) event;
		String medalName = logEvent.getMedal();
		String logEntry = "The  " + medalName + " has been awarded to " + logEvent.getPilotName();
		addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
	}

	private void addSquadronMoveToCampaignLogs(Campaign campaign, AAREvent event) throws PWCGException
	{
		SquadronMoveEvent logEvent = (SquadronMoveEvent) event;
		String airfieldName = logEvent.getNewAirfield();
		String logEntry = logEvent.getSquadronName() + " has moved to " + airfieldName;
		addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
	}

	private void addPromotionToCampaignLogs(AAREvent event)
	{
		PromotionEvent logEvent = (PromotionEvent) event;
		String newRankName = logEvent.getNewRank();
		String logEntry = logEvent.getPilotName() + " has been promoted to " + newRankName;
		addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
	}

	private void addPilotlostToCampaignLogs(AAREvent event)
	{
		PilotStatusEvent logEvent = (PilotStatusEvent) event;
		if (logEvent.getStatus() == SquadronMemberStatus.STATUS_KIA)
		{
			String logEntry = logEvent.getPilotName() + " has been killed in action";
			addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
		}
		else if (logEvent.getStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
		{
			String logEntry = logEvent.getPilotName() + " has been seriously wounded in action";
			addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
		}
		else if (logEvent.getStatus() == SquadronMemberStatus.STATUS_WOUNDED)
		{
			String logEntry = logEvent.getPilotName() + " has been wounded in action";
			addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
		}
		else if (logEvent.getStatus() == SquadronMemberStatus.STATUS_CAPTURED)
		{
			String logEntry = logEvent.getPilotName() + " has been made a prisoner of war";
			addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
		}
	}

	private void addTransferToCampaignLogs(AAREvent event)
	{
		TransferEvent logEvent = (TransferEvent) event;
		String logEntry = logEvent.getPilotName() + " has transferred into the squadron";
		addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
	}

	private void addVictoryToCampaignLogs(Campaign campaign, AAREvent event) throws PWCGException
	{
		VictoryEvent logEvent = (VictoryEvent) event;
		VictoryDescription victoryDescription = new VictoryDescription(campaign, logEvent.getVictory());
		String logEntry = victoryDescription.createVictoryDescription();
		addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
	}

	private void addAceKilledToCampaignLogs(AAREvent event)
	{
		AceKilledEvent logEvent = (AceKilledEvent) event;
		String logEntry = "The great ace " + logEvent.getPilotName() + " was reported to be killed in action today";
		addCampaignLog(logEvent.getDate(), logEntry, logEvent.getSquadronId());
	}

	private void addCampaignLog(Date date, String logEntry, int squadronId)
	{
		Logger.log(LogLevel.DEBUG, "Added log on date " + date + "     " + logEntry);
		if (date != null)
		{
			String dateString = DateUtils.getDateStringYYYYMMDD(date);

			if (!campaignLogsByDate.containsKey(dateString))
			{
				CampaignLog logsForThisDate = new CampaignLog();
				logsForThisDate.setDate(date);
				campaignLogsByDate.put(dateString, logsForThisDate);
			}

			CampaignLog logsForThisDate = campaignLogsByDate.get(DateUtils.getDateStringYYYYMMDD(date));
			logsForThisDate.addLog(logEntry,squadronId);
		}
	}

	public List<CampaignLog> retrieveCampaignLogsInDateOrder()
	{
		return new ArrayList<>(campaignLogsByDate.values());
	}

	public void setCampaignLogs(Map<String, CampaignLog> campaignLogs)
	{
		this.campaignLogsByDate = campaignLogs;
	}

	public Map<String, CampaignLog> getCampaignLogs()
	{
		return campaignLogsByDate;
	}
}
