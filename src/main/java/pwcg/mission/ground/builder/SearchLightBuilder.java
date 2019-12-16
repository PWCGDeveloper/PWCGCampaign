package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.artillery.SearchLightUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class SearchLightBuilder
{
    private Campaign campaign;

    public SearchLightBuilder (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public IGroundUnitCollection createSearchLightGroup (TargetDefinition targetDefinition) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, targetDefinition);
        IGroundUnit searchLightUnit = new SearchLightUnit(groundUnitInformation);
        searchLightUnit.createGroundUnit();
        
        IGroundUnitCollection groundUnitCollection = makeSearchLightGroundUnit(groundUnitInformation, searchLightUnit);

        return groundUnitCollection;
    }
    
    public IGroundUnitCollection createOneSearchLight (ICountry country, Coordinate position) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformation(country, position);
        IGroundUnit searchLightUnit = new SearchLightUnit(groundUnitInformation);
        searchLightUnit.createGroundUnit();

        IGroundUnitCollection groundUnitCollection = makeSearchLightGroundUnit(groundUnitInformation, searchLightUnit);
        return groundUnitCollection;
    }

    private IGroundUnitCollection makeSearchLightGroundUnit(GroundUnitInformation groundUnitInformation, IGroundUnit searchLightUnit) throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Search Lights", 
                TacticalTarget.TARGET_INFANTRY,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection (groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(searchLightUnit);
        groundUnitCollection.setPrimaryGroundUnit(searchLightUnit);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformation(ICountry country, Coordinate position) throws PWCGException
    {
        String nationality = country.getNationality();
        String name = nationality + " Search Light";

        boolean isPlayerTarget = false;
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, country, name, TacticalTarget.TARGET_AIRFIELD, position, position, null, isPlayerTarget);
        
        groundUnitInformation.setUnitSize(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        return groundUnitInformation;
    }

}
