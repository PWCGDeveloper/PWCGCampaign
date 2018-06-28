package pwcg.campaign.squadron;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class SquadronMovingFrontOverlay
{
	private String name = "";
	private int squadronId;
	private Map<Date, String> airfields = new TreeMap<>();

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSquadronId()
	{
		return squadronId;
	}

	public void setSquadronId(int squadronId)
	{
		this.squadronId = squadronId;
	}

	public Map<Date, String> getAirfields()
	{
		return airfields;
	}

	public void setAirfields(Map<Date, String> airfields)
	{
		this.airfields = airfields;
	}

}
