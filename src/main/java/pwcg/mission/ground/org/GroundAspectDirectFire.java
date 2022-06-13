package pwcg.mission.ground.org;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuAttack;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;

public class GroundAspectDirectFire implements IGroundAspect
{
    private McuTimer attackTimer = new McuTimer();
    private McuAttack attackEntity = new McuAttack();
    private IVehicle vehicle;

    public GroundAspectDirectFire(IVehicle vehicle) 
    {
        this.vehicle = vehicle;
    }

    public void createGroundUnitAspect() 
    {
        attackTimer.setName("Attack Timer");      
        attackTimer.setDesc("Attack Timer");       
        attackTimer.setPosition(vehicle.getPosition());

        attackEntity.setName("Attack");
        attackEntity.setDesc("Attack");
        attackEntity.setOrientation(new Orientation());       
        attackEntity.setPosition(vehicle.getPosition()); 

        createTargetAssociations();
        createObjectAssociations();
    }   

    public void addTarget(int targetIndex)
    {
        attackEntity.setAttackTarget(targetIndex);
    }

    @Override
    public void linkToNextElement(int targetIndex)
    {
        attackTimer.setTimerTarget(targetIndex);
    }

    @Override
    public int getEntryPoint()
    {
        return attackTimer.getIndex();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        attackTimer.write(writer);
        attackEntity.write(writer);
    }

    private void createTargetAssociations() 
    {
        attackTimer.setTimerTarget(attackEntity.getIndex());
    }
    
    private void createObjectAssociations() 
    {
        attackEntity.setObject(vehicle.getEntity().getIndex());
    }

    @Override
    public BaseFlightMcu getEntryPointMcu()
    {
        return attackTimer;
    }

    @Override
    public void validate() throws PWCGException
    {
        if (!McuValidator.hasTarget(attackTimer, attackEntity.getIndex()))
        {
            throw new PWCGException("GroundElementDirectFire: attack timer not linked to attack");
        }
        
        if (!McuValidator.hasObject(attackEntity, vehicle.getEntity().getIndex()))
        {
            throw new PWCGException("GroundElementDirectFire: attack not linked to vehicle");
        }
    }
}	

