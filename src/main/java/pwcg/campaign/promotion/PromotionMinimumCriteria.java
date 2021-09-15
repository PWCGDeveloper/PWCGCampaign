package pwcg.campaign.promotion;

import java.util.Date;

import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSServiceManager;

public class PromotionMinimumCriteria
{
    private int pilotRankMedMinMissions = 20;
    private int pilotRankHighMinMissions = 50;
    private int pilotRankExecMinMissions = 80;
    private int pilotRankCommandMinMissions = 110;

    private int pilotRankMedMinVictories = 1;
    private int pilotRankHighMinVictories = 3;
    private int pilotRankExecMinVictories = 7;
    private int pilotRankCommandMinVictories = 15;

    public void setMinimumPromotionStandards(SquadronMember squadronMember, Date date) throws PWCGException
    {
        int serviceId = squadronMember.determineSquadron().getService();
        PwcgRoleCategory roleCategory = squadronMember.determineSquadron().determineSquadronPrimaryRoleCategory(date);

        setMissionsFlownForPromotion(serviceId, roleCategory);
        setVictoriesForPromotion(serviceId, roleCategory);
    }

    private void setMissionsFlownForPromotion(int serviceId, PwcgRoleCategory roleCategory)
    {
        if (serviceId == BoSServiceManager.LUFTWAFFE)
        {
            setMissionsForLuftwaffe();
        }

        if (serviceId == BoSServiceManager.USAAF)
        {
            setMissionsForUSAAF();
        }

        if (roleCategory == PwcgRoleCategory.RECON)
        {
            setMissionsForRecon();
        }
    }

    private void setVictoriesForPromotion(int serviceId, PwcgRoleCategory roleCategory)
    {
        if (roleCategory == PwcgRoleCategory.RECON)
        {
            setVictoriesForRecon();
        }
        else if (roleCategory != PwcgRoleCategory.FIGHTER)
        {
            setVictoriesForBomber();
        }
        else if (roleCategory == PwcgRoleCategory.FIGHTER && serviceId == BoSServiceManager.LUFTWAFFE)
        {
            setVictoriesForLuftwaffeFighter();
        }
    }

    private void setMissionsForRecon()
    {
        pilotRankMedMinMissions = 30;
        pilotRankHighMinMissions = 60;
        pilotRankExecMinMissions = 110;
        pilotRankCommandMinMissions = 150;
    }

    private void setMissionsForLuftwaffe()
    {
        pilotRankMedMinMissions = 30;
        pilotRankHighMinMissions = 60;
        pilotRankExecMinMissions = 110;
        pilotRankCommandMinMissions = 150;
    }

    private void setMissionsForUSAAF()
    {
        pilotRankMedMinMissions = 20;
        pilotRankHighMinMissions = 40;
        pilotRankExecMinMissions = 80;
        pilotRankCommandMinMissions = 100;
    }

    private void setVictoriesForBomber()
    {
        pilotRankMedMinVictories = 5;
        pilotRankHighMinVictories = 15;
        pilotRankExecMinVictories = 30;
        pilotRankCommandMinVictories = 50;
    }

    private void setVictoriesForRecon()
    {
        pilotRankMedMinVictories = 0;
        pilotRankHighMinVictories = 0;
        pilotRankExecMinVictories = 0;
        pilotRankCommandMinVictories = 0;
    }

    private void setVictoriesForLuftwaffeFighter()
    {
        pilotRankMedMinVictories = 5;
        pilotRankHighMinVictories = 15;
        pilotRankExecMinVictories = 30;
        pilotRankCommandMinVictories = 50;
    }

    public int getPilotRankMedMinMissions()
    {
        return pilotRankMedMinMissions;
    }

    public int getPilotRankHighMinMissions()
    {
        return pilotRankHighMinMissions;
    }

    public int getPilotRankExecMinMissions()
    {
        return pilotRankExecMinMissions;
    }

    public int getPilotRankCommandMinMissions()
    {
        return pilotRankCommandMinMissions;
    }

    public int getPilotRankMedMinVictories()
    {
        return pilotRankMedMinVictories;
    }

    public int getPilotRankHighMinVictories()
    {
        return pilotRankHighMinVictories;
    }

    public int getPilotRankExecMinVictories()
    {
        return pilotRankExecMinVictories;
    }

    public int getPilotRankCommandMinVictories()
    {
        return pilotRankCommandMinVictories;
    }

}
