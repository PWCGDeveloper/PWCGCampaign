package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit.ShipConvoyTypes;
import pwcg.mission.mcu.Coalition;

public class ShippingUnitFactory
{
    private Campaign campaign;
    private Coordinate location;
    private ICountry country;

    public ShippingUnitFactory (Campaign campaign, Coordinate location, ICountry country)
    {
        this.campaign  = campaign;
        this.location  = location.copy();
        this.country  = country;
    }

    public ShipConvoyUnit createShippingUnit () throws PWCGException 
    {
        ShipConvoyTypes shipType = chooseShipType();
        ShipConvoyUnit shipConvoyUnit = generateConvoy(shipType);
        return shipConvoyUnit;
    }


    public ShipConvoyUnit generateConvoy(ShipConvoyTypes shipType) throws PWCGException 
    {
        
        ShipConvoyUnit shipGroup = new ShipConvoyUnit(campaign, shipType);

        Coalition playerCoalition  = Coalition.getFriendlyCoalition(campaign.determineCountry());
        MissionBeginUnitCheckZone missionBeginUnitShips = new MissionBeginUnitCheckZone();
        missionBeginUnitShips.initialize(location, 30000, playerCoalition);

        shipGroup.initialize (missionBeginUnitShips, location, country);
        shipGroup.createUnitMission();
        
        return shipGroup;
    }

    private ShipConvoyTypes chooseShipType()
    {
        int shipTypeRoll = RandomNumberGenerator.getRandom(100);
        ShipConvoyTypes shipType = ShipConvoyTypes.MERCHANT;
        if (country.getSide() == Side.AXIS)
        {
            if (country.getSide() == Side.AXIS)
            {
                if (shipTypeRoll < 20)
                {
                    shipType = ShipConvoyTypes.SUBMARINE;
                }
                else if (shipTypeRoll < 85)
                {
                    shipType = ShipConvoyTypes.MERCHANT;
                }
                else
                {
                    shipType = ShipConvoyTypes.WARSHIP;
                }
            }
            else
            {
                if (shipTypeRoll < 70)
                {
                    shipType = ShipConvoyTypes.SUBMARINE;
                }
                else
                {
                    shipType = ShipConvoyTypes.WARSHIP;
                }
            }
        }
        else
        {
            if (country.getSide() == Side.ALLIED)
            {
                if (shipTypeRoll < 75)
                {
                    shipType = ShipConvoyTypes.MERCHANT;
                }
                else
                {
                    shipType = ShipConvoyTypes.WARSHIP;
                }
            }
            else
            {
                if (shipTypeRoll < 20)
                {
                    shipType = ShipConvoyTypes.MERCHANT;
                }
                else
                {
                    shipType = ShipConvoyTypes.WARSHIP;
                }
            }
        }
        return shipType;
    }

}
