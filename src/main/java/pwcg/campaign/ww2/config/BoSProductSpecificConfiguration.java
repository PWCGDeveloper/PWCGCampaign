package pwcg.campaign.ww2.config;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.TakeoffFormation;
import pwcg.mission.ground.BattleSize;

public class BoSProductSpecificConfiguration implements IProductSpecificConfiguration
{
    private static final int START_IN_AIR = 0;
    private static final int START_ON_RUNWAY = 1;
    private static final int START_PARKED = 2;
    private static final int INTERCEPT_LOOP_ANGLE = 60;
    private static final int INNER_LOOP_DISTANCE = 15000;
    private static final int CLIMB_DISTANCE = 10000;
    private static final int NEUTRAL_ZONE = 5000;
    private static final int FRONT_LINE_MISSION_RADIUS = 40000;
    private static final int BOMB_APPROACH_DISTANCE = 18000;
    private static final int BOMB_APPROACH_FINAL_DISTANCE = 10000;
    private static final int MIN_CLIMB_WP_ALT = 2000;
    private static final int CROSS_DIAMETER = 25000;
    private static final int CREEPING_LINE_LENGTH = 15000;
    private static final int CREEPING_LINE_CROSS = 8000;
    private static final int MAX_SEA_LANE_DISTANCE = 100000;
    private static final int INTERCEPT_RADIUS = 60000;
    private static final int CLOSE_TO_FRONT_DISTANCE = 60000;
    private static final int MAX_DISTANCE_FROM_PLAYER_BOX = 60000;
    private static final int MISSION_RADIUS_LARGE = 100000;
    private static final int MISSION_RADIUS_MEDIUM = 50000;
    private static final int MISSION_RADIUS_SMALL = 30000;
    private static final int MISSION_RADIUS_VERY_SMALL = 10000;
    private static final int AIR_START_MAX_DISTANCE_FROM_INGRESS = 60000;
    private static final int INITIAL_WAYPOINT_ALTITUDE = 1500;
    private static final int GROUND_ATTACK_INGRESS_DISTANCE = 10000;
    private static final int AIRCRAFT_SPACING_HORIZONTAL = 300;
    private static final int AIRCRAFT_SPACING_VERTICAL = 200;
    private static final int TAKEOFF_SPACING = 40;
    private static final int RENDEZVOUS_DISTANCE_FROM_FRONT = 20000;
    private static final int ADDITIONAL_ALTITUDE_FOR_ESCORT = 800;

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
        return true;
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
    public int startInAir()
    {
        return START_IN_AIR;
    }

    @Override
    public int startOnRunway()
    {
        return START_ON_RUNWAY;
    }

    @Override
    public int startParked()
    {
        return START_PARKED;
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
    public int getClimbDistance()
    {
        return CLIMB_DISTANCE;
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
    public int getMinClimbWPAlt()
    {
        return MIN_CLIMB_WP_ALT;
    }
    
    @Override
    public int getInitialTargetRadiusFromGeneralTargetLocation(FlightTypes flightType)
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
            initialDistance = 15000;
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.BOMB)
        {
            initialDistance = 15000;                    
        }
        else if (flightType == FlightTypes.ESCORT)
        {
            initialDistance = 15000;                    
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
            initialDistance = 15000;                    
        }
        else if (flightType == FlightTypes.RECON)
        {
            initialDistance = 15000;                    
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            initialDistance = 30000;
        }
        else if (flightType == FlightTypes.CARGO_DROP)
        {
            initialDistance = 30000;
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            initialDistance = 30000;
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING)
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
    public int getMaxTargetRadiusFromGeneralTargetLocation(FlightTypes flightType)
    {
        int initialDistance = 60000;
        if (flightType == FlightTypes.PATROL)
        {
            initialDistance = 50000;
        }
        else if (flightType == FlightTypes.LONE_WOLF)
        {
            initialDistance = 50000;
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            initialDistance = 50000;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            initialDistance = 50000;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            initialDistance = 70000;                    
        }
        else if (flightType == FlightTypes.OFFENSIVE)
        {
            initialDistance = 50000;
        }
        else if (flightType == FlightTypes.INTERCEPT)
        {
            initialDistance = 50000;
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            initialDistance = 70000;
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            initialDistance = 50000;
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            initialDistance = 70000;
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            initialDistance = 70000;
        }
        else if (flightType == FlightTypes.BOMB)
        {
            initialDistance = 70000;                    
        }
        else if (flightType == FlightTypes.RECON)
        {
            initialDistance = 100000;                    
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            initialDistance = 100000;
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            initialDistance = 100000;
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING)
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
    public int getNumAssaultSegments(BattleSize battleSize)
    {
        if (battleSize == BattleSize.BATTLE_SIZE_ASSAULT)
        {
            return (1 + RandomNumberGenerator.getRandom(4));
        }
        else if (battleSize == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            return (1 + RandomNumberGenerator.getRandom(8));
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
	public int getMaxSeaLaneDistance()
	{
		return MAX_SEA_LANE_DISTANCE;
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
