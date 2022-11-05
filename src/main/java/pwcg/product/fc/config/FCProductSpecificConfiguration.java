package pwcg.product.fc.config;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.initialposition.TakeoffFormation;
import pwcg.mission.ground.BattleSize;

public class FCProductSpecificConfiguration implements IProductSpecificConfiguration
{
    private static final int INNER_LOOP_DISTANCE = 6000;
    private static final int BALLOON_DEFENSE_LOOP_DISTANCE = 8000;
    private static final int CLIMB_DISTANCE = 5000;
    private static final int NEUTRAL_ZONE = 3000;
    private static final int MIN_CLIMB_WP_ALT = 1200;
    private static final int CROSS_DIAMETER = 15000;
    private static final int CREEPING_LINE_LENGTH = 10000;
    private static final int CREEPING_LINE_CROSS = 5000;
    private static final int CLOSE_TO_FRONT_DISTANCE = 30000;
    private static final int MAX_DISTANCE_FROM_PLAYER_BOX = 20000;
    private static final int MISSION_RADIUS_LARGE = 50000;
    private static final int MISSION_RADIUS_MEDIUM = 30000;
    private static final int MISSION_RADIUS_SMALL = 15000;
    private static final int MISSION_RADIUS_VERY_SMALL = 5000;
    private static final int AIR_START_MAX_DISTANCE_FROM_INGRESS = 20000;
    private static final int INITIAL_WAYPOINT_ALTITUDE = 800;
    private static final int GROUND_ATTACK_INGRESS_DISTANCE = 5000;
    private static final int AIRCRAFT_SPACING_HORIZONTAL = 100;
    private static final int AIRCRAFT_SPACING_VERTICAL = 70;
    private static final int TAKEOFF_SPACING = 30;
    private static final int ADDITIONAL_ALTITUDE_FOR_ESCORT = 300;
    private static final int INGRESS_AT_TARGET_MIN_DISTANCE = 2000;
    private static final int INGRESS_AT_TARGET_MAX_DISTANCE = 7000;
    private static final int ADDITIONAL_RENDEZVOUS_DISTANCE_FROM_FRONT = 6000;
    private static final int ATTACK_AREA_SELECT_TARGET_DISTANCE = 3000;
    private static final int ATTACK_AREA_BOMB_DROP_DISTANCE = 500;
    private static final int INGRESS_DISTANCE_FROM_FRONT = 6000;
    private static final int FORMATION_HORIZINTAL_SPACING = 100;
    private static final int FORMATION_VERTICAL_SPACING = 70;
    private static final int AIRFIELD_GO_AWAY_DISTANCE = 25000;
    private static final int VWP_SEPARATION_DISTANCE = 8000;
    private static final int VWP_PROXIMITY_TO_BOX_DISTANCE = 3000;
    private static final int VWP_PROXIMITY_TO_FRONT_DISTANCE = 8000;
    private static final int MAX_DEPTH_OF_PENETRATION_PATROL = 3000;
    private static final int MIN_DEPTH_OF_PENETRATION_OFFENSIVE = 3000;
    private static final int MAX_DEPTH_OF_PENETRATION_OFFENSIVE = 10000;
    private static final int MIN_DISTANCE_BETWEEN_PATROL_POINTS = 5000;

    @Override
    public boolean useWaypointGoal()
    {
        return false;
    }

    @Override
    public boolean usePlaneDir()
    {
        return true;
    }

    @Override
    public boolean useFlagDir()
    {
        return true;
    }

    @Override
    public boolean useCallSign()
    {
        return false;
    }

    @Override
    public boolean usePosition1()
    {
        return true;
    }

    @Override
    public TakeoffFormation getTakeoffFormation()
    {
        return TakeoffFormation.STAGGERED;
    }

    @Override
    public int getClimbDistance()
    {
        return CLIMB_DISTANCE;
    }

    @Override
    public int geNeutralZone()
    {
        return NEUTRAL_ZONE;
    }

    @Override
    public int getInterceptInnerLoopDistance()
    {
        return INNER_LOOP_DISTANCE;
    }

    @Override
    public int getMinClimbWPAlt()
    {
        return MIN_CLIMB_WP_ALT;
    }

    @Override
    public int getAdditionalInitialTargetRadius(FlightTypes flightType)
    {
        int initialDistance = 0;
        if (flightType == FlightTypes.SPY_EXTRACT)
        {
            initialDistance = 5000;
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            initialDistance = 5000;
        }
        else if (flightType == FlightTypes.CARGO_DROP)
        {
            initialDistance = 5000;
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            initialDistance = 5000;
        }
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            initialDistance = 30000;
        }

