package pwcg.campaign.personnel;

import java.util.Date;

public class CrewMemberFilterFactory
{

    public static CrewMemberFilterSpecification buildActiveAIFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(CrewMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndPlayerFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(CrewMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndAcesFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(CrewMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndPlayerAndAcesFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(CrewMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }
    
    public static CrewMemberFilterSpecification buildInactiveAIAndPlayerAndAcesFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(false);
        filterSpecification.setIncludeInactive(true);
        filterSpecification.setSpecifySquadron(CrewMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActivePlayerFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(false);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(false);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(CrewMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIByForSquadronFilter(Date date, int squadronId)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(squadronId);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndPlayerForSquadronFilter(Date date, int squadronId)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(squadronId);
        return filterSpecification;
    }
    
    public static CrewMemberFilterSpecification buildActiveAIAndPlayerAndAcesForSquadronFilter(Date date, int squadronId)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(squadronId);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAINoWoundedFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(false);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(CrewMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndPlayerAndAcesNoWoundedFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(false);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(CrewMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndAcesNoWoundedFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(false);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(CrewMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

}
