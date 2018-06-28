package pwcg.campaign.ww1.map.channel;

import java.awt.Point;

import pwcg.campaign.context.PWCGMap;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class ChannelMap extends PWCGMap
{

    public ChannelMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = CHANNEL_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.CHANNEL_MAP;
        this.mapCenter = new Point(2200, 1650);
        
        this.missionOptions = new ChannelMissionOptions();
        this.mapWeather = new ChannelMapWeather();
        
        frontParameters = new ChannelFrontParameters();

        super.configure();
    }
    
    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19181111"));

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
