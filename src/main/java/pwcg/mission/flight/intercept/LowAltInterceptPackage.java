package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightGroundUnitTargetGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;

public class LowAltInterceptPackage extends InterceptPackage
{
    private GroundUnitCollection groundUnitCollection;
    
    public LowAltInterceptPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
		flightType = FlightTypes.LOW_ALT_CAP;
    }

    @Override
    public Flight createPackage () throws PWCGException 
    {
        createBattle();
       return super.createPackage();
    }

    @Override
    protected List<Role> generateOpposingFlightRole()
    {
        List<Role> opposingFlightRoles = new ArrayList<>();
        opposingFlightRoles.add(Role.ROLE_ATTACK);
        opposingFlightRoles.add(Role.ROLE_DIVE_BOMB);
        return opposingFlightRoles;
    }
    
    @Override
    protected Coordinate getTargetCoordinates() throws PWCGException 
    {
        Side mySide = campaign.determineCountry().getSide();
        return groundUnitCollection.getTargetCoordinatesFromGroundUnits(mySide);
    }


    private void createBattle() throws PWCGException
    {
        FlightGroundUnitTargetGenerator targetGenerator = new FlightGroundUnitTargetGenerator(mission, campaign, squadron, FlightTypes.GROUND_ATTACK, isPlayerFlight);
        groundUnitCollection = targetGenerator.createGroundUnitsForFlightWithOverride(TacticalTarget.TARGET_DEFENSE);
    }
}
