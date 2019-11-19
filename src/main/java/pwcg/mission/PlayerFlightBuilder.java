package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.PWCGFlightFactoryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.factory.FlightFactory;
import pwcg.mission.flight.factory.IFlightTypeFactory;
import pwcg.mission.flight.factory.NightFlightTypeConverter;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunFlareUnit;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.group.FlareSequence;

public class PlayerFlightBuilder
{
    private Campaign campaign;
    private Mission mission;

    private Flight playerFlight;
 
    public PlayerFlightBuilder(Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public Flight createPlayerFlight(FlightTypes requestedFlightType, Squadron squadron) throws PWCGException 
    {
        FlightTypes flightType = finalizeFlightType(requestedFlightType, squadron);        
        buildFlight(flightType, squadron);
        return playerFlight;
    }

    private void buildFlight(FlightTypes flightType, Squadron squadron) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        boolean isPlayerFlight = true;
        playerFlight = flightFactory.buildFlight(mission, squadron, flightType, isPlayerFlight);        
        triggerLinkedUnitCZFromMyFlight(playerFlight);
        validatePlayerFlight();
    }

    private FlightTypes finalizeFlightType(FlightTypes flightType, Squadron squadron) throws PWCGException
    {
        IFlightTypeFactory flightTypeFactory = PWCGFlightFactoryFactory.createFlightFactory(campaign);
        if (flightType == FlightTypes.ANY)
        {
            flightType = getSpecialFlightType(mission.getParticipatingPlayers());
            if (flightType == FlightTypes.ANY)
            {
                boolean isPlayerFlight = true;
                flightType = flightTypeFactory.getFlightType(squadron, isPlayerFlight);
            }
        }
        
        flightType = NightFlightTypeConverter.getFlightType(mission, flightType);

        return flightType;
    }

    private void validatePlayerFlight() throws PWCGException
    {
        boolean playerIsInFlight = false;
        for (PlaneMCU plane : playerFlight.getPlanes())
        {
            if (plane.getPilot().isPlayer())
            {
                playerIsInFlight = true;
            }
        }
        
        if (!playerIsInFlight)
        {
            throw new PWCGException("No plane assigned to player");
        }
    }

    private void triggerLinkedUnitCZFromMyFlight(Unit parent) throws PWCGException 
    {
        for (Unit unit : parent.linkedUnits)
        {
            MissionBeginUnit mbu = unit.getMissionBeginUnit();
            if (mbu instanceof MissionBeginUnitCheckZone)
            {
                MissionBeginUnitCheckZone mbucz = (MissionBeginUnitCheckZone) mbu;
                McuCheckZone checkZone = mbucz.getSelfDeactivatingCheckZone().getCheckZone();
                checkZone.triggerCheckZoneByFlight(playerFlight);
            }
            
            if (unit instanceof GroundMachineGunFlareUnit)
            {
                GroundMachineGunFlareUnit flareUnit = (GroundMachineGunFlareUnit) unit;
                FlareSequence flareSequence = flareUnit.getFlares();
                MissionBeginUnitCheckZone mbucz = flareSequence.getMissionBeginUnit();
                McuCheckZone checkZone = mbucz.getSelfDeactivatingCheckZone().getCheckZone();
                checkZone.triggerCheckZoneByFlight(playerFlight);
            }

            triggerLinkedUnitCZFromMyFlight(unit);
        }
    }
        
    private FlightTypes getSpecialFlightType(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        if (!(campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COMPETITIVE))
        {
            List<Integer> playerSquadronsInMission = participatingPlayers.getParticipatingSquadronIds();
            if (playerSquadronsInMission.size() == 1)
            {
                Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronsInMission.get(0));
                IFlightTypeFactory flightTypeFactory = PWCGFlightFactoryFactory.createSpecialFlightFactory(campaign);
                boolean isPlayerFlight = true;
                FlightTypes playerFlightType = flightTypeFactory.getFlightType(squadron, isPlayerFlight);
                return playerFlightType;
            }
        }
        
        return FlightTypes.ANY;
    }


}
