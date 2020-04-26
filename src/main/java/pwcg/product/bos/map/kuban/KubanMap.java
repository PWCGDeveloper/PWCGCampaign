package pwcg.product.bos.map.kuban;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.BoSServiceManager;

public class KubanMap extends PWCGMap
{

    public KubanMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = KUBAN_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.KUBAN_MAP;
        
        if (missionOptions == null)
        {
            missionOptions = new KubanMissionOptions();
        }

        if (mapWeather == null)
        {
            this.mapWeather = new KubanMapWeather();
        }

        frontParameters = new KubanFrontParameters();

        buildArmedServicesActiveForMap();
        
        super.configure();
    }
    
    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(BoSServiceManager.VVS);
        armedServicesActiveForMap.add(BoSServiceManager.NORMANDIE);
        armedServicesActiveForMap.add(BoSServiceManager.LUFTWAFFE);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19420601"), DateUtils.getDateYYYYMMDD("19431231"));
        
        this.frontDatesForMap.addFrontDate("19420601");
        this.frontDatesForMap.addFrontDate("19420624");
        this.frontDatesForMap.addFrontDate("19420709");
        this.frontDatesForMap.addFrontDate("19420721");
        this.frontDatesForMap.addFrontDate("19430301");
        this.frontDatesForMap.addFrontDate("19430330");
        this.frontDatesForMap.addFrontDate("19430418");
        this.frontDatesForMap.addFrontDate("19430918");
        this.frontDatesForMap.addFrontDate("19430927");
        this.frontDatesForMap.addFrontDate("19431004");
        this.frontDatesForMap.addFrontDate("19431008");
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
