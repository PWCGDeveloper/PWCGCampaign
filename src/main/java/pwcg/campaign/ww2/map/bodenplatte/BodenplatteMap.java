package pwcg.campaign.ww2.map.bodenplatte;

import java.awt.Point;

import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.ww2.country.BoSServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class BodenplatteMap extends PWCGMap
{

    public BodenplatteMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = BODENPLATTE_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.BODENPLATTE_MAP;
        this.mapCenter = new Point(700, 700);
        
        this.missionOptions = new BodenplatteMissionOptions();
        this.mapWeather = new BodenplatteMapWeather();
        
        frontParameters = new BodenplatteFrontParameters();
        buildArmedServicesActiveForMap();
        
        super.configure();
    }
    
    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(BoSServiceManager.USAAF);
        armedServicesActiveForMap.add(BoSServiceManager.RAF);
        armedServicesActiveForMap.add(BoSServiceManager.LUFTWAFFE);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19440801"), DateUtils.getDateYYYYMMDD("19450503"));

        this.frontDatesForMap.addFrontDate("19440801");
    }

}
