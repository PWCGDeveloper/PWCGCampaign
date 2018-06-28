package pwcg.campaign;

import java.util.Date;

public class CombatReport 
{
	private String pilot = "";
	private String squadron = "";
	private Date date;
	private String time = "";
	private String type = "";
	private String locality = "";
	private String duty = "";
	private String haReport = "";
	private String narrative = "";
	private String altitude = "";
		
	public CombatReport ()
	{
	}

	public String getPilot()
	{
		return pilot;
	}

	public void setPilot(String pilot)
	{
		this.pilot = pilot;
	}

	public String getSquadron()
	{
		return squadron;
	}

	public void setSquadron(String squadron)
	{
		this.squadron = squadron;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getLocality()
	{
		return locality;
	}

	public void setLocality(String locality)
	{
		this.locality = locality;
	}

	public String getDuty()
	{
		return duty;
	}

	public void setDuty(String duty)
	{
		this.duty = duty;
	}

	public String getHaReport()
	{
		return haReport;
	}

	public void setHaReport(String haReport)
	{
		this.haReport = haReport;
	}

	public String getNarrative()
	{
		return narrative;
	}

	public void setNarrative(String narrative)
	{
		this.narrative = narrative;
	}

	public String getAltitude()
	{
		return altitude;
	}

	public void setAltitude(String altitude)
	{
		this.altitude = altitude;
	}


}
