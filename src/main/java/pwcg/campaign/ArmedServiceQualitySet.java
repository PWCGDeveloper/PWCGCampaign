package pwcg.campaign;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class ArmedServiceQualitySet
{
	Map<Date, ArmedServiceQuality> qualitySet = new TreeMap<>();
	
	public void addQuality(ArmedService service, Date qualityDate, int qualityValue)
	{
		ArmedServiceQuality armedServiceQuality = new ArmedServiceQuality();
		armedServiceQuality.setQualityDate(qualityDate);
		armedServiceQuality.setQualityValue(qualityValue);
		
		qualitySet.put(qualityDate, armedServiceQuality);
	}
	
	public ArmedServiceQuality getQuality(Date qualityDate)
	{
		ArmedServiceQuality armedServiceQualityForDate = null;
		for (ArmedServiceQuality armedServiceQuality : qualitySet.values())
		{
			if (armedServiceQuality.getQualityDate().before(qualityDate))
			{
				armedServiceQualityForDate = armedServiceQuality;
			}
		}
		
		return armedServiceQualityForDate;
	}
}
