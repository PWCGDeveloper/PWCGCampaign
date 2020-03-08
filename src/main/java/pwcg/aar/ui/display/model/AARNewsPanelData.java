package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.aar.ui.events.model.NewspaperEvent;

public class AARNewsPanelData
{
    private List<AceKilledEvent> acesKilledDuringElapsedTime = new ArrayList<>();
    private List<NewspaperEvent> newspaperEventsDuringElapsedTime = new ArrayList<>();

    public List<AceKilledEvent> getAcesKilledDuringElapsedTime()
    {
        return acesKilledDuringElapsedTime;
    }

    public void setAcesKilledDuringElapsedTime(List<AceKilledEvent> acesKilledDuringElapsedTime)
    {
        this.acesKilledDuringElapsedTime = acesKilledDuringElapsedTime;
    }

    public List<NewspaperEvent> getNewspaperEventsDuringElapsedTime()
    {
        return newspaperEventsDuringElapsedTime;
    }

    public void setNewspaperEventsDuringElapsedTime(List<NewspaperEvent> newspaperEventsDuringElapsedTime)
    {
        this.newspaperEventsDuringElapsedTime = newspaperEventsDuringElapsedTime;
    }

    public void merge(AARNewsPanelData newsPanelData)
    {
        acesKilledDuringElapsedTime.addAll(acesKilledDuringElapsedTime);
        newspaperEventsDuringElapsedTime.addAll(newspaperEventsDuringElapsedTime);
    }
}
