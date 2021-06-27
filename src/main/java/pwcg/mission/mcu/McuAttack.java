package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.waypoint.WaypointPriority;

public class McuAttack extends BaseFlightMcu
{
	private int attackGroup  = 1;
	private WaypointPriority priority = WaypointPriority.PRIORITY_HIGH;

	public McuAttack ()
	{
 		super();
	}
	
    public McuAttack copy ()
    {
        McuAttack clone = new McuAttack();
        
        super.clone(clone);
        
        clone.attackGroup = this.attackGroup;
        clone.priority = this.priority;
            
        return clone;
    }

	public int getAttackGroup() {
		return attackGroup;
	}

	public void setAttackGroup(int attackGroup) {
		this.attackGroup = attackGroup;
	}

	public WaypointPriority getPriority() {
		return priority;
	}

	public void setPriority(WaypointPriority priority) {
		this.priority = priority;
	}

	public void write(BufferedWriter writer) throws PWCGException
	{
		try
        {
            writer.write("MCU_CMD_AttackTarget");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
            
            writer.write("  AttackGroup = " + attackGroup + ";");
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
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}	
}
