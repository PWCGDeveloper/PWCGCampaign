package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import java.util.HashMap;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.event.IAType12;

public class LogTurrets
{
    private Map<String, LogTurret> logTurrets = new HashMap<>();

    public LogTurret createTurret(IAType12 atype12,LogAIEntity parent) throws PWCGException
    {
        LogTurret logTurret = new LogTurret(atype12.getSequenceNum());
        logTurret.initializeEntityFromEvent(atype12);
        logTurret.setParent(parent);
        logTurrets.put(atype12.getId(), logTurret);
        return logTurret;
    }
    
    public boolean hasTurret(String turretKey)
    {
        return logTurrets.containsKey(turretKey);
    }
}
