package pwcg.campaign;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignGeneratorModel
{
    private ArmedService service;
    private String campaignName;
    private String playerName;
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

    public Squadron getCampaignSquadron() throws PWCGException
    {
    	SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        Squadron playerSquadron = squadronManager.getSquadronByName(squadronName, campaignDate);
        return playerSquadron;
    }

}
