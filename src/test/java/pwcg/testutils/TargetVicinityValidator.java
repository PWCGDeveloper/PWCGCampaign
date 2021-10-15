package pwcg.testutils;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;

public class TargetVicinityValidator
{

    public static void verifyProximityToTargetUnit(IFlight flight) throws PWCGException
    {
        System.out.println("\n\nVerify Proximity To Ground Unit");

        System.out.println("Target Information: Name:  " + flight.getTargetDefinition().getTargetName());
        System.out.println("Target Information: Category:  " + flight.getTargetDefinition().getTargetCategory());
        System.out.println("Target Information: Type:  " + flight.getTargetDefinition().getTargetType());
        System.out.println("Target Information: Type:  " + flight.getTargetDefinition().getPosition());

        MissionPointAttackSet attackMissionPoint = (MissionPointAttackSet)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ATTACK);
        Coordinate attackPosition = attackMissionPoint.getAttackSequence().getAttackAreaMcu().getPosition();
        System.out.println("Attack Position at " + attackPosition);

        boolean groundAttackCloseToTarget = false;
        for (GroundUnitCollection groundUnitCollection : flight.getMission().getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                double distanceFromGroundUnit = MathUtils.calcDist(attackPosition, groundUnit.getPosition());
                if (distanceFromGroundUnit < 5000)
                {
                    groundAttackCloseToTarget = true;
                }
            }
        }
        assert (groundAttackCloseToTarget == true);
    }
    

    public static void verifyProximityToTargetAirfield(IFlight flight) throws PWCGException
    {
        System.out.println("\n\nVerify Proximity To Airfield");

        System.out.println("Target Information: Name:  " + flight.getTargetDefinition().getTargetName());
        System.out.println("Target Information: Category:  " + flight.getTargetDefinition().getTargetCategory());
        System.out.println("Target Information: Type:  " + flight.getTargetDefinition().getTargetType());
        System.out.println("Target Information: Type:  " + flight.getTargetDefinition().getPosition());

        MissionPointAttackSet attackMissionPoint = (MissionPointAttackSet)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ATTACK);
        Coordinate attackPosition = attackMissionPoint.getAttackSequence().getAttackAreaMcu().getPosition();
        System.out.println("Attack Position at " + attackPosition);

        Airfield airfield = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(flight.getTargetDefinition().getTargetName());
        double distanceToAirfield = MathUtils.calcDist(flight.getTargetDefinition().getPosition(), airfield.getPosition());
        System.out.println("Distance to " + airfield.getName() + " " + airfield.determineCountry().getCountryName() +  " distance is " + distanceToAirfield);

        assert (distanceToAirfield < 5000);
    }


}