        return initialDistance;
    }

    @Override
    public int getAdditionalMaxTargetRadius(FlightTypes flightType)
    {
        int initialDistance = 10000;
        if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            initialDistance = 70000;
        }

        return initialDistance;
    }

    @Override
    public int getNumAssaultSegments(BattleSize battleSize)
    {
        if (battleSize == BattleSize.BATTLE_SIZE_ASSAULT)
        {
            return (1 + RandomNumberGenerator.getRandom(2));
        }
        else if (battleSize == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            return (1 + RandomNumberGenerator.getRandom(4));
        }
        else
        {
            return 1;
        }
    }

    @Override
    public int getInterceptCrossDiameterDistance()
    {
        return CROSS_DIAMETER;
    }

    @Override
    public int getInterceptCreepLegDistance()
    {
        return CREEPING_LINE_LENGTH;
    }

    @Override
    public int getInterceptCreepCrossDistance()
    {
        return CREEPING_LINE_CROSS;
    }

    @Override
    public int getCloseToFrontDistance()
    {
        return CLOSE_TO_FRONT_DISTANCE;
    }

    @Override
    public int getMaxDistanceForVirtualFlightFromPlayerBox()
    {
        return MAX_DISTANCE_FROM_PLAYER_BOX;
    }

    @Override
    public int getLargeMissionRadius()
    {
        return MISSION_RADIUS_LARGE;
    }

    @Override
    public int getMediumMissionRadius()
    {
        return MISSION_RADIUS_MEDIUM;
    }

    @Override
    public int getSmallMissionRadius()
    {
        return MISSION_RADIUS_SMALL;
    }

    @Override
    public int getVerySmallMissionRadius()
    {
        return MISSION_RADIUS_VERY_SMALL;
    }

    @Override
    public int getMaxDistanceForVirtualFlightAirStart()
    {
        return AIR_START_MAX_DISTANCE_FROM_INGRESS;
    }

    @Override
    public int getInitialWaypointAltitude()
    {
        return INITIAL_WAYPOINT_ALTITUDE;
    }

    @Override
    public int getGroundAttackIngressDistance()
    {
        return GROUND_ATTACK_INGRESS_DISTANCE;
    }

    @Override
    public int getAircraftSpacingHorizontal()
    {
        return AIRCRAFT_SPACING_HORIZONTAL;
    }

    @Override
    public int getAircraftSpacingVertical()
    {
        return AIRCRAFT_SPACING_VERTICAL;
    }

    @Override
    public int getTakeoffSpacing()
    {
        return TAKEOFF_SPACING;
    }

    @Override
    public int getAdditionalAltitudeForEscort()
    {
        return ADDITIONAL_ALTITUDE_FOR_ESCORT;
    }

    @Override
    public int getIngressAtTargetMinDistance()
    {
        return INGRESS_AT_TARGET_MIN_DISTANCE;
    }

    @Override
    public int getIngressAtTargetMaxDistance()
    {
        return INGRESS_AT_TARGET_MAX_DISTANCE;
    }

    @Override
    public int getRendezvousDistanceFromFront()
    {
        return getAttackAreaSelectTargetRadius() + ADDITIONAL_RENDEZVOUS_DISTANCE_FROM_FRONT;
    }

    @Override
    public int getBombApproachDistance()
    {
        return getBombFinalApproachDistance() + 5000;
    }

    @Override
    public int getBombFinalApproachDistance()
    {
        return getAttackAreaTriggerRadius() - 1000;
    }

    @Override
    public int getAttackAreaSelectTargetRadius()
    {
        return ATTACK_AREA_SELECT_TARGET_DISTANCE;
    }

    @Override
    public int getAttackAreaBombDropRadius()
    {
        return ATTACK_AREA_BOMB_DROP_DISTANCE;
    }

    @Override
    public int getAttackAreaTriggerRadius()
    {
        return getAttackAreaSelectTargetRadius() + 2000;
    }

    @Override
    public int getDefaultIngressDistanceFromFront()
    {
        return INGRESS_DISTANCE_FROM_FRONT;
    }
    
    @Override
    public int getFormationHorizontalSpacing()
    {
        return FORMATION_HORIZINTAL_SPACING;
    }

    @Override
    public int getFormationVerticalSpacing()
    {
        return FORMATION_VERTICAL_SPACING;
    }
    
    @Override
    public int getBalloonDefenseLoopDistance()
    {
        return BALLOON_DEFENSE_LOOP_DISTANCE;
    }

    @Override
    public int getAirfieldGoAwayDistance()
    {
        return AIRFIELD_GO_AWAY_DISTANCE;
    }    

    @Override
    public int getVwpSeparationDistance()
    {
        return VWP_SEPARATION_DISTANCE;
    }    

    @Override
    public int getVwpProximityToBoxDistance()
    {
        return VWP_PROXIMITY_TO_BOX_DISTANCE;
    }

    @Override
    public int getVwpProximityToFrontDistance()
    {
        return VWP_PROXIMITY_TO_FRONT_DISTANCE;
    }    

    @Override
    public int getMaxDepthOfPenetrationPatrol()
    {
        return MAX_DEPTH_OF_PENETRATION_PATROL;
    }

    @Override
    public int getMaxDepthOfPenetrationOffensive()
    {
        return MAX_DEPTH_OF_PENETRATION_OFFENSIVE;
    }

    @Override
    public int getMinDepthOfPenetrationOffensive()
    {
        return MIN_DEPTH_OF_PENETRATION_OFFENSIVE;
    }    

    @Override
    public double getMinimumDistanceBetweenPatrolPoints()
    {
        return MIN_DISTANCE_BETWEEN_PATROL_POINTS;
    }
}
