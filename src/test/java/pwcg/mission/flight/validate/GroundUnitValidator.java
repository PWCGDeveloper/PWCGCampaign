package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.mission.IUnit;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.ground.balloon.BalloonDefenseGroup;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitSpawning;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.artillery.GroundAAArtilleryBattery;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunFlareUnit;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;

public class GroundUnitValidator
{
    public void validateGroundUnitsForMission(Mission mission)
    {
        List<Flight> flights = mission.getMissionFlightBuilder().getAllAerialFlights();
        for (Flight flight : flights)
        {
            validateGroundUnitsForFlight(flight);
        }
    }

    public void validateGroundUnitsForFlight(Flight flight)
    {
        if (flight.isPlayerFlight() || flight.getFlightInformation().isEscortedByPlayerFlight() || flight.getFlightInformation().isEscortForPlayerFlight())
        {
            validatePlayerGroundUnits(flight);
        }
        else
        {
            validateAiGroundUnits(flight);
        }
    }
    
    private void validatePlayerGroundUnits(Flight flight)
    {
        for (IUnit linkedUnit : flight.getLinkedUnits())
        {
            if (linkedUnit instanceof GroundTrainUnit)
            {
                IGroundUnitCollection target = (IGroundUnitCollection)linkedUnit;
                assert(target.getGroundUnit().getSpawners().size() == 1);
            }
            else if (linkedUnit instanceof BalloonDefenseGroup)
            {
                IGroundUnitCollection target = (IGroundUnitCollection)linkedUnit;
                assert(target.getGroundUnit().getSpawners() != null);
            }
            else if (linkedUnit instanceof GroundMachineGunFlareUnit)
            {
                IGroundUnitCollection target = (IGroundUnitCollection)linkedUnit;
                assert(target.getGroundUnit().getSpawners().size() == 1);
            }
            else if (linkedUnit instanceof IGroundUnitCollection)
            {
                IGroundUnitCollection target = (IGroundUnitCollection)linkedUnit;
                assert(target.getGroundUnit().getSpawners().size() > 0);
            }
            else if (linkedUnit instanceof GroundUnitSpawning)
            {
                IGroundUnitCollection target = (IGroundUnitCollection)linkedUnit;
                assert(target.getGroundUnit().getSpawners().size() > 0);
                
            }
            else if (linkedUnit instanceof GroundUnit)
            {
                IGroundUnit target = (IGroundUnit)linkedUnit;
                assert(target.getVehicle() != null);
            }
        }
    }

    private void validateAiGroundUnits(Flight flight)
    {
        for (IUnit linkedUnit : flight.getLinkedUnits())
        {
            if (linkedUnit instanceof GroundTrainUnit)
            {
                IGroundUnitCollection target = (IGroundUnitCollection)linkedUnit;
                assert(target.getGroundUnit().getSpawners().size() == 1);
            }
            else if (linkedUnit instanceof BalloonDefenseGroup)
            {
                IGroundUnitCollection target = (IGroundUnitCollection)linkedUnit;
                assert(target.getGroundUnit().getSpawners() != null);
            }
            else if (linkedUnit instanceof GroundAAArtilleryBattery)
            {
                IGroundUnitCollection target = (IGroundUnitCollection)linkedUnit;
                assert(target.getGroundUnit().getSpawners().size() > 0);
            }
            else if (linkedUnit instanceof GroundUnitSpawning)
            {
                IGroundUnitCollection target = (IGroundUnitCollection)linkedUnit;
                assert(target.getGroundUnit().getSpawners().size() == 1);
                
            }
            else if (linkedUnit instanceof GroundUnit)
            {
                IGroundUnit target = (IGroundUnit)linkedUnit;
                assert(target.getVehicle() != null);
            }
        }
    }

}
