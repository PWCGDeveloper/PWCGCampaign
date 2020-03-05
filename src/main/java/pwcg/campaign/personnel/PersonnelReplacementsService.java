package pwcg.campaign.personnel;

import java.util.Date;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class PersonnelReplacementsService
{
    public static final int NUM_POINTS_FOR_ONE_PILOT = 10;

    private Integer serviceId;
    private SquadronMembers replacements = new SquadronMembers();
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

    public SquadronMembers getReplacements()
    {
        return replacements;
    }

    public void setReplacements(SquadronMembers replacements)
    {
        this.replacements = replacements;
    }

    public boolean hasReplacements() throws PWCGException
    {
        if (replacements.getSquadronMemberCollection().isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void addReplacement(SquadronMember replacement) throws PWCGException
    {
        replacements.addToSquadronMemberCollection(replacement);
    }

    public SquadronMember findReplacement() throws PWCGException
    {
        SquadronMember replacement = replacements.findSquadronMember();
        return replacement;
    }

    public SquadronMember removeReplacement(int serialNumber) throws PWCGException
    {
        SquadronMember replacement = replacements.removeSquadronMember(serialNumber);
        return replacement;
    }

    public SquadronMember getAvailableReplacement(int serialNumber) throws PWCGException
    {
        SquadronMember replacement = replacements.getSquadronMember(serialNumber);
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
        int numPilotsAdded = getNumReplacements();
        replacementPoints -= (NUM_POINTS_FOR_ONE_PILOT * numPilotsAdded);
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
