package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.target.locator.ShippingLane;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.factory.ShippingUnitFactory;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

public class ShipConvoyGenerator
{
    public List<ShipConvoyUnit> generateConvoys(FlightInformation flightInformation, ShippingLane shippingLane) throws PWCGException 
    {
        List<ShipConvoyUnit> alliedConvoys = new ArrayList<ShipConvoyUnit>();
        List<ShipConvoyUnit> axisConvoys = new ArrayList<ShipConvoyUnit>();
        List<ShipConvoyUnit> convoys = new ArrayList<ShipConvoyUnit>();
        int numConvoys = RandomNumberGenerator.getRandom(6);
        for (int i = 0; i < numConvoys; ++i)
        {
            ShippingUnitFactory shippingFactory = new ShippingUnitFactory(flightInformation.getCampaign(), flightInformation.getTargetDefinition());
            ShipConvoyUnit convoy = shippingFactory.createShippingUnit();
            if (convoy.getPwcgGroundUnitInformation().getCountry().getSide() == Side.ALLIED)
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
            if (alliedConvoy.getShipConvoyType() != VehicleClass.ShipCargo)
            {
                for (ShipConvoyUnit axisConvoy : axisConvoys)
                {
                    alliedConvoy.addTarget(axisConvoy.getVehicles().get(0).getEntity().getIndex());
                }
            }
        }
        
        
        for (ShipConvoyUnit axisConvoy : axisConvoys)
        {
            if (axisConvoy.getShipConvoyType() != VehicleClass.ShipCargo)
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
