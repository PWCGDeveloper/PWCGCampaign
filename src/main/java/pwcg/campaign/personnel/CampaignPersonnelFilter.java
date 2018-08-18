package pwcg.campaign.personnel;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class CampaignPersonnelFilter
{
    private Map<Integer, SquadronMember> initialSquadronMembers;
    
    public CampaignPersonnelFilter(Map<Integer, SquadronMember> initialSquadronMembers)
    {
        this.initialSquadronMembers = initialSquadronMembers;
    }

    public Map<Integer, SquadronMember> getFilteredSquadronMembers(SquadronMemberFilterSpecification filterSpecification) throws PWCGException
    {        
        Map<Integer, SquadronMember> selectedSquadronMembers = filterForPlayer(filterSpecification, initialSquadronMembers);        
        selectedSquadronMembers = filterForAces(filterSpecification, selectedSquadronMembers);
        selectedSquadronMembers = filterForAI(filterSpecification, selectedSquadronMembers);
        selectedSquadronMembers = filterForActive(filterSpecification, selectedSquadronMembers);        
        selectedSquadronMembers = filterForSquadron(filterSpecification, selectedSquadronMembers);

        return selectedSquadronMembers;
    }

    private Map<Integer, SquadronMember> filterForSquadron(SquadronMemberFilterSpecification filterSpecification, Map<Integer, SquadronMember> inputSquadronMembers)
    {
        Map<Integer, SquadronMember> selectedSquadronMembers = new HashMap<>();
        if (filterSpecification.getSpecifySquadron() > 0)
        {
            PersonnelFilter personnelFilter = new PersonnelFilter(false);
            selectedSquadronMembers = personnelFilter.applySquadronFilter(inputSquadronMembers, filterSpecification.getSpecifySquadron());
        }
        else
        {
            selectedSquadronMembers = inputSquadronMembers;
        }
        
        return selectedSquadronMembers;
    }

    private Map<Integer, SquadronMember> filterForPlayer(SquadronMemberFilterSpecification filterSpecification, Map<Integer, SquadronMember> inputSquadronMembers)
    {
        Map<Integer, SquadronMember> selectedSquadronMembers = new HashMap<>();
        if (!filterSpecification.isIncludePlayer())
        {
            PersonnelFilter personnelFilter = new PersonnelFilter(true);
            selectedSquadronMembers = personnelFilter.applyPlayerFilter(inputSquadronMembers);
        }
        else
        {
            selectedSquadronMembers = inputSquadronMembers;
        }
        
        return selectedSquadronMembers;
    }

    private Map<Integer, SquadronMember> filterForAces(SquadronMemberFilterSpecification filterSpecification, Map<Integer, SquadronMember> inputSquadronMembers) throws PWCGException
    {
        Map<Integer, SquadronMember> selectedSquadronMembers = new HashMap<>();
        if (!filterSpecification.isIncludeAces())
        {
            PersonnelFilter personnelFilter = new PersonnelFilter(true);
            selectedSquadronMembers = personnelFilter.applyAceFilter(inputSquadronMembers);
        }
        else
        {
            selectedSquadronMembers = inputSquadronMembers;
        }

        return selectedSquadronMembers;
    }
    

    private Map<Integer, SquadronMember> filterForAI(SquadronMemberFilterSpecification filterSpecification, Map<Integer, SquadronMember> inputSquadronMembers)
    {
        Map<Integer, SquadronMember> selectedSquadronMembers = new HashMap<>();
        if (!filterSpecification.isIncludeAI())
        {
            PersonnelFilter personnelFilter = new PersonnelFilter(true);
            selectedSquadronMembers = personnelFilter.applyAIFilter(inputSquadronMembers);
        }
        else
        {
            selectedSquadronMembers = inputSquadronMembers;
        }

        return selectedSquadronMembers;
    }


    private Map<Integer, SquadronMember> filterForActive(SquadronMemberFilterSpecification filterSpecification, Map<Integer, SquadronMember> inputSquadronMembers)
    {
        Map<Integer, SquadronMember> selectedSquadronMembers = new HashMap<>();
        if (filterSpecification.isIncludeActive() && !filterSpecification.isIncludeInactive())
        {
            PersonnelActiveFilter personnelFilter = new PersonnelActiveFilter();
            selectedSquadronMembers = personnelFilter.getActive(inputSquadronMembers);
        }
        else if (!filterSpecification.isIncludeActive() && filterSpecification.isIncludeInactive())
        {
            PersonnelActiveFilter personnelFilter = new PersonnelActiveFilter();
            selectedSquadronMembers = personnelFilter.getInactive(inputSquadronMembers);
        }
        else
        {
            selectedSquadronMembers = inputSquadronMembers;
        }
        
        return selectedSquadronMembers;
    }
}
