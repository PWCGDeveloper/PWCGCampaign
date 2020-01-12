package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.attack.GroundAttackPackage;
import pwcg.mission.flight.bomb.BombingPackage;
import pwcg.mission.flight.divebomb.DiveBombingPackage;
import pwcg.mission.flight.plot.FlightInformationFactory;
import pwcg.mission.flight.recon.ReconPackage;

public class InterceptOpposingFlightBuilder
{
    private IFlightInformation playerFlightInformation;

    public InterceptOpposingFlightBuilder(IFlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public List<IFlight> buildOpposingFlights() throws PWCGException
    {
        InterceptOpposingFlightSquadronChooser opposingFlightSquadronChooser = new InterceptOpposingFlightSquadronChooser(playerFlightInformation);
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
        IFlight interceptOpposingFlight = null;

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
        IAirfield opposingField =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(opposingFieldName);
        double angleFromFieldToTarget = MathUtils.calcAngle(playerFlightInformation.getTargetPosition(), opposingField.getPosition());
            
        double distancePlayerFromTarget = MathUtils.calcDist(playerFlightInformation.getSquadron().determineCurrentPosition(
                playerFlightInformation.getCampaign().getDate()), playerFlightInformation.getTargetPosition());
        Coordinate startingPosition = MathUtils.calcNextCoord(playerFlightInformation.getTargetPosition(), angleFromFieldToTarget, distancePlayerFromTarget);
        return startingPosition;
    }

    private IFlight buildOpposingFlight(Squadron opposingSquadron, Coordinate startingPosition) throws PWCGException 
    {
        FlightTypes opposingFlightType = getFlightType(opposingSquadron);
        IFlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(
                opposingSquadron, playerFlightInformation.getMission(), opposingFlightType);
        IFlight opposingFlight = buildOpposingFlight(opposingFlightInformation);
        opposingFlight.createFlight();
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
    
    private IFlight buildOpposingFlight(IFlightInformation opposingFlightInformation) throws PWCGException
    {
        if (opposingFlightInformation.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            GroundAttackPackage bombingPackage = new GroundAttackPackage(opposingFlightInformation);
            return bombingPackage.createPackage();
        }
        else if (opposingFlightInformation.getFlightType() == FlightTypes.DIVE_BOMB)
        {   
            DiveBombingPackage bombingPackage = new DiveBombingPackage(opposingFlightInformation);
            return bombingPackage.createPackage();
        }
        else if (opposingFlightInformation.getFlightType() == FlightTypes.RECON)
        {   
            ReconPackage bombingPackage = new ReconPackage(opposingFlightInformation);
            return bombingPackage.createPackage();
        }
        else
        {
            BombingPackage bombingPackage = new BombingPackage(opposingFlightInformation);
            return bombingPackage.createPackage();
        }
    }
}
