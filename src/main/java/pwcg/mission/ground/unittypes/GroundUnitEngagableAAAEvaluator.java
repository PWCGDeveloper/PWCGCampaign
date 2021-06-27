package pwcg.mission.ground.unittypes;

import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;
import pwcg.mission.ground.building.PwcgStructure;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TargetType;

public class GroundUnitEngagableAAAEvaluator
{

    public static void setAAUnitsEngageableStatus(Mission mission)
    {
        for (GroundUnitCollection groundUnitCollection : mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                determineEngageableAAAUnit(mission, groundUnit);
            }
        }
    }

    private static void determineEngageableAAAUnit(Mission mission, IGroundUnit groundUnit)
    {
        try
        {
            VehicleClass vehicleClass = groundUnit.getVehicleClass();
            if (vehicleClass == VehicleClass.AAAArtillery || vehicleClass == VehicleClass.AAAMachineGun)
            {
                if (isAAAGuardingStructure(mission, groundUnit))
                {
                    setVehiclesInUnitEngageable(groundUnit);
                }
                else
                {
                    setVehiclesInUnitNotEngageable(groundUnit);
                }
            }
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
        }
    }

    private static boolean isAAAGuardingStructure(Mission mission, IGroundUnit groundUnit) throws PWCGException
    {
        for (FixedPosition block : mission.getMissionBlocks().getAllStructuresForMission())
        {
            if (isCloseEnough(block.getPosition(), groundUnit.getPosition()))
            {
                PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding(block.getModel());
                if (building.toTargetType() == TargetType.TARGET_BRIDGE)
                {
                    return true;
                }
                else if (building.toTargetType() == TargetType.TARGET_AIRFIELD)
                {
                    return true;
                }
            }
        }
        
        for (Airfield airfield : mission.getFieldsForPatrol())
        {
            if (isCloseEnough(airfield.getPosition(), groundUnit.getPosition()))
            {
                return true;
            }
        }
        return false;
    }    

    private static boolean isCloseEnough(Coordinate blockPosition, Coordinate groundUnitPosition) throws PWCGException
    {
        double distance = MathUtils.calcDist(blockPosition, groundUnitPosition);
        if (distance < 1500)
        {
            return true;
        }
        return false;
    }

    private static void setVehiclesInUnitEngageable(IGroundUnit groundUnit)
    {
        for (IVehicle vehicle : groundUnit.getVehicles())
        {
            vehicle.setEngageable(1);
        }
    }

    private static void setVehiclesInUnitNotEngageable(IGroundUnit groundUnit)
    {
        for (IVehicle vehicle : groundUnit.getVehicles())
        {
            vehicle.setEngageable(0);
        }
    }
}
