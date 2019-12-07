package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

public enum FlightTypes
{
    PATROL(FlightTypeCategory.FIGHTER),
    OFFENSIVE(FlightTypeCategory.FIGHTER),
    INTERCEPT(FlightTypeCategory.FIGHTER),
    SCRAMBLE(FlightTypeCategory.FIGHTER),
    HOME_DEFENSE(FlightTypeCategory.FIGHTER),
    LONE_WOLF(FlightTypeCategory.FIGHTER),
    SEA_PATROL(FlightTypeCategory.FIGHTER),
    ESCORT(FlightTypeCategory.FIGHTER),
    SCRAMBLE_OPPOSE(FlightTypeCategory.FIGHTER), 
    LOW_ALT_CAP(FlightTypeCategory.FIGHTER, FlightTypeCategory.FIGHTER),
    LOW_ALT_PATROL(FlightTypeCategory.FIGHTER, FlightTypeCategory.FIGHTER),
    
    BALLOON_BUST(FlightTypeCategory.FIGHTER, FlightTypeCategory.ATTACK),
    BALLOON_DEFENSE(FlightTypeCategory.FIGHTER, FlightTypeCategory.ATTACK),
    GROUND_ATTACK(FlightTypeCategory.ATTACK),
    BOMB(FlightTypeCategory.ATTACK),
    LOW_ALT_BOMB(FlightTypeCategory.ATTACK),
    DIVE_BOMB(FlightTypeCategory.ATTACK),
    ANTI_SHIPPING_BOMB(FlightTypeCategory.ATTACK),
    ANTI_SHIPPING_DIVE_BOMB(FlightTypeCategory.ATTACK),
    ANTI_SHIPPING_ATTACK(FlightTypeCategory.ATTACK),
    CONTACT_PATROL(FlightTypeCategory.ATTACK),
    ARTILLERY_SPOT(FlightTypeCategory.ATTACK),
    CARGO_DROP(FlightTypeCategory.ATTACK),
    PARATROOP_DROP(FlightTypeCategory.ATTACK),

    STRATEGIC_BOMB(FlightTypeCategory.STRATEGIC),

    TRANSPORT(FlightTypeCategory.OTHER),
    RECON(FlightTypeCategory.OTHER),
    SPY_EXTRACT(FlightTypeCategory.OTHER),
    PORT_RECON(FlightTypeCategory.OTHER),
    FERRY(FlightTypeCategory.OTHER),
    
    GROUND_FORCES(FlightTypeCategory.INVALID),
    ANY(FlightTypeCategory.INVALID);

	List<FlightTypeCategory> categories = new ArrayList<>();
	
	private FlightTypes(FlightTypeCategory ... categoryList)
	{
		for (FlightTypeCategory category : categoryList)
		{
			categories.add(category);
		}
	}

	public boolean isCategory(FlightTypeCategory categoryToFind) 
	{
		for (FlightTypeCategory category : categories)
		{
			if (category == categoryToFind)
			{
				return true;
			}
		}
		return false;
	}

    public static List<FlightTypes> getFlightTypesByCategory(FlightTypeCategory category)
    {
        List<FlightTypes> flightTypesByCategory = new ArrayList<>();
        for (FlightTypes flightType : FlightTypes.values()) 
        {
            if (flightType.categories.contains(category))
            {
                flightTypesByCategory.add(flightType);
            }
         }
        return flightTypesByCategory;
    }
    
    public static boolean isHighPriorityFlight(FlightTypes flightType)
    {
        if (flightType == FlightTypes.BOMB ||
            flightType == FlightTypes.ANTI_SHIPPING_BOMB ||
            flightType == FlightTypes.STRATEGIC_BOMB ||
            flightType == FlightTypes.PARATROOP_DROP ||
            flightType == FlightTypes.CARGO_DROP ||
            flightType == FlightTypes.TRANSPORT ||
            flightType == FlightTypes.RECON)
        {
            return true;
        }
         
        return false;
    }
}
