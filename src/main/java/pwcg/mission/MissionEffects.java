package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.effect.FirePotSeries;
import pwcg.mission.mcu.group.SmokeGroup;

public class MissionEffects
{
    private List<FirePotSeries> firePotsForPlayerRunways = new ArrayList<>();
    private List<SmokeGroup> smokeGroups = new ArrayList<>();

    public void createFirePots(Mission mission) throws PWCGException 
    {
        for (IFlight flight: mission.getFlights().getPlayerFlights())
        {
            FirePotBuilder firePotBuilder = new FirePotBuilder();
            FirePotSeries firePotSeries = firePotBuilder.createFirePots(flight);
            firePotsForPlayerRunways.add(firePotSeries);
        }
    }

    public void addSmokeGroup(SmokeGroup smokeGroup) throws PWCGException 
    {
        smokeGroups.add(smokeGroup);
    }

    public List<SmokeGroup> getSmokeGroups()
    {
        return smokeGroups;
    }

    public List<FirePotSeries> getFirePotsForPlayerRunways()
    {
        return firePotsForPlayerRunways;
    }
}
