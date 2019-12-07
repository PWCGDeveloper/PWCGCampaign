package pwcg.mission.ground.org;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuAttack;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;

public class GroundElementDirectFire implements IGroundElement
{
    private McuTimer attackTimer = new McuTimer();
    private McuAttack attackEntity = new McuAttack();
    private GroundUnitInformation pwcgGroundUnitInformation;
    private IVehicle vehicle;

    public GroundElementDirectFire(GroundUnitInformation pwcgGroundUnitInformation, IVehicle vehicle) 
    {
        this.pwcgGroundUnitInformation = pwcgGroundUnitInformation;
        this.vehicle = vehicle;
    }

    public void createGroundUnitElement() 
    {
        attackTimer.setName(pwcgGroundUnitInformation.getName() + " Attack Timer");      
        attackTimer.setDesc(pwcgGroundUnitInformation.getName() + " Attack Timer");       
        attackTimer.setPosition(pwcgGroundUnitInformation.getPosition());

        attackEntity.setName(pwcgGroundUnitInformation.getName() + " Attack");
        attackEntity.setDesc(pwcgGroundUnitInformation.getName() + " Attack");
        attackEntity.setOrientation(new Orientation());       
        attackEntity.setPosition(pwcgGroundUnitInformation.getPosition()); 

        createTargetAssociations();
        createObjectAssociations();
    }   

    public void addTarget(int targetIndex)
    {
        attackEntity.setTarget(targetIndex);
    }

    @Override
    public void linkToNextElement(int targetIndex)
    {
        attackTimer.setTarget(targetIndex);
    }

    @Override
    public int getEntryPoint()
    {
        return attackTimer.getIndex();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        attackTimer.write(writer);
        attackEntity.write(writer);
    }

    private void createTargetAssociations() 
    {
        attackTimer.setTarget(attackEntity.getIndex());
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

