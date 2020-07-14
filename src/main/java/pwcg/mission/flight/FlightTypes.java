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
    LOW_ALT_CAP(FlightTypeCategory.FIGHTER),
    LOW_ALT_PATROL(FlightTypeCategory.FIGHTER),
    BALLOON_BUST(FlightTypeCategory.FIGHTER),
    BALLOON_DEFENSE(FlightTypeCategory.FIGHTER),
    
    GROUND_ATTACK(FlightTypeCategory.ATTACK),
    
    BOMB(FlightTypeCategory.BOMB),
    LOW_ALT_BOMB(FlightTypeCategory.BOMB),
    DIVE_BOMB(FlightTypeCategory.BOMB),
    
    CONTACT_PATROL(FlightTypeCategory.SINGLE),
    ARTILLERY_SPOT(FlightTypeCategory.SINGLE),
    RECON(FlightTypeCategory.SINGLE),
    SPY_EXTRACT(FlightTypeCategory.SINGLE),

    CARGO_DROP(FlightTypeCategory.TRANSPORT),
    PARATROOP_DROP(FlightTypeCategory.TRANSPORT),
    TRANSPORT(FlightTypeCategory.TRANSPORT),
    FERRY(FlightTypeCategory.TRANSPORT),

    STRATEGIC_BOMB(FlightTypeCategory.STRATEGIC),
    
    GROUND_FORCES(FlightTypeCategory.INVALID),
    ANY(FlightTypeCategory.INVALID);

    FlightTypeCategory category = FlightTypeCategory.INVALID;
	
	private FlightTypes(FlightTypeCategory category)
	{
	    this.category = category;
	}

	public boolean isCategory(FlightTypeCategory categoryToFind) 
	{
        if (category == categoryToFind)
        {
            return true;
        }
		return false;
	}

    public static List<FlightTypes> getFlightTypesByCategory(FlightTypeCategory categoryToFind)
    {
        List<FlightTypes> flightTypesByCategory = new ArrayList<>();
        for (FlightTypes flightType : FlightTypes.values()) 
        {
            if (flightType.category == categoryToFind)
            {
                flightTypesByCategory.add(flightType);
            }
         }
        return flightTypesByCategory;
    }

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
            flightType == FlightTypes.STRATEGIC_BOMB)
        {
            return AttackAreaType.INDIRECT;
        }
        else if (flightType == FlightTypes.GROUND_ATTACK ||
                 flightType == FlightTypes.DIVE_BOMB)
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
