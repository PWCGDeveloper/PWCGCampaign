package pwcg.mission.mcu.group;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.AttackAreaType;

public class AirGroundAttackMcuSequenceFactory
{
    public static IAirGroundAttackMcuSequence buildAirGroundAttackSequence(IFlight flight, int maxAttackTimeSeconds, int bingoLoiterTimeSeconds, AttackAreaType attackAreaType) throws PWCGException
    {
        IAirGroundAttackMcuSequence attackMcuSequence = new AirGroundAttackMcuSequence(flight);
        attackMcuSequence.createAttackArea(maxAttackTimeSeconds, bingoLoiterTimeSeconds, attackAreaType);        
        return attackMcuSequence;
    }
}
