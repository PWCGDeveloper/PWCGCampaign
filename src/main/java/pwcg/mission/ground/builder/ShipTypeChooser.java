package pwcg.mission.ground.builder;

import pwcg.campaign.api.Side;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.VehicleClass;

public class ShipTypeChooser
{
    public static VehicleClass chooseShipType(Side side)
    {
        int shipTypeRoll = RandomNumberGenerator.getRandom(100);
        VehicleClass shipType = VehicleClass.ShipCargo;
        if (side == Side.AXIS)
        {
            if (shipTypeRoll < 20)
            {
                shipType = VehicleClass.Submarine;
            }
            else if (shipTypeRoll < 85)
            {
                shipType = VehicleClass.ShipCargo;
            }
            else
            {
                shipType = VehicleClass.ShipWarship;
            }
        }
        else
        {
            if (shipTypeRoll < 10)
            {
                shipType = VehicleClass.Submarine;
            }
            else if (shipTypeRoll < 70)
            {
                shipType = VehicleClass.ShipCargo;
            }
            else
            {
                shipType = VehicleClass.ShipWarship;
            }
        }
        return shipType;
    }

    public static VehicleClass chooseShipTypeForEncounter()
    {
        int shipTypeRoll = RandomNumberGenerator.getRandom(100);
        VehicleClass shipType = VehicleClass.ShipWarship;
        if (shipTypeRoll < 30)
        {
            shipType = VehicleClass.ShipCargo;
        }
        else
        {
            shipType = VehicleClass.ShipWarship;
        }
        return shipType;
    }

}
