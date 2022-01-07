package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

public enum FlightTypes
{
    LOW_ALT_CAP(FlightTypeCategory.FIGHTER), 
    LOW_ALT_PATROL(FlightTypeCategory.FIGHTER), 

    GROUND_ATTACK(FlightTypeCategory.ATTACK), 
    LOW_ALT_BOMB(FlightTypeCategory.BOMB), 
    DIVE_BOMB(FlightTypeCategory.BOMB),
    CARGO_DROP(FlightTypeCategory.TRANSPORT), 
    PARATROOP_DROP(FlightTypeCategory.TRANSPORT), 
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
        return false;
    }

    public static boolean isLowAltFlightType(FlightTypes flightType)
    {
        if (flightType == FlightTypes.GROUND_ATTACK)
        {
            return true;
        }

        return false;
    }

    public static boolean isBombingFlight(FlightTypes flightType)
    {
        if (flightType == FlightTypes.LOW_ALT_BOMB  || 
            flightType == FlightTypes.GROUND_ATTACK || 
            flightType == FlightTypes.DIVE_BOMB)
        {
            return true;
        }

        return false;
    }

    public static boolean isFighterFlight(FlightTypes flightType)
    {
        if (flightType == FlightTypes.LOW_ALT_CAP       ||
            flightType == FlightTypes.LOW_ALT_PATROL)
        {
            return true;
        }

        return false;
    }

    public static boolean isGroundAttackFlight(FlightTypes flightType)
    {
        if (flightType == FlightTypes.GROUND_ATTACK)
        {
            return true;
        }

        return false;
    }

    public static boolean isTacticalLevelBombingFlight(FlightTypes flightType)
    {
        if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            return true;
        }

        return false;
    }

    public static boolean isLevelBombingFlight(FlightTypes flightType)
    {
        if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            return true;
        }

        return false;
    }

    public static boolean isEscortedTransportFlight(FlightTypes flightType)
    {
        if (flightType == FlightTypes.PARATROOP_DROP || 
            flightType == FlightTypes.CARGO_DROP)
        {
            return true;
        }

        return false;
    }

    public static boolean isFlightWithTargetArea(FlightTypes flightType)
    {
        if (isBombingFlight(flightType))
        {
            return true;
        }

        if (flightType == FlightTypes.PARATROOP_DROP || 
            flightType == FlightTypes.CARGO_DROP)
        {
            return true;
        }

        return false;
    }

    public boolean isLowAltEquivalentFlightType(FlightTypes flightType)
    {
        if (this == flightType)
        {
            return true;
        }

        if (this == FlightTypes.DIVE_BOMB && flightType == FlightTypes.GROUND_ATTACK)
        {
            return true;
        }

        return false;
    }
}
