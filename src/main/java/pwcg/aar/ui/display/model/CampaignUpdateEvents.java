package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;

public class CampaignUpdateEvents
{
    private List<PilotStatusEvent> pilotsLost = new ArrayList<>();
    private List<PlaneStatusEvent> planesLost = new ArrayList<>();
    private List<VictoryEvent> victories = new ArrayList<>();

    public List<PilotStatusEvent> getPilotsLost()
    {
        return pilotsLost;
    }

    public List<PlaneStatusEvent> getPlanesLost()
    {
        return planesLost;
    }

    public List<VictoryEvent> getVictories()
    {
        return victories;
    }


    public void addPilotLost(PilotStatusEvent pilotLostEvent)
    {
        pilotsLost.add(pilotLostEvent);
    }

    public void addVictory(VictoryEvent victory)
    {
        victories.add(victory);
    }

    public void addPlaneLost(PlaneStatusEvent planeLostEvent)
    {
        planesLost.add(planeLostEvent);        
    }

}
