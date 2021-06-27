package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.BaseFlightMcu;

public interface IAirGroundAttackAreaMcuSequence
{
    Coordinate getPosition();

    BaseFlightMcu getAttackAreaMcu();

    void setLinkToNextTarget(int targetIndex);

    void write(BufferedWriter writer) throws PWCGException;

    void setAttackToTriggerOnPlane(List<PlaneMcu> planes) throws PWCGException;    
    
    void setVehiclesToAttack(List<IVehicle> vehicles) throws PWCGException;    
}