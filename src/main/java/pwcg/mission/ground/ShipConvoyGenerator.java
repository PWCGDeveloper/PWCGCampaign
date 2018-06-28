package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.target.ShippingLane;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit.ShipConvoyTypes;

public class ShipConvoyGenerator
{
    public List<ShipConvoyUnit> generateConvoys(Campaign campaign, ShippingLane shippingLane, Coordinate targetPosition, boolean isPlayerFlight) throws PWCGException 
    {
        CoordinateBox coordinateBorders = shippingLane.getShippingLaneBorders();

        List<ShipConvoyUnit> alliedConvoys = new ArrayList<ShipConvoyUnit>();
        List<ShipConvoyUnit> axisConvoys = new ArrayList<ShipConvoyUnit>();
        List<ShipConvoyUnit> convoys = new ArrayList<ShipConvoyUnit>();
        int numConvoys = RandomNumberGenerator.getRandom(6);
        for (int i = 0; i < numConvoys; ++i)
        {
            if (i > 0)
            {
                targetPosition = coordinateBorders.getCoordinateInBox();
            }

            ICountry shipCountry = CountryFactory.makeMapReferenceCountry(Side.ALLIED);
            int shipCountryRoll = RandomNumberGenerator.getRandom(100);
            if (shipCountryRoll < 50)
            {
                shipCountry = CountryFactory.makeMapReferenceCountry(Side.AXIS);
            }
            
            GroundUnitShippingFactory shippingFactory = new GroundUnitShippingFactory(campaign, targetPosition, shipCountry);
            ShipConvoyUnit convoy = shippingFactory.createShippingUnit();
            if (convoy.getCountry().getSide() == Side.ALLIED)
            {
                alliedConvoys.add(convoy);
            }
            else
            {
                axisConvoys.add(convoy);
            }
        }
        
        for (ShipConvoyUnit alliedConvoy : alliedConvoys)
        {
            if (alliedConvoy.getShipConvoyType() != ShipConvoyTypes.MERCHANT)
            {
                for (ShipConvoyUnit axisConvoy : axisConvoys)
                {
                    alliedConvoy.addTarget(axisConvoy.getVehicles().get(0).getEntity().getIndex());
                }
            }
        }
        
        
        for (ShipConvoyUnit axisConvoy : axisConvoys)
        {
            if (axisConvoy.getShipConvoyType() != ShipConvoyTypes.MERCHANT)
            {
                for (ShipConvoyUnit alliedConvoy : alliedConvoys)
                {
                    axisConvoy.addTarget(alliedConvoy.getVehicles().get(0).getEntity().getIndex());
                }
            }
        }
        
        convoys.addAll(alliedConvoys);
        convoys.addAll(axisConvoys);
        
        return convoys;
    }

}
