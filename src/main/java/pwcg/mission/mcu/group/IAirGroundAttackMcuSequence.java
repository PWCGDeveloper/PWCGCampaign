package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuWaypoint;

public interface IAirGroundAttackMcuSequence
{

    void createAttackArea(int attackTime, AttackAreaType attackAreaType) throws PWCGException;

    Coordinate getPosition();

    McuAttackArea getAttackAreaMcu();

    void setLinkToNextTarget(int targetIndex);

    void write(BufferedWriter writer) throws PWCGException;

    void setAttackToTriggerOnPlane(int planeIndex);

    void setLinkToAttack(McuWaypoint linkToAttack);

}