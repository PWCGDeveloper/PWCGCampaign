package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.artillery.RadarUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class RadarUnitBuilder
{
    private static final int RADAR_RANGE = 45000;
    private Campaign campaign;
    private Coordinate radarPosition;
    private Side side;
    
    public RadarUnitBuilder(Campaign campaign, Coordinate radarPosition, Side side)
    {
        this.campaign = campaign;
        this.radarPosition = radarPosition;
        this.side = side;
    }

    public GroundUnitCollection createRadarForSide () throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformation();
        IGroundUnit radarUnit = new RadarUnit(groundUnitInformation);
        radarUnit.createGroundUnit();

        GroundUnitCollection groundUnitCollection = makeRadarGroundUnit(groundUnitInformation, radarUnit);
        return groundUnitCollection;
    }


    private GroundUnitInformation createGroundUnitInformation() throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(side), 
                TargetType.TARGET_RADAR,
                radarPosition, 
                radarPosition,
                Orientation.createRandomOrientation());
        
        groundUnitInformation.setUnitSize(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        return groundUnitInformation;
    }
    
    private GroundUnitCollection makeRadarGroundUnit(GroundUnitInformation groundUnitInformation, IGroundUnit radarUnit) throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Radar", 
                TargetType.TARGET_RADAR,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        GroundUnitCollection groundUnitCollection = new GroundUnitCollection (campaign, "Radar", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(radarUnit);
        groundUnitCollection.setPrimaryGroundUnit(radarUnit);
        int radarSpawnDistance = 75000;
        groundUnitCollection.finishGroundUnitCollectionWithSpqwnDistance(radarSpawnDistance);
        SpotterDecorator.createSpotter(groundUnitCollection, RADAR_RANGE);
        return groundUnitCollection;
    }

}
