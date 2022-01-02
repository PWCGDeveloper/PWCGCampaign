package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.squadron.Company;
import pwcg.mission.flight.FlightTypes;

public class MissionSquadronFlightTypes
{
    private Map<Integer, MissionSquadronFlightType> flightTypesForSquadrons = new HashMap<>();
    
    public static MissionSquadronFlightTypes buildPlayerFlightType(FlightTypes flightType, Company squadron)
    {
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        playerFlightTypes.add(squadron, flightType);
        return playerFlightTypes;
    }
    
    public void add(Company squadron, FlightTypes flightType)
    {
        MissionSquadronFlightType squadronFlightType = new MissionSquadronFlightType();
        squadronFlightType.squadron = squadron;
        squadronFlightType.flightType = flightType;
        flightTypesForSquadrons.put(squadron.getSquadronId(), squadronFlightType);
    }

    public FlightTypes getFlightTypeForSquadron(int squadronId)
    {
        MissionSquadronFlightType squadronFlightType = flightTypesForSquadrons.get(squadronId);
        if (squadronFlightType != null)
        {
            return squadronFlightType.flightType;
        }
        return FlightTypes.ANY;
    }

    public boolean isPlayerRaidFlight()
    {
        for (MissionSquadronFlightType squadronFlightType : flightTypesForSquadrons.values())
        {
            if (squadronFlightType.flightType == FlightTypes.RAID)
            {
                return true;
            }
        }
        return false;
    }

    public List<FlightTypes> getFlightTypes()
    {
        List<FlightTypes> flightTypes = new ArrayList<>();
        for (MissionSquadronFlightType squadronFlightType : flightTypesForSquadrons.values())
        {
            flightTypes.add(squadronFlightType.flightType);
        }
        return flightTypes;
    }

    public List<Company> getSquadrons()
    {
        List<Company> squadrons = new ArrayList<>();
        for (MissionSquadronFlightType squadronFlightType : flightTypesForSquadrons.values())
        {
            squadrons.add(squadronFlightType.squadron);
        }
        return squadrons;
    }

    public Company getSquadron(int squadronId)
    {
        MissionSquadronFlightType squadronFlightType = flightTypesForSquadrons.get(squadronId);
        if (squadronFlightType != null)
        {
            return squadronFlightType.squadron;
        }
        return null;
    }

    public boolean hasPlayerFlightTypes()
    {
        if (flightTypesForSquadrons.isEmpty())
        {
            return false;
        }
        return true;
    }

    private class MissionSquadronFlightType
    {
        private Company squadron;
        private FlightTypes flightType;
    }
}
