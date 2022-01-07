package pwcg.mission.mcu;

import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;

public class AttackAreaFactory
{
    public static final int ATTACK_AREA_SELECT_TARGET_DISTANCE = 5000;
    public static final int ATTACK_AREA_BOMB_DROP_DISTANCE = 1000;
    public static final int ATTACK_AREA_AA_TRUCK_PROXIMITY_DISTANCE = 1000;
    
    public static McuAttackArea createAttackArea(FlightInformation flightInformation, Coordinate targetCoords, int attackTime)
    {
        FlightTypes flightType = flightInformation.getFlightType();
        McuAttackArea attackArea = null;
        if (flightType == FlightTypes.LOW_ALT_BOMB || 
            flightType == FlightTypes.PARATROOP_DROP ||
            flightType == FlightTypes.CARGO_DROP)
        {
            attackArea = new McuAttackArea(AttackAreaType.INDIRECT);
            attackArea.setAttackRadius(ATTACK_AREA_BOMB_DROP_DISTANCE);
        }
        else
        {
            attackArea = new McuAttackArea(AttackAreaType.GROUND_TARGETS);
            attackArea.setAttackRadius(ATTACK_AREA_SELECT_TARGET_DISTANCE);
        }
        
        if (flightInformation.getMission().isAAATruckMission())
        {
            attackArea.setAttackRadius(ATTACK_AREA_AA_TRUCK_PROXIMITY_DISTANCE);
        }

        attackArea.setName("Attack Area");
        attackArea.setDesc("Attack Area");
        attackArea.setTime(attackTime);

        attackArea.setOrientation(new Orientation());

        Coordinate attackAreaCoords = targetCoords.copy();
        attackAreaCoords.setYPos(flightInformation.getAltitude());

        attackArea.setPosition(attackAreaCoords);

        return attackArea;
    }
}
