package pwcg.campaign.ww2.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit.ShipConvoyTypes;
import pwcg.mission.mcu.McuTREntity;

public class Ship extends Vehicle
{
    private String[][] germanShips = 
    {
        { "torpedoboat38", "torpedoboat38" },
        { "destroyertype7", "destroyertype7" },
    };

    private String[][] russianShips = 
    {
        { "destroyertype7", "destroyertype7" },
        { "destroyertype7", "destroyertype7" },
        { "destroyertype7", "destroyertype7" },
        { "torpboatg5s11b", "torpboatg5s11b" },
        { "torpboatg5s11b", "torpboatg5s11b" },
        { "torpboatg5s11b213", "torpboatg5s11b" },
        { "torpboatg5s11b213", "torpboatg5s11b" },
        { "landboata", "landboata" },
    };
	

    private String[][] merchantShips = 
    {
            { "largecargoshiptype1", "largecargoshiptype1" },
            { "largetankershiptype1", "largetankershiptype1" },
    };
	
	private Ship(ICountry country) 
	{
	    super();
        this.country = country;
	}
	   
	public Ship(ICountry country, ShipConvoyTypes shipConvoyType) throws PWCGException 
	{
        super();
        this.country = country;
		
		String shipId= "";
		String shipDir= "";
		
        if (country.getSideNoNeutral() == Side.ALLIED)
		{
            if (shipConvoyType == ShipConvoyTypes.WARSHIP)
			{
				int selectedShip = RandomNumberGenerator.getRandom(russianShips.length);
				shipId = russianShips[selectedShip][0];
		        shipDir = russianShips[selectedShip] [1];
				displayName = "Russian Warship";
			}
			else
			{
				int selectedShip = RandomNumberGenerator.getRandom(merchantShips.length);
				shipId = merchantShips[selectedShip][1];
				displayName = getDisplayName(shipId);
			}
		}
		else
		{
            if (shipConvoyType == ShipConvoyTypes.WARSHIP)
			{
				int selectedShip = RandomNumberGenerator.getRandom(germanShips.length);
				shipId = germanShips[selectedShip] [0];
		        shipDir = germanShips[selectedShip] [1];
				displayName = "German Warship";
			}
			else
			{
				int selectedShip = RandomNumberGenerator.getRandom(merchantShips.length);
				shipId = merchantShips[selectedShip][0];
		        shipDir = merchantShips[selectedShip] [1];
				displayName = "Cargo Ship";
			}
		}
		
		name = shipId;
		script = "LuaScripts\\WorldObjects\\Ships\\" + shipId + ".txt";
		model = "graphics\\ships\\" + shipDir + shipId + ".mgm";
	}

	private String getDisplayName(String id)
	{
		if (id.contains("tanker"))
		{
			return  "Tanker";
		}
		else if (id.contains("pass"))
		{
			return  "Troop Transport";
		}
		else
		{
			return  "Cargo";
		}
	}

	public Ship copy () 
	{
		Ship ship = new Ship(country);
		
		ship.index = IndexGenerator.getInstance().getNextIndex();
		
		ship.name = this.name;
		ship.displayName = this.displayName;
		ship.linkTrId = this.linkTrId;
		ship.script = this.script;
		ship.model = this.model;
		ship.Desc = this.Desc;
		ship.aiLevel = this.aiLevel;
		ship.numberInFormation = this.numberInFormation;
		ship.vulnerable = this.vulnerable;
		ship.engageable = this.engageable;
		ship.limitAmmo = this.limitAmmo;
		ship.damageReport = this.damageReport;
		ship.country = this.country;
		ship.damageThreshold = this.damageThreshold; 
		
		ship.position = new Coordinate();
		ship.orientation = new Orientation();
		
		ship.entity = new McuTREntity();
		
		ship.populateEntity();
		
		return ship;
	}

	@Override
	public void populateEntity()
	{
		super.populateEntity();
		entity.setEnabled(1);
	}
}
