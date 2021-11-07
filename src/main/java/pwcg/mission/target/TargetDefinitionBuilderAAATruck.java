package pwcg.mission.target;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;

public class TargetDefinitionBuilderAAATruck implements ITargetDefinitionBuilder
{
    private FlightInformation flightInformation;

    public TargetDefinitionBuilderAAATruck(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }

    public TargetDefinition buildTargetDefinition() throws PWCGException
    {

        Coordinate truckCoordinate = flightInformation.getMission().getMissionAAATrucks().getPosition().copy();
        IGroundUnit targetGroundUnit = getBestTargetGroundUnit(flightInformation.getCountry().getSide().getOppositeSide(), truckCoordinate);

        TargetDefinition targetDefinition = new TargetDefinition(targetGroundUnit.getVehicleClass().getTargetType(), truckCoordinate, targetGroundUnit.getCountry(), targetGroundUnit.getVehicleClass().getName());
        return targetDefinition;
    }


    private IGroundUnit getBestTargetGroundUnit(Side enemyUnitSide, Coordinate truckPosition) throws PWCGException
    {
        double closestUnitDistance = 1000000000.0;
        IGroundUnit unitClosestToMissionCenter = null;
        for (GroundUnitCollection groundUnitCollection : flightInformation.getMission().getGroundUnitBuilder().getAssaults())
        {
            for (IGroundUnit friendlyGroundUnit : groundUnitCollection.getGroundUnitsForSide(enemyUnitSide))
            {
                double distanceBetweenCenterAndUnit = MathUtils.calcDist(truckPosition, friendlyGroundUnit.getPosition());
                if (distanceBetweenCenterAndUnit < closestUnitDistance)
                {
                    closestUnitDistance = distanceBetweenCenterAndUnit;
                    unitClosestToMissionCenter = friendlyGroundUnit;
                }
            }
        }
        return unitClosestToMissionCenter;
    }

}
