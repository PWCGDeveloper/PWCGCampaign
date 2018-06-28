package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.intercept.InterceptOpposingFlight;

public class OpposingFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private Coordinate targetCoordinates;
    private Role opposingFlightRole;
    private FlightTypes opposingFlightType;

    public OpposingFlightBuilder(Mission mission, Coordinate targetCoordinates, Role opposingFlightRole, FlightTypes opposingFlightType)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.targetCoordinates = targetCoordinates;
        this.opposingFlightRole = opposingFlightRole;
        this.opposingFlightType = opposingFlightType;
    }

    public List<InterceptOpposingFlight> buildOpposingFlights() throws PWCGException
    {
        Squadron opposingSquadron = getOpposingSquad();            
        return createOpposingFlight(opposingSquadron);
    }

    private List<InterceptOpposingFlight> createOpposingFlight(Squadron opposingSquad) throws PWCGException
    {
        List<InterceptOpposingFlight> interceptOpposingFlights = new ArrayList<>();

        if (opposingSquad != null)
        {
            String opposingFieldName = opposingSquad.determineCurrentAirfieldName(campaign.getDate());
            if (opposingFieldName != null)
            {
                IAirfield opposingField =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(opposingFieldName);
                double angle = MathUtils.calcAngle(targetCoordinates, opposingField.getPosition());
                
                double distancePlayerFromTarget = MathUtils.calcDist(campaign.getPosition(), targetCoordinates);
                Coordinate startingPosition = MathUtils.calcNextCoord(targetCoordinates, angle, distancePlayerFromTarget);
                
                interceptOpposingFlights = getOpposingFlights(mission, targetCoordinates, startingPosition);
            }
        }
        
        return interceptOpposingFlights;
    }

    private Squadron getOpposingSquad() throws PWCGException
    {
        Squadron opposingSquad = null;
        
        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(opposingFlightRole);
        List<Squadron> opposingSquads = null;
        
        Side enemySide = campaign.determineCountry().getSide().getOppositeSide();
        Squadron playerSquadron = campaign.determineSquadron();
        opposingSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(playerSquadron.determineCurrentPosition(
                campaign.getDate()), 1, 500000.0, acceptableRoles, enemySide, campaign.getDate());

        if (opposingSquads != null && opposingSquads.size() != 0)
        {
            int index= RandomNumberGenerator.getRandom(opposingSquads.size());
            opposingSquad = opposingSquads.get(index);
        }
        
        return opposingSquad;
    }

    private List<InterceptOpposingFlight> getOpposingFlights(Mission mission, Coordinate targetPosition, Coordinate rootStartPosition) throws PWCGException 
    {
        List<InterceptOpposingFlight> opposingBombingFlights = new ArrayList<InterceptOpposingFlight>();

        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(opposingFlightRole);
        
        List<Squadron> opposingSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(
                        targetPosition.copy(), 1, 250000.0, acceptableRoles, campaign.determineCountry().getSide().getOppositeSide(), campaign.getDate());

        if (opposingSquads != null && opposingSquads.size() != 0)
        {
            int index= RandomNumberGenerator.getRandom(opposingSquads.size());
            Squadron opposingSquad = opposingSquads.get(index);

            // And the opposing missions
            // Target is the start of the opposing flight while the player field 
            // is the target for the opposing flight.
            int numOpposingFlights = 1;
            if (opposingFlightType == FlightTypes.STRATEGIC_BOMB)
            {
                // Night bombers come in a stream
                if (opposingSquad.determineIsNightSquadron())
                {
                    numOpposingFlights = 3 + RandomNumberGenerator.getRandom(4);
                }
            }
            
            double targetToStartAngle = MathUtils.calcAngle(targetPosition, rootStartPosition);
            
            for (int i = 0; i < numOpposingFlights; ++i)
            {
                // Alternate before and after
                double startPositionAngle = targetToStartAngle;
                if (i%2 == 0)
                {
                    startPositionAngle = MathUtils.adjustAngle (startPositionAngle, 180);       
                }
                
                Coordinate startPosition = MathUtils.calcNextCoord(rootStartPosition, startPositionAngle, i * 3000.0);
                MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
                missionBeginUnit.initialize(startPosition.copy());
                
                InterceptOpposingFlight opposingBombingFlight = new InterceptOpposingFlight ();
                opposingBombingFlight.initialize(mission, campaign, targetPosition.copy(), startPosition, opposingSquad, 
                                opposingFlightType, missionBeginUnit, true, false);
                        
                opposingBombingFlight.createUnitMission();
                
                // Start the bombers right away
                opposingBombingFlight.getMissionBeginUnit().setStartTime(2);
                
                opposingBombingFlights.add(opposingBombingFlight);
            }

        }
        
        return opposingBombingFlights;
    }


}
