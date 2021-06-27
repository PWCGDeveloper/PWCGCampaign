package pwcg.mission.mcu.group;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.AttackAreaType;

public class AirGroundAttackMcuSequenceFactory
{
    public static IAirGroundAttackAreaMcuSequence buildAirGroundAttackSequence(
            IFlight flight, 
            int maxAttackTimeSeconds, 
            int bingoLoiterTimeSeconds, 
            AttackAreaType attackAreaType) throws PWCGException
    {
        if (attackAreaType == AttackAreaType.SPECIFIC_TARGETS)
        {
            AirGroundAttackTargetMcuSequence attackMcuSequence = new AirGroundAttackTargetMcuSequence(flight);
            attackMcuSequence.createAttackSequence(maxAttackTimeSeconds, bingoLoiterTimeSeconds);        
            return attackMcuSequence;
        }
        else
        {
            AirGroundAttackMcuSequence attackMcuSequence = new AirGroundAttackMcuSequence(flight);
            attackMcuSequence.createAttackSequence(maxAttackTimeSeconds, bingoLoiterTimeSeconds, attackAreaType);        
            return attackMcuSequence;
        }
    }
}
