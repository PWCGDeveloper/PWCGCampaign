package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CampaignLog
{
	Date date;
	List<CampaignLogEntry> logs = new ArrayList<>();

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public List<CampaignLogEntry> getLogs()
	{
		return logs;
	}

	public void addLog(String logEntryText, int squadronId)
	{
	    CampaignLogEntry logEntry = new CampaignLogEntry();
	    logEntry.setLog(logEntryText);
	    logEntry.setSquadronId(squadronId);
        logs.add(logEntry);
	}

	
}
