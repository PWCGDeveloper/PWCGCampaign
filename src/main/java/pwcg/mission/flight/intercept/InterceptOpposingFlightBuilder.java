package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;

public class InterceptOpposingFlightBuilder
{
    private FlightInformation playerFlightInformation;

    public InterceptOpposingFlightBuilder(FlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public List<InterceptOpposingFlight> buildOpposingFlights() throws PWCGException
    {
        InterceptOpposingFlightSquadronChooser opposingFlightSquadronChooser = new InterceptOpposingFlightSquadronChooser(playerFlightInformation);
        List<Squadron> opposingSquadrons = opposingFlightSquadronChooser.getOpposingSquadrons();            
        return createOpposingFlights(opposingSquadrons);
    }
    
    private List<InterceptOpposingFlight> createOpposingFlights(List<Squadron> opposingSquadrons) throws PWCGException
    {
        List<InterceptOpposingFlight> opposingFlights = new ArrayList<>();
        for (Squadron squadron : opposingSquadrons)
        {
            InterceptOpposingFlight opposingFlight = createOpposingFlight(squadron);
            if (opposingFlight != null)
            {
                opposingFlights.add(opposingFlight);
            }
        }
        return opposingFlights;
    }

    private InterceptOpposingFlight createOpposingFlight(Squadron opposingSquadron) throws PWCGException
    {
        InterceptOpposingFlight interceptOpposingFlight = null;

        String opposingFieldName = opposingSquadron.determineCurrentAirfieldName(playerFlightInformation.getCampaign().getDate());
        if (opposingFieldName != null)
        {
            Coordinate startingPosition = determineOpposingFlightStartPosition(opposingFieldName);
            interceptOpposingFlight = buildOpposingFlight(opposingSquadron, startingPosition);
        }
        
        return interceptOpposingFlight;
    }

    private Coordinate determineOpposingFlightStartPosition(String opposingFieldName) throws PWCGException
    {
        IAirfield opposingField =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(opposingFieldName);
        double angleFromFieldToTarget = MathUtils.calcAngle(playerFlightInformation.getTargetCoords(), opposingField.getPosition());
            
        double distancePlayerFromTarget = MathUtils.calcDist(playerFlightInformation.getSquadron().determineCurrentPosition(
                playerFlightInformation.getCampaign().getDate()), playerFlightInformation.getTargetCoords());
        Coordinate startingPosition = MathUtils.calcNextCoord(playerFlightInformation.getTargetCoords(), angleFromFieldToTarget, distancePlayerFromTarget);
        return startingPosition;
    }

    private InterceptOpposingFlight buildOpposingFlight(Squadron opposingSquadron, Coordinate startingPosition) throws PWCGException 
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startingPosition.copy());
        FlightTypes opposingFlightType = getFlightType(opposingSquadron);
        
        FlightInformation opposingFlightInformation = FlightInformationFactory.buildInterceptOpposingInformation(
                opposingSquadron, playerFlightInformation.getMission(), opposingFlightType, playerFlightInformation.getTargetDefinition().getLinkedFlightTargetDefinition());
        InterceptOpposingFlight opposingFlight = new InterceptOpposingFlight (opposingFlightInformation, missionBeginUnit, startingPosition);
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
