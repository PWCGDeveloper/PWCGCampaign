package pwcg.mission.flight.strategicintercept;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.bomb.StrategicBombingPackage;
import pwcg.mission.flight.escort.VirtualEscortFlightBuilder;
import pwcg.mission.flight.plot.FlightInformationFactory;

public class StrategicInterceptOpposingFlightBuilder
{
    private IFlightInformation playerFlightInformation;
    private StrategicInterceptOpposingFlightSquadronChooser opposingFlightSquadronChooser;

    public StrategicInterceptOpposingFlightBuilder(IFlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
        opposingFlightSquadronChooser = new StrategicInterceptOpposingFlightSquadronChooser(playerFlightInformation);
    }

    public List<IFlight> buildOpposingFlights() throws PWCGException
    {
        List<IFlight> opposingFlights = new ArrayList<>();
        
        Squadron opposingBomberSquadron = opposingFlightSquadronChooser.getOpposingBomberSquadron();            
        if (opposingBomberSquadron != null)
        {
            IFlight opposingBomberFlight = createOpposingBomberFlights(opposingBomberSquadron);
            if (opposingBomberFlight != null)
            {
                opposingFlights.add(opposingBomberFlight);
                
                IFlight opposingEscortFlight =  createOpposingEscortFlights(opposingBomberFlight);
                if (opposingEscortFlight != null)
                {
                    opposingFlights.add(opposingEscortFlight);
                }
            }
        }
        return opposingFlights;
    }

    private IFlight createOpposingBomberFlights(Squadron opposingBomberSquadron) throws PWCGException
    {
        Coordinate startingPosition = determineOpposingFlightStartPosition(opposingBomberSquadron);
        IFlight opposingBomberFlight = buildOpposingFlight(opposingBomberSquadron, startingPosition);
        return opposingBomberFlight;
    }

    private Coordinate determineOpposingFlightStartPosition(Squadron opposingBomberSquadron) throws PWCGException
    {
        Date campaignDate = playerFlightInformation.getCampaign().getDate();
        
        Coordinate playerAirfieldPosition = playerFlightInformation.getSquadron().determineCurrentPosition(playerFlightInformation.getCampaign().getDate());
        double distancePlayerFromTarget = MathUtils.calcDist(playerAirfieldPosition , playerFlightInformation.getTargetPosition());
        
        Coordinate closestFrontLocationToTarget = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaignDate).findClosestFrontCoordinateForSide(
                playerFlightInformation.getTargetPosition(), opposingBomberSquadron.getCountry().getSide());
        double distanceFrontToTarget = MathUtils.calcDist(closestFrontLocationToTarget , playerFlightInformation.getTargetPosition());
        
        double distanceEnemyAirfieldToTarget = MathUtils.calcDist(opposingBomberSquadron.determineCurrentPosition(campaignDate), playerFlightInformation.getTargetPosition());
        double distanceFromFrontForStart = distanceFrontToTarget;
        if (distanceFrontToTarget > distanceEnemyAirfieldToTarget)
        {
            distanceFromFrontForStart = distanceEnemyAirfieldToTarget;
        }
        
        if (distanceFrontToTarget > distancePlayerFromTarget)
        {
            distanceFromFrontForStart = distancePlayerFromTarget;
        }

        double angleFromBomberFieldToTarget = MathUtils.calcAngle(
                closestFrontLocationToTarget,
                opposingBomberSquadron.determineCurrentPosition(campaignDate));
                    
        Coordinate startingPosition = MathUtils.calcNextCoord(closestFrontLocationToTarget, angleFromBomberFieldToTarget, distanceFromFrontForStart);
        return startingPosition;
    }

    private IFlight buildOpposingFlight(Squadron opposingSquadron, Coordinate startingPosition) throws PWCGException 
    {
        IFlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(
                opposingSquadron, playerFlightInformation.getMission(), FlightTypes.STRATEGIC_BOMB);

        StrategicBombingPackage bombingPackage = new StrategicBombingPackage(playerFlightInformation);
        IFlight opposingFlight = bombingPackage.createPackage();
        opposingFlight.createFlight();
        return opposingFlight;
    }

    private IFlight createOpposingEscortFlights(IFlight escortedFlight) throws PWCGException
    {
        VirtualEscortFlightBuilder virtualEscortFlightBuilder = new VirtualEscortFlightBuilder();
        IFlight escortForAiFlight = virtualEscortFlightBuilder.createVirtualEscortFlight(escortedFlight);
        return escortForAiFlight;
    }
}
