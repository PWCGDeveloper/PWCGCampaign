package pwcg.mission.flight.strategicintercept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.bomb.StrategicBombingFlight;
import pwcg.mission.flight.waypoint.begin.IngressBackoffUsingConfigsCalculator;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class StrategicInterceptOpposingFlightBuilder
{
    private FlightInformation playerFlightInformation;
    private TargetDefinition playerTargetDefinition;
    private StrategicInterceptOpposingFlightSquadronChooser opposingFlightSquadronChooser;

    public StrategicInterceptOpposingFlightBuilder(FlightInformation playerFlightInformation, TargetDefinition playerTargetDefinition)
    {
        this.playerFlightInformation = playerFlightInformation;
        this.playerTargetDefinition = playerTargetDefinition;
        opposingFlightSquadronChooser = new StrategicInterceptOpposingFlightSquadronChooser(playerFlightInformation);
    }

    public List<IFlight> buildOpposingFlights() throws PWCGException
    {
        List<IFlight> opposingFlights = new ArrayList<>();
     
        ICountry countryRepresentingOpposingSide = PWCGContext.getInstance().getMap(
                playerFlightInformation.getCampaign().getCampaignMap()).getGroundCountryForMapBySide(playerFlightInformation.getSquadron().determineSide().getOppositeSide());
        TargetDefinition opposingTargetDefinition = buildBaselineOpposingTargetDefintion(countryRepresentingOpposingSide);    

        List<Squadron> opposingBomberSquadrons = opposingFlightSquadronChooser.getOpposingBomberSquadron();
        Collections.shuffle(opposingBomberSquadrons); 

        int numStrategicBombingFlights = getNumOpposingFlights(opposingBomberSquadrons);
        for (int i = 0; i < numStrategicBombingFlights; ++i)
        {
            Squadron opposingBomberSquadron = opposingBomberSquadrons.get(i);
            TargetDefinition opposingTargetDefinitionForFlight = new TargetDefinition(
                    opposingTargetDefinition.getTargetType(),
                    opposingTargetDefinition.getPosition(),
                    opposingBomberSquadron.getCountry(),
                    opposingTargetDefinition.getTargetName());

            IFlight opposingBomberFlight = createStrategicBomberFlight(opposingBomberSquadron, opposingTargetDefinitionForFlight);
            if (opposingBomberFlight != null)
            {
                opposingFlights.add(opposingBomberFlight);
            }
        }
        return opposingFlights;
    }

    private TargetDefinition buildBaselineOpposingTargetDefintion(ICountry country) throws PWCGException
    {
        TargetDefinition opposingTargetDefinition = new TargetDefinition(TargetType.TARGET_CITY, playerTargetDefinition.getPosition(), country, "City");
        return opposingTargetDefinition;
    }

    private IFlight createStrategicBomberFlight(Squadron opposingBomberSquadron, TargetDefinition opposingTargetDefinition) throws PWCGException
    {
        FlightInformation opposingFlightInformation = buildOpposingFlightInformation(opposingBomberSquadron);
        double ingressDistanceFromTarget = IngressBackoffUsingConfigsCalculator.calculateIngressDistanceFromTarget(opposingFlightInformation, opposingTargetDefinition);
        IFlight opposingFlight = new StrategicBombingFlight(opposingFlightInformation, opposingTargetDefinition, ingressDistanceFromTarget);
        opposingFlight.createFlight();
        return opposingFlight;
    }

    private FlightInformation buildOpposingFlightInformation(Squadron opposingSquadron) throws PWCGException
    {
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(playerFlightInformation.getMission(), opposingSquadron, NecessaryFlightType.OPPOSING_FLIGHT);
        FlightInformation opposingFlightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.STRATEGIC_BOMB);
        return opposingFlightInformation;
    }
    
    private int getNumOpposingFlights(List<Squadron> opposingBomberSquadrons) throws PWCGException
    {
        int numStrategicBombingFlights = 2;
        
        ConfigManagerCampaign configManager = playerFlightInformation.getCampaign().getCampaignConfigManager();
        String currentAirSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigAirKey);
        if (currentAirSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            numStrategicBombingFlights = 1;
        }
        else if (currentAirSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            numStrategicBombingFlights = 2;
        }
        else if (currentAirSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            numStrategicBombingFlights = 3;
        }
        
        if (opposingBomberSquadrons.size() < numStrategicBombingFlights)
        {
            numStrategicBombingFlights = opposingBomberSquadrons.size();
        }
        
        return numStrategicBombingFlights;
    }
}
