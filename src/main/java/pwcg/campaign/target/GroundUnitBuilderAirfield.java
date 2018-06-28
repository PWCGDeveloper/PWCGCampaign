package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.GrountUnitAirfieldFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderAirfield
{

    public static GroundUnitCollection createAirfieldUnits(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException
    {
        GrountUnitAirfieldFactory groundUnitFactory = new GrountUnitAirfieldFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetOrientation(), targetDefinition.getTargetCountry());
        GroundUnit targetUnit = groundUnitFactory.createAirfieldUnit();
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.STATIC_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.STATIC_UNIT, targetUnit);
        return groundUnitCollection;
    }
}
