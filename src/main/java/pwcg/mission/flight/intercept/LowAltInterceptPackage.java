package pwcg.mission.flight.intercept;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightGroundUnitTargetGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;

public class LowAltInterceptPackage extends InterceptPackage
{
    public LowAltInterceptPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);

		flightType = FlightTypes.LOW_ALT_CAP;

    }

    @Override
    public Flight createPackage () throws PWCGException 
    {
        GroundUnitCollection groundUnitCollection = createBattle();
        
        Side mySide = campaign.determineCountry().getSide();
        InterceptFlight interceptFlight = createInterceptFlight(campaign, groundUnitCollection.getTargetCoordinatesFromGroundUnits(mySide));

        if (isPlayerFlight)
        {
            generateOpposingFlightRole();
            addOpposingFlight(interceptFlight, groundUnitCollection.getTargetCoordinatesFromGroundUnits(mySide));
            interceptFlight.linkGroundUnitsToFlight(groundUnitCollection);
        }
        
        return interceptFlight;
    }


    private GroundUnitCollection createBattle() throws PWCGException
    {
        FlightGroundUnitTargetGenerator targetGenerator = new FlightGroundUnitTargetGenerator(mission, campaign, squadron, opposingFlightType, false);
        GroundUnitCollection groundUnitCollection = targetGenerator.createGroundUnitsForFlightWithOverride(TacticalTarget.TARGET_DEFENSE);
        return groundUnitCollection;
    }

    private void generateOpposingFlightRole()
    {
        int roll = RandomNumberGenerator.getRandom(100);
		if (roll < 20)
		{
		    opposingFlightRole = Role.ROLE_BOMB;
		    opposingFlightType = FlightTypes.LOW_ALT_BOMB;
		}
		else
		{
            opposingFlightRole = Role.ROLE_ATTACK;
            opposingFlightType = FlightTypes.GROUND_ATTACK;
		}
    }
}
