package pwcg.campaign;

import java.util.Date;

import pwcg.campaign.squadmember.SerialNumber;

public class CampaignData
{

	private Date date = null;
	private String name = "";
    private int squadId = -1;
    private boolean isCoop = false;
    private SerialNumber serialNumber = new SerialNumber();

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSquadId()
	{
		return squadId;
	}

	public void setSquadId(int squadId)
	{
		this.squadId = squadId;
	}
	
    public boolean isCoop()
    {
        return isCoop;
    }

    public void setCoop(boolean isCoop)
    {
        this.isCoop = isCoop;
    }

    public SerialNumber getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(SerialNumber serialNumber)
    {
        this.serialNumber = serialNumber;
    }
	
	
}
