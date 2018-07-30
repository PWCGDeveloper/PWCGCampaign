package pwcg.campaign.ww2.config;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.TakeoffFormation;
import pwcg.mission.ground.BattleSize;

public class BoSProductSpecificConfiguration implements IProductSpecificConfiguration
{
    private static int START_IN_AIR = 0;
    private static int START_ON_RUNWAY = 1;
    private static int START_PARKED = 2;
    private static int INTERCEPT_LOOP_ANGLE = 60;
    private static int INNER_LOOP_DISTANCE = 15000;
    private static int CLIMB_DISTANCE = 10000;
    private static int NEUTRAL_ZONE = 5000;
    private static int FRONT_LINE_MISSION_RADIUS = 40000;
    public static int BOMB_APPROACH_DISTANCE = 18000;
    public static int BOMB_APPROACH_FINAL_DISTANCE = 10000;
    public static int MIN_CLIMB_WP_ALT = 2000;
    public static int CROSS_DIAMETER = 25000;
    public static int CREEPING_LINE_LENGTH = 15000;
    public static int CREEPING_LINE_CROSS = 8000;
    public static int MAX_SEA_LANE_DISTANCE = 100000;
    public static int INTERCEPT_RADIUS = 60000;
    public static int CLOSE_TO_FRONT_DISTANCE = 60000;

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
            initialDistance = 20000;
        }
        else if (flightType == FlightTypes.LONE_WOLF)
        {
            initialDistance = 20000;
        }
        else if (flightType == FlightTypes.OFFENSIVE)
        {
            initialDistance = 15000;
        }
        else if (flightType == FlightTypes.INTERCEPT)
        {
            initialDistance = 30000;
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            initialDistance = 20000;
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            initialDistance = 15000;
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            initialDistance = 15000;
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            initialDistance = 120000;
        }
        else if (flightType == FlightTypes.BOMB)
        {
            initialDistance = 15000;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            initialDistance = 15000;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            initialDistance = 15000;                    
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
            initialDistance = 50000;
        }
        else if (flightType == FlightTypes.CARGO_DROP)
        {
            initialDistance = 50000;
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            initialDistance = 50000;
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
        int initialDistance = 130000;
        if (flightType == FlightTypes.PATROL)
        {
            initialDistance = 80000;
        }
        else if (flightType == FlightTypes.LONE_WOLF)
        {
            initialDistance = 80000;
        }
        else if (flightType == FlightTypes.LOW_ALT_PATROL)
        {
            initialDistance = 80000;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            initialDistance = 90000;                    
        }
        else if (flightType == FlightTypes.LOW_ALT_BOMB)
        {
            initialDistance = 110000;                    
        }
        else if (flightType == FlightTypes.OFFENSIVE)
        {
            initialDistance = 150000;
        }
        else if (flightType == FlightTypes.INTERCEPT)
        {
            initialDistance = 110000;
        }
        else if (flightType == FlightTypes.CONTACT_PATROL)
        {
            initialDistance = 160000;
        }
        else if (flightType == FlightTypes.GROUND_ATTACK)
        {
            initialDistance = 160000;
        }
        else if (flightType == FlightTypes.DIVE_BOMB)
        {
            initialDistance = 160000;
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            initialDistance = 160000;
        }
        else if (flightType == FlightTypes.BOMB)
        {
            initialDistance = 180000;                    
        }
        else if (flightType == FlightTypes.RECON)
        {
            initialDistance = 180000;                    
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            initialDistance = 180000;
        }
        else if (flightType == FlightTypes.TRANSPORT)
        {
            initialDistance = 180000;
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING)
        {
            initialDistance = 180000;                    
        }
        else if (flightType == FlightTypes.SEA_PATROL)
        {
            initialDistance = 180000;                    
        }
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            initialDistance = 180000;                    
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
}
