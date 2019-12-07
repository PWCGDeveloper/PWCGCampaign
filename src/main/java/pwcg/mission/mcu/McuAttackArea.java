package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.mission.flight.waypoint.WaypointPriority;

public class McuAttackArea extends BaseFlightMcu
{
    public enum AttackAreaType
    {
        AIR_TARGETS,
        GROUND_TARGETS,
        INDIRECT
    }
    
	private int attackIndirect  = 0;
	private int attackAir  = 0;
	private int attackGTargets = 0;
	private int attackRadius = 2000;
	private int time = 60;
	private WaypointPriority priority = WaypointPriority.PRIORITY_HIGH;

	public McuAttackArea (AttackAreaType attackAreaType)
	{
 		super();
 		setName("Command AttackArea");
 		
        if (attackAreaType == AttackAreaType.AIR_TARGETS)
        {
           attackAir = 1;
        }
        else if (attackAreaType == AttackAreaType.GROUND_TARGETS)
        {
            attackGTargets = 1;
        }
        else
        {
            attackIndirect = 1;
        }
	}
	
    private McuAttackArea()
    {
        super();
        setName("Command AttackArea");
    }

    public McuAttackArea copy ()
    {
        McuAttackArea clone = new McuAttackArea();
        
        super.clone(clone);
        
        clone.attackIndirect = this.attackIndirect;
        clone.attackAir = this.attackAir;
        clone.attackGTargets = this.attackGTargets;
        clone.attackRadius = this.attackRadius;
        clone.time = this.time;
        clone.priority = this.priority;
            
        return clone;
    }
	
	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
        {
            writer.write("MCU_CMD_AttackArea");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
            
            writer.write("  AttackGround = " + attackIndirect + ";");
            writer.newLine();
            writer.write("  AttackAir = " + attackAir + ";");
            writer.newLine();
            writer.write("  AttackGTargets = " + attackGTargets + ";");
            writer.newLine();
            writer.write("  AttackArea = " + attackRadius + ";");
            writer.newLine();
            writer.write("  Time = " + time + ";");
            writer.newLine();
            writer.write("  Priority = " + priority.getPriorityValue() + ";");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public WaypointPriority getPriority()
	{
		return priority;
	}

	public void setPriority(WaypointPriority priority)
	{
		this.priority = priority;
	}

    public void setAttackRadius(int attackRadius)
    {
        this.attackRadius = attackRadius;
    }
}
