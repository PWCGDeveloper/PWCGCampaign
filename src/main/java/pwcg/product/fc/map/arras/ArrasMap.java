package pwcg.product.fc.map.arras;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.country.FCServiceManager;

public class ArrasMap extends PWCGMap
{

    public ArrasMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = ARRAS_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.ARRAS_MAP;
        
        if (missionOptions == null)
        {
            missionOptions = new ArrasMissionOptions();
        }

        if (mapWeather == null)
        {
            this.mapWeather = new ArrasMapWeather();
        }
        
        frontParameters = new ArrasFrontParameters();
        buildArmedServicesActiveForMap();
        
        super.configure();
    }
    
    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(FCServiceManager.AVIATION_MILITAIRE_BELGE);
        armedServicesActiveForMap.add(FCServiceManager.LAVIATION_MILITAIRE);
        armedServicesActiveForMap.add(FCServiceManager.RAF);
        armedServicesActiveForMap.add(FCServiceManager.RFC);
        armedServicesActiveForMap.add(FCServiceManager.RNAS);
        armedServicesActiveForMap.add(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19170801"), DateUtils.getDateYYYYMMDD("19181201"));

        this.frontDatesForMap.addFrontDate("19170801");
        /*
        this.frontDatesForMap.addFrontDate("19180326");
        this.frontDatesForMap.addFrontDate("19180404");
        this.frontDatesForMap.addFrontDate("19180419");
        this.frontDatesForMap.addFrontDate("19180429");
        this.frontDatesForMap.addFrontDate("19180604");
        this.frontDatesForMap.addFrontDate("19180612");
        this.frontDatesForMap.addFrontDate("19180717");
        this.frontDatesForMap.addFrontDate("19180925");
        this.frontDatesForMap.addFrontDate("19181005");
        this.frontDatesForMap.addFrontDate("19181015");
        this.frontDatesForMap.addFrontDate("19181025");
        this.frontDatesForMap.addFrontDate("19181105");
        this.frontDatesForMap.addFrontDate("19181110");
        */
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
