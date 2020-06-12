package pwcg.campaign;

import java.util.Date;

import pwcg.core.constants.AiSkillLevel;

public class ArmedServiceQuality
{
    private static final int qualityRequiredForCommonAi = 55;
	private int serviceQualityValue;
	private Date qualityDate;
	
	public ArmedServiceQuality ()
	{
	}

	public int getQualityValue()
	{
		return serviceQualityValue;
	}

	public void setQualityValue(int qualityValue)
	{
		this.serviceQualityValue = qualityValue;
	}

	public Date getQualityDate()
	{
		return qualityDate;
	}

	public void setQualityDate(Date qualityDate)
	{
		this.qualityDate = qualityDate;
	}
	
	public AiSkillLevel getNewAiSkillLevelForQuality()
	{
	    if (serviceQualityValue > qualityRequiredForCommonAi)
	    {
	        return AiSkillLevel.COMMON;
	    }
	    else
	    {
	        return AiSkillLevel.NOVICE;
	    }
	}
}
