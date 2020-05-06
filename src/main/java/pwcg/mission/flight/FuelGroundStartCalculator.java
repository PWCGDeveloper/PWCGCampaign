package pwcg.mission.flight;

import pwcg.mission.flight.plane.PlaneMcu;

public class FuelGroundStartCalculator
{
    static double calculateAirStartFuel(PlaneMcu plane)
    {
        double airStartFuel = 1.00;
        if (plane.getArchType().contentEquals("p51"))
        {
            airStartFuel = .60;
        }
        else if (plane.getArchType().contentEquals("p47"))
        {
            airStartFuel = .60;
        }
        else if (plane.getArchType().contentEquals("p38"))
        {
            airStartFuel = .60;
        }
        else if (plane.getArchType().contentEquals("he111"))
        {
            airStartFuel = .80;
        }
        else if (plane.getArchType().contentEquals("ju88"))
        {
            airStartFuel = .80;
        }
        else if (plane.getArchType().contentEquals("b25"))
        {
            airStartFuel = .80;
        }
        else if (plane.getArchType().contentEquals("a20"))
        {
            airStartFuel = .80;
        }
        return airStartFuel;
    }
}
