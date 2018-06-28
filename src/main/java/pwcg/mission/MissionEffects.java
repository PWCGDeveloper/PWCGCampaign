package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.effect.FirePotSeries;
import pwcg.mission.mcu.group.SmokeGroup;

public class MissionEffects
{
    private FirePotSeries firePotSeries = new FirePotSeries();
    private List<SmokeGroup> smokeGroups = new ArrayList<>();

    public void createFirePots(IAirfield playerAirfield) throws PWCGException 
    {
        FirePotBuilder firePotBuilder = new FirePotBuilder();
        firePotSeries = firePotBuilder.createFirePots(playerAirfield);
    }

    public void addSmokeGroup(SmokeGroup smokeGroup) throws PWCGException 
    {
        smokeGroups.add(smokeGroup);
    }

    public FirePotSeries getFirePotSeries()
    {
        return firePotSeries;
    }

    public void setFirePotSeries(FirePotSeries firePotSeries)
    {
        this.firePotSeries = firePotSeries;
    }

    public List<SmokeGroup> getSmokeGroups()
    {
        return smokeGroups;
    }

    public void setSmokeGroups(List<SmokeGroup> smokeGroups)
    {
        this.smokeGroups = smokeGroups;
    }
}
