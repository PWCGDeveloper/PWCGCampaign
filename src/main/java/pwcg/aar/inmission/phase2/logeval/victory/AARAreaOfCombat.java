package pwcg.aar.inmission.phase2.logeval.victory;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class AARAreaOfCombat
{
    private List<Coordinate> crashSites = new ArrayList<>();
    private List<LogVictory> victories = new ArrayList<>();

    public AARAreaOfCombat(List<LogVictory> victories)
    {
        this.victories = victories;
    }
    
    public void noteLocationOfCrashes() throws PWCGException 
    {
        for (LogVictory missionResultVictory : victories)
        {
            if (missionResultVictory.getLocation() != null)
            {
                crashSites.add(missionResultVictory.getLocation());
            }
        }
    }

    public boolean isNearAreaOfCombat(Coordinate victimCoordinate)
    {
        for (Coordinate combatCoordinate : crashSites)
        {
            double distance = MathUtils.calcDist(victimCoordinate, combatCoordinate);
            if (distance < 5000.0)
            {
                return true;
            }
        }
        
        return false;
    }
}
