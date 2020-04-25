package pwcg.product.bos.map.east1945;

import pwcg.campaign.context.PWCGMap;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.BoSServiceManager;

public class East1945Map extends PWCGMap
{

    public East1945Map()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = EAST1945_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.EAST1945_MAP;
        
        this.missionOptions = new East1945MissionOptions();
        this.mapWeather = new East1945MapWeather();
        
        frontParameters = new East1945FrontParameters();
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
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19450101"), DateUtils.getDateYYYYMMDD("19450503"));

        this.frontDatesForMap.addFrontDate("19450101");
        this.frontDatesForMap.addFrontDate("19450201");
        this.frontDatesForMap.addFrontDate("19450301");
        this.frontDatesForMap.addFrontDate("19450401");
        this.frontDatesForMap.addFrontDate("19450501");
    }

}
