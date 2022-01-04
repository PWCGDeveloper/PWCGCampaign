package pwcg.mission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;

public class MissionFlights
{
    private Campaign campaign;
    private Mission mission;
    private List<IFlight> flights = new ArrayList<>();

    public MissionFlights(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }
    
    public void generateFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        MissionFlightBuilder missionFlightBuilder = new MissionFlightBuilder(mission);

        List<IFlight> aiFlights = missionFlightBuilder.createAiFlights(playerFlightTypes);
        flights.addAll(aiFlights);

        List<IFlight> playerFlights = missionFlightBuilder.createPlayerFlights(playerFlightTypes);
        flights.addAll(playerFlights);

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(mission, flights);
        flights = missionFlightKeeper.keepLimitedFlights();
    }

    public void finalizeMissionFlights() throws PWCGException
    {
        MissionFlightFinalizer flightFinalizer = new MissionFlightFinalizer(campaign, mission);
        flightFinalizer.finalizeMissionFlights();
    }

    public List<Integer> determinePlayerPlaneIds() throws PWCGException
    {
        List<Integer> playerPlaneIds = new ArrayList<>();
        for (IFlight playerFlight : getPlayerFlights())
        {
            for (PlaneMcu playerPlane : playerFlight.getFlightPlanes().getPlayerPlanes())
            {
                playerPlaneIds.add(playerPlane.getLinkTrId());
            }
        }
        return playerPlaneIds;
    }

    public List<IFlight> getAiFlightsForSide(Side side) throws PWCGException
    {
        List<IFlight> aiFlightsForSide = new ArrayList<IFlight>();
        for (IFlight flight : getAiFlights())
        {
            if (flight.getFlightInformation().getCountry().getSide() == side)
            {
                aiFlightsForSide.add(flight);
            }
        }
        return aiFlightsForSide;
    }

    public List<IFlight> getFlightsForSide(Side side)
    {
        List<IFlight> flightsForSide = new ArrayList<IFlight>();
        for (IFlight flight : getAllAerialFlights())
        {
            if (flight.getFlightInformation().getCountry().getSide() == side)
            {
                flightsForSide.add(flight);
            }
        }
        return flightsForSide;
    }

    public List<IFlight> getPlayerFlightsForSide(Side side) throws PWCGException
    {
        List<IFlight> playerFlightsForSide = new ArrayList<IFlight>();
        for (IFlight flight : getPlayerFlights())
        {
            if (flight.getFlightInformation().getCountry().getSide() == side)
            {
                playerFlightsForSide.add(flight);
            }
        }

        return playerFlightsForSide;
    }
    
    public List<IFlight> getNecessaryFlightsByType(NecessaryFlightType necessaryFlightType) throws PWCGException
    {
        List<IFlight> necessaryFlights = new ArrayList<IFlight>();
        for (IFlight flight : this.getAllAerialFlights())
        {
            if (flight.getFlightInformation().getNecessaryFlightType() == necessaryFlightType)
            {
                necessaryFlights.add(flight);
            }
        }

        return necessaryFlights;
    }

    public List<IFlight> getAllAerialFlights()
    {
        Set<Integer> flightIds = new HashSet<>();

        ArrayList<IFlight> allFlights = new ArrayList<IFlight>();
        for (IFlight flight : flights)
        {
            if (flight.getFlightPlanes().getPlanes().size() > 0)
            {
                if (!flightIds.contains(flight.getFlightId()))
                {
                    allFlights.add((IFlight) flight);
                    flightIds.add(flight.getFlightId());
                }
            }
        }
        
        return allFlights;
    }

    public IFlight findOpposingFlight(List<FlightTypes> opposingFlightTypes, Side side) throws PWCGException
    {
        for (FlightTypes opposingFlightType : opposingFlightTypes)
        {
            IFlight opposingFlight = findOpposingFlightOfType(opposingFlightType, side);
            if (opposingFlight != null)
            {
                return opposingFlight;
            }
        }
        
        return null;
    }

    private IFlight findOpposingFlightOfType(FlightTypes opposingFlightType, Side side) throws PWCGException
    {
        for (IFlight opposingFlight: getAiFlightsForSide(side))
        {
            if (opposingFlight.getFlightInformation().isOpposingFlight())
            {
                if (opposingFlight.getFlightInformation().getFlightType() == opposingFlightType)
                {
                    return opposingFlight;
                }
            }
        }
        
        return null;
    }

    public boolean hasPlayerFlightWithFlightTypes(List<FlightTypes> flightTypes)
    {
        for (FlightTypes flightType : flightTypes)
        {
            if (hasPlayerFlightWithFlightType(flightType))
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasPlayerFlightWithFlightType(FlightTypes flightType)
    {
        for (IFlight playerFlight : getPlayerFlights())
        {
            if (playerFlight.getFlightType() == flightType)
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasPlayerFlightForSide(Side side) throws PWCGException
    {
        for (IFlight playerFlight : getPlayerFlights())
        {
            if (playerFlight.getSquadron().determineSide() == side)
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasPlayerFighterFlightType()
    {
        return hasPlayerFlightWithFlightTypes(FlightTypes.getFlightTypesByCategory(FlightTypeCategory.FIGHTER));
    }

    public IFlight getPlayerFlightForSquadron(int squadronId)
    {
        for (IFlight flight : getPlayerFlights())
        {
            if (flight.getSquadron().getCompanyId() == squadronId)
            {
                return flight;
            }
        }
        return null;
    }

    public IFlight getAiFlightForSquadron(int squadronId)
    {
        for (IFlight flight : getAiFlights())
        {
            if (flight.getSquadron().getCompanyId() == squadronId)
            {
                return flight;
            }
        }
        return null;
    }

    public IFlight getPlayerFlight(CrewMember player) throws PWCGException
    {
        for (IFlight flight : getPlayerFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlayerPlanes())
            {
                CrewMember planeCrewMember = plane.getCrewMember();
                if (planeCrewMember.getSerialNumber() == player.getSerialNumber())
                {
                    return flight;
                }
            }
        }
        return null;
    }

    public List<Integer> getPlayersInMission() throws PWCGException
    {
        List<Integer> playersInMission = new ArrayList<>();
        for (IFlight flight : getPlayerFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlayerPlanes())
            {
                if (plane.getCrewMember().isPlayer())
                {
                    playersInMission.add(plane.getLinkTrId());
                }
            }
        }
        return playersInMission;
    }

    public IFlight getReferencePlayerFlight()
    {
        return getPlayerFlights().get(0);
    }

    public List<IFlight> getPlayerFlights()
    {
    	List<IFlight> playerFlights = new ArrayList<>();
    	for (IFlight flight : flights)
    	{
    		if (flight.getFlightInformation().isPlayerFlight())
    		{
    			playerFlights.add(flight);
    		}
    	}
        return playerFlights;
    }

    public List<IFlight> getAiFlights()
    {
        List<IFlight> aiFlights = new ArrayList<>();
        for (IFlight flight : flights)
        {
            if (!flight.getFlightInformation().isPlayerFlight())
            {
            	aiFlights.add(flight);
            }
        }
        return aiFlights;
    }

    public IFlight getFlightForAirfield(Airfield airfield)
    {
        for (IFlight flight : flights)
        {
            Airfield squadronAirfield = flight.getSquadron().determineCurrentAirfieldCurrentMap(campaign.getDate());
            if(squadronAirfield.getName().equals(airfield.getName()))
            {
                return flight;
            }
        }
        return null;
    }

    public List<Coordinate> getTargetCoordinatesForPlayerFlights() throws PWCGException
    {
        List<Coordinate> missionTargetCoordiinates = new ArrayList<>();
        for (IFlight flight : flights)
        {
            if (flight.isPlayerFlight())
            {
                if (FlightTypes.isBombingFlight(flight.getFlightType()))
                {
                    MissionPoint targetArea = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_ATTACK);
                    missionTargetCoordiinates.add(targetArea.getPosition());
                }
            }
        }
        return missionTargetCoordiinates;
    }

    public void removePlayerFlights()
    {
        List<IFlight> aiFlights = getAiFlights();
        flights = aiFlights;
    }
}
