package pwcg.mission;

import pwcg.campaign.group.airfield.Airfield;
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
        Airfield airfield = flight.getSquadron().determineCurrentAirfieldAnyMap(flight.getCampaign().getDate());

        FirePotSeries firePotSeries = new FirePotSeries();

        PWCGLocation takeoffLocation = airfield.getTakeoffLocation(flight.getMission());
        PWCGLocation landingLocation = airfield.getLandingLocation(flight.getMission());
        double airfieldOrientation = MathUtils.calcAngle(takeoffLocation.getPosition(), landingLocation.getPosition());
        
        Coordinate planePosition = takeoffLocation.getPosition().copy();
        Double angleOffsetFirePots = MathUtils.adjustAngle(airfieldOrientation, -90);
        Coordinate positionLeftOfPlane = MathUtils.calcNextCoord(flight.getCampaign().getCampaignMap(), planePosition, angleOffsetFirePots, 60.0);
        
        double airfieldLength = MathUtils.calcDist(takeoffLocation.getPosition(), landingLocation.getPosition());
        double distanceBetween = airfieldLength / FirePotSeries.NUM_FIRE_POT_PAIRS;

        firePotSeries.createSeries(flight.getCampaignMap(), positionLeftOfPlane, airfieldOrientation, distanceBetween);
        
        return firePotSeries;
    }
}
