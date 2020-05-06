package pwcg.mission.flight;

import pwcg.mission.flight.plane.PlaneMcu;

public class FuelAirStartCalculator
{
    static double calculateAirStartFuel(PlaneMcu plane)
    {
        double airStartFuel = .60;
        if (plane.getArchType().contentEquals("p51"))
        {
            airStartFuel = .40;
        }
        else if (plane.getArchType().contentEquals("p47"))
        {
            airStartFuel = .45;
        }
        else if (plane.getArchType().contentEquals("p38"))
        {
            airStartFuel = .40;
        }
        else if (plane.getArchType().contentEquals("he111"))
        {
            airStartFuel = .40;
        }
        else if (plane.getArchType().contentEquals("ju88"))
        {
            airStartFuel = .50;
        }
        else if (plane.getArchType().contentEquals("b25"))
        {
            airStartFuel = .50;
        }
        else if (plane.getArchType().contentEquals("a20"))
        {
            airStartFuel = .50;
        }
        return airStartFuel;
    }
}
