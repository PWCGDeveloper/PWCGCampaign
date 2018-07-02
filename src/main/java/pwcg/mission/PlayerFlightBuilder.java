package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.factory.PWCGFlightFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.factory.FlightFactory;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.ground.unittypes.infantry.GroundPillBoxFlareUnit;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.group.FlareSequence;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;

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
    
    public Flight createPlayerFlight(FlightTypes flightType) throws PWCGException 
    {
        Squadron squad =  campaign.determineSquadron();

        // get plane to allow determination of role
        FlightFactory flightFactory = PWCGFlightFactory.createFlightFactory(campaign);
        if (flightType == FlightTypes.ANY)
        {
            flightType = flightFactory.buildFlight(squad, true);
        }
        
        if (flightType == FlightTypes.FERRY)
        {
            playerFlight = flightFactory.buildFerryFlight(mission, squad, true);
        }
        else
        {
            playerFlight = flightFactory.buildFlight(mission, squad, flightType, true);
        }
        
        triggerLinkedUnitCZFromMyFlight(playerFlight);
        
        validatePlayerFlight();
        
        return playerFlight;
    }

    private void validatePlayerFlight() throws PWCGException
    {
        SquadronMember player = campaign.getPlayer();
                
        boolean playerIsInFlight = false;
        for (PlaneMCU plane : playerFlight.getPlanes())
        {
            if (plane.getPilot().isPlayer())
            {
                playerIsInFlight = true;
                if (plane.getPilot().getSerialNumber() != player.getSerialNumber())
                {
                    throw new PWCGException("Plane serial number does not match player serial number");
                }
                if (!plane.getName().equals(player.getNameAndRank()))
                {
                    throw new PWCGException("Plane name does not match player name");
                }
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
                SelfDeactivatingCheckZone selfDeactivatingCheckZone = mbucz.getCheckZone();
                setCheckZoneForPlayer(selfDeactivatingCheckZone);
            }
            if (mbu instanceof MissionBeginUnitBehavior)
            {
                MissionBeginUnitBehavior mcub = (MissionBeginUnitBehavior) mbu;

                SelfDeactivatingCheckZone spawnCZ = mcub.getSpawnCheckZone();
                setCheckZoneForPlayer(spawnCZ);

                SelfDeactivatingCheckZone behaviorCZ = mcub.getBehaviorCheckZone();
                setCheckZoneForPlayer(behaviorCZ);
            }
            if (unit instanceof GroundPillBoxFlareUnit)
            {
                GroundPillBoxFlareUnit flareUnit = (GroundPillBoxFlareUnit) unit;
                FlareSequence flareSequence = flareUnit.getFlares();
                MissionBeginUnitCheckZone mbucz = flareSequence.getMissionBeginUnit();
                SelfDeactivatingCheckZone cz = mbucz.getCheckZone();
                setCheckZoneForPlayer(cz);
            }

            triggerLinkedUnitCZFromMyFlight(unit);
        }
    }

    private void setCheckZoneForPlayer(SelfDeactivatingCheckZone selfDeactivatingCheckZone) throws PWCGException 
    {
        if (playerFlight != null)
        {
            PlaneMCU playerPlane = playerFlight.getPlayerPlane();
            if (playerPlane != null)
            {
                McuCheckZone checkZone = selfDeactivatingCheckZone.getCheckZone();
                checkZone.setCheckZoneForPlayer(playerFlight.getMission());
            }
        }
    }
}
