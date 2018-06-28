package pwcg.campaign;

import java.util.Date;

public class ArmedServiceQuality
{
	private int serviceQUalityValue;
	private Date qualityDate;
	
	public ArmedServiceQuality ()
	{
	}

	public int getQualityValue()
	{
		return serviceQUalityValue;
	}

	public void setQualityValue(int qualityValue)
	{
		this.serviceQUalityValue = qualityValue;
	}

	public Date getQualityDate()
	{
		return qualityDate;
	}

	public void setQualityDate(Date qualityDate)
	{
		this.qualityDate = qualityDate;
	}
}
