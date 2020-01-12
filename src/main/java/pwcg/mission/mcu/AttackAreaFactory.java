package pwcg.mission.mcu;

import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuAttackArea.AttackAreaType;

public class AttackAreaFactory
{
    public static final int ATTACK_AREA_SELECT_TARGET_DISTANCE = 7000;
    public static final int ATTACK_AREA_BOMB_DROP_DISTANCE = 1000;
    
    public static McuAttackArea createAttackArea(FlightTypes flightType, Coordinate targetCoords, int altitude, int attackTime)
    {
        McuAttackArea attackArea = null;
        if (flightType == FlightTypes.BOMB || 
            flightType == FlightTypes.ANTI_SHIPPING_BOMB || 
            flightType == FlightTypes.LOW_ALT_BOMB || 
            flightType == FlightTypes.TRANSPORT ||
            flightType == FlightTypes.CARGO_DROP)
        {
            attackArea = new McuAttackArea(AttackAreaType.INDIRECT);
            attackArea.setAttackRadius(ATTACK_AREA_BOMB_DROP_DISTANCE);
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING_ATTACK || 
                flightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB ||
                flightType == FlightTypes.DIVE_BOMB || 
                flightType == FlightTypes.GROUND_ATTACK)
        {
            attackArea = new McuAttackArea(AttackAreaType.GROUND_TARGETS);
            attackArea.setAttackRadius(ATTACK_AREA_SELECT_TARGET_DISTANCE);
        }
        else
        {
            attackArea = new McuAttackArea(AttackAreaType.AIR_TARGETS);
        }

        attackArea.setName("Attack Area");
        attackArea.setDesc("Attack Area");
        attackArea.setTime(attackTime);

        attackArea.setOrientation(new Orientation());

        Coordinate attackAreaCoords = targetCoords.copy();
        attackAreaCoords.setYPos(altitude);

        attackArea.setPosition(attackAreaCoords);

        return attackArea;
    }
}
