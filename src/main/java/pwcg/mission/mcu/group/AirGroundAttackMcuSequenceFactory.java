package pwcg.mission.mcu.group;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.AttackAreaType;

public class AirGroundAttackMcuSequenceFactory
{
    public static IAirGroundAttackMcuSequence buildAirGroundAttackSequence(IFlight flight, int attackTimeSeconds, AttackAreaType attackAreaType) throws PWCGException
    {
        if (flight.getFlightInformation().isPlayerFlight())
        {
            IAirGroundAttackMcuSequence attackMcuSequence = new PlayerAirGroundAttackMcuSequence(flight);
            attackMcuSequence.createAttackArea(attackTimeSeconds, attackAreaType);        
            return attackMcuSequence;
        }
        else
        {
            IAirGroundAttackMcuSequence attackMcuSequence = new AiAirGroundAttackMcuSequence(flight);
            attackMcuSequence.createAttackArea(attackTimeSeconds, attackAreaType);        
            return attackMcuSequence;
        }
    }
}
