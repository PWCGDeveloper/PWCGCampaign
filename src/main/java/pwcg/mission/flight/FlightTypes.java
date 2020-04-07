package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.mission.mcu.AttackAreaType;

public enum FlightTypes
{
    PATROL(FlightTypeCategory.FIGHTER),
    OFFENSIVE(FlightTypeCategory.FIGHTER),
    INTERCEPT(FlightTypeCategory.FIGHTER),
    STRATEGIC_INTERCEPT(FlightTypeCategory.FIGHTER),
    SCRAMBLE(FlightTypeCategory.FIGHTER),
    LONE_WOLF(FlightTypeCategory.FIGHTER),
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
    
//    
//    public static boolean isHighPriorityFlight(FlightTypes flightType)
//    {
//        if (flightType == FlightTypes.BOMB ||
//            flightType == FlightTypes.ANTI_SHIPPING_BOMB ||
//            flightType == FlightTypes.STRATEGIC_BOMB ||
//            flightType == FlightTypes.PARATROOP_DROP ||
//            flightType == FlightTypes.CARGO_DROP ||
//            flightType == FlightTypes.TRANSPORT ||
//            flightType == FlightTypes.RECON)
//        {
//            return true;
//        }
//         
//        return false;
//    }
    
    public static boolean isHighPriorityFlight(FlightTypes flightType)
    {
        if (flightType == FlightTypes.STRATEGIC_BOMB ||
            flightType == FlightTypes.PARATROOP_DROP ||
            flightType == FlightTypes.CARGO_DROP)
        {
            return true;
        }
         
        return false;
    }

    public static boolean isLowAltFlightType(FlightTypes flightType)
    {
        if (flightType == FlightTypes.ARTILLERY_SPOT    ||
            flightType == FlightTypes.CONTACT_PATROL    ||
            flightType == FlightTypes.GROUND_ATTACK     ||
            flightType == FlightTypes.SCRAMBLE          ||
            flightType == FlightTypes.SCRAMBLE          ||
            flightType == FlightTypes.SCRAMBLE          ||
            flightType == FlightTypes.SPY_EXTRACT)
        {
            return true;
        }
        
        return false;
    }
    

    public static boolean isBombingFlight(FlightTypes flightType)
    {
        if (flightType == FlightTypes.BOMB ||
            flightType == FlightTypes.LOW_ALT_BOMB ||
            flightType == FlightTypes.GROUND_ATTACK ||
            flightType == FlightTypes.DIVE_BOMB ||
            flightType == FlightTypes.ANTI_SHIPPING_BOMB || 
            flightType == FlightTypes.ANTI_SHIPPING_ATTACK || 
            flightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB ||
            flightType == FlightTypes.STRATEGIC_BOMB)
        {
            return true;
        }
        
        return false;
    }
    

    public static AttackAreaType getAttackAreaTypeByFlightyType(FlightTypes flightType)
    {
        if (flightType == FlightTypes.BOMB ||
            flightType == FlightTypes.LOW_ALT_BOMB ||
            flightType == FlightTypes.DIVE_BOMB ||
            flightType == FlightTypes.ANTI_SHIPPING_BOMB || 
            flightType == FlightTypes.STRATEGIC_BOMB)
        {
            return AttackAreaType.INDIRECT;
        }
        else if (flightType == FlightTypes.GROUND_ATTACK ||
                 flightType == FlightTypes.DIVE_BOMB ||
                 flightType == FlightTypes.ANTI_SHIPPING_ATTACK || 
                 flightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB)
        {
            
            return AttackAreaType.GROUND_TARGETS;
        }
        else
        {
            return AttackAreaType.AIR_TARGETS;
        }
     }

    public static boolean isFlightNeedsEscort(FlightTypes flightType)
    {
        if (flightType == FlightTypes.TRANSPORT)
        {
            return false;
        }
        
        return true;
    }

}
