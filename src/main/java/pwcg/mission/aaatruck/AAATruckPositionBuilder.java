package pwcg.mission.aaatruck;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

public class AAATruckPositionBuilder
{
    private static final int DISTANCE_OFFSET = 75;
    private Mission mission;
    private Coordinate truckPosition;
    private Orientation truckOrientation;
    
    public AAATruckPositionBuilder(Mission mission)
    {
        this.mission = mission;
    }

    public void buildTruckPosition(Side truckSide) throws PWCGException
    {
        mission.getMissionFlights().removePlayerFlights();
        getBestTruckPosition(truckSide);
    }

    public Coordinate getTruckPosition()
    {
        return truckPosition;
    }

    public Orientation getTruckOrientation()
    {
        return truckOrientation;
    }

    private void getBestTruckPosition(Side truckSide) throws PWCGException
    {
        IGroundUnit selectedFriendlyUnit = null;

        List<IGroundUnit> vehiclesByPreference = getBestGroundUnitsForVehicleClasses(truckSide);
        for (IGroundUnit friendlyGroundUnit : vehiclesByPreference)
        {
            if (friendlyGroundUnit != null)
            {
                selectedFriendlyUnit = friendlyGroundUnit;
                break;
            }
        }
        
        truckOrientation = selectedFriendlyUnit.getSpawns().get(0).getOrientation().copy();

        double angleOffset = MathUtils.adjustAngle(truckOrientation.getyOri(), 180);
        truckPosition = MathUtils.calcNextCoord(selectedFriendlyUnit.getPosition(), angleOffset, DISTANCE_OFFSET);
    }

    List<IGroundUnit> getBestGroundUnitsForVehicleClasses(Side truckSide) throws PWCGException
    {
        List<IGroundUnit> vehiclesByPreference = new ArrayList<>();

        IGroundUnit tankUnit = getBestGroundUnitForVehicleClass(truckSide, VehicleClass.Tank);
        IGroundUnit atGunUnit = getBestGroundUnitForVehicleClass(truckSide, VehicleClass.ArtilleryAntiTank);
        IGroundUnit truckUnit = getBestGroundUnitForVehicleClass(truckSide, VehicleClass.Truck);
        IGroundUnit trainUnit = getBestGroundUnitForVehicleClass(truckSide, VehicleClass.TrainLocomotive);
        IGroundUnit aaaMGUnit = getBestGroundUnitForVehicleClass(truckSide, VehicleClass.AAAMachineGun);
        IGroundUnit howitzerUnit = getBestGroundUnitForVehicleClass(truckSide, VehicleClass.ArtilleryHowitzer);

        vehiclesByPreference.add(tankUnit);
        vehiclesByPreference.add(atGunUnit);
        vehiclesByPreference.add(truckUnit);
        vehiclesByPreference.add(trainUnit);
        vehiclesByPreference.add(aaaMGUnit);
        vehiclesByPreference.add(howitzerUnit);
        return vehiclesByPreference;
    }

    private IGroundUnit getBestGroundUnitForVehicleClass(Side truckSide, VehicleClass vehicleTypeToFind) throws PWCGException
    {
        Coordinate missionCenter = mission.getMissionBorders().getCenter();
        double closestUnitDistance = 1000000000.0;
        IGroundUnit friendlyUnitClosestToMissionCenter = null;
        for (GroundUnitCollection groundUnitCollection : mission.getMissionGroundUnitBuilder().getAssaults())
        {
            for (IGroundUnit friendlyGroundUnit : groundUnitCollection.getGroundUnitsForSide(truckSide))
            {
                if (friendlyGroundUnit.getVehicleClass() == vehicleTypeToFind)
                {
                    double distanceBetweenCenterAndUnit = MathUtils.calcDist(missionCenter, friendlyGroundUnit.getPosition());
                    if (distanceBetweenCenterAndUnit < closestUnitDistance)
                    {
                        closestUnitDistance = distanceBetweenCenterAndUnit;
                        friendlyUnitClosestToMissionCenter = friendlyGroundUnit;
                    }
                }
            }
        }
        return friendlyUnitClosestToMissionCenter;
    }
}
