package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.mission.flight.waypoint.WaypointGoal;
import pwcg.mission.flight.waypoint.WaypointPriority;

public class McuAttackArea extends BaseFlightMcu
{
	private int attackGround  = 0;
	private int attackAir  = 0;
	private int attackGTargets = 0;
	private int attackArea = 2000;
	private int time = 60;
	private WaypointPriority priority = WaypointPriority.PRIORITY_HIGH;
	private WaypointGoal goalType = WaypointGoal.GOAL_PRIMARY;

	public McuAttackArea ()
	{
 		super();
 		setName("Command AttackArea");
	}
	
	   
    public McuAttackArea copy ()
    {
        McuAttackArea clone = new McuAttackArea();
        
        super.clone(clone);
        
        clone.attackGround = this.attackGround;
        clone.attackAir = this.attackAir;
        clone.attackGTargets = this.attackGTargets;
        clone.attackArea = this.attackArea;
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
            
            writer.write("  AttackGround = " + attackGround + ";");
            writer.newLine();
            writer.write("  AttackAir = " + attackAir + ";");
            writer.newLine();
            writer.write("  AttackGTargets = " + attackGTargets + ";");
            writer.newLine();
            writer.write("  AttackArea = " + attackArea + ";");
            writer.newLine();
            writer.write("  Time = " + time + ";");
            writer.newLine();
            writeMCUGoal(writer, goalType.getGoal());
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

	public int getAttackGround() {
		return attackGround;
	}

	public void setAttackGround(int attackGround) {
		this.attackGround = attackGround;
	}

	public int getAttackAir() {
		return attackAir;
	}

	public void setAttackAir(int attackAir) {
		this.attackAir = attackAir;
	}

	public int getAttackGTargets() {
		return attackGTargets;
	}

	public void setAttackGTargets(int attackGTargets) {
		this.attackGTargets = attackGTargets;
	}

	public int getAttackArea() {
		return attackArea;
	}

	public void setAttackArea(int attackArea) {
		this.attackArea = attackArea;
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


	public WaypointGoal getGoalType()
	{
		return goalType;
	}


	public void setGoalType(WaypointGoal goalType)
	{
		this.goalType = goalType;
	}
	
}
