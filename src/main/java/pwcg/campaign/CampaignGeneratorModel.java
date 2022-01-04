package pwcg.campaign;

import java.util.Date;

import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;

public class CampaignGeneratorModel
{
    private ArmedService service;
    private String campaignName;
    private String playerName;
    private String userName;
    private String playerRank;
    private String squadronName;
    private Date campaignDate;
    private String playerRegion;
    private CampaignMode campaignMode = CampaignMode.CAMPAIGN_MODE_NONE;

    public ArmedService getService()
    {
        return service;
    }

    public void setService(ArmedService service)
    {
        this.service = service;
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

    public String getPlayerRank()
    {
        return playerRank;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setPlayerRank(String playerRank)
    {
        this.playerRank = playerRank;
    }

    public String getSquadronName()
    {
        return squadronName;
    }

    public void setSquadronName(String squadronName)
    {
        this.squadronName = squadronName;
    }

    public Date getCampaignDate()
    {
        return campaignDate;
    }

    public void setCampaignDate(Date campaignDate)
    {
        this.campaignDate = campaignDate;
    }

    public String getPlayerRegion()
    {
        return playerRegion;
    }

    public void setPlayerRegion(String playerRegion)
    {
        this.playerRegion = playerRegion;
    }

    public CampaignMode getCampaignMode()
    {
        return campaignMode;
    }

    public void setCampaignMode(CampaignMode campaignMode)
    {
        this.campaignMode = campaignMode;
    }

    public Company getCampaignCompany() throws PWCGException
    {
    	CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        Company playerSquadron = squadronManager.getCompanyByName(squadronName, campaignDate);
        return playerSquadron;
    }
    

    public void validateCampaignInputs() throws PWCGException
    {
        if (getCampaignDate() == null)
        {
            throw new PWCGUserException ("Invalid date - no campaign start date provided");
        }


        Date earliest = DateUtils.getBeginningOfGame();
        Date latest = DateUtils.getEndOfWar();

        if (getCampaignDate().before(earliest) || getCampaignDate().after(latest))
        {
            throw new PWCGUserException ("Invalid date - must be between start and end of war");
        }

        if (getCampaignName() == null || getCampaignName().length() == 0)
        {
            throw new PWCGUserException ("Invalid name - no campaign name provided");
        }

        if (getPlayerName() == null || getPlayerName().length() == 0)
        {
            throw new PWCGUserException ("Invalid name - no crewMember name provided");
        }

        if (getPlayerRank() == null || getPlayerRank().length() == 0)
        {
            throw new PWCGUserException ("Invalid rank - no rank provided");
        }

        if (getSquadronName() == null || getSquadronName().length() == 0)
        {
            throw new PWCGUserException ("Invalid squaron - no squadron provided");
        }
        
        if (getPlayerRegion() == null)
        {
            setPlayerRegion("");
        }
    }

}
