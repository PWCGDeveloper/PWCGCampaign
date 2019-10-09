package pwcg.product.rof.map.galicia;

import java.awt.Point;

import pwcg.campaign.context.PWCGMap;
import pwcg.product.rof.country.RoFServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class GaliciaMap extends PWCGMap
{

    public GaliciaMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = GALICIA_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.GALICIA_MAP;
        this.mapCenter = new Point(700, 900);
        
        this.missionOptions = new GaliciaMissionOptions();
        this.mapWeather = new GaliciaMapWeather();
        
        frontParameters = new GaliciaFrontParameters();
        buildArmedServicesActiveForMap();

        super.configure();
    }
    
    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(RoFServiceManager.RUSSIAN_AIR_SERVICE);
        armedServicesActiveForMap.add(RoFServiceManager.AUSTRO_HUNGARIAN_AIR_SERVICE);
        armedServicesActiveForMap.add(RoFServiceManager.DEUTSCHE_LUFTSTREITKRAFTE);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19170801"));

        this.frontDatesForMap.addFrontDate("19160101");
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
    }
}
