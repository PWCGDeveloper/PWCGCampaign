package pwcg.product.fc.config;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.TakeoffFormation;
import pwcg.mission.ground.BattleSize;

public class FCProductSpecificConfiguration implements IProductSpecificConfiguration
{
    private static final int INTERCEPT_LOOP_ANGLE = 60;
    private static final int INNER_LOOP_DISTANCE = 6000;
    private static final int CLIMB_DISTANCE = 5000;
    private static final int NEUTRAL_ZONE = 3000;
    private static final int FRONT_LINE_MISSION_RADIUS = 15000;
    private static final int BOMB_APPROACH_DISTANCE = 10000;
    private static final int BOMB_APPROACH_FINAL_DISTANCE = 6000;
    private static final int MIN_CLIMB_WP_ALT = 1200;
    private static final int CROSS_DIAMETER = 15000;
    private static final int CREEPING_LINE_LENGTH = 10000;
    private static final int CREEPING_LINE_CROSS = 5000;
    private static final int INTERCEPT_RADIUS = 20000;
    private static final int CLOSE_TO_FRONT_DISTANCE = 30000;
    private static final int MAX_DISTANCE_FROM_PLAYER_BOX = 20000;
    private static final int MISSION_RADIUS_LARGE = 50000;
    private static final int MISSION_RADIUS_MEDIUM = 30000;
    private static final int MISSION_RADIUS_SMALL = 15000;
    private static final int MISSION_RADIUS_VERY_SMALL = 5000;
    private static final int AIR_START_MAX_DISTANCE_FROM_INGRESS = 20000;
    private static final int INITIAL_WAYPOINT_ALTITUDE = 800;
    private static final int GROUND_ATTACK_INGRESS_DISTANCE = 5000;
    private static final int AIRCRAFT_SPACING_HORIZONTAL = 150;
    private static final int AIRCRAFT_SPACING_VERTICAL = 100;
    private static final int TAKEOFF_SPACING = 30;
    private static final int RENDEZVOUS_DISTANCE_FROM_FRONT = 8000;
    private static final int ADDITIONAL_ALTITUDE_FOR_ESCORT = 300;

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
    public int getInitialFrontLineRadius()
    {
        return FRONT_LINE_MISSION_RADIUS;
    }

    @Override
    public int geNeutralZone()
    {
        return NEUTRAL_ZONE;
    }

    @Override
    public int getBombApproachDistance()
    {
        return BOMB_APPROACH_DISTANCE;
    }

    @Override
    public int getBombFinalApproachDistance()
    {
        return BOMB_APPROACH_FINAL_DISTANCE;
    }

    @Override
    public int getInterceptInnerLoopDistance()
    {
        return INNER_LOOP_DISTANCE;
    }

    @Override
    public int getInterceptLoopAngle()
    {
        return INTERCEPT_LOOP_ANGLE;
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
        if (flightType == FlightTypes.PATROL)
        {
            initialDistance = 0;
        }
        else if (flightType == FlightTypes.LONE_WOLF)
        {
            initialDistance = 0;
        }
        else if (flightType == FlightTypes.OFFENSIVE)
        {
            initialDistance = 0;
        }
        else if (flightType == FlightTypes.INTERCEPT)
        {
            initialDistance = 0;
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            initialDistance = 0;
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            initialDistance = 0;
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            initialDistance = 0;
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            initialDistance = 5000;
        }
        else if (flightType == FlightTypes.BOMB)
        {
            initialDistance = 0;                    
        }
        else if (flightType == FlightTypes.ESCORT)
        {
            initialDistance = 0;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            initialDistance = 0;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            initialDistance = 0;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            initialDistance = 0;                    
        }
        else if (flightType == FlightTypes.RECON)
        {
            initialDistance = 0;                    
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
        else if (flightType == FlightTypes.ANTI_SHIPPING_BOMB || flightType == FlightTypes.ANTI_SHIPPING_ATTACK || flightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB)
        {
            initialDistance = 50000;                    
        }
        else if (flightType == FlightTypes.SEA_PATROL)
        {
            initialDistance = 50000;                    
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
        if (flightType == FlightTypes.PATROL)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.LONE_WOLF)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            initialDistance = 10000;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            initialDistance = 10000;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            initialDistance = 10000;                    
        }
        else if (flightType == FlightTypes.OFFENSIVE)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.INTERCEPT)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.BOMB)
        {
            initialDistance = 10000;                    
        }
        else if (flightType == FlightTypes.RECON)
        {
            initialDistance = 10000;                    
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING_BOMB || flightType == FlightTypes.ANTI_SHIPPING_ATTACK || flightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB)
        {
            initialDistance = 100000;                    
        }
        else if (flightType == FlightTypes.SEA_PATROL)
        {
            initialDistance = 100000;                    
        }
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            initialDistance = 100000;                    
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
    public int getInterceptRadius()
    {
        return INTERCEPT_RADIUS;
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
    public int getRendezvousDistanceFromFront()
    {
        return RENDEZVOUS_DISTANCE_FROM_FRONT;
    }

    @Override
    public int getAdditionalAltitudeForEscort()
    {
        return ADDITIONAL_ALTITUDE_FOR_ESCORT;
    }
}
