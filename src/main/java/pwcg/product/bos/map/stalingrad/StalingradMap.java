package pwcg.product.bos.map.stalingrad;

import java.awt.Point;

import pwcg.campaign.context.PWCGMap;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

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
        this.mapCenter = new Point(700, 700);
        
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
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19440101"), DateUtils.getDateYYYYMMDD("19450501"));

        this.frontDatesForMap.addFrontDate("19420301");
        this.frontDatesForMap.addFrontDate("19420801");
        this.frontDatesForMap.addFrontDate("19420906");
        this.frontDatesForMap.addFrontDate("19421011");
        this.frontDatesForMap.addFrontDate("19421123");
        this.frontDatesForMap.addFrontDate("19421223");
        this.frontDatesForMap.addFrontDate("19430120");
        this.frontDatesForMap.addFrontDate("19440101");
    }

}
