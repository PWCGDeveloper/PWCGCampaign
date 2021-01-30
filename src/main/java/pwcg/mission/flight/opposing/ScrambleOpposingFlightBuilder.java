package pwcg.mission.flight.opposing;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class ScrambleOpposingFlightBuilder implements IOpposingFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private Squadron playerSquadron;
    
    public ScrambleOpposingFlightBuilder(Mission mission, Squadron playerSquadron)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.playerSquadron = playerSquadron;
    }

    @Override
    public IFlight createOpposingFlight() throws PWCGException
    {
        ScrambleOpposingFlightSquadronChooser opposingFlightSquadronChooser = new ScrambleOpposingFlightSquadronChooser(campaign, playerSquadron.determineEnemySide());
        Squadron opposingSquadron = opposingFlightSquadronChooser.getOpposingSquadrons();            
        return createOpposingFlight(opposingSquadron);
    }

    private IFlight createOpposingFlight(Squadron opposingSquadron) throws PWCGException
    {
        IFlight ScrambleOpposingFlight = null;
        String opposingFieldName = opposingSquadron.determineCurrentAirfieldName(campaign.getDate());
        if (opposingFieldName != null)
        {
            ScrambleOpposingFlight = buildOpposingFlight(opposingSquadron);
        }
        
        return ScrambleOpposingFlight;
    }

    private IFlight buildOpposingFlight(Squadron opposingSquadron) throws PWCGException 
    {
        FlightTypes opposingFlightType = getFlightType(opposingSquadron);
        
        FlightInformation opposingFlightInformation = ScrambleOpposingFlightInformationBuilder.buildAiScrambleOpposingFlightInformation(
                mission, opposingSquadron, playerSquadron, opposingFlightType);
        
        TargetDefinition opposingTargetDefinition = buildOpposingTargetDefintion(opposingFlightInformation);
                
        if (opposingFlightType == FlightTypes.BOMB)
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
        if (opposingSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_ATTACK))
        {
            possibleOpposingFlightTypes.add(FlightTypes.GROUND_ATTACK);
        }
        if (opposingSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_DIVE_BOMB))
        {   
            possibleOpposingFlightTypes.add(FlightTypes.DIVE_BOMB);
        }
        if (opposingSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_BOMB))
        {   
            possibleOpposingFlightTypes.add(FlightTypes.BOMB);
        }
        
        if (possibleOpposingFlightTypes.size() == 0) 
        {
            throw new PWCGException("No valid scramble opposing flight role for squadron " + opposingSquadron.getFileName());
        }
        
     
        int index = RandomNumberGenerator.getRandom(possibleOpposingFlightTypes.size());
        return possibleOpposingFlightTypes.get(index);
    }

    private TargetDefinition buildOpposingTargetDefintion(FlightInformation opposingFlightInformation) throws PWCGException
    {
        Coordinate playerSquadronLocation = playerSquadron.determineCurrentPosition(campaign.getDate());
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_AIRFIELD, playerSquadronLocation, playerSquadron.getCountry());
        return targetDefinition;
        
    }
}
