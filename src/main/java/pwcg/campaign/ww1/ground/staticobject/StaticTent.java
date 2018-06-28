package pwcg.campaign.ww1.ground.staticobject;

import pwcg.campaign.api.ICountry;


public class StaticTent extends StaticObject
{
	public StaticTent(ICountry country) 
	{
		super(country);
		
		this.country = country;
		
		name = "Tent";
		script = "LuaScripts\\WorldObjects\\tent02" + ".txt";
		model = "graphics\\battlefield\\tent02" + ".mgm";
	}

	public StaticTent copy () 
	{
		StaticTent clonedObject = new StaticTent(country);
		
		super.makeCopy(clonedObject);
		
		return clonedObject;
	}

}
