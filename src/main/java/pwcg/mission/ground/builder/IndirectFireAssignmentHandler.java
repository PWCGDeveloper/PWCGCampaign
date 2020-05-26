package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryBattery;

public class IndirectFireAssignmentHandler
{
    private Mission mission;
    private List<IGroundUnit> allAlliedGroundUnits = new ArrayList<>();
    private List<IGroundUnit> allAxisGroundUnits = new ArrayList<>();

    public IndirectFireAssignmentHandler (Mission mission)
    {
        this.mission = mission;
    }
    
    public void makeIndirectFireAssignments() throws PWCGException
    {
        loadGroundUnitsBySide();
        assignIndirectFireTargets(allAlliedGroundUnits, allAxisGroundUnits);
        assignIndirectFireTargets(allAxisGroundUnits, allAlliedGroundUnits);
    }

    private void loadGroundUnitsBySide() throws PWCGException
    {
        List<IGroundUnitCollection> allGroundUnits = new ArrayList<>();
        allGroundUnits.addAll(mission.getMissionFlightBuilder().getAllFlightLinkedGroundUnits());
        allGroundUnits.addAll(mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits());
        
        for (IGroundUnitCollection groundUnitCollection : allGroundUnits)
        {
            allAlliedGroundUnits.addAll(groundUnitCollection.getGroundUnitsForSide(Side.ALLIED));
            allAxisGroundUnits.addAll(groundUnitCollection.getGroundUnitsForSide(Side.AXIS));
        }
    }

    private void assignIndirectFireTargets(List<IGroundUnit> firingUnits, List<IGroundUnit> targetUnits) throws PWCGException
    {
        for (IGroundUnit firingUnit : firingUnits)
        {
            if (firingUnit instanceof GroundArtilleryBattery)
            {
                Coordinate targetPosition = findNearbyTarget(firingUnit.getPosition(), targetUnits);
                if (targetPosition != null)
                {
                    GroundArtilleryBattery artilleryBattery = (GroundArtilleryBattery)firingUnit;
                    artilleryBattery.setTargetPosition(targetPosition);
                }
            }
        }
    }

    private Coordinate findNearbyTarget(Coordinate firingUnitPosition, List<IGroundUnit> targetUnits) throws PWCGException
    {
        List<IGroundUnit> possibleTargets = new ArrayList<>();
        for (IGroundUnit targetUnit : targetUnits)
        {
            double distanceToTarget = MathUtils.calcDist(firingUnitPosition, targetUnit.getPosition());
            if (distanceToTarget < 20000.0)
            {
                possibleTargets.add(targetUnit);
            }
            
        }
        
        Coordinate targetPosition = null;
        if (possibleTargets.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(possibleTargets.size());
            targetPosition = possibleTargets.get(index).getPosition();
        }
        
        return targetPosition;
    }
}
