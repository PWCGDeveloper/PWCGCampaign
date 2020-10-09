package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.Mission;
import pwcg.mission.mcu.Coalition;

public interface ICheckZone {

    void write(BufferedWriter writer) throws PWCGIOException;

    int getActivateEntryPoint();

    int getDeactivateEntryPoint();

    void setCheckZoneTarget(int targetMcuIndex);

    void setCheckZoneTriggerObject(int objectMcuIndex);

    void setCheckZoneTriggerCoalition(Coalition coalition);

    void setCheckZoneTriggerCoalitions(List<Coalition> coalitions);

    void validate() throws PWCGException;

    void validateTarget(int entryPoint) throws PWCGException;

    void triggerCheckZone(Mission mission) throws PWCGException;
}