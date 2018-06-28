package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.MedalEvent;

public class AARMedalPanelData
{
    private List<MedalEvent> medalsAwarded = new ArrayList<>();

    public List<MedalEvent> getMedalsAwarded()
    {
        return medalsAwarded;
    }

    public void setMedalsAwarded(List<MedalEvent> medalsAwarded)
    {
        this.medalsAwarded = medalsAwarded;
    }
}
