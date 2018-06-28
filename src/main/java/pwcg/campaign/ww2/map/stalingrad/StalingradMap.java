package pwcg.campaign.ww2.map.stalingrad;

import java.awt.Point;

import pwcg.campaign.context.PWCGMap;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class StalingradMap extends PWCGMap
{

    public StalingradMap()
    {
        super();
    }
    
    
    /**
     * Configure all of the manager objects for this map
     * @throws PWCGException 
     */
    public void configure() throws PWCGException
    {
        this.mapName = STALINGRAD_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.STALINGRAD_MAP;
        this.mapCenter = new Point(700, 700);
        
        this.missionOptions = new StalingradMissionOptions();
        this.mapWeather = new StalingradMapWeather();
        
        frontParameters = new StalingradFrontParameters();

        super.configure();
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

}
