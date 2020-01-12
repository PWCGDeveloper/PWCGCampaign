package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuTimer;

public interface IPlaneRemover
{
    public void initialize(IFlight flight, PlaneMcu planeToRemove, PlaneMcu playerPlane) throws PWCGException;
    public void write(BufferedWriter writer) throws PWCGIOException;
    public McuTimer getEntryPoint();
}
