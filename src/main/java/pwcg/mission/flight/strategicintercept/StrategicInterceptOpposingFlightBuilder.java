package pwcg.mission.flight.strategicintercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.escort.VirtualEscortFlightBuilder;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderFactory;

public class StrategicInterceptOpposingFlightBuilder
{
    private IFlightInformation playerFlightInformation;
    private TargetDefinition playerTargetDefinition;
    private StrategicInterceptOpposingFlightSquadronChooser opposingFlightSquadronChooser;

    public StrategicInterceptOpposingFlightBuilder(IFlightInformation playerFlightInformation, TargetDefinition playerTargetDefinition)
    {
        this.playerFlightInformation = playerFlightInformation;
        this.playerTargetDefinition = playerTargetDefinition;
        opposingFlightSquadronChooser = new StrategicInterceptOpposingFlightSquadronChooser(playerFlightInformation);
    }

    public List<IFlight> buildOpposingFlights() throws PWCGException
    {
        List<IFlight> opposingFlights = new ArrayList<>();
        
        List<Squadron> opposingBomberSquadrons = opposingFlightSquadronChooser.getOpposingBomberSquadron();            
        if (opposingBomberSquadrons.size() > 0)
        {
            List<IFlight> opposingBomberFlights = createOpposingBomberFlights(opposingBomberSquadrons);
            if (opposingBomberFlights.size() > 0)
            {
                for (IFlight opposingBomberFlight : opposingBomberFlights)
                {
                    opposingFlights.add(opposingBomberFlight);
                }
                
                IFlight opposingEscortFlight =  createOpposingEscortFlights(opposingBomberFlights.get(0));
                if (opposingEscortFlight != null)
                {
                    opposingFlights.add(opposingEscortFlight);
                }
            }
        }
        return opposingFlights;
    }

    private List<IFlight> createOpposingBomberFlights(List<Squadron> opposingBomberSquadrons) throws PWCGException
    {
        List<IFlight> opposingBomberFlights = new ArrayList<>();
        int numBomberFlights = getNumberOfFlights(opposingBomberSquadrons);

        for (int i = 0; i < numBomberFlights; ++i)
        {
            if (i == 0)
            {
                IFlight opposingFlight = createFirstFlight(opposingBomberSquadrons.get(i));
                opposingBomberFlights.add(opposingFlight);
            }
            else
            {
                IFlight opposingFlight = createNextFlight(opposingBomberSquadrons.get(i), opposingBomberFlights.get(0), i);
                opposingBomberFlights.add(opposingFlight);
            }
        }

        return opposingBomberFlights;
    }
    
    private IFlight createFirstFlight(Squadron opposingBomberSquadron) throws PWCGException
    {
        IFlightInformation opposingFlightInformation = buildOpposingFlightInformation(opposingBomberSquadron);
        TargetDefinition opposingTargetDefinition = buildOpposingTargetDefintion(opposingFlightInformation);    
        IFlight opposingFlight = new BombingFlight(opposingFlightInformation, opposingTargetDefinition);
        opposingFlight.createFlight();
        return opposingFlight;
    }
    
    private IFlight createNextFlight(Squadron opposingBomberSquadron, IFlight firstFlight, int flightNum) throws PWCGException
    {
        IFlightInformation opposingFlightInformation = buildOpposingFlightInformation(opposingBomberSquadron);
        IFlight opposingFlight = new BombingFlight(opposingFlightInformation, firstFlight.getTargetDefinition());
        opposingFlight.createFlight();
        synchAdditionalBomberFlightWithFirstFlight(opposingFlight, firstFlight, flightNum);
        return opposingFlight;
    }
    
    private void synchAdditionalBomberFlightWithFirstFlight(IFlight opposingFlight, IFlight firstFlight, int flightNum) throws PWCGException
    {
        int altitudeOffset = 600 * flightNum;
        int horizontalOffset = 300 * flightNum;

        opposingFlight.getWaypointPackage().clearMissionPointSet();
        
        IMissionPointSet flightActivate = MissionPointSetFactory.createFlightActivate(opposingFlight);
        opposingFlight.getWaypointPackage().addMissionPointSet(flightActivate);

        IMissionPointSet duplicatedWaypointsWithOffset = MissionPointSetFactory.duplicateWaypointsWithOffset(firstFlight, altitudeOffset, horizontalOffset);
        opposingFlight.getWaypointPackage().addMissionPointSet(duplicatedWaypointsWithOffset);
    }

    private int getNumberOfFlights(List<Squadron> opposingBomberSquadrons) throws PWCGException
    {
        ConfigManagerCampaign configManager = playerFlightInformation.getCampaign().getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigAirKey);
        int numBomberFlights = 0;
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            numBomberFlights = 3;      
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            numBomberFlights = 2;
        }
        else
        {
            numBomberFlights = 1;
        }
        
        if (numBomberFlights > opposingBomberSquadrons.size())
        {
            numBomberFlights = opposingBomberSquadrons.size();
        }
        return numBomberFlights;
    }

    private IFlightInformation buildOpposingFlightInformation(Squadron opposingSquadron) throws PWCGException
    {
        boolean isPlayerFlight = false;
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(this.playerFlightInformation.getMission(), opposingSquadron, isPlayerFlight);
        IFlightInformation opposingFlightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.STRATEGIC_BOMB);
        return opposingFlightInformation;
    }

    private TargetDefinition buildOpposingTargetDefintion(IFlightInformation opposingFlightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(opposingFlightInformation);
        TargetDefinition opposingTargetDefinition =  targetDefinitionBuilder.buildTargetDefinition();
        opposingTargetDefinition.setTargetPosition(playerTargetDefinition.getTargetPosition());
        return opposingTargetDefinition;
    }

    private IFlight createOpposingEscortFlights(IFlight escortedFlight) throws PWCGException
    {
        VirtualEscortFlightBuilder virtualEscortFlightBuilder = new VirtualEscortFlightBuilder();
        IFlight escortForAiFlight = virtualEscortFlightBuilder.createVirtualEscortFlight(escortedFlight);
        return escortForAiFlight;
    }
}
