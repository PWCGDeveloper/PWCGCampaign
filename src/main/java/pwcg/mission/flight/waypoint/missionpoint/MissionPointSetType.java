package pwcg.mission.flight.waypoint.missionpoint;

public enum MissionPointSetType
{
    MISSION_POINT_SET_FLIGHT_BEGIN(1),
    MISSION_POINT_SET_MISSION_PATROL(3),
    MISSION_POINT_SET_MISSION_ATTACK(4),
    MISSION_POINT_SET_MISSION_COVER(5),
    MISSION_POINT_SET_MISSION_ESCORT(6),
    MISSION_POINT_SET_FLIGHT_END(100);

    private int missionPointSetTypeOrder;

    private MissionPointSetType(int missionPointSetTypeOrder)
    {
        this.missionPointSetTypeOrder = missionPointSetTypeOrder;
    }

    public int getMissionPointSetTypeOrder()
    {
        return missionPointSetTypeOrder;
    }
}
