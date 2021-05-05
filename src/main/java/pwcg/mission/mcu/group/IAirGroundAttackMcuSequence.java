package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuAttackArea;

public interface IAirGroundAttackMcuSequence
{

    void createAttackArea(int maxAttackTimeSeconds, int bingoLoiterTimeSeconds, AttackAreaType attackAreaType) throws PWCGException;

    Coordinate getPosition();

    McuAttackArea getAttackAreaMcu();

    void setLinkToNextTarget(int targetIndex);

    void write(BufferedWriter writer) throws PWCGException;

    void setAttackToTriggerOnPlane(List<PlaneMcu> planes) throws PWCGException;
}