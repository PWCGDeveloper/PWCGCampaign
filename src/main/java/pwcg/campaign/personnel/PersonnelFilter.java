package pwcg.campaign.personnel;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;

public class PersonnelFilter 
{
    private boolean invertFilter = false;
    
    public PersonnelFilter (boolean invertFilter)
    {
        this.invertFilter = invertFilter;
    }

    public Map<Integer, CrewMember> applyPlayerFilter(Map<Integer, CrewMember> input) 
    {
        Map<Integer, CrewMember> returnCrewMembers = new HashMap<>();
        for (CrewMember crewMember : input.values())
        {
            if (!invertFilter)
            {
                if (SerialNumber.getSerialNumberClassification(crewMember.getSerialNumber()) == SerialNumberClassification.PLAYER)
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
            else
            {
                if (SerialNumber.getSerialNumberClassification(crewMember.getSerialNumber()) != SerialNumberClassification.PLAYER)
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
        }

        return returnCrewMembers;
    }

    public Map<Integer, CrewMember> applyAceFilter(Map<Integer, CrewMember> input) 
    {
        Map<Integer, CrewMember> returnCrewMembers = new HashMap<>();
        for (CrewMember crewMember : input.values())
        {
            if (!invertFilter)
            {
                if (SerialNumber.getSerialNumberClassification(crewMember.getSerialNumber()) == SerialNumberClassification.ACE)
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
            else
            {
                if (SerialNumber.getSerialNumberClassification(crewMember.getSerialNumber()) != SerialNumberClassification.ACE)
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
        }

        return returnCrewMembers;
    }   

    public Map<Integer, CrewMember> applyAIFilter(Map<Integer, CrewMember> input)
    {
        Map<Integer, CrewMember> returnCrewMembers = new HashMap<>();
        for (CrewMember crewMember : input.values())
        {
            if (!invertFilter)
            {
                if (SerialNumber.getSerialNumberClassification(crewMember.getSerialNumber()) == SerialNumberClassification.AI)
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
            else
            {
                if (SerialNumber.getSerialNumberClassification(crewMember.getSerialNumber()) != SerialNumberClassification.AI)
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
        }

        return returnCrewMembers;
    }

    public Map<Integer, CrewMember> applyWoundedFilter(Map<Integer, CrewMember> input)
    {
        Map<Integer, CrewMember> returnCrewMembers = new HashMap<>();
        for (CrewMember crewMember : input.values())
        {
            if (!invertFilter)
            {
                if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_WOUNDED)
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
            else
            {
                if (!(crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_WOUNDED))
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
        }

        return returnCrewMembers;
    }

    
    public Map<Integer, CrewMember> applySquadronFilter(Map<Integer, CrewMember> input, int squadronId) 
    {
        Map<Integer, CrewMember> returnCrewMembers = new HashMap<>();
        for (CrewMember crewMember : input.values())
        {
            if (!invertFilter)
            {
                if (crewMember.getCompanyId() == squadronId)
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
            else
            {
                if (crewMember.getCompanyId() != squadronId)
                {
                    returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
                }
            }
        }

        return returnCrewMembers;
    }
}
