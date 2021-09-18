package pwcg.mission.mcu.group;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.AttackAreaType;

public class AirGroundAttackMcuSequenceFactory
{
    public static AirGroundAttackMcuSequence buildAirGroundAttackSequence(
            IFlight flight, 
            int maxAttackTimeSeconds, 
            int bingoLoiterTimeSeconds, 
            AttackAreaType attackAreaType) throws PWCGException
    {
        AirGroundAttackMcuSequence attackMcuSequence = new AirGroundAttackMcuSequence(flight);
        attackMcuSequence.createAttackSequence(maxAttackTimeSeconds, bingoLoiterTimeSeconds, attackAreaType);        
        return attackMcuSequence;
    }
    
    public static AirGroundAttackTargetMcuSequence buildAirGroundTargetMcuSequence(
            IFlight flight, 
            List<IVehicle> vehicles,
            int maxAttackTimeSeconds, 
            int bingoLoiterTimeSeconds) throws PWCGException
    {
        AirGroundAttackTargetMcuSequence attackMcuSequence = new AirGroundAttackTargetMcuSequence(flight, vehicles);
        attackMcuSequence.createAttackSequence(maxAttackTimeSeconds, bingoLoiterTimeSeconds);        
        return attackMcuSequence;
    }
}
