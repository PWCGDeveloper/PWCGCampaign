package pwcg.campaign.group.airfield;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;

public class RunwayChooser
{
    private Mission mission;
    private Runway bestRunway = null;
    private boolean invertRunway = false;

    public RunwayChooser(Mission mission)
    {
        this.mission = mission;
    }
    
    public void selectRunway(List<Runway> runways) throws PWCGException
    {
        // Note: wind direction is as specified in mission file, i.e. heading wind blows to
        // Adjust by 180 degrees to give standard direction
        double windDirection =  MathUtils.adjustAngle(mission.getWeather().getWindDirection(), 180);
        double bestOffset = 1000.0;

        for (Runway runway : runways) {
            double offset = MathUtils.calcNumberOfDegrees(windDirection, runway.getHeading());
            boolean invert = false;

            if (offset > 90.0) {
                offset = 180 - offset;
                invert = true;
            }

            if (offset < bestOffset) {
                bestOffset = offset;
                bestRunway = runway;
                invertRunway = invert;
            }
        }
    }

    public Runway getBestRunway()
    {
        return bestRunway;
    }

    public void setBestRunway(Runway bestRunway)
    {
        this.bestRunway = bestRunway;
    }

    public boolean isInvertRunway()
    {
        return invertRunway;
    }

    public void setInvertRunway(boolean invertRunway)
    {
        this.invertRunway = invertRunway;
    }
}
