package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.AceLeaveEvent;

public class AARAceLeavePanelData
{
    private List<AceLeaveEvent> acesOnLeaveDuringElapsedTime = new ArrayList<>();

    public List<AceLeaveEvent> getAcesOnLeaveDuringElapsedTime()
    {
        return acesOnLeaveDuringElapsedTime;
    }

    public void addAceOnLeaveDuringElapsedTime(AceLeaveEvent aceOnLeave)
    {
        this.acesOnLeaveDuringElapsedTime.add(aceOnLeave);
    }

}
