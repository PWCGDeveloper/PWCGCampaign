package pwcg.mission.flight.strategicintercept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

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
            Collections.shuffle(opposingBomberSquadrons); 

            IFlight opposingBomberFlight = createBomberFlight(opposingBomberSquadrons.get(0));
            if (opposingBomberFlight != null)
            {
                opposingFlights.add(opposingBomberFlight);
            }
        }
        return opposingFlights;
    }
    
    private IFlight createBomberFlight(Squadron opposingBomberSquadron) throws PWCGException
    {
        IFlightInformation opposingFlightInformation = buildOpposingFlightInformation(opposingBomberSquadron);
        TargetDefinition opposingTargetDefinition = buildOpposingTargetDefintion(opposingFlightInformation);    
        IFlight opposingFlight = new BombingFlight(opposingFlightInformation, opposingTargetDefinition);
        opposingFlight.createFlight();
        return opposingFlight;
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
        TargetDefinition opposingTargetDefinition = new TargetDefinition(TargetType.TARGET_AIR, playerTargetDefinition.getPosition(), opposingFlightInformation.getCountry());
        return opposingTargetDefinition;
    }
}
