package pwcg.campaign.target.unit;

import pwcg.campaign.Campaign;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.factory.ShippingUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderShipping
{
    public static GroundUnitCollection createShipping(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException
    {
        ShippingUnitFactory groundUnitFactory = new ShippingUnitFactory(campaign, targetDefinition);
        GroundUnit targetUnit = groundUnitFactory.createShippingUnit();
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.TRANSPORT_UNIT, targetUnit);
        return groundUnitCollection;
    }
}
