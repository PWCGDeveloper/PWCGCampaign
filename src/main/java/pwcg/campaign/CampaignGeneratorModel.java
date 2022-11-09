package pwcg.campaign;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;

public class CampaignGeneratorModel
{
    private PWCGProduct product;
    private String campaignName;
    private String userName;
    private String squadronName;
    private Date campaignDate;
    private CampaignPilotGeneratorModel pilotModel = new CampaignPilotGeneratorModel();
    private CampaignMode campaignMode = CampaignMode.CAMPAIGN_MODE_NONE;

    public CampaignGeneratorModel(PWCGProduct product)
    {    
        this.product = product;
    }
    
    public PWCGProduct getProduct()
    {
        return product;
    }

    public ArmedService getService()
    {
        return pilotModel.getService();
    }

    public void setService(ArmedService service)
    {
        this.pilotModel.setService(service);
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
        return pilotModel.getPlayerName();
    }

    public void setPlayerName(String playerName)
    {
        this.pilotModel.setPlayerName(playerName);
    }

    public String getPlayerRank()
    {
        return pilotModel.getPlayerRank();
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
        this.pilotModel.setPlayerRank(playerRank);
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
        return pilotModel.getPlayerRegion();
    }

    public void setPlayerRegion(String playerRegion)
    {
        this.pilotModel.setPlayerRegion(playerRegion);
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
            throw new PWCGUserException ("Invalid name - no pilot name provided");
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

    public CampaignPilotGeneratorModel getPilotModel()
    {
        return pilotModel;
    }

}
