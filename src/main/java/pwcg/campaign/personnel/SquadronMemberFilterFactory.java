package pwcg.campaign.personnel;

import java.util.Date;

public class SquadronMemberFilterFactory
{

    public static SquadronMemberFilterSpecification buildActiveAIFilter(Date date)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(SquadronMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static SquadronMemberFilterSpecification buildActiveAIAndPlayerFilter(Date date)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(SquadronMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static SquadronMemberFilterSpecification buildActiveAIAndAcesFilter(Date date)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(SquadronMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static SquadronMemberFilterSpecification buildActiveAIAndPlayerAndAcesFilter(Date date)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(SquadronMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }
    
    public static SquadronMemberFilterSpecification buildInactiveAIAndPlayerAndAcesFilter(Date date)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(false);
        filterSpecification.setIncludeInactive(true);
        filterSpecification.setSpecifySquadron(SquadronMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static SquadronMemberFilterSpecification buildActivePlayerFilter(Date date)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(false);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(false);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(SquadronMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static SquadronMemberFilterSpecification buildActiveAIByForSquadronFilter(Date date, int squadronId)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
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

    public static SquadronMemberFilterSpecification buildActiveAIAndPlayerForSquadronFilter(Date date, int squadronId)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
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
    
    public static SquadronMemberFilterSpecification buildActiveAIAndPlayerAndAcesForSquadronFilter(Date date, int squadronId)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
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

    public static SquadronMemberFilterSpecification buildActiveAINoWoundedFilter(Date date)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(false);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(SquadronMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static SquadronMemberFilterSpecification buildActiveAIAndPlayerAndAcesNoWoundedFilter(Date date)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(false);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(SquadronMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

    public static SquadronMemberFilterSpecification buildActiveAIAndAcesNoWoundedFilter(Date date)
    {
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(false);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifySquadron(SquadronMemberFilterSpecification.NO_SQUADRON_FILTER);
        return filterSpecification;
    }

}
