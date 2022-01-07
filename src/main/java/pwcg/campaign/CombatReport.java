package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CombatReport 
{
    private List<String> flightCrewMembers = new ArrayList<>();
    private Integer crewMemberSerialNumber = 0;
    private String reportCrewMemberName = "";
	private String squadron = "";
	private Date date;
	private String time = "";
	private String type = "";
	private String locality = "";
	private String duty = "";
	private String haReport = "";
	private String narrative = "";
		
	public CombatReport ()
	{
	}
	
	public void addFlightCrewMember(String crewMemberName)
	{
	    flightCrewMembers.add(crewMemberName);
	}

	public Integer getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public void setCrewMemberSerialNumber(Integer crewMemberSerialNumber)
    {
        this.crewMemberSerialNumber = crewMemberSerialNumber;
    }

    public List<String> getFlightCrewMembers() 
	{
		return flightCrewMembers;
	}

	public String getSquadron()
	{
		return squadron;
	}

	public void setSquadron(String squadron)
	{
		this.squadron = squadron;
	}

	public String getReportCrewMemberName() 
	{
		return reportCrewMemberName;
	}

	public void setReportCrewMemberName(String reportCrewMemberName) 
	{
		this.reportCrewMemberName = reportCrewMemberName;
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
}
