package pwcg.mission.mcu;

import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuAttackArea.AttackAreaType;

public class AttackAreaFactory
{
    public static McuAttackArea createAttackArea(FlightTypes flightType, String name, Coordinate targetCoords, int altitude, int attackTime)
    {
        McuAttackArea attackArea = null;
        if (flightType == FlightTypes.BOMB || 
            flightType == FlightTypes.ANTI_SHIPPING_BOMB || 
            flightType == FlightTypes.LOW_ALT_BOMB || 
            flightType == FlightTypes.TRANSPORT ||
            flightType == FlightTypes.CARGO_DROP)
        {
            attackArea = new McuAttackArea(AttackAreaType.INDIRECT);
            attackArea.setAttackRadius(500);
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING_ATTACK || 
                flightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB ||
                flightType == FlightTypes.DIVE_BOMB || 
                flightType == FlightTypes.GROUND_ATTACK)
        {
            attackArea = new McuAttackArea(AttackAreaType.GROUND_TARGETS);
            attackArea.setAttackRadius(7000);
        }
        else
        {
            attackArea = new McuAttackArea(AttackAreaType.AIR_TARGETS);
        }

        attackArea.setName("Attack Area for " + name);
        attackArea.setDesc("Attack Area for " + name);
        attackArea.setTime(attackTime);

        attackArea.setOrientation(new Orientation());

        Coordinate attackAreaCoords = targetCoords.copy();
        attackAreaCoords.setYPos(altitude);

        attackArea.setPosition(attackAreaCoords);

        return attackArea;
    }
}
