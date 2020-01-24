package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.mcu.McuWaypoint;

public interface IFlight
{
    IFlightData getFlightData();
    Mission getMission();
    Campaign getCampaign();
    void write(BufferedWriter writer) throws PWCGException;
    void finalizeFlight() throws PWCGException;
    void createFlight() throws PWCGException;
    IMissionPointSet createFlightSpecificWaypoints(McuWaypoint ingressWaypoint) throws PWCGException;
}