package pwcg.campaign.personnel;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.campaign.squadmember.SquadronMember;

public class PersonnelFilter 
{
    private boolean invertFilter = false;
    
    public PersonnelFilter (boolean invertFilter)
    {
        this.invertFilter = invertFilter;
    }

    public Map<Integer, SquadronMember> applyPlayerFilter(Map<Integer, SquadronMember> input) 
    {
        Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<>();
        for (SquadronMember pilot : input.values())
        {
            if (!invertFilter)
            {
                if (SerialNumber.getSerialNumberClassification(pilot.getSerialNumber()) == SerialNumberClassification.PLAYER)
                {
                    returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
                }
            }
            else
            {
                if (SerialNumber.getSerialNumberClassification(pilot.getSerialNumber()) != SerialNumberClassification.PLAYER)
                {
                    returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
                }
            }
        }

        return returnSquadronMembers;
    }

    public Map<Integer, SquadronMember> applyAceFilter(Map<Integer, SquadronMember> input) 
    {
        Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<>();
        for (SquadronMember pilot : input.values())
        {
            if (!invertFilter)
            {
                if (SerialNumber.getSerialNumberClassification(pilot.getSerialNumber()) == SerialNumberClassification.ACE)
                {
                    returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
                }
            }
            else
            {
                if (SerialNumber.getSerialNumberClassification(pilot.getSerialNumber()) != SerialNumberClassification.ACE)
                {
                    returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
                }
            }
        }

        return returnSquadronMembers;
    }
    

    public Map<Integer, SquadronMember> applyAIFilter(Map<Integer, SquadronMember> input)
    {
        Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<>();
        for (SquadronMember pilot : input.values())
        {
            if (!invertFilter)
            {
                if (SerialNumber.getSerialNumberClassification(pilot.getSerialNumber()) == SerialNumberClassification.AI)
                {
                    returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
                }
            }
            else
            {
                if (SerialNumber.getSerialNumberClassification(pilot.getSerialNumber()) != SerialNumberClassification.AI)
                {
                    returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
                }
            }
        }

        return returnSquadronMembers;
    }

    
    public Map<Integer, SquadronMember> applySquadronFilter(Map<Integer, SquadronMember> input, int squadronId) 
    {
        Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<>();
        for (SquadronMember pilot : input.values())
        {
            if (!invertFilter)
            {
                if (pilot.getSquadronId() == squadronId)
                {
                    returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
                }
            }
            else
            {
                if (pilot.getSquadronId() != squadronId)
                {
                    returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
                }
            }
        }

        return returnSquadronMembers;
    }
}
