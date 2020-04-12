package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.colors.IServiceColorMap;

public class ArmedService
{

    private int serviceId;
    private String generalRankForService;
    private String serviceIcon;
    private ICountry country;
    private String name;
    private Date startDate;
    private Date endDate;
    private int dailyPersonnelReplacementRate = 15;
    private int dailyEquipmentReplacementRate = 30;
    private ArmedServiceQualitySet serviceQuality = new ArmedServiceQualitySet();
    private IServiceColorMap serviceColorMap;
    private List<String> picDirs = new ArrayList<String>();
    private ICountry nameCountry = CountryFactory.makeCountryByCountry(Country.NEUTRAL);

    public ArmedService() throws PWCGException 
    {
        startDate = DateUtils.getBeginningOfGame();
        endDate = DateUtils.getEndOfWar();
    }
        
    public int getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(int serviceId)
    {
        this.serviceId = serviceId;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getServiceIcon()
    {
        return serviceIcon;
    }

    public void setServiceIcon(String serviceIcon)
    {
        this.serviceIcon = serviceIcon;
    }

    public Date getServiceStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getServiceEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public List<String> getPicDirs()
    {
        return picDirs;
    }

    public void setPicDirs(List<String> picDirs)
    {
        this.picDirs = picDirs;
    }

    public void setServiceColorMap(IServiceColorMap serviceColorMap)
    {
        this.serviceColorMap = serviceColorMap;
    }

    public IServiceColorMap getServiceColorMap()
    {
        return serviceColorMap;
    }

    public String getGeneralRankForService()
    {
        return generalRankForService;
    }

    public void setGeneralRankForService(String generalRankForService)
    {
        this.generalRankForService = generalRankForService;
    }

	public ArmedServiceQualitySet getServiceQuality()
	{
		return serviceQuality;
	}

	public void addServiceQuality(Date qualityDate, int qualityValue)
	{
		serviceQuality.addQuality(this, qualityDate, qualityValue);;
	}

    public int getDailyPersonnelReplacementRate()
    {
        return dailyPersonnelReplacementRate;
    }

    public void setDailyPersonnelReplacementRate(int dailyPersonnelReplacementRate)
    {
        this.dailyPersonnelReplacementRate = dailyPersonnelReplacementRate;
    }

    public int getDailyEquipmentReplacementRate()
    {
        return dailyEquipmentReplacementRate;
    }

    public void setDailyEquipmentReplacementRate(int dailyEquipmentReplacementRate)
    {
        this.dailyEquipmentReplacementRate = dailyEquipmentReplacementRate;
    }

    public ICountry getNameCountry()
    {
        if (nameCountry.getCountry() != Country.NEUTRAL)
        {
            return nameCountry;
        }
        else
        {
            return country;
        }
    }

    public void setNameCountry(ICountry nameCountry)
    {
        this.nameCountry = nameCountry;
    }

    public boolean isActive(Date date) throws PWCGException
    {
        if (DateUtils.isDateInRange(date, startDate, endDate))
        {
            return true;
        }
        return false;
    }
}
