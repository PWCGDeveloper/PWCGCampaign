package pwcg.mission;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.effect.FirePotSeries;

public class FirePotBuilder
{
    public FirePotSeries createFirePots(IAirfield airfield) throws PWCGException 
    {
        FirePotSeries firePotSeries = new FirePotSeries();

        double airfieldOrientation = airfield.getPlaneOrientation();
        Coordinate planePosition = airfield.getPlanePosition().getPosition().copy();

        Double angleOffsetFirePots = MathUtils.adjustAngle(airfieldOrientation, -90);
        Coordinate positionLeftOfPlane = MathUtils.calcNextCoord(planePosition, angleOffsetFirePots, 30.0);

        firePotSeries.createSeries(positionLeftOfPlane, airfieldOrientation, 20.0, 10);
        
        return firePotSeries;
    }
}
