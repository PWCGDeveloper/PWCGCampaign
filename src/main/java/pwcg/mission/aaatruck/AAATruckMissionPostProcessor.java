package pwcg.mission.aaatruck;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;

public class AAATruckMissionPostProcessor
{
    private static final int KEEP_RADIUS = 20000;
    private Mission mission;

    public AAATruckMissionPostProcessor(Mission mission)
    {
        this.mission = mission;
    }

    public void convertToPlayerVehicleMission(Side truckSide) throws PWCGException
    {
        removePlayerFlight();
        removeStructuresOutsideOfRange();
        removeUnitsOutsideOfRange();
    }

    private void removePlayerFlight() throws PWCGException
    {
        mission.getMissionFlights().removePlayerFlights();
        mission.getMissionWaypointIconBuilder().removeWaypointIcons();
    }

    private void removeStructuresOutsideOfRange() throws PWCGException
    {
        mission.getMissionBlocks().removeExtraStructures(mission.getMissionAAATrucks().getPosition(), KEEP_RADIUS);
    }

    private void removeUnitsOutsideOfRange() throws PWCGException
    {
        mission.getMissionGroundUnitBuilder().removeExtraUnits(mission.getMissionAAATrucks().getPosition(), KEEP_RADIUS);
    }
}
