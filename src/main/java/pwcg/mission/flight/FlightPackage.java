package pwcg.mission.flight;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitCollection;

public abstract class FlightPackage 
{
	abstract public Flight createPackage() throws PWCGException;

    protected Mission mission;
    protected Campaign campaign;
    protected Squadron squadron;
    protected FlightTypes flightType;
    protected boolean isPlayerFlight;

	protected FlightPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
	{
        this.mission = mission;
        this.campaign = campaign;
        this.squadron = squadron;
        this.isPlayerFlight = isPlayerFlight;
	}


    protected GroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        FlightGroundUnitTargetGenerator groundUnitGenerator = new FlightGroundUnitTargetGenerator(mission, campaign, squadron, flightType, isPlayerFlight);
        GroundUnitCollection groundUnits = groundUnitGenerator.createGroundUnitsForFlight();
        return groundUnits;
    }

    protected FlightInformation createFlightInformation(Coordinate targetWaypoint)
    {
        FlightInformation flightInformation = null;
        if (isPlayerFlight)
        {
            flightInformation = FlightInformationFactory.buildPlayerFlightInformation(squadron, mission, flightType, targetWaypoint);
        }
        else
        {
            flightInformation = FlightInformationFactory.buildPlayerFlightInformation(squadron, mission, flightType, targetWaypoint);
        }
        return flightInformation;
    }

    protected GroundUnitCollection createSpecificGroundUnitsForFlight(TacticalTarget targetType) throws PWCGException
    {
        FlightGroundUnitTargetGenerator groundUnitGenerator = new FlightGroundUnitTargetGenerator(mission, campaign, squadron, flightType, isPlayerFlight);
        GroundUnitCollection groundUnits = groundUnitGenerator.createGroundUnitsForFlightWithOverride(targetType);
        return groundUnits;
    }

    protected void addPossibleEscort(Flight flight) throws PWCGException 
    {
        if (flight.isPlayerFlight())
        {
            flight.createEscortForPlayerFlight();
        }
        else if (!flight.isFriendly())
        {
            addPossibleEscortForEnemyFlight(flight);
        } 
    }

    protected void addPossibleEscortForEnemyFlight(Flight flight) throws PWCGException, PWCGException
    {
        if (mission.getMissionFlightBuilder().getPlayerFlight().isFighterFlight())
        {
            if (campaign.isFighterCampaign())
            {
                int escortedOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.IsEscortedOddsKey);
                int escortedDiceRoll = RandomNumberGenerator.getRandom(100);        
                if (escortedDiceRoll < escortedOdds)
                {
                    flight.createVirtualEscortFlight();
                }
            }
        }
    }
    
    protected void addPossibleEnemyScramble(Flight flight, GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        PackageAirfieldScramble packageAirfieldScramble = new PackageAirfieldScramble(mission, campaign);
        Flight enemyScramble = packageAirfieldScramble.createEnemyScramble(flight, groundUnitCollection.getTargetCoordinatesFromGroundUnits(flight.getCountry().getSide().getOppositeSide()));
        if (enemyScramble != null)
        {
            flight.addLinkedUnit(enemyScramble);
        }
    }
}
