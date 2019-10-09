package pwcg.mission.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;

public class VehicleDefinition
{
    private String scriptDir;
    private String modelDir;
    private String vehicleType;
    private ICountry country;
    
    public VehicleDefinition(String scriptDir, String modelDir, String vehicleType, Country defaultCountry)
    {
        this.scriptDir = scriptDir;
        this.modelDir = modelDir;
        this.vehicleType = vehicleType;
        this.country = CountryFactory.makeCountryByCountry(defaultCountry);
    }
    
    public String getScriptDir()
    {
        return scriptDir;
    }

    public String getModelDir()
    {
        return modelDir;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public ICountry getCountry()
    {
        return country;
    }
}
