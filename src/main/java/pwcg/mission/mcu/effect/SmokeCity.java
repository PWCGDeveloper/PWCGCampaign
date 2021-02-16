package pwcg.mission.mcu.effect;

public class SmokeCity extends Effect
{	
	public SmokeCity()
	{
		super();
		
        script = "luascripts\\worldobjects\\mapemitters\\city_fire_loop.txt";
		name = "City Smoke";
	}
}
