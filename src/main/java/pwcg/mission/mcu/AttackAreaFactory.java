package pwcg.mission.mcu;

import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightTypes;

public class AttackAreaFactory
{
    public static McuAttackArea createAttackArea(FlightTypes flightType, String name, Coordinate targetCoords, int altitude, int attackTime)
    {
        McuAttackArea attackArea = new McuAttackArea();
        attackArea.setAttackGround(0);
        attackArea.setAttackGTargets(0);
        attackArea.setAttackAir(0);
        attackArea.setName("Attack Area for " + name);
        attackArea.setDesc("Attack Area for " + name);
        attackArea.setTime(attackTime);

        attackArea.setOrientation(new Orientation());

        Coordinate attackAreaCoords = targetCoords.copy();
        attackAreaCoords.setYPos(altitude);

        attackArea.setPosition(attackAreaCoords);

        if (flightType == FlightTypes.BOMB || flightType == FlightTypes.ANTI_SHIPPING_BOMB)
        {
            attackArea.setAttackGround(1);
            attackArea.setAttackArea(500);
        }
        else
        {
            attackArea.setAttackGTargets(1);
            attackArea.setAttackArea(7000);
        }
        
        return attackArea;
    }
}
