package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.mission.Mission;
import pwcg.mission.Unit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.balloondefense.BalloonDefenseGroup;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.unittypes.artillery.GroundAAABattery;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunFlareUnit;
import pwcg.mission.ground.unittypes.infantry.GroundTroopConcentration;
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
        for (Unit linkedUnit : flight.getLinkedUnits())
        {
            if (linkedUnit instanceof GroundTrainUnit)
            {
                GroundTrainUnit target = (GroundTrainUnit)linkedUnit;
                assert(target.getSpawners().size() == 1);
            }
            else if (linkedUnit instanceof BalloonDefenseGroup)
            {
                BalloonDefenseGroup target = (BalloonDefenseGroup)linkedUnit;
                assert(target.getBalloon() != null);
            }
            else if (linkedUnit instanceof GroundMachineGunFlareUnit)
            {
                GroundMachineGunFlareUnit target = (GroundMachineGunFlareUnit)linkedUnit;
                assert(target.getSpawners().size() == 1);
            }
            else if (linkedUnit instanceof GroundAAABattery)
            {
                GroundAAABattery target = (GroundAAABattery)linkedUnit;
                assert(target.getSpawners().size() > 0);
            }
            else if (linkedUnit instanceof GroundUnitSpawning)
            {
                GroundUnitSpawning target = (GroundUnitSpawning)linkedUnit;
                assert(target.getSpawners().size() > 0);
                
            }
            else if (linkedUnit instanceof GroundUnit)
            {
                GroundUnit target = (GroundUnit)linkedUnit;
                assert(target.getVehicles().size() > 0);
            }
        }
    }

    private void validateAiGroundUnits(Flight flight)
    {
        for (Unit linkedUnit : flight.getLinkedUnits())
        {
            if (linkedUnit instanceof GroundTrainUnit)
            {
                GroundTrainUnit target = (GroundTrainUnit)linkedUnit;
                assert(target.getSpawners().size() == 1);
            }
            else if (linkedUnit instanceof BalloonDefenseGroup)
            {
                BalloonDefenseGroup target = (BalloonDefenseGroup)linkedUnit;
                assert(target.getBalloon() != null);
            }
            else if (linkedUnit instanceof GroundTroopConcentration)
            {
                GroundTroopConcentration target = (GroundTroopConcentration)linkedUnit;
                assert(target.getVehicles().size() == 3);  // One truck, one tank, one gun
            }
            else if (linkedUnit instanceof GroundAAABattery)
            {
                GroundAAABattery target = (GroundAAABattery)linkedUnit;
                assert(target.getSpawners().size() > 0);
            }
            else if (linkedUnit instanceof GroundUnitSpawning)
            {
                GroundUnitSpawning target = (GroundUnitSpawning)linkedUnit;
                assert(target.getSpawners().size() == 1);
                
            }
            else if (linkedUnit instanceof GroundUnit)
            {
                GroundUnit target = (GroundUnit)linkedUnit;
                assert(target.getVehicles().size() <= 2);
            }
        }
    }

}
