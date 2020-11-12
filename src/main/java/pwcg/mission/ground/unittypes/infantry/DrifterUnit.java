package pwcg.mission.ground.unittypes.infantry;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.IVehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleRequestDefinition;

public class DrifterUnit extends GroundUnit
{
    public DrifterUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Drifter, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException
    {
        super.createSpawnTimer();
        
        VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(pwcgGroundUnitInformation.getCountry().getCountry(), pwcgGroundUnitInformation.getDate(), vehicleClass);
        IVehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionForRequest(requestDefinition);

        List<Coordinate> vehicleStartPositions = createVehicleStartPositions(vehicleDefinition);
        super.createVehiclesFromDefinition(vehicleStartPositions, vehicleDefinition);
        super.linkElements();
    }

    private List<Coordinate> createVehicleStartPositions(IVehicleDefinition vehicleDefinition) throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        int numDrifter = calcNumUnits(vehicleDefinition);

        // Face towards orientation
        double drifterFacingAngle = MathUtils.adjustAngle(pwcgGroundUnitInformation.getOrientation().getyOri(), 180.0);
        Orientation drifterOrient = new Orientation();
        drifterOrient.setyOri(drifterFacingAngle);
        
        Coordinate drifterCoords = pwcgGroundUnitInformation.getPosition().copy();

        double drifterSpacing = 30.0;
        if (vehicleDefinition.getVehicleLength() > drifterSpacing)
        {
            drifterSpacing = vehicleDefinition.getVehicleLength();
        }
        
        // Direction in which subsequent units will be placed
        double placementOrientation = pwcgGroundUnitInformation.getOrientation().getyOri();        

        for (int i = 0; i < numDrifter; ++i)
        {   
            drifterCoords.setYPos(0.0);
            spawnerLocations.add(drifterCoords);

            drifterCoords = MathUtils.calcNextCoord(drifterCoords, placementOrientation, drifterSpacing);
        }
        return spawnerLocations;       
    }

    private int calcNumUnits(IVehicleDefinition vehicleDefinition) throws PWCGException
    {
        int numUnits = 1;
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            numUnits = GroundUnitNumberCalculator.calcNumUnits(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            numUnits = GroundUnitNumberCalculator.calcNumUnits(2, 3);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            numUnits = GroundUnitNumberCalculator.calcNumUnits(2, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            numUnits = GroundUnitNumberCalculator.calcNumUnits(3, 6);
        }
        
        if (vehicleDefinition.getVehicleLength() > 200.0)
        {
            if (numUnits > 2)
            {
                numUnits = 2;
            }
        }
        else if (vehicleDefinition.getVehicleLength() > 50.0)
        {
            if (numUnits > 3)
            {
                numUnits = 3;
            }
        }
        
        return numUnits;
    }
}