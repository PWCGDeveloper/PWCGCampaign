package pwcg.mission.ground.unittypes.infantry;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundElementFactory;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.org.IGroundElement;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.AttackAreaType;

public class DrifterAAAUnit extends GroundUnit
{
    static private int DRIFTER_AA_ATTACK_AREA = 1200;

    public DrifterAAAUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.DrifterAAA, pwcgGroundUnitInformation);
    }   

    @Override
    protected List<Coordinate> createSpawnerLocations() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        int numDrifter = calcNumUnits();

        // Face towards orientation
        double drifterFacingAngle = MathUtils.adjustAngle(pwcgGroundUnitInformation.getOrientation().getyOri(), 180.0);
        Orientation drifterOrient = new Orientation();
        drifterOrient.setyOri(drifterFacingAngle);
        
        Coordinate drifterCoords = pwcgGroundUnitInformation.getPosition().copy();
        drifterCoords.setZPos(drifterCoords.getZPos() + 30.0);

        double drifterSpacing = 30.0;
        
        // Direction in which subsequent units will be placed
        double placementOrientation = pwcgGroundUnitInformation.getOrientation().getyOri();        

        for (int i = 0; i < numDrifter; ++i)
        {   
            spawnerLocations.add(drifterCoords);
            drifterCoords = MathUtils.calcNextCoord(drifterCoords, placementOrientation, drifterSpacing);
        }
        return spawnerLocations;       
    }

    protected int calcNumUnits() throws PWCGException
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
             return GroundUnitNumberCalculator.calcNumUnits(1, 1);
        }
        else
        {
             return GroundUnitNumberCalculator.calcNumUnits(2, 2);
        }
    }

    @Override
    protected void addElements() throws PWCGException
    {
        IGroundElement areaFire = GroundElementFactory.createGroundElementAreaFire(pwcgGroundUnitInformation, pwcgGroundUnitInformation.getPosition(), vehicle, AttackAreaType.AIR_TARGETS, DRIFTER_AA_ATTACK_AREA);
        this.addGroundElement(areaFire);        
    }
}