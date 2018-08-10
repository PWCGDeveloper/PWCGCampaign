package pwcg.campaign.ww1.config;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.TakeoffFormation;
import pwcg.mission.ground.BattleSize;

public class RoFProductSpecificConfiguration implements IProductSpecificConfiguration
{
    private static final int START_IN_AIR = 1;
    private static final int START_ON_RUNWAY = 0;
    private static final int START_PARKED = 0;
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
    private static final int MAX_SEA_LANE_DISTANCE = 50000;
    private static final int INTERCEPT_RADIUS = 20000;
    private static final int CLOSE_TO_FRONT_DISTANCE = 30000;
    private static final int MAX_DISTANCE_FROM_PLAYER_BOX = 20000;

    @Override
    public boolean useWaypointGoal()
    {
        return true;
    }

    @Override
    public boolean usePlaneDir()
    {
        return false;
    }

    @Override
    public boolean useFlagDir()
    {
        return false;
    }

    @Override
    public boolean useCallSign()
    {
        return false;
    }

    @Override
    public boolean usePosition1()
    {
        return false;
    }

    @Override
    public TakeoffFormation getTakeoffFormation()
    {
        return TakeoffFormation.LINE_ABREAST;
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
    public int getInitialTargetRadiusFromGeneralTargetLocation(FlightTypes flightType)
    {
        int initialDistance = 8000;
        if (flightType == FlightTypes.OFFENSIVE)
        {
            initialDistance = 10000;
        }
        else if (flightType == FlightTypes.PATROL)
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
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            initialDistance = 10000;                    
        }
        else if (flightType == FlightTypes.BALLOON_BUST)
        {
            initialDistance = 20000;
        }
        else if (flightType == FlightTypes.ARTILLERY_SPOT)
        {
            initialDistance = 20000;                    
        }
        else if (flightType == FlightTypes.BOMB)
        {
            initialDistance = 20000;                    
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            initialDistance = 35000;                    
        }
        else if (flightType == FlightTypes.RECON)
        {
            initialDistance = 20000;                    
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
            initialDistance = 20000;                    
        }

        return initialDistance;
    }
    
    @Override
    public int getMaxTargetRadiusFromGeneralTargetLocation(FlightTypes flightType)
    {
        int maxDistance = 30000;
        if (flightType == FlightTypes.OFFENSIVE)
        {
            maxDistance = 30000;
        }
        else if (flightType == FlightTypes.PATROL)
        {
            maxDistance = 30000;
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            maxDistance = 30000;
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            maxDistance = 30000;
        }
        else if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            maxDistance = 50000;                    
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            maxDistance = 45000;                    
        }
        else if (flightType == FlightTypes.BALLOON_BUST)
        {
            maxDistance = 30000;
        }
        else if (flightType == FlightTypes.ARTILLERY_SPOT)
        {
            maxDistance = 45000;                    
        }
        else if (flightType == FlightTypes.BOMB)
        {
            maxDistance = 50000;                    
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            maxDistance = 45000;                    
        }
        else if (flightType == FlightTypes.RECON)
        {
            maxDistance = 45000;                    
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING)
        {
            maxDistance = 80000;                    
        }
        else if (flightType == FlightTypes.SEA_PATROL)
        {
            maxDistance = 80000;                    
        }
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            maxDistance = 140000;                    
        }

        return maxDistance;
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
}
