package pwcg.campaign.ww1.ground.vehicle;

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
	// German
	private String[][] germanShips = 
	{
		{ "gercruiser", "gerships" }
	};
	
	// German
	private String[][] germanSubs = 
	{
		{ "gersubmarine", "gerships"}
	};
	
	// British
	private String[][] britishShips = 
	{
		{ "hmscruiser", "gbrships" }
	};
	
	
	// British
	private String[][] merchantShips = 
	{
		{ "ship_cargo", "merchant" },
		{ "ship_cargo", "merchant" },
		{ "ship_cargo", "merchant" },
		{ "ship_cargo", "merchant" },
		{ "ship_cargo", "merchant" },
		{ "ship_cargo", "merchant" },
		{ "ship_tank", "merchant" },
		{ "ship_tank", "merchant" },
		{ "ship_pass", "merchant" }
	};
	
	private Ship(ICountry country) 
	{
	    super(country);
	}
	   
	public Ship(ICountry country, ShipConvoyTypes shipConvoyType) throws PWCGException 
	{
        super(country);
		
		String shipId= "";
		String shipDir = "";
		
        if (country.getSideNoNeutral() == Side.ALLIED)
		{
		    // Reuse the German submarine as a British sub
            if (shipConvoyType == ShipConvoyTypes.SUBMARINE)
            {
                shipId = germanSubs[0] [0];
                shipDir = germanSubs[0] [1];
                displayName = "Submarine";
            }
            else if (shipConvoyType == ShipConvoyTypes.WARSHIP)
			{
				shipId = britishShips[0] [0];
				shipDir = britishShips[0] [1];
				displayName = "Cruiser";
			}
			else
			{
				int selectedShip = RandomNumberGenerator.getRandom(merchantShips.length);
				shipId = merchantShips[selectedShip] [0];
				shipDir = merchantShips[selectedShip] [1];
				displayName = getDisplayName(shipId);
			}
		}
		else
		{
            if (shipConvoyType == ShipConvoyTypes.SUBMARINE)
			{
				shipId = germanSubs[0] [0];
				shipDir = germanSubs[0] [1];
				displayName = "Submarine";
			}
            else if (shipConvoyType == ShipConvoyTypes.WARSHIP)
			{
				shipId = germanShips[0] [0];
				shipDir = germanShips[0] [1];
				displayName = "Cruiser";
			}
			else
			{
				int selectedShip = RandomNumberGenerator.getRandom(merchantShips.length);
				shipId = merchantShips[selectedShip] [0];
				shipDir = merchantShips[selectedShip] [1];
				displayName = getDisplayName(shipId);
			}
		}
		
		name = shipId;
		script = "LuaScripts\\WorldObjects\\" + shipId + ".txt";
		model = "graphics\\ships\\" + shipDir + "\\" + shipId + ".mgm";
	}
	
	/**
	 * @param id
	 * @return
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
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


	/**
	 * Override to always enable ships
	 */
	@Override
	public void populateEntity()
	{
		super.populateEntity();
		entity.setEnabled(1);
	}
}
