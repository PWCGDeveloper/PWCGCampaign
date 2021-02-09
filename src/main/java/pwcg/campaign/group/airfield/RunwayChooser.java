package pwcg.campaign.group.airfield;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;

public class RunwayChooser
{
    private Mission mission;
    private Runway bestRunway = null;

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

            if (offset > 90.0) {
                offset = 180 - offset;
            }

            if (offset < bestOffset) {
                bestOffset = offset;
                bestRunway = runway;
            }
        }
    }

    public Runway getBestRunway()
    {
        return bestRunway;
    }
}
