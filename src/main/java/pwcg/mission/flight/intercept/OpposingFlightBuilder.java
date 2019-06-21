package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;

public class OpposingFlightBuilder
{
    private FlightInformation flightInformation;
    private List<Role> opposingFlightRoles;

    public OpposingFlightBuilder(FlightInformation flightInformation, List<Role> opposingFlightRoles)
    {
        this.flightInformation = flightInformation;
        this.opposingFlightRoles = opposingFlightRoles;
    }

    public List<InterceptOpposingFlight> buildOpposingFlights() throws PWCGException
    {
        OpposingFlightSquadronChooser opposingFlightSquadronChooser = new OpposingFlightSquadronChooser(flightInformation, opposingFlightRoles);
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

        String opposingFieldName = opposingSquadron.determineCurrentAirfieldName(flightInformation.getCampaign().getDate());
        if (opposingFieldName != null)
        {
            IAirfield opposingField =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(opposingFieldName);
            double angleFromFieldToTarget = MathUtils.calcAngle(flightInformation.getTargetCoords(), opposingField.getPosition());
                
            double distancePlayerFromTarget = MathUtils.calcDist(flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate()), flightInformation.getTargetCoords());
            Coordinate startingPosition = MathUtils.calcNextCoord(flightInformation.getTargetCoords(), angleFromFieldToTarget, distancePlayerFromTarget);
                
            interceptOpposingFlight = getOpposingFlight(opposingSquadron, startingPosition);
        }
        
        return interceptOpposingFlight;
    }

    private InterceptOpposingFlight getOpposingFlight(Squadron opposingSquadron, Coordinate startingPosition) throws PWCGException 
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startingPosition.copy());
        FlightTypes opposingFlightType = getFlightType(opposingSquadron);
        InterceptOpposingFlight opposingFlight = new InterceptOpposingFlight (opposingFlightInformation, missionBeginUnit, startingPosition);
        opposingFlight.createUnitMission();
        opposingFlight.getMissionBeginUnit().setStartTime(2);                
        return opposingFlight;
    }
    
    private FlightTypes getFlightType(Squadron opposingSquadron) throws PWCGException
    {
        if (opposingSquadron.determineSquadronPrimaryRole(flightInformation.getCampaign().getDate()) == Role.ROLE_DIVE_BOMB)
        {
            return FlightTypes.GROUND_ATTACK;
        }
        else if (opposingSquadron.determineSquadronPrimaryRole(flightInformation.getCampaign().getDate()) == Role.ROLE_DIVE_BOMB)
        {   
            return FlightTypes.DIVE_BOMB;
        }
        else
        {
            return FlightTypes.BOMB;            
        }
    }
}
