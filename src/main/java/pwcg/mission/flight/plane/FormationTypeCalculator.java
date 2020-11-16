package pwcg.mission.flight.plane;

import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuFormation;

public class FormationTypeCalculator 
{
    public static int calculateFormationType(FlightTypes flightType)
    {
        if (flightType.isCategory(FlightTypeCategory.FIGHTER))
        {
            return McuFormation.FORMATION_V;
        }
        else if (flightType.isCategory(FlightTypeCategory.TRANSPORT) ||
                flightType == FlightTypes.DIVE_BOMB)
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 30)
            {
                return McuFormation.FORMATION_LEFT;
            }
            else
            {
                return McuFormation.FORMATION_RIGHT;
            }
        }
        else if (flightType.isCategory(FlightTypeCategory.ATTACK) ||
                flightType.isCategory(FlightTypeCategory.BOMB) ||
                flightType.isCategory(FlightTypeCategory.STRATEGIC))
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 40)
            {
                return McuFormation.FORMATION_V;
            }
            else if (roll < 60)
            {
                return McuFormation.FORMATION_LEFT;
            }
            else
            {
                return McuFormation.FORMATION_RIGHT;
            }
        }

        return McuFormation.FORMATION_V;
    }
}
