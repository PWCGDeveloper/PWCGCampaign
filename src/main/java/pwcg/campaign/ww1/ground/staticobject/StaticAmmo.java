package pwcg.campaign.ww1.ground.staticobject;

import pwcg.campaign.api.ICountry;
import pwcg.core.utils.RandomNumberGenerator;

public class StaticAmmo extends StaticObject
{
	private String[] ammo = 
	{
		"shells_01", "shells_02"
	};
	
    public StaticAmmo(ICountry country) 
    {
        super(country);
    }
    
	public StaticAmmo(ICountry country, boolean many) 
	{
		super(country);
		
		this.country = country;
		
		String staticAmmoId= "";
		
		if (many)
		{
		    staticAmmoId = "shells_03";
		}
		else
		{
	        int selectedStaticAmmo = RandomNumberGenerator.getRandom(ammo.length);
	        staticAmmoId = ammo[selectedStaticAmmo];           
		}
		
        displayName = "Ammo";
				
		name = staticAmmoId;
		script = "LuaScripts\\WorldObjects\\" + staticAmmoId + ".txt";
		model = "graphics\\battlefield\\" + staticAmmoId + ".mgm";
	}

	public StaticAmmo copy () 
	{
		StaticAmmo clonedObject = new StaticAmmo(country);
		
		super.makeCopy(clonedObject);
		
		return clonedObject;
	}
}
