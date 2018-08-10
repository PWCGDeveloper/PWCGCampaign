package pwcg.campaign.api;

import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.TakeoffFormation;
import pwcg.mission.ground.BattleSize;

public interface IProductSpecificConfiguration
{
    boolean useWaypointGoal();
    boolean usePlaneDir();
    boolean useFlagDir();
    boolean useCallSign();
    boolean usePosition1();
    TakeoffFormation getTakeoffFormation();
    int startInAir();
    int startOnRunway();
    int startParked();
    int getInitialTargetRadiusFromGeneralTargetLocation(FlightTypes flightType);
    int getMaxTargetRadiusFromGeneralTargetLocation(FlightTypes flightType);
    int getInitialFrontLineRadius();
    int geNeutralZone();
    int getInterceptRadius();
    int getInterceptCrossDiameterDistance();
    int getInterceptCreepLegDistance();
    int getInterceptCreepCrossDistance();
    int getInterceptInnerLoopDistance();
    int getInterceptLoopAngle();
    int getClimbDistance();
    int getBombApproachDistance();
    int getBombFinalApproachDistance();
    int getMinClimbWPAlt();
    int getNumAssaultSegments(BattleSize battleSize);
    int getMaxSeaLaneDistance();
    int getCloseToFrontDistance();
    int getMaxDistanceForVirtualFlightFromPlayerBox();
}
