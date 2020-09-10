package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuCounter;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;

public interface IVirtualWaypoint
{

    void initialize(IFlight flight, VirtualWayPointCoordinate vwpCoordinate, Coalition coalition) throws PWCGException;

    void write(BufferedWriter writer) throws PWCGException;

    void addAdditionalTime(int additionalTime);

    McuTimer getEntryPoint();

    void linkToNextVirtualWaypoint(IVirtualWaypoint nextVWP);

    McuTimer getKillVwpTimer();

    void registerPlaneCounter(McuCounter counter);

    void setVwpTriggerObject(int triggerObject);

    SelfDeactivatingCheckZone getCheckZone();

    McuTimer getVwpTimedOutTimer();

    McuTimer getVwpTimer();

    McuTimer getMasterVwpTimer();

    McuTimer getInitiateNextVwpTimer();

    McuTimer getVwpTriggeredTimer();

    McuDeactivate getStopNextVwp();

    ActivateContainer getActivateContainer();

}