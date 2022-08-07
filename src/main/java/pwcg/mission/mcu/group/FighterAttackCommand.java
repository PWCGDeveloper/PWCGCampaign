package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.mcu.McuAttack;
import pwcg.mission.mcu.McuTimer;

public class FighterAttackCommand
{
    private McuTimer startFighterAttackTimer  = new McuTimer();
    private McuAttack fighterAttack = new McuAttack();
    
    
    public FighterAttackCommand(int planeIndex)
    {
        this.fighterAttack.setObject(planeIndex);  
        
        startFighterAttackTimer.setTime(20);
        startFighterAttackTimer.setTimerTarget(fighterAttack.getIndex());
    }


    public void write(BufferedWriter writer) throws PWCGException
    {
        startFighterAttackTimer.write(writer);
        fighterAttack.write(writer);
    }


    public void setPosition(Coordinate position)
    {
        startFighterAttackTimer.setPosition(position.copy());
        fighterAttack.setPosition(position.copy());
    }

    public void setOrientation(Orientation orientation)
    {
        fighterAttack.setOrientation(orientation.copy());
    }

    public void addTargets(List<Integer> targets)
    {
        for (int target : targets)
        {
            fighterAttack.setAttackTarget(target);
        }
    }

    public int getFighterAttackTarget()
    {
        return startFighterAttackTimer.getIndex();
    }
}
