package pwcg.product.bos.map.stalingrad;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.BoSServiceManager;

public class StalingradMap extends PWCGMap
{

    public StalingradMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = STALINGRAD_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.STALINGRAD_MAP;
        
        if (missionOptions == null)
        {
            missionOptions = new StalingradMissionOptions();
        }

        if (mapWeather == null)
        {
            this.mapWeather = new StalingradMapWeather();
        }
        
        frontParameters = new StalingradFrontParameters();
        buildArmedServicesActiveForMap();
        
        super.configure();
    }
    
    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(BoSServiceManager.VVS);
        armedServicesActiveForMap.add(BoSServiceManager.NORMANDIE);
        armedServicesActiveForMap.add(BoSServiceManager.LUFTWAFFE);
        armedServicesActiveForMap.add(BoSServiceManager.REGIA_AERONAUTICA);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19420301"), DateUtils.getDateYYYYMMDD("19430202"));

        this.frontDatesForMap.addFrontDate("19420301");
        this.frontDatesForMap.addFrontDate("19420801");
        this.frontDatesForMap.addFrontDate("19420906");
        this.frontDatesForMap.addFrontDate("19421011");
        this.frontDatesForMap.addFrontDate("19421123");
        this.frontDatesForMap.addFrontDate("19421223");
        this.frontDatesForMap.addFrontDate("19430120");
    }

    @Override
    public ICountry getGroundCountryForMapBySide(Side side)
    {
        if (side == Side.ALLIED)
        {
            return CountryFactory.makeCountryByCountry(Country.RUSSIA);
        }
        else
        {
            return CountryFactory.makeCountryByCountry(Country.GERMANY);
        }
    }
}
