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
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class CampaignLogs
{
	Map<String, CampaignLog> campaignLogs = new TreeMap<>();

	public CampaignLogs()
	{
	}

	public void setCampaignLogs(Campaign campaign, List<AAREvent> eventList) throws PWCGException
	{
		for (AAREvent event : eventList)
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
				addVictoryToCampaignLogs(event);
			}
		}
	}

	private void addAircraftIntroducedToCampaignLogs(AAREvent event)
	{
		AircraftIntroducedEvent logEvent = (AircraftIntroducedEvent) event;
		String planeName = logEvent.getAircraft();
		String logEntry = "The " + planeName + " has been introduced to the squadron inventory";
		addCampaignLog(logEvent.getDate(), logEntry);
	}

	private void addAircraftRetiredToCampaignLogs(AAREvent event)
	{
		AircraftRetiredEvent logEvent = (AircraftRetiredEvent) event;
		String planeName = logEvent.getAircraft();
		String logEntry = "The " + planeName + " has been retired from the squadron inventory";
		addCampaignLog(logEvent.getDate(), logEntry);
	}

	private void addClaimDeniedToCampaignLogs(Campaign campaign, AAREvent event) throws PWCGException
	{
		SquadronMember player = campaign.getPlayer();

		ClaimDeniedEvent logEvent = (ClaimDeniedEvent) event;
		String planeName = logEvent.getType();
		String pilotName = player.getRank() + " " + player.getName();
		String logEntry = "A claim for a  " + planeName + ", made by " + pilotName + ",  has been denied";
		addCampaignLog(logEvent.getDate(), logEntry);
	}

	private void addMedalToCampaignLogs(AAREvent event)
	{
		MedalEvent logEvent = (MedalEvent) event;
		String medalName = logEvent.getMedal();
		String pilotName = logEvent.getPilot().getRank() + " " + logEvent.getPilot().getName();
		String logEntry = "The  " + medalName + " has been awarded to " + pilotName;
		addCampaignLog(logEvent.getDate(), logEntry);
	}

	private void addSquadronMoveToCampaignLogs(Campaign campaign, AAREvent event) throws PWCGException
	{
		SquadronMoveEvent logEvent = (SquadronMoveEvent) event;
		Squadron squadron = logEvent.getSquadron();

		String airfieldName = logEvent.getNewAirfield().getName();
		String logEntry = squadron.determineDisplayName(campaign.getDate()) + " has moved to " + airfieldName;
		addCampaignLog(logEvent.getDate(), logEntry);
	}

	private void addPromotionToCampaignLogs(AAREvent event)
	{
		PromotionEvent logEvent = (PromotionEvent) event;
		String newRankName = logEvent.getNewRank();
		String pilotName = logEvent.getPilot().getName();
		String logEntry = pilotName + " has been promoted to " + newRankName;
		addCampaignLog(logEvent.getDate(), logEntry);
	}

	private void addPilotlostToCampaignLogs(AAREvent event)
	{
		PilotStatusEvent logEvent = (PilotStatusEvent) event;
		if (logEvent.getStatus() == SquadronMemberStatus.STATUS_KIA)
		{
			String pilotName = logEvent.getPilot().getRank() + " " + logEvent.getPilot().getName();
			String logEntry = pilotName + " has been killed in action";
			addCampaignLog(logEvent.getDate(), logEntry);
		}
		else if (logEvent.getStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
		{
			String pilotName = logEvent.getPilot().getRank() + " " + logEvent.getPilot().getName();
			String logEntry = pilotName + " has been seriously wounded in action";
			addCampaignLog(logEvent.getDate(), logEntry);
		}
		else if (logEvent.getStatus() == SquadronMemberStatus.STATUS_WOUNDED)
		{
			String pilotName = logEvent.getPilot().getRank() + " " + logEvent.getPilot().getName();
			String logEntry = pilotName + " has been wounded in action";
			addCampaignLog(logEvent.getDate(), logEntry);
		}
		else if (logEvent.getStatus() == SquadronMemberStatus.STATUS_CAPTURED)
		{
			String pilotName = logEvent.getPilot().getRank() + " " + logEvent.getPilot().getName();
			String logEntry = pilotName + " has been made a prisoner of war";
			addCampaignLog(logEvent.getDate(), logEntry);
		}
	}

	private void addTransferToCampaignLogs(AAREvent event)
	{
		TransferEvent logEvent = (TransferEvent) event;
		String pilotName = logEvent.getPilot().getRank() + " " + logEvent.getPilot().getName();
		String logEntry = pilotName + " has transferred out of the squadron";
		if (logEvent.isTransferIn())
		{
			logEntry = pilotName + " has transferred into the squadron";
		}
		addCampaignLog(logEvent.getDate(), logEntry);
	}

	private void addVictoryToCampaignLogs(AAREvent event) throws PWCGException
	{
		VictoryEvent logEvent = (VictoryEvent) event;
		String logEntry = logEvent.getVictory().createVictoryDescription();
		addCampaignLog(logEvent.getDate(), logEntry);
	}

	private void addAceKilledToCampaignLogs(AAREvent event)
	{
		AceKilledEvent logEvent = (AceKilledEvent) event;
		String aceName = logEvent.getPilot().determineRankAbbrev() + " " + logEvent.getPilot().getName();
		String logEntry = "The great ace " + aceName + " was reported to be killed in action today";
		addCampaignLog(logEvent.getDate(), logEntry);
	}

	public void addCampaignLog(Date date, String logEntry)
	{
		Logger.log(LogLevel.DEBUG, "Added log on date " + date + "     " + logEntry);
		if (date != null)
		{
			String dateString = DateUtils.getDateStringYYYYMMDD(date);

			if (!campaignLogs.containsKey(dateString))
			{
				CampaignLog logsForThisDate = new CampaignLog();
				logsForThisDate.setDate(date);
				campaignLogs.put(dateString, logsForThisDate);
			}

			CampaignLog logsForThisDate = campaignLogs.get(DateUtils.getDateStringYYYYMMDD(date));
			logsForThisDate.addLog(logEntry);
		}
	}

	public List<CampaignLog> retrieveCampaignLogsInDateOrder()
	{
		return new ArrayList<>(campaignLogs.values());
	}

	public void setCampaignLogs(Map<String, CampaignLog> campaignLogs)
	{
		this.campaignLogs = campaignLogs;
	}

	public Map<String, CampaignLog> getCampaignLogs()
	{
		return campaignLogs;
	}
}
