package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.PlaneMCU;

public class MissionFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private List<Flight> playerFlights = new ArrayList<>();
    private List<Flight> aiFlights = new ArrayList<Flight>();

    public MissionFlightBuilder(Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }

    public void generateFlights(MissionHumanParticipants participatingPlayers, FlightTypes flightType) throws PWCGException
    {
        createPlayerFlights(participatingPlayers, flightType);
        createAiFlights();
    }

    public void finalizeMissionFlights() throws PWCGException
    {
        MissionFlightFinalizer flightFinalizer = new MissionFlightFinalizer(campaign, mission);
        aiFlights = flightFinalizer.finalizeMissionFlights();
    }

    private void createPlayerFlights(MissionHumanParticipants participatingPlayers, FlightTypes flightType) throws PWCGException
    {
        for (Integer squadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            PlayerFlightBuilder playerFlightBuilder = new PlayerFlightBuilder(campaign, mission);
            Flight playerFlight = playerFlightBuilder.createPlayerFlight(flightType, squadron);
            playerFlights.add(playerFlight);
        }
    }

    public List<Integer> determinePlayerPlaneIds() throws PWCGException
    {
        List<Integer> playerPlaneIds = new ArrayList<>();
        for (Flight playerFlight : playerFlights)
        {
            for (PlaneMCU playerPlane : playerFlight.getPlayerPlanes())
            {
                playerPlaneIds.add(playerPlane.getLinkTrId());
            }
        }
        return playerPlaneIds;
    }

    private void createAiFlights() throws PWCGException
    {
        AiFlightBuilder aiFlightBuilder = new AiFlightBuilder(campaign, mission);
        aiFlights = aiFlightBuilder.createAiFlights();
    }

    public List<Flight> getAllFlightsForSide(Side side) throws PWCGException
    {
        List<Flight> flightsForSide = new ArrayList<Flight>();
        for (Flight flight : this.getAllAerialFlights())
        {
            if (flight.getCountry().getSide() == side)
            {
                flightsForSide.add(flight);
            }
        }
        return flightsForSide;
    }

    public List<Flight> getAiFlightsForSide(Side side) throws PWCGException
    {
        List<Flight> aiFlightsForSide = new ArrayList<Flight>();
        for (Flight flight : aiFlights)
        {
            if (flight.getCountry().getSide() == side)
            {
                aiFlightsForSide.add(flight);
            }
        }
        return aiFlightsForSide;
    }

    public List<Flight> getPlayerFlightsForSide(Side side) throws PWCGException
    {
        List<Flight> aiFlightsForSide = new ArrayList<Flight>();
        for (Flight flight : playerFlights)
        {
            if (flight.getCountry().getSide() == side)
            {
                aiFlightsForSide.add(flight);
            }
        }

        return aiFlightsForSide;
    }

    public List<Flight> getAllAerialFlights()
    {
        ArrayList<Flight> allFlights = new ArrayList<Flight>();
        allFlights.addAll(playerFlights);
        for (Flight playerFlight : playerFlights)
        {
            for (IUnit linkedUnit : playerFlight.getLinkedUnits())
            {
                if (linkedUnit instanceof Flight)
                {
                    allFlights.add((Flight) linkedUnit);
                }
            }
        }

        for (Flight flight : aiFlights)
        {
            if (flight.getPlanes().size() > 0)
            {
                allFlights.add(flight);
            }

            for (IUnit linkedUnit : flight.getLinkedUnits())
            {
                if (linkedUnit instanceof Flight)
                {
                    allFlights.add((Flight) linkedUnit);
                }
            }
        }

        return allFlights;
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
        for (Flight playerFlight : playerFlights)
        {
            if (playerFlight.getFlightType() == flightType)
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

    public Flight getPlayerFlightForSquadron(int squadronId)
    {
        for (Flight flight : playerFlights)
        {
            if (flight.getSquadron().getSquadronId() == squadronId)
            {
                return flight;
            }
        }
        return null;
    }

    public Flight getPlayerFlight(SquadronMember player) throws PWCGException
    {
        for (Flight flight : playerFlights)
        {
            for (PlaneMCU plane : flight.getPlayerPlanes())
            {
                SquadronMember planePilot = plane.getPilot();
                if (planePilot.getSerialNumber() == player.getSerialNumber())
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
        for (Flight flight : playerFlights)
        {
            for (PlaneMCU plane : flight.getPlayerPlanes())
            {
                if (plane.getPilot().isPlayer())
                {
                    playersInMission.add(plane.getLinkTrId());
                }
            }
        }
        return playersInMission;
    }

    public Flight getReferencePlayerFlight()
    {
        return playerFlights.get(0);
    }

    public List<Flight> getPlayerFlights()
    {
        return playerFlights;
    }

    public void addPlayerFlight(Flight playerFlight)
    {
        this.playerFlights.add(playerFlight);
    }

    public List<Flight> getAiFlights()
    {
        return aiFlights;
    }

}
