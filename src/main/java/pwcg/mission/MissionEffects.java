package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.group.SmokeGroup;

public class MissionEffects
{
    private List<SmokeGroup> smokeGroups = new ArrayList<>();

    public void addSmokeGroup(SmokeGroup smokeGroup) throws PWCGException 
    {
        smokeGroups.add(smokeGroup);
    }

    public List<SmokeGroup> getSmokeGroups()
    {
        return smokeGroups;
    }
}
