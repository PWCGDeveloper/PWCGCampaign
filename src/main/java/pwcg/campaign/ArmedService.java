package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
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
    private double dailyPersonnelReplacementRatePerSquadron = 1.5;
    private double dailyEquipmentReplacementRatePerSquadron = 3.0;
    private ArmedServiceQualitySet serviceQuality = new ArmedServiceQualitySet();
    private IServiceColorMap serviceColorMap;
    private List<String> picDirs = new ArrayList<String>();
    private ICountry nameCountry = CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    private int airVictoriesForgreatAce = 30;
    private int groundVictoriesForgreatAce = 100;
    private ArmedServiceType armedServiceType = ArmedServiceType.ARMED_SERVICE_AIR;

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
		serviceQuality.addQuality(this, qualityDate, qualityValue);
	}

	public void setDailyEquipmentReplacementRatePerSquadron(double dailyEquipmentReplacementRatePerSquadron)
    {
        this.dailyEquipmentReplacementRatePerSquadron = dailyEquipmentReplacementRatePerSquadron;
    }

	public void setDailyPersonnelReplacementRatePerSquadron(double dailyPersonnelReplacementRatePerSquadron)
    {
        this.dailyPersonnelReplacementRatePerSquadron = dailyPersonnelReplacementRatePerSquadron;
    }

    public int getDailyEquipmentReplacementRate(Date date) throws PWCGException
    {
        List<Company> squadrons = PWCGContext.getInstance().getCompanyManager().getActiveCompaniesForService(date, this);
        double dailyEquipmentReplacementRateForThisDate = dailyEquipmentReplacementRatePerSquadron * squadrons.size();
        return Double.valueOf(dailyEquipmentReplacementRateForThisDate).intValue();
    }

    public int getDailyPersonnelReplacementRate(Date date) throws PWCGException
    {
        List<Company> squadrons = PWCGContext.getInstance().getCompanyManager().getActiveCompaniesForService(date, this);
        double dailyEquipmentReplacementRateForThisDate = dailyPersonnelReplacementRatePerSquadron * squadrons.size();
        return Double.valueOf(dailyEquipmentReplacementRateForThisDate).intValue();
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

    public int getAirVictoriesForgreatAce()
    {
        return airVictoriesForgreatAce;
    }

    public void setAirVictoriesForgreatAce(int airVictoriesForgreatAce)
    {
        this.airVictoriesForgreatAce = airVictoriesForgreatAce;
    }

    public int getGroundVictoriesForgreatAce()
    {
        return groundVictoriesForgreatAce;
    }

    public void setGroundVictoriesForgreatAce(int groundVictoriesForgreatAce)
    {
        this.groundVictoriesForgreatAce = groundVictoriesForgreatAce;
    }

    public ArmedServiceType getArmedServiceType()
    {
        return armedServiceType;
    }

    public void setArmedServiceType(ArmedServiceType armedServiceType)
    {
        this.armedServiceType = armedServiceType;
    }
}
