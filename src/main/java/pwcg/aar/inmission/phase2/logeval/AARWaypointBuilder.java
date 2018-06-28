package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogWaypoint;

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
            LogWaypoint wpEvent = new LogWaypoint();
            wpEvent.setLocation(atype17.getLocation());
            wpEvent.setSequenceNum(atype17.getSequenceNum());
            waypointEventList.add(wpEvent);
        }
        
        return waypointEventList;
    }

}
