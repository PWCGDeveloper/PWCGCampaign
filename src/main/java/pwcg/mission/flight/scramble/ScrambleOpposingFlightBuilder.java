package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderFactory;

public class ScrambleOpposingFlightBuilder
{
    private IFlightInformation playerFlightInformation;

    public ScrambleOpposingFlightBuilder(IFlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public List<IFlight> buildOpposingFlights() throws PWCGException
    {
        ScrambleOpposingFlightSquadronChooser opposingFlightSquadronChooser = new ScrambleOpposingFlightSquadronChooser(playerFlightInformation);
        List<Squadron> opposingSquadrons = opposingFlightSquadronChooser.getOpposingSquadrons();            
        return createOpposingFlights(opposingSquadrons);
    }
    
    private List<IFlight> createOpposingFlights(List<Squadron> opposingSquadrons) throws PWCGException
    {
        List<IFlight> opposingFlights = new ArrayList<>();
        for (Squadron squadron : opposingSquadrons)
        {
            IFlight opposingFlight = createOpposingFlight(squadron);
            if (opposingFlight != null)
            {
                opposingFlights.add(opposingFlight);
            }
        }
        return opposingFlights;
    }

    private IFlight createOpposingFlight(Squadron opposingSquadron) throws PWCGException
    {
        IFlight ScrambleOpposingFlight = null;
        String opposingFieldName = opposingSquadron.determineCurrentAirfieldName(playerFlightInformation.getCampaign().getDate());
        if (opposingFieldName != null)
        {
            ScrambleOpposingFlight = buildOpposingFlight(opposingSquadron);
        }
        
        return ScrambleOpposingFlight;
    }

    private IFlight buildOpposingFlight(Squadron opposingSquadron) throws PWCGException 
    {
        FlightTypes opposingFlightType = getFlightType(opposingSquadron);
        
        IFlightInformation opposingFlightInformation = ScrambleOpposingFlightInformationBuilder.buildAiScrambleOpposingFlightInformation(
                opposingSquadron, playerFlightInformation, opposingFlightType);
        
        TargetDefinition opposingTargetDefinition = buildOpposingTargetDefintion(opposingFlightInformation);
                
        if (opposingFlightType == FlightTypes.PATROL)
        {
            ScrambleOpposingFighterFlight opposingFlight = new ScrambleOpposingFighterFlight (opposingFlightInformation, opposingTargetDefinition);
            opposingFlight.createFlight();
            return opposingFlight;            
        }
        else if (opposingFlightType == FlightTypes.BOMB)
        {
            ScrambleOpposingBombFlight opposingFlight = new ScrambleOpposingBombFlight (opposingFlightInformation, opposingTargetDefinition);
            opposingFlight.createFlight();
            return opposingFlight;            
        }
        else if (opposingFlightType == FlightTypes.DIVE_BOMB)
        {
            ScrambleOpposingDiveBombFlight opposingFlight = new ScrambleOpposingDiveBombFlight (opposingFlightInformation, opposingTargetDefinition);
            opposingFlight.createFlight();
            return opposingFlight;            
        }
        else if (opposingFlightType == FlightTypes.GROUND_ATTACK)
        {
            ScrambleOpposingGroundAttackFlight opposingFlight = new ScrambleOpposingGroundAttackFlight (opposingFlightInformation, opposingTargetDefinition);
            opposingFlight.createFlight();
            return opposingFlight;            
        }
        else 
        {
            throw new PWCGException("No valid scramble flight type generated " + opposingSquadron.getFileName());
        }
    }
    
    private FlightTypes getFlightType(Squadron opposingSquadron) throws PWCGException
    {
        List<FlightTypes> possibleOpposingFlightTypes = new ArrayList<>();
        
        if (opposingSquadron.isSquadronThisRole(playerFlightInformation.getCampaign().getDate(), Role.ROLE_ATTACK))
        {
            possibleOpposingFlightTypes.add(FlightTypes.GROUND_ATTACK);
        }
        if (opposingSquadron.isSquadronThisRole(playerFlightInformation.getCampaign().getDate(), Role.ROLE_DIVE_BOMB))
        {   
            possibleOpposingFlightTypes.add(FlightTypes.DIVE_BOMB);
        }
        if (opposingSquadron.isSquadronThisRole(playerFlightInformation.getCampaign().getDate(), Role.ROLE_BOMB))
        {   
            possibleOpposingFlightTypes.add(FlightTypes.BOMB);
        }
        if (opposingSquadron.isSquadronThisRole(playerFlightInformation.getCampaign().getDate(), Role.ROLE_FIGHTER))
        {
            possibleOpposingFlightTypes.add(FlightTypes.PATROL);
        }
        
        if (possibleOpposingFlightTypes.size() == 0) 
        {
            throw new PWCGException("No valid scramble opposing flight role for squadron " + opposingSquadron.getFileName());
        }
        
     
        int index = RandomNumberGenerator.getRandom(possibleOpposingFlightTypes.size());
        return possibleOpposingFlightTypes.get(index);
    }

    private TargetDefinition buildOpposingTargetDefintion(IFlightInformation opposingFlightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(opposingFlightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        
        Squadron playerSquadron = playerFlightInformation.getSquadron();
        targetDefinition.setTargetPosition(playerSquadron.determineCurrentPosition(playerFlightInformation.getCampaign().getDate()));
        return targetDefinition;
        
    }
}
