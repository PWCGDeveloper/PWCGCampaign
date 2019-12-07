package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.factory.ShippingUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class ShipConvoyGenerator
{
    public List<IGroundUnitCollection> generateConvoys(FlightInformation flightInformation, ShippingLane shippingLane) throws PWCGException 
    {
        List<IGroundUnitCollection> alliedConvoys = new ArrayList<>();
        List<IGroundUnitCollection> axisConvoys = new ArrayList<>();
        List<IGroundUnitCollection> convoys = new ArrayList<>();
        int numConvoys = RandomNumberGenerator.getRandom(6);
        for (int i = 0; i < numConvoys; ++i)
        {
            ShippingUnitBuilder shippingFactory = new ShippingUnitBuilder(flightInformation.getCampaign(), flightInformation.getTargetDefinition());
            IGroundUnitCollection convoy = shippingFactory.createShippingUnit();
            if (convoy.getCountry().getSide() == Side.ALLIED)
            {
                alliedConvoys.add(convoy);
            }
            else
            {
                axisConvoys.add(convoy);
            }
        }

        convoys.addAll(alliedConvoys);
        convoys.addAll(axisConvoys);        
        return convoys;
    }
}
