package pwcg.mission.ground.unittypes.artillery;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundElementFactory;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.org.IGroundElement;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.AttackAreaType;

public class GroundAAMachineGunBattery extends GroundUnit
{
    static private int AA_MG_ATTACK_AREA = 800;

    public GroundAAMachineGunBattery(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.AAAMachineGun, pwcgGroundUnitInformation);
    }

    @Override
    protected List<Coordinate> createSpawnerLocations() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();
        
        int numAAA = calcNumUnits();
        for (int i = 0; i < numAAA; ++i)
        {
            Coordinate spawnPosition = pwcgGroundUnitInformation.getPosition().copy();
            if (numAAA == 1)
            {
                spawnPosition = pwcgGroundUnitInformation.getPosition().copy();
            }
            else
            {
                if (i == 0)
                {
                    spawnPosition.setXPos(spawnPosition.getXPos() + 100);
                }
                else if (i == 1)
                {
                    spawnPosition.setXPos(spawnPosition.getXPos() - 100);
                }
                else if (i == 2)
                {
                    spawnPosition.setZPos(spawnPosition.getZPos() - 100);             
                }
                else if (i == 3)
                {
                    spawnPosition.setZPos(spawnPosition.getZPos() + 100);             
                }
            }
            spawnerLocations.add(spawnPosition);
        }
        return spawnerLocations;
    }

    protected int calcNumUnits() throws PWCGException
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            return GroundUnitNumberCalculator.calcNumUnits(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            return GroundUnitNumberCalculator.calcNumUnits(1, 2);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 3);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(3, 4);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    @Override
    protected void addElements() throws PWCGException
    {
        IGroundElement areaFire = GroundElementFactory.createGroundElementAreaFire(pwcgGroundUnitInformation, pwcgGroundUnitInformation.getPosition(), vehicle, AttackAreaType.AIR_TARGETS, AA_MG_ATTACK_AREA);
        this.addGroundElement(areaFire);        
    }
}
