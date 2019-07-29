package pwcg.campaign.ww2.map.moscow;

import java.awt.Point;

import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.ww2.country.BoSServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

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
        this.mapCenter = new Point(700, 700);
        
        this.missionOptions = new MoscowMissionOptions();
        this.mapWeather = new MoscowMapWeather();
        
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
