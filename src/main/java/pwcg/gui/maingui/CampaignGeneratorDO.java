package pwcg.gui.maingui;

import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.plane.Role;

public class CampaignGeneratorDO
{
    private ArmedService service = null;
    private FrontMapIdentifier frontMap = null;
    private String campaignName = "";
    private String playerName = "";
    private String region = "";
    private String rankName = null;
    private String squadName = "";
    private Date startDate = null;
    private Role role = Role.ROLE_FIGHTER;
    private boolean isCoop = false;

    public boolean isDataSetValid()
    {
        if (campaignName.isEmpty())
        {
            return false;
        }
        
        if (playerName.isEmpty())
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
        
        if (startDate == null)
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
        if (service != null)
        {
            role = Role.ROLE_FIGHTER;
            
            IRankHelper ranks = RankFactory.createRankHelper();
            List<String> rankList = ranks.getRanksByService(service);
            
            rankName = rankList.get(rankList.size()-1);
    
            startDate = service.getServiceStartDate();
        }
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

    public String getCampaignName()
    {
        return campaignName;
    }

    public void setCampaignName(String campaignName)
    {
        this.campaignName = campaignName;
    }
    
   public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
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

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

	public FrontMapIdentifier getFrontMap()
	{
		return frontMap;
	}

	public void setFrontMap(FrontMapIdentifier frontMap)
	{
		this.frontMap = frontMap;
	}

    public boolean isCoop()
    {
        return isCoop;
    }

    public void setCoop(boolean isCoop)
    {
        this.isCoop = isCoop;
    }
    
    
}
