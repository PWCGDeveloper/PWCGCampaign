package pwcg.product.rof.map.france;

import java.awt.Point;

import pwcg.campaign.context.PWCGMap;
import pwcg.product.rof.country.RoFServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class FranceMap extends PWCGMap
{

    public FranceMap()
    {
        super();
    }

    public void configure() throws PWCGException
    {
        this.mapName = FRANCE_MAP_NAME;        
        this.mapIdentifier = FrontMapIdentifier.FRANCE_MAP;
        this.mapCenter = new Point(300, 700);

        this.missionOptions = new FranceMissionOptions();
        this.mapWeather = new FranceMapWeather();
        
        frontParameters = new FranceFrontParameters();
        buildArmedServicesActiveForMap();
        
        super.configure();
    }
    
    private void buildArmedServicesActiveForMap()
    {
        armedServicesActiveForMap.add(RoFServiceManager.AVIATION_MILITAIRE_BELGE);
        armedServicesActiveForMap.add(RoFServiceManager.LAVIATION_MILITAIRE);
        armedServicesActiveForMap.add(RoFServiceManager.RFC);
        armedServicesActiveForMap.add(RoFServiceManager.RNAS);
        armedServicesActiveForMap.add(RoFServiceManager.RAF);
        armedServicesActiveForMap.add(RoFServiceManager.USAS);
        armedServicesActiveForMap.add(RoFServiceManager.DEUTSCHE_LUFTSTREITKRAFTE);
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
