package pwcg.mission.ground.org;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;

public class GroundAspectAreaFire implements IGroundAspect
{
    private McuTimer attackAreaTimer = new McuTimer();
    private McuAttackArea attackArea;
    private IVehicle vehicle;
    private int attackAreaDistance = 1000;
    private Coordinate targetPosition;

	public GroundAspectAreaFire(Coordinate targetPosition, IVehicle vehicle, AttackAreaType attackAreaType, int attackAreaDistance)
	{
        this.vehicle = vehicle;
        this.targetPosition = targetPosition;
        this.attackAreaDistance = attackAreaDistance;
        this.attackArea = new McuAttackArea(attackAreaType);
	}

	@Override
    public void createGroundUnitAspect() throws PWCGException 
    {
        attackAreaTimer.setName("AttackArea Timer");      
        attackAreaTimer.setDesc("AttackArea Timer");       
        attackAreaTimer.setPosition(targetPosition);

        attackArea.setName("AttackArea");
        attackArea.setDesc("AttackArea");
        attackArea.setPriority(WaypointPriority.PRIORITY_HIGH);
        attackArea.setAttackRadius(attackAreaDistance);
        attackArea.setTime(3600);
        
        attackArea.setOrientation(new Orientation());       
        attackArea.setPosition(targetPosition); 

        createTargetAssociations();
        createObjectAssociations();
    }   

    @Override
    public void linkToNextElement(int targetIndex)
    {
        attackAreaTimer.setTimerTarget(targetIndex);
    }

	@Override
    public int getEntryPoint()
	{
	    return attackAreaTimer.getIndex();
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        attackAreaTimer.write(writer);
        attackArea.write(writer);
    }

    private void createTargetAssociations() 
    {
        attackAreaTimer.setTimerTarget(attackArea.getIndex());
    }
    
    private void createObjectAssociations() 
    {
        attackArea.setObject(vehicle.getEntity().getIndex());
    }

    @Override
    public BaseFlightMcu getEntryPointMcu()
    {
        return attackAreaTimer;
    }

    @Override
    public void validate() throws PWCGException
    {
        if (!McuValidator.hasTarget(attackAreaTimer, attackArea.getIndex()))
        {
            throw new PWCGException("GroundElementAreaFire: attack area timer not linked to attack area");
        }
        
        if (!McuValidator.hasObject(attackArea, vehicle.getEntity().getIndex()))
        {
            throw new PWCGException("GroundElementAreaFire: attack not linked to vehicle");
        }
    }

    public void setTargetPosition(Coordinate targetPosition)
    {
        this.targetPosition = targetPosition.copy();
        attackArea.setPosition(this.targetPosition.copy()); 
    }
}	

