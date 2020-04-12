package pwcg.gui.maingui.campaigngenerate;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

public class NewPilotGeneratorDO
{
    private ArmedService service = null;
    private String playerPilotName = "";
    private String coopUser = "";
    private String region = "";
    private String rankName = null;
    private String squadName = "";
    private Role role = Role.ROLE_FIGHTER;

    public boolean isDataSetValid()
    {

        if (playerPilotName.isEmpty())
        {
            return false;
        }
        
        if (rankName.isEmpty())
        {
            return false;
        }
        
        if (squadName.isEmpty())
        {
            return false;
        }
        
        if (service == null)
        {
            return false;
        }

        return true;
    }

    public void initializeForService()
    {
        role = Role.ROLE_FIGHTER;
        IRankHelper ranks = RankFactory.createRankHelper();
        List<String> rankList = ranks.getRanksByService(service);
        
        rankName = rankList.get(rankList.size()-1);
    }

    public boolean isCommandRank()
    {
        if (rankName != null && service != null)
        {
            IRankHelper rank = RankFactory.createRankHelper();
            return rank.isCommandRank(rankName, service);
        }
        
        return false;
    }
    
    public void createCoopUserAndPersona(Campaign campaign, SquadronMember player) throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            CoopUser coopUser = CoopUserManager.getIntance().getCoopUser(getCoopUser());
            if (coopUser == null)
            {
                coopUser = CoopUserManager.getIntance().buildCoopUser(getCoopUser());
            }
            
            CoopUserManager.getIntance().createCoopPersona(campaign, player, getCoopUser());
        }
    }

    public ArmedService getService()
    {
        return service;
    }

    public void setService(ArmedService service)
    {
        this.service = service;
        
        initializeForService();
    }

    public String getPlayerPilotName()
    {
        return playerPilotName;
    }

    public void setPlayerPilotName(String playerPilotName)
    {
        this.playerPilotName = playerPilotName;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getRank()
    {
        return rankName;
    }

    public void setRank(String rank)
    {
        this.rankName = rank;
    }

    public String getSquadName()
    {
        return squadName;
    }

    public void setSquadName(String squadName)
    {
        this.squadName = squadName;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

	public String getCoopUser() 
	{
		return coopUser;
	}

	public void setCoopUser(String coopUser) 
	{
		this.coopUser = coopUser;
	}
}
