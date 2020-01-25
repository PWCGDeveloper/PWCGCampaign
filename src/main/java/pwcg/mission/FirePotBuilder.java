package pwcg.mission;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.effect.FirePotSeries;

public class FirePotBuilder
{
    public FirePotSeries createFirePots(IFlight flight) throws PWCGException 
    {
        IAirfield airfield = flight.getFlightInformation().getSquadron().determineCurrentAirfieldAnyMap(flight.getCampaign().getDate());

        FirePotSeries firePotSeries = new FirePotSeries();

        PWCGLocation takeoffLocation = airfield.getTakeoffLocation();
        PWCGLocation landingLocation = airfield.getLandingLocation();
        double airfieldOrientation = MathUtils.calcAngle(takeoffLocation.getPosition(), landingLocation.getPosition());
        
        Coordinate planePosition = takeoffLocation.getPosition().copy();
        Double angleOffsetFirePots = MathUtils.adjustAngle(airfieldOrientation, -90);
        Coordinate positionLeftOfPlane = MathUtils.calcNextCoord(planePosition, angleOffsetFirePots, 60.0);
        
        double airfieldLength = MathUtils.calcDist(takeoffLocation.getPosition(), landingLocation.getPosition());
        double distanceBetween = airfieldLength / FirePotSeries.NUM_FIRE_POT_PAIRS;

        firePotSeries.createSeries(positionLeftOfPlane, airfieldOrientation, distanceBetween);
        
        return firePotSeries;
    }
}
