package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class Fuel
{

    public static double calculateFuelForFlight(IFlight flight) throws PWCGException
    {
        double fuel = 1.0;
        if (!flight.getFlightInformation().isAirStart())
        {
            fuel = calculateGroundStartFuel(flight.getFlightPlanes().getFlightLeader());
        }
        else
        {
            fuel = calculateAirStartFuel(flight.getFlightPlanes().getFlightLeader());
        }
        return fuel;
    }

    static private double calculateGroundStartFuel(PlaneMcu plane)
    {
        double groundStartFuel = 1.00;
        if (plane.getArchType().equals("p38"))
        {
            groundStartFuel = .50;
        }
        else if (plane.getArchType().equals("p39"))
        {
            groundStartFuel = .80;
        }
        else if (plane.getArchType().equals("p40"))
        {
            groundStartFuel = .80;
        }
        else if (plane.getArchType().equals("p47"))
        {
            groundStartFuel = .60;
        }
         else if (plane.getArchType().equals("p51"))
        {
            groundStartFuel = .50;
        }
        else if (plane.getArchType().equals("he111"))
        {
            groundStartFuel = .30;
        }
        else if (plane.getArchType().equals("me110"))
        {
            groundStartFuel = .70;
        }
        else if (plane.getArchType().contentEquals("ju52"))
        {
            groundStartFuel = .60;
        }
        else if (plane.getArchType().contentEquals("ju87"))
        {
            groundStartFuel = .70;
        }
        else if (plane.getArchType().equals("ju88"))
        {
            groundStartFuel = .50;
        }
        else if (plane.getArchType().equals("b25"))
        {
            groundStartFuel = .60;
        }
        else if (plane.getArchType().equals("a20"))
        {
            groundStartFuel = .70;
        }
        else if (plane.getArchType().contentEquals("pe2"))
        {
            groundStartFuel = .70;
        }
        return groundStartFuel;
    }

    static private double calculateAirStartFuel(PlaneMcu plane)
    {
        double airStartFuel = .60;
        if (plane.getArchType().contentEquals("p51"))
        {
            airStartFuel = .40;
        }
        else if (plane.getArchType().equals("p39"))
        {
            airStartFuel = .50;
        }
        else if (plane.getArchType().equals("p40"))
        {
            airStartFuel = .50;
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
            airStartFuel = .30;
        }
        else if (plane.getArchType().equals("me110"))
        {
            airStartFuel = .50;
        }
        else if (plane.getArchType().contentEquals("ju52"))
        {
            airStartFuel = .40;
        }
        else if (plane.getArchType().contentEquals("ju87"))
        {
            airStartFuel = .50;
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
        else if (plane.getArchType().contentEquals("pe2"))
        {
            airStartFuel = .50;
        }
        return airStartFuel;
    }

}
