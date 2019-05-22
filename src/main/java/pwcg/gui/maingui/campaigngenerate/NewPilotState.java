package pwcg.gui.maingui.campaigngenerate;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.StringValidity;

public class NewPilotState
{
    private PilotGeneratorWorkflow currentStep = PilotGeneratorWorkflow.CHOOSE_PLAYER_NAME;
    private CampaignGeneratorDO campaignGeneratorDO = new CampaignGeneratorDO();

    public enum PilotGeneratorWorkflow
    {
        CHOOSE_PLAYER_NAME,
        CHOOSE_COOP_HOST,
        CHOOSE_REGION,
        CHOOSE_ROLE,
        CHOOSE_RANK,
        CHOOSE_SQUADRON,
        COMPLETE
    }
    
    private Campaign campaign;
    
    public NewPilotState(Campaign campaign, CampaignGeneratorDO campaignGeneratorDO)
    {
        this.campaignGeneratorDO = campaignGeneratorDO;
        this.campaign = campaign;
    }

    public void goToNextStep() throws PWCGException
    {
        if (currentStep == PilotGeneratorWorkflow.CHOOSE_PLAYER_NAME)
        {
            if (StringValidity.isAlpha(campaignGeneratorDO.getCampaignName()))
            {
                currentStep = PilotGeneratorWorkflow.CHOOSE_PLAYER_NAME;
            }
            else
            {
                throw new PWCGException ("Name must be English characters");
            }
        }
        
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_PLAYER_NAME)
        {
            if (StringValidity.isAlpha(campaignGeneratorDO.getPlayerName()))
            {
                currentStep = PilotGeneratorWorkflow.CHOOSE_COOP_HOST;
                if (!campaign.getCampaignData().isCoop())
                {
                    currentStep = PilotGeneratorWorkflow.CHOOSE_ROLE;
                    if (useRegion())
                    {
                        currentStep = PilotGeneratorWorkflow.CHOOSE_REGION;
                    }
                }
            }
            else
            {
                throw new PWCGException ("Name must be English characters");
            }
        }
        
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_COOP_HOST)
        {
            currentStep = PilotGeneratorWorkflow.CHOOSE_ROLE;
            if (useRegion())
            {
                currentStep = PilotGeneratorWorkflow.CHOOSE_REGION;
            }
        }
        
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_REGION)
        {
            currentStep = PilotGeneratorWorkflow.CHOOSE_ROLE;
        }
        
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_ROLE)
        {
            currentStep = PilotGeneratorWorkflow.CHOOSE_RANK;
       }
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_RANK)
        {
            currentStep = PilotGeneratorWorkflow.CHOOSE_SQUADRON;
        }
        
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_SQUADRON)
        {
            currentStep = PilotGeneratorWorkflow.COMPLETE;
        }
    }

    public void goToPreviousStep() throws PWCGException
    {   
        if (currentStep == PilotGeneratorWorkflow.COMPLETE)
        {
            currentStep = PilotGeneratorWorkflow.CHOOSE_SQUADRON;
        }
        
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_SQUADRON)
        {
            currentStep = PilotGeneratorWorkflow.CHOOSE_RANK;
        }
        
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_RANK)
        {
            currentStep = PilotGeneratorWorkflow.CHOOSE_ROLE;
        }

        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_ROLE)
        {
        	if (useRegion())
        	{
                currentStep = PilotGeneratorWorkflow.CHOOSE_REGION;
        	}
        	else if (campaign.getCampaignData().isCoop())
        	{
                currentStep = PilotGeneratorWorkflow.CHOOSE_COOP_HOST;
        	}
        	else
        	{
                currentStep = PilotGeneratorWorkflow.CHOOSE_PLAYER_NAME;
        	}
        }
        
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_REGION)
        {
        	if (campaign.getCampaignData().isCoop())
        	{
                currentStep = PilotGeneratorWorkflow.CHOOSE_COOP_HOST;
        	}
        	else
        	{
                currentStep = PilotGeneratorWorkflow.CHOOSE_PLAYER_NAME;
        	}
        }
        
        else if (currentStep == PilotGeneratorWorkflow.CHOOSE_COOP_HOST)
        {
            currentStep = PilotGeneratorWorkflow.CHOOSE_PLAYER_NAME;
        }        
    }

	private boolean useRegion() 
	{
		if (PWCGContextManager.isRoF())
		{
		    ICountry country = campaignGeneratorDO.getService().getCountry();
		    if (country.getCountry() == Country.GERMANY)
		    {
		        return true;
		    }
		}
		return false;
	}

    public boolean isComplete()
    {
        if (currentStep == PilotGeneratorWorkflow.COMPLETE)
        {
            return true;
        }
        
        return false;
    }

    public PilotGeneratorWorkflow getCurrentStep()
    {
        return currentStep;
    }
}
