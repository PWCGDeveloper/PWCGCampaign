package pwcg.gui.maingui.campaigngenerate;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.StringValidity;

public class CampaignGeneratorState
{
    private CampaignGeneratorWorkflow currentStep = CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_TYPE;
    private CampaignGeneratorDO campaignGeneratorDO = new CampaignGeneratorDO();

    public enum CampaignGeneratorWorkflow
    {
        CHOOSE_CAMPAIGN_TYPE,
        CHOOSE_CAMPAIGN_NAME,
        CHOOSE_PLAYER_NAME,
        CHOOSE_REGION,
        CHOOSE_MAP,
        CHOOSE_DATE,
        CHOOSE_ROLE,
        CHOOSE_RANK,
        CHOOSE_SQUADRON,
        COMPLETE
    }
    
    public CampaignGeneratorState(CampaignGeneratorDO campaignGeneratorDO)
    {
        this.campaignGeneratorDO = campaignGeneratorDO;
    }

    public void goToNextStep() throws PWCGException
    {
        if (currentStep == CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_TYPE)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_NAME;
        }

        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_NAME)
        {
            if (StringValidity.isAlpha(campaignGeneratorDO.getCampaignName()))
            {
                currentStep = CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME;
            }
            else
            {
                throw new PWCGException ("Name must be English characters");
            }
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME)
        {
            if (StringValidity.isAlpha(campaignGeneratorDO.getPlayerName()))
            {
                currentStep = CampaignGeneratorWorkflow.CHOOSE_MAP;
                if (PWCGContextManager.isRoF())
                {
                    ICountry country = campaignGeneratorDO.getService().getCountry();
                    if (country.getCountry() == Country.GERMANY)
                    {
                        currentStep = CampaignGeneratorWorkflow.CHOOSE_REGION;
                    }
                }
            }
            else
            {
                throw new PWCGException ("Name must be English characters");
            }
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_REGION)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_MAP;
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_MAP)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_DATE;
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_DATE)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_ROLE;
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_ROLE)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_RANK;
       }
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_RANK)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_SQUADRON;
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_SQUADRON)
        {
            currentStep = CampaignGeneratorWorkflow.COMPLETE;
        }
    }

    public void goToPreviousStep() throws PWCGException
    {   
        if (currentStep == CampaignGeneratorWorkflow.COMPLETE)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_SQUADRON;
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_SQUADRON)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_RANK;
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_RANK)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_ROLE;
        }

        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_ROLE)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_DATE;
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_DATE)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_MAP;
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_MAP)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME;
            if (PWCGContextManager.isRoF())
            {
                ICountry country = campaignGeneratorDO.getService().getCountry();
                if (country.getCountry() == Country.GERMANY)
                {
                    currentStep = CampaignGeneratorWorkflow.CHOOSE_REGION;
                }
            }
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_NAME;
        }
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_REGION)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME;
        }        
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_NAME;
        }        
        
        else if (currentStep == CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_NAME)
        {
            currentStep = CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_TYPE;
        }        
    }

    public boolean isComplete()
    {
        if (currentStep == CampaignGeneratorWorkflow.COMPLETE)
        {
            return true;
        }
        
        return false;
    }

    public CampaignGeneratorWorkflow getCurrentStep()
    {
        return currentStep;
    }
}
