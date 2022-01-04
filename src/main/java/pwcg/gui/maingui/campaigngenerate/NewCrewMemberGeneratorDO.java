package pwcg.gui.maingui.campaigngenerate;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.tank.PwcgRole;

public class NewCrewMemberGeneratorDO
{
    private ArmedService service = null;
    private String playerCrewMemberName = "";
    private String coopUser = "";
    private String region = "";
    private String rankName = null;
    private String squadName = "";
    private PwcgRole role = PwcgRole.ROLE_FIGHTER;

    public boolean isDataSetValid()
    {

        if (playerCrewMemberName.isEmpty())
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
        role = PwcgRole.ROLE_FIGHTER;
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

    public ArmedService getService()
    {
        return service;
    }

    public void setService(ArmedService service)
    {
        this.service = service;
        
        initializeForService();
    }

    public String getPlayerCrewMemberName()
    {
        return playerCrewMemberName;
    }

    public void setPlayerCrewMemberName(String playerCrewMemberName)
    {
        this.playerCrewMemberName = playerCrewMemberName;
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

    public PwcgRole getRole()
    {
        return role;
    }

    public void setRole(PwcgRole role)
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
