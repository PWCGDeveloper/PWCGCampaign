package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.newspapers.Newspaper;

public class AARNewsPanelData
{
    private List<AceKilledEvent> acesKilledDuringElapsedTime = new ArrayList<>();
    private List<Newspaper> newspaperEventsDuringElapsedTime = new ArrayList<>();

    public List<AceKilledEvent> getAcesKilledDuringElapsedTime()
    {
        return acesKilledDuringElapsedTime;
    }

    public void setAcesKilledDuringElapsedTime(List<AceKilledEvent> acesKilledDuringElapsedTime)
    {
        this.acesKilledDuringElapsedTime = acesKilledDuringElapsedTime;
    }

    public List<Newspaper> getNewspaperEventsDuringElapsedTime()
    {
        return newspaperEventsDuringElapsedTime;
    }

    public void setNewspaperEventsDuringElapsedTime(List<Newspaper> newspaperEventsDuringElapsedTime)
    {
        this.newspaperEventsDuringElapsedTime = newspaperEventsDuringElapsedTime;
    }

    public void merge(AARNewsPanelData newsPanelData)
    {
        acesKilledDuringElapsedTime.addAll(acesKilledDuringElapsedTime);
        newspaperEventsDuringElapsedTime.addAll(newspaperEventsDuringElapsedTime);
    }
}
