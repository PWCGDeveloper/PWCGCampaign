package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuCounter;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;

public interface IVirtualWaypoint
{

    void initialize(IFlight flight, VirtualWayPointCoordinate vwpCoordinate, Coalition coalition) throws PWCGException;

    void write(BufferedWriter writer) throws PWCGIOException;

    void addAdditionalTime(int additionalTime);

    McuTimer getEntryPoint();

    void linkToNextVirtualWaypoint(IVirtualWaypoint nextVWP);

    McuTimer getKillVwpTimer();

    void registerPlaneCounter(McuCounter counter);

    void setVirtualWaypointTriggerObject(int triggerObject);

    VwpSpawnContainer getVwpSpawnContainerForPlane(int planeIndex);

    List<McuSubtitle> getSubTitleList();

    boolean isUseSubtitles();

    SelfDeactivatingCheckZone getCheckZone();

    McuTimer getVwpTimedOutTimer();

    McuTimer getVwpTimer();

    McuTimer getMasterSpawnTimer();

    McuTimer getInitiateNextVirtualWaypointTimer();

    McuTimer getVwpTriggeredTimer();

    McuDeactivate getStopNextVwp();

}