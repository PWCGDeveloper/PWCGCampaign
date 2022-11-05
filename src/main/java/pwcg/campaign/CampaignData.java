package pwcg.campaign;

import java.util.Date;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.squadmember.SerialNumber;

public class CampaignData
{
    private String name = "";
	private Date date = null;
    private boolean isCoop = false;
    private CampaignMode campaignMode = CampaignMode.CAMPAIGN_MODE_NONE;
    private FrontMapIdentifier initialMap = FrontMapIdentifier.NO_MAP;
    private SerialNumber serialNumber = new SerialNumber();
    private int referencePlayerSerialNumber = 0;

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

    public SerialNumber getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(SerialNumber serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public CampaignMode getCampaignMode()
    {
        return campaignMode;
    }

    public void setCampaignMode(CampaignMode campaignMode)
    {
        this.campaignMode = campaignMode;
    }

    public boolean isCoop()
    {
        return isCoop;
    }

    public int getReferencePlayerSerialNumber()
    {
        return referencePlayerSerialNumber;
    }

    public void setReferencePlayerSerialNumber(int referencePlayerSerialNumber)
    {
        this.referencePlayerSerialNumber = referencePlayerSerialNumber;
    }

    public FrontMapIdentifier getInitialMap()
    {
        return initialMap;
    }

    public void setInitialMap(FrontMapIdentifier initialMap)
    {
        this.initialMap = initialMap;
    }
}
