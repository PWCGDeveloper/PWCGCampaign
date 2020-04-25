package pwcg.product.bos.map.moscow;

import pwcg.campaign.context.PWCGMap;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.BoSServiceManager;

public class MoscowMap extends PWCGMap
{

    public MoscowMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = MOSCOW_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.MOSCOW_MAP;
        
        if (missionOptions == null)
        {
            missionOptions = new MoscowMissionOptions();
        }

        if (mapWeather == null)
        {
            mapWeather = new MoscowMapWeather();
        }

        frontParameters = new MoscowFrontParameters();
        buildArmedServicesActiveForMap();
        super.configure();
    }
    
    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(BoSServiceManager.VVS);
        armedServicesActiveForMap.add(BoSServiceManager.LUFTWAFFE);
        armedServicesActiveForMap.add(BoSServiceManager.REGIA_AERONAUTICA);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19411001"), DateUtils.getDateYYYYMMDD("19420301"));

        this.frontDatesForMap.addFrontDate("19411001");
        this.frontDatesForMap.addFrontDate("19411020");
        this.frontDatesForMap.addFrontDate("19411110");
        this.frontDatesForMap.addFrontDate("19411120");
        this.frontDatesForMap.addFrontDate("19411215");
        this.frontDatesForMap.addFrontDate("19420110");
    }
}
