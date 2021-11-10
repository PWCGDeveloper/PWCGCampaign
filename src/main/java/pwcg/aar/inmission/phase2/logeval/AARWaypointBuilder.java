package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogWaypoint;
import pwcg.core.logfiles.event.IAType17;

public class AARWaypointBuilder
{
    private List<LogWaypoint> waypointEventList = new ArrayList<LogWaypoint>();

    private AARLogEventData logEventData;
    
    public AARWaypointBuilder(AARLogEventData logEventData)
    {
        this.logEventData = logEventData;
    }
    
    public List<LogWaypoint> buildWaypointEvents()
    {
        for (IAType17 atype17 : logEventData.getWaypointEvents())
        {
            LogWaypoint wpEvent = new LogWaypoint(atype17.getSequenceNum());
            wpEvent.setLocation(atype17.getLocation());
            waypointEventList.add(wpEvent);
        }
        
        return waypointEventList;
    }

}
