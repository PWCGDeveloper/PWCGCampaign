package pwcg.campaign.personnel;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class CampaignPersonnelFilter
{
    private Map<Integer, CrewMember> initialCrewMembers;
    
    public CampaignPersonnelFilter(Map<Integer, CrewMember> initialCrewMembers)
    {
        this.initialCrewMembers = initialCrewMembers;
    }

    public Map<Integer, CrewMember> getFilteredCrewMembers(CrewMemberFilterSpecification filterSpecification) throws PWCGException
    {        
        Map<Integer, CrewMember> selectedCrewMembers = filterForPlayer(filterSpecification, initialCrewMembers);        
        selectedCrewMembers = filterForAces(filterSpecification, selectedCrewMembers);
        selectedCrewMembers = filterForAI(filterSpecification, selectedCrewMembers);
        selectedCrewMembers = filterForActive(filterSpecification, selectedCrewMembers);        
        selectedCrewMembers = filterForWounded(filterSpecification, selectedCrewMembers);        
        selectedCrewMembers = filterForSquadron(filterSpecification, selectedCrewMembers);

        return selectedCrewMembers;
    }

    private Map<Integer, CrewMember> filterForWounded(CrewMemberFilterSpecification filterSpecification, Map<Integer, CrewMember> inputCrewMembers)
    {
        Map<Integer, CrewMember> selectedCrewMembers = new HashMap<>();
        if (!filterSpecification.isIncludeWounded())
        {
            PersonnelFilter personnelFilter = new PersonnelFilter(true);
            selectedCrewMembers = personnelFilter.applyWoundedFilter(inputCrewMembers);
        }
        else
        {
            selectedCrewMembers = inputCrewMembers;
        }
        
        return selectedCrewMembers;
    }

    private Map<Integer, CrewMember> filterForSquadron(CrewMemberFilterSpecification filterSpecification, Map<Integer, CrewMember> inputCrewMembers)
    {
        Map<Integer, CrewMember> selectedCrewMembers = new HashMap<>();
        if (filterSpecification.getSpecifySquadron() > 0)
        {
            PersonnelFilter personnelFilter = new PersonnelFilter(false);
            selectedCrewMembers = personnelFilter.applySquadronFilter(inputCrewMembers, filterSpecification.getSpecifySquadron());
        }
        else
        {
            selectedCrewMembers = inputCrewMembers;
        }
        
        return selectedCrewMembers;
    }

    private Map<Integer, CrewMember> filterForPlayer(CrewMemberFilterSpecification filterSpecification, Map<Integer, CrewMember> inputCrewMembers)
    {
        Map<Integer, CrewMember> selectedCrewMembers = new HashMap<>();
        if (!filterSpecification.isIncludePlayer())
        {
            PersonnelFilter personnelFilter = new PersonnelFilter(true);
            selectedCrewMembers = personnelFilter.applyPlayerFilter(inputCrewMembers);
        }
        else
        {
            selectedCrewMembers = inputCrewMembers;
        }
        
        return selectedCrewMembers;
    }

    private Map<Integer, CrewMember> filterForAces(CrewMemberFilterSpecification filterSpecification, Map<Integer, CrewMember> inputCrewMembers) throws PWCGException
    {
        Map<Integer, CrewMember> selectedCrewMembers = new HashMap<>();
        if (!filterSpecification.isIncludeAces())
        {
            PersonnelFilter personnelFilter = new PersonnelFilter(true);
            selectedCrewMembers = personnelFilter.applyAceFilter(inputCrewMembers);
        }
        else
        {
            selectedCrewMembers = inputCrewMembers;
        }

        return selectedCrewMembers;
    }
    

    private Map<Integer, CrewMember> filterForAI(CrewMemberFilterSpecification filterSpecification, Map<Integer, CrewMember> inputCrewMembers)
    {
        Map<Integer, CrewMember> selectedCrewMembers = new HashMap<>();
        if (!filterSpecification.isIncludeAI())
        {
            PersonnelFilter personnelFilter = new PersonnelFilter(true);
            selectedCrewMembers = personnelFilter.applyAIFilter(inputCrewMembers);
        }
        else
        {
            selectedCrewMembers = inputCrewMembers;
        }

        return selectedCrewMembers;
    }


    private Map<Integer, CrewMember> filterForActive(CrewMemberFilterSpecification filterSpecification, Map<Integer, CrewMember> inputCrewMembers)
    {
        Map<Integer, CrewMember> selectedCrewMembers = new HashMap<>();
        if (filterSpecification.isIncludeActive() && !filterSpecification.isIncludeInactive())
        {
            PersonnelActiveFilter personnelFilter = new PersonnelActiveFilter();
            selectedCrewMembers = personnelFilter.getActive(inputCrewMembers);
        }
        else if (!filterSpecification.isIncludeActive() && filterSpecification.isIncludeInactive())
        {
            PersonnelActiveFilter personnelFilter = new PersonnelActiveFilter();
            selectedCrewMembers = personnelFilter.getInactive(inputCrewMembers);
        }
        else
        {
            selectedCrewMembers = inputCrewMembers;
        }
        
        return selectedCrewMembers;
    }
}
