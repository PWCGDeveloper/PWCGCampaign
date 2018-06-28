package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.GroundUnitDrifterFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderDrifter
{

    public static GroundUnitCollection createDrifters(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException
    {
        GroundUnitDrifterFactory groundUnitFactory = new GroundUnitDrifterFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetOrientation(), targetDefinition.getTargetCountry());
        GroundUnit targetUnit = groundUnitFactory.createDrifterUnit();
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.TRANSPORT_UNIT, targetUnit);
        return groundUnitCollection;
    }
}
