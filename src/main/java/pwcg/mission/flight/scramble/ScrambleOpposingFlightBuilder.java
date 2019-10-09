package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;

public class ScrambleOpposingFlightBuilder
{
    private FlightInformation playerFlightInformation;

    public ScrambleOpposingFlightBuilder(FlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public List<ScrambleOpposingFlight> buildOpposingFlights() throws PWCGException
    {
        ScrambleOpposingFlightSquadronChooser opposingFlightSquadronChooser = new ScrambleOpposingFlightSquadronChooser(playerFlightInformation);
        List<Squadron> opposingSquadrons = opposingFlightSquadronChooser.getOpposingSquadrons();            
        return createOpposingFlights(opposingSquadrons);
    }
    
    private List<ScrambleOpposingFlight> createOpposingFlights(List<Squadron> opposingSquadrons) throws PWCGException
    {
        List<ScrambleOpposingFlight> opposingFlights = new ArrayList<>();
        for (Squadron squadron : opposingSquadrons)
        {
            ScrambleOpposingFlight opposingFlight = createOpposingFlight(squadron);
            if (opposingFlight != null)
            {
                opposingFlights.add(opposingFlight);
            }
        }
        return opposingFlights;
    }

    private ScrambleOpposingFlight createOpposingFlight(Squadron opposingSquadron) throws PWCGException
    {
        ScrambleOpposingFlight ScrambleOpposingFlight = null;
        String opposingFieldName = opposingSquadron.determineCurrentAirfieldName(playerFlightInformation.getCampaign().getDate());
        if (opposingFieldName != null)
        {
            Coordinate startingPosition = determineOpposingFlightStartPosition(opposingFieldName);
            ScrambleOpposingFlight = buildOpposingFlight(opposingSquadron, startingPosition);
        }
        
        return ScrambleOpposingFlight;
    }

    private Coordinate determineOpposingFlightStartPosition(String opposingFieldName) throws PWCGException
    {
        IAirfield opposingField =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(opposingFieldName);
        double angleFromFieldToTarget = MathUtils.calcAngle(playerFlightInformation.getTargetCoords(), opposingField.getPosition());
            
        double distancePlayerFromTarget = MathUtils.calcDist(playerFlightInformation.getSquadron().determineCurrentPosition(
                playerFlightInformation.getCampaign().getDate()), playerFlightInformation.getTargetCoords());
        Coordinate startingPosition = MathUtils.calcNextCoord(playerFlightInformation.getTargetCoords(), angleFromFieldToTarget, distancePlayerFromTarget);
        return startingPosition;
    }

    private ScrambleOpposingFlight buildOpposingFlight(Squadron opposingSquadron, Coordinate startingPosition) throws PWCGException 
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startingPosition.copy());
        FlightTypes opposingFlightType = getFlightType(opposingSquadron);
        
        // TODO I never build the linked flight target definition in the scramble package
        FlightInformation opposingFlightInformation = FlightInformationFactory.buildInterceptOpposingInformation(
                opposingSquadron, playerFlightInformation.getMission(), opposingFlightType, playerFlightInformation.getTargetDefinition().getLinkedFlightTargetDefinition());
        ScrambleOpposingFlight opposingFlight = new ScrambleOpposingFlight (opposingFlightInformation, missionBeginUnit);
        opposingFlight.createUnitMission();
        opposingFlight.getMissionBeginUnit().setStartTime(2);                
        return opposingFlight;
    }
    
    private FlightTypes getFlightType(Squadron opposingSquadron) throws PWCGException
    {
        if (opposingSquadron.determineSquadronPrimaryRole(playerFlightInformation.getCampaign().getDate()) == Role.ROLE_ATTACK)
        {
            return FlightTypes.GROUND_ATTACK;
        }
        else if (opposingSquadron.determineSquadronPrimaryRole(playerFlightInformation.getCampaign().getDate()) == Role.ROLE_DIVE_BOMB)
        {   
            return FlightTypes.DIVE_BOMB;
        }
        else if (opposingSquadron.determineSquadronPrimaryRole(playerFlightInformation.getCampaign().getDate()) == Role.ROLE_RECON)
        {   
            return FlightTypes.RECON;
        }
        else
        {
            return FlightTypes.BOMB;            
        }
    }
}
