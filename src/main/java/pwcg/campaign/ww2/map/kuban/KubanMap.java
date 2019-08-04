package pwcg.campaign.ww2.map.kuban;

import java.awt.Point;

import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.ww2.country.BoSServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

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

        this.mapCenter = new Point(700, 700);
        
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
        armedServicesActiveForMap.add(BoSServiceManager.LUFTWAFFE);
        armedServicesActiveForMap.add(BoSServiceManager.REGIA_AERONAUTICA);
    }

    @Override
    protected void configureTransitionDates() throws PWCGException
    {
        this.frontDatesForMap.addMapDateRange(DateUtils.getDateYYYYMMDD("19420601"), DateUtils.getDateYYYYMMDD("19431131"));
        
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
}
