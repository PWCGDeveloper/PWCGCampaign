package pwcg.campaign.personnel;

import java.util.Date;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;

public class PersonnelReplacementsService
{
    public static final int NUM_POINTS_FOR_ONE_PILOT = 10;

    private Integer serviceId;
    private CrewMembers replacements = new CrewMembers();
    private CrewMembers replacementsDesignatedForAssignment = new CrewMembers();
    private Date lastReplacementDate;
    private int dailyReplacementRate = NUM_POINTS_FOR_ONE_PILOT;
    private int replacementPoints = 0;

    public PersonnelReplacementsService()
    {
    }

    public Integer getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }

    public CrewMembers getReplacements()
    {
        return replacements;
    }

    public void setReplacements(CrewMembers replacements)
    {
        this.replacements = replacements;
    }

    public boolean hasReplacements() throws PWCGException
    {
        if (replacements.getCrewMemberCollection().isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void addReplacement(CrewMember replacement) throws PWCGException
    {
        replacements.addToCrewMemberCollection(replacement);
    }

    public CrewMember findReplacement() throws PWCGException
    {
        CrewMember replacement = replacements.findCrewMember();
        if (replacement != null)
        {
            replacements.removeCrewMember(replacement.getSerialNumber());
            replacementsDesignatedForAssignment.addToCrewMemberCollection(replacement);
        }
        return replacement;
    }

    public CrewMember transferFromReservesToActive(int serialNumber) throws PWCGException
    {
        CrewMember replacement = replacementsDesignatedForAssignment.removeCrewMember(serialNumber);
        return replacement;
    }

    public CrewMember getReplacement(int serialNumber) throws PWCGException
    {
        CrewMember replacement = replacements.getCrewMember(serialNumber);
        if (replacement == null)
        {
            replacement = replacementsDesignatedForAssignment.getCrewMember(serialNumber);
        }
        return replacement;
    }

    public Date getLastReplacementDate()
    {
        return lastReplacementDate;
    }

    public void setLastReplacementDate(Date lastReplacementDate)
    {
        this.lastReplacementDate = lastReplacementDate;
    }

    public int getDailyReplacementRate()
    {
        return dailyReplacementRate;
    }

    public void setDailyReplacementRate(int dailyReplacementRate)
    {
        this.dailyReplacementRate = dailyReplacementRate;
    }

    public int getNumReplacements()
    {
        return replacementPoints / NUM_POINTS_FOR_ONE_PILOT;
    }

    public void addReplacementPoints(int daysSinceLastUpdate)
    {
        replacementPoints += (dailyReplacementRate * daysSinceLastUpdate);
    }

    public void removeReplacementPoints()
    {
        int numCrewMembersAdded = getNumReplacements();
        replacementPoints -= (NUM_POINTS_FOR_ONE_PILOT * numCrewMembersAdded);
    }

    public int getReplacementPoints()
    {
        return replacementPoints;
    }

    public void setReplacementPoints(int replacementPoints)
    {
        this.replacementPoints = replacementPoints;
    }

}
