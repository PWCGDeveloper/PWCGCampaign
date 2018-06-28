package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CampaignLog
{
	Date date;
	List<String> logs = new ArrayList<>();


	public void addLog(String log)
	{
		logs.add(log);
	}

	public CampaignLog()
	{
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public List<String> getLogs()
	{
		return logs;
	}

	public void setLogs(List<String> logs)
	{
		this.logs = logs;
	}

	
}
