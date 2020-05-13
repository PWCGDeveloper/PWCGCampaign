package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class MissionFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private List<IFlight> playerFlights = new ArrayList<>();
    private List<IFlight> aiFlights = new ArrayList<IFlight>();

    public MissionFlightBuilder(Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }

    public void generateFlights(MissionHumanParticipants participatingPlayers, List<FlightTypes> playerFlightTypes) throws PWCGException
    {
        createPlayerFlights(participatingPlayers, playerFlightTypes);
        createAiFlights();
        keepAiFlights();
    }

    private void keepAiFlights() throws PWCGException
    {
        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(campaign, mission);
        List<IFlight> finalizedMissionFlights = missionFlightKeeper.keepLimitedFlights();
        aiFlights = finalizedMissionFlights;
    }

    public void finalizeMissionFlights() throws PWCGException
    {
        MissionFlightFinalizer flightFinalizer = new MissionFlightFinalizer(campaign, mission);
        flightFinalizer.finalizeMissionFlights();
    }

    private void createPlayerFlights(MissionHumanParticipants participatingPlayers, List<FlightTypes> playerFlightTypes) throws PWCGException
    {
        int index = 0;
        for (Integer squadronId : participatingPlayers.getParticipatingSquadronIds())
        {
            FlightTypes playerFlightType = playerFlightTypes.get(index);
            ++index;
            
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            PlayerFlightBuilder playerFlightBuilder = new PlayerFlightBuilder(campaign, mission);
            IFlight playerFlight = playerFlightBuilder.createPlayerFlight(playerFlightType, squadron, participatingPlayers, mission.isNightMission());
            playerFlights.add(playerFlight);
        }
    }

    public List<Integer> determinePlayerPlaneIds() throws PWCGException
    {
        List<Integer> playerPlaneIds = new ArrayList<>();
        for (IFlight playerFlight : playerFlights)
        {
            for (PlaneMcu playerPlane : playerFlight.getFlightPlanes().getPlayerPlanes())
            {
                playerPlaneIds.add(playerPlane.getLinkTrId());
            }
        }
        return playerPlaneIds;
    }

    private void createAiFlights() throws PWCGException
    {
        if (isCreateAiFlights())
        {
            AiFlightBuilder aiFlightBuilder = new AiFlightBuilder(campaign, mission);
            aiFlights = aiFlightBuilder.createAiFlights();
        }
    }

    private boolean isCreateAiFlights()
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            if (playerFlights.get(0).getFlightInformation().getFlightType() == FlightTypes.STRATEGIC_INTERCEPT)
            {
                return false;
            }
        }
        return true;
    }
    
    public List<IFlight> getAllFlightsForSide(Side side) throws PWCGException
    {
        List<IFlight> flightsForSide = new ArrayList<IFlight>();
        for (IFlight flight : this.getAllAerialFlights())
        {
            if (flight.getFlightInformation().getCountry().getSide() == side)
            {
                flightsForSide.add(flight);
            }
        }
        return flightsForSide;
    }

    public List<IFlight> getAiFlightsForSide(Side side) throws PWCGException
    {
        List<IFlight> aiFlightsForSide = new ArrayList<IFlight>();
        for (IFlight flight : aiFlights)
        {
            if (flight.getFlightInformation().getCountry().getSide() == side)
            {
                aiFlightsForSide.add(flight);
            }
        }
        return aiFlightsForSide;
    }

    public List<IFlight> getPlayerFlightsForSide(Side side) throws PWCGException
    {
        List<IFlight> aiFlightsForSide = new ArrayList<IFlight>();
        for (IFlight flight : playerFlights)
        {
            if (flight.getFlightInformation().getCountry().getSide() == side)
            {
                aiFlightsForSide.add(flight);
            }
        }

        return aiFlightsForSide;
    }

    public List<IFlight> getAllAerialFlights()
    {
        ArrayList<IFlight> allFlights = new ArrayList<IFlight>();
        allFlights.addAll(playerFlights);
        for (IFlight playerFlight : playerFlights)
        {
            for (IFlight linkedFlight : playerFlight.getLinkedFlights().getLinkedFlights())
            {
                allFlights.add((IFlight) linkedFlight);
            }
        }

        for (IFlight flight : aiFlights)
        {
            if (flight.getFlightPlanes().getPlanes().size() > 0)
            {
                allFlights.add(flight);
            }

            for (IFlight linkedFlight : flight.getLinkedFlights().getLinkedFlights())
            {
                    allFlights.add((IFlight) linkedFlight);
            }
        }

        return allFlights;
    }


    public List<IGroundUnitCollection> getAllFlightLinkedGroundUnits()
    {
        List<IGroundUnitCollection> allAmbientGroundUnits = new ArrayList<>();
        for (IFlight flight : getAllAerialFlights())
        {
            allAmbientGroundUnits.addAll(flight.getLinkedGroundUnits().getLinkedGroundUnits());
        }

        return allAmbientGroundUnits;
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
        for (IFlight playerFlight : playerFlights)
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

    public IFlight getPlayerFlightForSquadron(int squadronId)
    {
        for (IFlight flight : playerFlights)
        {
            if (flight.getSquadron().getSquadronId() == squadronId)
            {
                return flight;
            }
        }
        return null;
    }

    public IFlight getAiFlightForSquadron(int squadronId)
    {
        for (IFlight flight : aiFlights)
        {
            if (flight.getSquadron().getSquadronId() == squadronId)
            {
                return flight;
            }
        }
        return null;
    }

    public IFlight getPlayerFlight(SquadronMember player) throws PWCGException
    {
        for (IFlight flight : playerFlights)
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlayerPlanes())
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
        for (IFlight flight : playerFlights)
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlayerPlanes())
            {
                if (plane.getPilot().isPlayer())
                {
                    playersInMission.add(plane.getLinkTrId());
                }
            }
        }
        return playersInMission;
    }

    public IFlight getReferencePlayerFlight()
    {
        return playerFlights.get(0);
    }

    public List<IFlight> getPlayerFlights()
    {
        return playerFlights;
    }

    public void addPlayerFlight(IFlight playerFlight)
    {
        this.playerFlights.add(playerFlight);
    }

    public List<IFlight> getAiFlights()
    {
        return aiFlights;
    }

    public IFlight getFlightForAirfield(IAirfield airfield)
    {
        for (IFlight flight : this.getAllAerialFlights())
        {
            IAirfield squadronAirfield = flight.getSquadron().determineCurrentAirfieldCurrentMap(campaign.getDate());
            if(squadronAirfield.getName().equals(airfield.getName()))
            {
                return flight;
            }
        }
        return null;
    }

}
