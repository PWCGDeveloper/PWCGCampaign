package pwcg.campaign.target.unit;

import pwcg.campaign.Campaign;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.factory.AirfieldUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderAirfield
{

    public static GroundUnitCollection createAirfieldUnits(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException
    {
        AirfieldUnitFactory groundUnitFactory = new AirfieldUnitFactory(campaign, targetDefinition);
        GroundUnit targetUnit = groundUnitFactory.createAirfieldUnit();
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.STATIC_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.STATIC_UNIT, targetUnit);
        return groundUnitCollection;
    }
}
