package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.Side;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.EscortForPlayerFlightBuilder;
import pwcg.mission.flight.escort.NeedsEscortDecider;
import pwcg.mission.flight.opposing.AiOpposingFlightBuilder;
import pwcg.mission.flight.plane.PlaneMcu;

public class MissionFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private List<IFlight> playerFlights = new ArrayList<>();
    private List<IFlight> aiFlights = new ArrayList<IFlight>();

    public MissionFlightBuilder(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public void generateFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        createOpposingAiFlights(playerFlightTypes);
        createAiFlights(playerFlightTypes);
        createPlayerFlights(playerFlightTypes);
        keepAiFlights();
    }

    public void finalizeMissionFlights() throws PWCGException
    {
        MissionFlightFinalizer flightFinalizer = new MissionFlightFinalizer(campaign, mission);
        flightFinalizer.finalizeMissionFlights();
    }

    private void createPlayerFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        for (Squadron playerSquadron : playerFlightTypes.getSquadrons())
        {
            FlightTypes playerFlightType = playerFlightTypes.getFlightTypeForSquadron(playerSquadron.getSquadronId());
            
            PlayerFlightBuilder playerFlightBuilder = new PlayerFlightBuilder(campaign, mission);
            IFlight playerFlight = playerFlightBuilder.createPlayerFlight(playerFlightType, playerSquadron, mission.getParticipatingPlayers(), mission.isNightMission());
            playerFlights.add(playerFlight);
            
            if (NeedsEscortDecider.playerNeedsEscort(playerFlight))
            {
                EscortForPlayerFlightBuilder escortFlightBuilder = new EscortForPlayerFlightBuilder();
                escortFlightBuilder.addEscort(mission, playerFlight);
            }
            
            mission.getMissionSquadronChooser().registerSquadronInUse(playerSquadron);
        }
    }

    private void createOpposingAiFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        AiOpposingFlightBuilder aiOpposingFlightBuilder = new AiOpposingFlightBuilder(mission, playerFlightTypes);
        List<IFlight> opposingFlights = aiOpposingFlightBuilder.createOpposingAiFlights();
        aiFlights.addAll(opposingFlights);
    }

    private void createAiFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        if (isCreateAiFlights(playerFlightTypes))
        {
            AiFlightBuilder aiFlightBuilder = new AiFlightBuilder(campaign, mission);
            List<IFlight> otherAiFlights = aiFlightBuilder.createAiFlights(mission.getWeather());
            aiFlights.addAll(otherAiFlights);
        }
    }

    private void keepAiFlights() throws PWCGException
    {
        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(campaign, mission);
        List<IFlight> finalizedMissionFlights = missionFlightKeeper.keepLimitedFlights();
        aiFlights = finalizedMissionFlights;
    }

    private boolean isCreateAiFlights(MissionSquadronFlightTypes playerFlightTypes)
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            for (FlightTypes playerFlightType : playerFlightTypes.getFlightTypes())
            {
                if (playerFlightType == FlightTypes.STRATEGIC_INTERCEPT)
                {
                    return false;
                }
            }
        }
        return true;
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
        for (IFlight playerFlight : playerFlights)
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
        for (IFlight playerFlight : playerFlights)
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

    public IFlight getFlightForAirfield(Airfield airfield)
    {
        for (IFlight flight : this.getAllAerialFlights())
        {
            Airfield squadronAirfield = flight.getSquadron().determineCurrentAirfieldCurrentMap(campaign.getDate());
            if(squadronAirfield.getName().equals(airfield.getName()))
            {
                return flight;
            }
        }
        return null;
    }

}
