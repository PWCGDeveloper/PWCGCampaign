package pwcg.mission.flight.objective;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.EscortedByPlayerFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.recon.ReconFlight;
import pwcg.mission.flight.transport.TransportFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;

public class MissionObjectiveFactory
{
    public static String formMissionObjective(IFlight flight, Date date) throws PWCGException
    {
        if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            return GroundAttackObjective.getMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.BALLOON_BUST)
        {
            return getBalloonBustMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            return getBalloonDefenseMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.CARGO_DROP)
        {
            return getCargoDropMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.CONTACT_PATROL)
        {
            return getContactPatrolMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.ESCORT)
        {
            return getEscortMissionObjective(flight, date);
        }

        if (flight.getFlightType() == FlightTypes.BOMB ||
            flight.getFlightType() == FlightTypes.GROUND_ATTACK ||
            flight.getFlightType() == FlightTypes.DIVE_BOMB ||
            flight.getFlightType() == FlightTypes.LOW_ALT_BOMB)
        {
            return GroundAttackObjective.getMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT ||
                flight.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT)
        {
            return getInterceptMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.LONE_WOLF)
        {
            return getLoneWolfMissionObjective();
        }
        else if (flight.getFlightType() == FlightTypes.OFFENSIVE)
        {
            return OffensivePatrolObjective.getMissionObjective((OffensiveFlight) flight);
        }
        else if (flight.getFlightType() == FlightTypes.PARATROOP_DROP)
        {
            return getParatroopDropMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.PATROL ||
                flight.getFlightType() == FlightTypes.LOW_ALT_PATROL)
        {
            return getPatrolMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            return getCAPMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.RECON)
        {
            return ReconObjective.getMissionObjective((ReconFlight) flight);
        }
        else if (flight.getFlightType() == FlightTypes.SCRAMBLE)
        {
            return getScrambleMissionObjective();
        }
        else if (flight.getFlightType() == FlightTypes.SPY_EXTRACT)
        {
            return getSpyExtractMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.STRATEGIC_BOMB)
        {
            return StrategicBombObjective.getMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.TRANSPORT)
        {
            return TransportObjective.getMissionObjective((TransportFlight) flight);
        }

        return "Do something useful for God and Country!";
    }

    private static String getBalloonBustMissionObjective(IFlight flight) throws PWCGException 
    {
        String objective = "Destroy the enemy balloon" + MissionObjective.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + ".";       
        return objective;
    }

    private static String getBalloonDefenseMissionObjective(IFlight flight) throws PWCGException 
    {
        String objective = "Defend our balloon" + MissionObjective.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + ".";      

        return objective;
    }

    private static String getContactPatrolMissionObjective(IFlight flight) throws PWCGException 
    {
        String objective = "Perform reconnaissance at the specified front location.  " + 
                "Make contact with friendly troop concentrations to establish front lines.";
        
        objective = "Perform reconnaissance" + MissionObjective.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + 
                        ".  Make contact with friendly troop concentrations to establish front lines.";
        
        return objective;
    }

    private static String getEscortMissionObjective(IFlight flight, Date date) throws PWCGException 
    {
        EscortedByPlayerFlight escortedByPlayerFlight = flight.getLinkedFlights().getEscortedByPlayer();
        MissionPoint rendezvousPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        String rendezvousName =  MissionObjective.formMissionObjectiveLocation(rendezvousPoint.getPosition());
        String objectiveName =  MissionObjective.formMissionObjectiveLocation(escortedByPlayerFlight.getTargetDefinition().getPosition().copy());

        String objective = "Rendezvous with " + escortedByPlayerFlight.getFlightPlanes().getFlightLeader().getDisplayName() + "s of " + escortedByPlayerFlight.getSquadron().determineDisplayName(date);
        if (!rendezvousName.isEmpty())
        {
            objective += rendezvousName;
        }
        objective += ". Escort them to ";
        if (!objectiveName.isEmpty())
        {
            objective += "the location" + objectiveName + ".";
        } else {
            objective += "the specified location.";
        }
        objective += " Accompany them until they cross our lines.";

        return objective;
    }

    private static String getInterceptMissionObjective(IFlight flight) throws PWCGException 
    {
        String objective = "Intercept enemy aircraft" + MissionObjective.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + ".";      
        
        return objective;
    }

    private static String getLoneWolfMissionObjective() throws PWCGException 
    {
        return "You have chosen to fly lone.  Be careful.";
    }
    
    private static String getCargoDropMissionObjective(IFlight flight) throws PWCGException
    {
        String objective = "Perform a cargo drop" + MissionObjective.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + ".";     
        return objective;
    }
    
    private static String getParatroopDropMissionObjective(IFlight flight) throws PWCGException
    {
        String objective = "Drop our paratroops" + MissionObjective.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + ".";       
        return objective;
    }

    private static String getPatrolMissionObjective(IFlight flight) throws PWCGException
    {
        String objective = "Patrol aircpace at the specified front location.  " + 
                "Engage any enemy aircraft that you encounter.  ";
        String objectiveName =  MissionObjective.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + "."; 
        if (!objectiveName.isEmpty())
        {
            objective = "Patrol airspace " + objectiveName + 
                    ".  Engage any enemy aircraft that you encounter."; 
        }
        
        return objective;
    }

    private static String getCAPMissionObjective(IFlight flight) throws PWCGException
    {
        String objective = "Close Air Patrol over our troops.  " + 
                "Engage any enemy aircraft that you encounter as a first priority.  " +
                "Attack targets of opportuity on the ground.  ";
        String objectiveName =  MissionObjective.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + "."; 
        if (!objectiveName.isEmpty())
        {
            objective = "Close Air Patrol over our troops " + objectiveName +
                    "  Engage any enemy aircraft that you encounter as a first priority.  " +
                    "Attack targets of opportuity on the ground.  ";
        }
        
        return objective;
    }

    private static String getScrambleMissionObjective() 
    {
        return "Incoming enemy aircraft are near our airbase.  Get airborne and destroy them!";
    }

    private static String getSpyExtractMissionObjective(IFlight flight) throws PWCGException 
    {
        String objective = "Extract our spy at the specified location" + 
                MissionObjective.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + "."  + 
                ".  Don't get caught!";       
        
        return objective;
    }
}
