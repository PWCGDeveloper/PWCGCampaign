package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.McuTimer;

public interface IPlaneRemover
{
    public void initialize(Flight flight, PlaneMCU planeToRemove, PlaneMCU playerPlane) throws PWCGException;
    public void write(BufferedWriter writer) throws PWCGIOException;
    public McuTimer getEntryPoint();
}
