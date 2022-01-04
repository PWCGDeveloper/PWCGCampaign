package pwcg.campaign.promotion;

import java.util.Date;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSServiceManager;

public class PromotionMinimumCriteria
{
    private int crewMemberRankMedMinMissions = 20;
    private int crewMemberRankHighMinMissions = 50;
    private int crewMemberRankExecMinMissions = 80;
    private int crewMemberRankCommandMinMissions = 110;

    private int crewMemberRankMedMinVictories = 1;
    private int crewMemberRankHighMinVictories = 3;
    private int crewMemberRankExecMinVictories = 7;
    private int crewMemberRankCommandMinVictories = 10;

    public void setMinimumPromotionStandards(CrewMember crewMember, Date date) throws PWCGException
    {
        int serviceId = crewMember.determineSquadron().getService();
        PwcgRoleCategory roleCategory = crewMember.determineSquadron().determineSquadronPrimaryRoleCategory(date);

        setMissionsFlownForPromotion(serviceId, roleCategory);
        setVictoriesForPromotion(serviceId, roleCategory);
    }

    private void setMissionsFlownForPromotion(int serviceId, PwcgRoleCategory roleCategory)
    {
        if (serviceId == BoSServiceManager.WEHRMACHT)
        {
            setMissionsForWehrmacht();
        }

        if (serviceId == BoSServiceManager.US_ARMY || serviceId == BoSServiceManager.BRITISH_ARMY)
        {
            setMissionsForUSArmy();
        }
    }

    private void setVictoriesForPromotion(int serviceId, PwcgRoleCategory roleCategory)
    {
        if (serviceId == BoSServiceManager.WEHRMACHT)
        {
            setVictoriesForWehrmacht();
        }
    }

    private void setMissionsForWehrmacht()
    {
        crewMemberRankMedMinMissions = 30;
        crewMemberRankHighMinMissions = 60;
        crewMemberRankExecMinMissions = 110;
        crewMemberRankCommandMinMissions = 150;
    }

    private void setMissionsForUSArmy()
    {
        crewMemberRankMedMinMissions = 20;
        crewMemberRankHighMinMissions = 40;
        crewMemberRankExecMinMissions = 80;
        crewMemberRankCommandMinMissions = 100;
    }

    private void setVictoriesForWehrmacht()
    {

        crewMemberRankMedMinVictories = 1;
        crewMemberRankHighMinVictories = 5;
        crewMemberRankExecMinVictories = 10;
        crewMemberRankCommandMinVictories = 15;
    }

    public int getCrewMemberRankMedMinMissions()
    {
        return crewMemberRankMedMinMissions;
    }

    public int getCrewMemberRankHighMinMissions()
    {
        return crewMemberRankHighMinMissions;
    }

    public int getCrewMemberRankExecMinMissions()
    {
        return crewMemberRankExecMinMissions;
    }

    public int getCrewMemberRankCommandMinMissions()
    {
        return crewMemberRankCommandMinMissions;
    }

    public int getCrewMemberRankMedMinVictories()
    {
        return crewMemberRankMedMinVictories;
    }

    public int getCrewMemberRankHighMinVictories()
    {
        return crewMemberRankHighMinVictories;
    }

    public int getCrewMemberRankExecMinVictories()
    {
        return crewMemberRankExecMinVictories;
    }

    public int getCrewMemberRankCommandMinVictories()
    {
        return crewMemberRankCommandMinVictories;
    }

}
