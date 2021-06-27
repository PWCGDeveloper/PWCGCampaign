package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
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
import pwcg.mission.ground.unittypes.artillery.SearchLightUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class SearchLightBuilder
{
    private Campaign campaign;

    public SearchLightBuilder (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public GroundUnitCollection createSearchLightGroup (TargetDefinition targetDefinition) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                targetDefinition.getCountry(), 
                TargetType.TARGET_ARTILLERY,
                targetDefinition.getPosition(), 
                targetDefinition.getPosition(),
                Orientation.createRandomOrientation());

        IGroundUnit searchLightUnit = new SearchLightUnit(groundUnitInformation);
        searchLightUnit.createGroundUnit();
        
        GroundUnitCollection groundUnitCollection = makeSearchLightGroundUnit(groundUnitInformation, searchLightUnit);

        return groundUnitCollection;
    }
    
    public GroundUnitCollection createOneSearchLight (ICountry country, Coordinate position) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformation(country, position);
        IGroundUnit searchLightUnit = new SearchLightUnit(groundUnitInformation);
        searchLightUnit.createGroundUnit();

        GroundUnitCollection groundUnitCollection = makeSearchLightGroundUnit(groundUnitInformation, searchLightUnit);
        return groundUnitCollection;
    }

    private GroundUnitCollection makeSearchLightGroundUnit(GroundUnitInformation groundUnitInformation, IGroundUnit searchLightUnit) throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Search Lights", 
                TargetType.TARGET_ARTILLERY,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        GroundUnitCollection groundUnitCollection = new GroundUnitCollection (campaign, "Search Light", groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(searchLightUnit);
        groundUnitCollection.setPrimaryGroundUnit(searchLightUnit);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformation(ICountry country, Coordinate position) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country, 
                TargetType.TARGET_ARTILLERY,
                position, 
                position,
                Orientation.createRandomOrientation());
        
        groundUnitInformation.setUnitSize(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        return groundUnitInformation;
    }

}
