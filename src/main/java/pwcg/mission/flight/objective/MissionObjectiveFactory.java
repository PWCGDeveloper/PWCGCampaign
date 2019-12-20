package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.recon.ReconFlight;
import pwcg.mission.flight.transport.TransportFlight;

public class MissionObjectiveFactory
{
    public static String formMissionObjective(Flight flight) throws PWCGException
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
            return getEscortMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.FERRY)
        {
            return getFerryMissionObjective();
        }
        if (flight.getFlightType() == FlightTypes.BOMB ||
            flight.getFlightType() == FlightTypes.GROUND_ATTACK ||
            flight.getFlightType() == FlightTypes.DIVE_BOMB ||
            flight.getFlightType() == FlightTypes.LOW_ALT_BOMB ||
            flight.getFlightType() == FlightTypes.ANTI_SHIPPING_ATTACK ||
            flight.getFlightType() == FlightTypes.ANTI_SHIPPING_DIVE_BOMB ||
            flight.getFlightType() == FlightTypes.ANTI_SHIPPING_BOMB)
        {
            return GroundAttackObjective.getMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT ||
                flight.getFlightType() == FlightTypes.HOME_DEFENSE)
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
                flight.getFlightType() == FlightTypes.LOW_ALT_CAP ||
                flight.getFlightType() == FlightTypes.LOW_ALT_PATROL)
        {
            return getPatrolMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.RECON)
        {
            return ReconObjective.getMissionObjective((ReconFlight) flight);
        }
        else if (flight.getFlightType() == FlightTypes.SCRAMBLE)
        {
            return getScrambleMissionObjective();
        }
        else if (flight.getFlightType() == FlightTypes.SEA_PATROL)
        {
            return getSeapatrolMissionObjective();
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

    private static String getBalloonBustMissionObjective(Flight flight) throws PWCGException 
    {
        String objective = "Destroy the enemy balloon" + MissionObjective.formMissionObjectiveLocation(flight.getTargetPosition().copy()) + ".";       
        return objective;
    }

    private static String getBalloonDefenseMissionObjective(Flight flight) throws PWCGException 
    {
        String objective = "Defend our balloon" + MissionObjective.formMissionObjectiveLocation(flight.getTargetPosition().copy()) + ".";      

        return objective;
    }

    private static String getContactPatrolMissionObjective(Flight flight) throws PWCGException 
    {
        String objective = "Perform reconnaissance at the specified front location.  " + 
                "Make contact with friendly troop concentrations to establish front lines.";
        
        objective = "Perform reconnaissance" + MissionObjective.formMissionObjectiveLocation(flight.getTargetPosition().copy()) + 
                        ".  Make contact with friendly troop concentrations to establish front lines.";
        
        return objective;
    }

    private static String getEscortMissionObjective(Flight flight) throws PWCGException 
    {
        FlightInformation flightInformation = flight.getFlightInformation();
        String objective = "Escort to the specified location and accompany them until they cross our lines.";
        String objectiveName =  MissionObjective.formMissionObjectiveLocation(flightInformation.getTargetPosition().copy()) + ".";
        if (!objectiveName.isEmpty())
        {
            objective = "Escort to the location" + objectiveName + 
                    ".  Accompany them until they cross our lines.";
        }
        
        return objective;
    }

    private static String getFerryMissionObjective() 
    {
        return "Ferry your aircraft to your new airbase";
    }

    private static String getInterceptMissionObjective(Flight flight) throws PWCGException 
    {
        FlightInformation flightInformation = flight.getFlightInformation();
        String objective = "Intercept enemy aircraft" + MissionObjective.formMissionObjectiveLocation(flightInformation.getTargetPosition().copy()) + ".";      
        
        return objective;
    }

    private static String getLoneWolfMissionObjective() throws PWCGException 
    {
        return "You have chosen to fly lone.  Be careful.";
    }
    
    private static String getCargoDropMissionObjective(Flight flight) throws PWCGException
    {
        FlightInformation flightInformation = flight.getFlightInformation();
        String objective = "Perform a carrgo drop" + MissionObjective.formMissionObjectiveLocation(flightInformation.getTargetPosition().copy()) + ".";     
        return objective;
    }
    
    private static String getParatroopDropMissionObjective(Flight flight) throws PWCGException
    {
        FlightInformation flightInformation = flight.getFlightInformation();
        String objective = "Drop our paratroops" + MissionObjective.formMissionObjectiveLocation(flightInformation.getTargetPosition().copy()) + ".";       
        return objective;
    }

    private static String getPatrolMissionObjective(Flight flight) throws PWCGException
    {
        FlightInformation flightInformation = flight.getFlightInformation();
        String objective = "Patrol aircpace at the specified front location.  " + 
                "Engage any enemy aircraft that you encounter.  ";
        String objectiveName =  MissionObjective.formMissionObjectiveLocation(flightInformation.getTargetPosition().copy()) + "."; 
        if (!objectiveName.isEmpty())
        {
            objective = "Patrol airspace " + objectiveName + 
                    ".  Engage any enemy aircraft that you encounter."; 
        }
        
        return objective;
    }

    private static String getScrambleMissionObjective() 
    {
        return "Incoming enemy aircraft are near our airbase.  Get airborne and destroy them!";
    }

    private static String getSeapatrolMissionObjective() 
    {
        String objective = "Patrol sea lanes on the specified route.  " + 
                        "Engage any enemy shipping that you encounter";

        return objective;
    }

    private static String getSpyExtractMissionObjective(Flight flight) throws PWCGException 
    {
        FlightInformation flightInformation = flight.getFlightInformation();
        String objective = "Extract our spy at the specified location" + 
                MissionObjective.formMissionObjectiveLocation(flightInformation.getTargetPosition().copy()) + "."  + 
                ".  Don't get caught!";       
        
        return objective;
    }
}
