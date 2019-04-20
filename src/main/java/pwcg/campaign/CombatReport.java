package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CombatReport 
{
    private List<String> flightPilots = new ArrayList<>();
    private Integer pilotSerialNumber = 0;
    private String reportPilotName = "";
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
	
	public void addFlightPilot(String pilotName)
	{
	    flightPilots.add(pilotName);
	}

	public Integer getPilotSerialNumber()
    {
        return pilotSerialNumber;
    }

    public void setPilotSerialNumber(Integer pilotSerialNumber)
    {
        this.pilotSerialNumber = pilotSerialNumber;
    }

    public List<String> getFlightPilots() 
	{
		return flightPilots;
	}

	public String getSquadron()
	{
		return squadron;
	}

	public void setSquadron(String squadron)
	{
		this.squadron = squadron;
	}

	public String getReportPilotName() 
	{
		return reportPilotName;
	}

	public void setReportPilotName(String reportPilotName) 
	{
		this.reportPilotName = reportPilotName;
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
