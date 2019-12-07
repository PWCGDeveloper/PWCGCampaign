package pwcg.mission.ground.org;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuAttackArea.AttackAreaType;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;

public class GroundElementAreaFire implements IGroundElement
{
    private McuTimer attackAreaTimer = new McuTimer();
    private McuAttackArea attackArea = new McuAttackArea(AttackAreaType.AIR_TARGETS);
    private GroundUnitInformation pwcgGroundUnitInformation;
    private IVehicle vehicle;
    private int attackAreaDistance = 1000;

	public GroundElementAreaFire(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle, int attackAreaDistance)
	{
        this.pwcgGroundUnitInformation = pwcgGroundUnitInformation;
        this.vehicle = vehicle;
        this.attackAreaDistance = attackAreaDistance;
	}

	@Override
    public void createGroundUnitElement() 
    {
        attackAreaTimer.setName(pwcgGroundUnitInformation.getName() + " AttackArea Timer");      
        attackAreaTimer.setDesc(pwcgGroundUnitInformation.getName() + " AttackArea Timer");       
        attackAreaTimer.setPosition(pwcgGroundUnitInformation.getPosition());

        attackArea.setName(pwcgGroundUnitInformation.getName() + " AttackArea");
        attackArea.setDesc(pwcgGroundUnitInformation.getName() + " AttackArea");
        attackArea.setOrientation(new Orientation());       
        attackArea.setPosition(pwcgGroundUnitInformation.getFireTarget()); 
        attackArea.setPriority(WaypointPriority.PRIORITY_HIGH);
        attackArea.setAttackRadius(attackAreaDistance);
        attackArea.setTime(3600);

        createTargetAssociations();
        createObjectAssociations();
    }   

    @Override
    public void linkToNextElement(int targetIndex)
    {
        attackAreaTimer.setTarget(targetIndex);
    }

	@Override
    public int getEntryPoint()
	{
	    return attackAreaTimer.getIndex();
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        attackAreaTimer.write(writer);
        attackArea.write(writer);
    }

    private void createTargetAssociations() 
    {
        attackAreaTimer.setTarget(attackArea.getIndex());
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
}	

