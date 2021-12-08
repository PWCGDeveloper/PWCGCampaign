package pwcg.aar.inmission.phase2.logeval;

import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.core.logfiles.AARLogEventData;
import pwcg.core.logfiles.event.IAType3;
import pwcg.core.logfiles.event.IAType6;

public class AARVehiclePlaneLanded 
{
    private AARLogEventData logEventData;
    
    public AARVehiclePlaneLanded (AARLogEventData logEventData)
    {
        this.logEventData = logEventData;
    }

    public void buildLandedLocations(Map <String, LogPlane> planeAiEntities)
    {
        notePlaneCrashedLocation(planeAiEntities);
        notePlaneLandedLocation(planeAiEntities);
    }

    private void notePlaneCrashedLocation(Map <String, LogPlane> planeAiEntities)
    {
        for (IAType3 atype3 : logEventData.getDestroyedEvents())
        {
            for (LogPlane planeEntity: planeAiEntities.values())
            {
                if (planeEntity.getId().equals(atype3.getVictim()))
                {
                    planeEntity.setLandAt(atype3.getLocation());
                }
            }
        }
    }

    private void notePlaneLandedLocation(Map <String, LogPlane> planeAiEntities)
    {
        for (IAType6 atype6 : logEventData.getLandingEvents())
        {
            for (LogPlane planeEntity: planeAiEntities.values())
            {
                if (planeEntity.getId().equals(atype6.getPid()))
                {
                    planeEntity.setLandAt(atype6.getLocation());
                }
            }
        }
    }

}

