package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.GroundUnitShippingFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderShipping
{
    public static GroundUnitCollection createShipping(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException
    {
        GroundUnitShippingFactory groundUnitFactory = new GroundUnitShippingFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetCountry());
        GroundUnit targetUnit = groundUnitFactory.createShippingUnit();
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.TRANSPORT_UNIT, targetUnit);
        return groundUnitCollection;
    }
}
