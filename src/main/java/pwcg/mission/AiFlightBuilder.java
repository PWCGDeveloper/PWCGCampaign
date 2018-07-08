package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.PWCGFlightFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.factory.FlightFactory;

public class AiFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private ConfigManager configManager;

    AiFlightBuilder (Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
        
        configManager = campaign.getCampaignConfigManager();
    }
    
    private List<Flight> missionFlights = new ArrayList<Flight>();

    public List<Flight> createAiFlights() throws PWCGException
    {
        if (missionHasAiFlights(mission.getMissionFlightBuilder().getPlayerFlight().getFlightType()))
        {
            if (campaign.determineCountry().getSide() == Side.ALLIED)
            {
                createFlightsForSquadrons(mission.getMissionParameters().getAxisSquads());
                createFlightsForSquadrons(mission.getMissionParameters().getAlliedSquads());
            }
            else
            {
                createFlightsForSquadrons(mission.getMissionParameters().getAlliedSquads());
                createFlightsForSquadrons(mission.getMissionParameters().getAxisSquads());
            }
        }
        
        return missionFlights;
    }
    
    private boolean missionHasAiFlights(FlightTypes playerFlightType) throws PWCGException
    {
        if (!(playerFlightType == FlightTypes.SCRAMBLE) &&
            !(playerFlightType == FlightTypes.SPY_EXTRACT) &&
            !(playerFlightType == FlightTypes.STRATEGIC_BOMB) &&
            !(playerFlightType == FlightTypes.HOME_DEFENSE))
        {
            return true;
        }
        
        return false;
    }
    

    private void createFlightsForSquadrons(List<Squadron> squads) throws PWCGException 
    {
        TestDriver testDriver = TestDriver.getInstance();
        if (testDriver.isCreatePlayerOnly())
        {
            return;
        }
        
        boolean isMySquadNightSquadron = mission.getMissionFlightBuilder().getPlayerFlight().getSquadron().determineIsNightSquadron();
        if (isMySquadNightSquadron)
        {
            return;
        }
        
        for (Squadron squadron : squads)
        {
            if (!squadronWillGenerateAFlight(squadron))
            {
                continue;
            }

            FlightFactory flightFactory = PWCGFlightFactory.createFlightFactory(campaign);
            FlightTypes flightType = flightFactory.buildFlight(squadron, false);
            Flight flight = flightFactory.buildFlight(mission, squadron, flightType, false);
            if (flight != null)
            {
                missionFlights.add(flight);
            }
        }
    }
    
    private boolean squadronWillGenerateAFlight(Squadron squadron) throws PWCGException
    {
        if (squadron.getSquadronId() == campaign.getSquadronId())
        {
            return false;
        }

        if (squadron.determineIsNightSquadron())
        {
            return false;
        }
        
        if (!squadronGeneratesFlightRandom())
        {
            return false;
        }

        if (!squadron.isSquadronViable(campaign))
        {
            return false;
        }

        return true;
    }

    private boolean squadronGeneratesFlightRandom() throws PWCGException 
    {
        int squadronGeneratesFlightOdds = configManager.getIntConfigParam(ConfigItemKeys.SquadronGeneratesMissionOddsKey);
        int squadronGeneratesFlightModifier = configManager.getIntConfigParam(ConfigItemKeys.SquadronGeneratesMissionModifierKey);
        
        if (!campaign.isBattle())
        {
            squadronGeneratesFlightOdds -= squadronGeneratesFlightModifier;
        }
        
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < squadronGeneratesFlightOdds)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
