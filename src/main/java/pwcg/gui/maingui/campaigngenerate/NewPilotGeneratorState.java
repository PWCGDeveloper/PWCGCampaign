package pwcg.gui.maingui.campaigngenerate;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.PWCGStringValidator;

public class NewPilotGeneratorState
{
    private CampaignGeneratorWorkflow currentStep = CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME;
    private CampaignGeneratorDO campaignGeneratorDO;
    private List<CampaignGeneratorWorkflow> stateStack = new ArrayList<>();
    private int stateIndex = 0;
    
    public enum CampaignGeneratorWorkflow
    {
        CHOOSE_PLAYER_NAME,
        CHOOSE_REGION,
        CHOOSE_COOP_USER,
        CHOOSE_MAP,
        CHOOSE_DATE,
        CHOOSE_ROLE,
        CHOOSE_RANK,
        CHOOSE_SQUADRON,
        COMPLETE
    }
    
    public NewPilotGeneratorState(CampaignGeneratorDO campaignGeneratorDO)
    {
        this.campaignGeneratorDO = campaignGeneratorDO;
    }

    public void buildStateStack() throws PWCGException
    {
        stateStack.add(CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME);
        
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            ICountry country = campaignGeneratorDO.getService().getCountry();
            if (country.getCountry() == Country.GERMANY)
            {
                stateStack.add(CampaignGeneratorWorkflow.CHOOSE_REGION);
            }
        }

        if (campaignGeneratorDO.getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            stateStack.add(CampaignGeneratorWorkflow.CHOOSE_COOP_USER);
        }
        
        stateStack.add(CampaignGeneratorWorkflow.CHOOSE_MAP);
        stateStack.add(CampaignGeneratorWorkflow.CHOOSE_DATE);
        stateStack.add(CampaignGeneratorWorkflow.CHOOSE_ROLE);
        stateStack.add(CampaignGeneratorWorkflow.CHOOSE_RANK);
        stateStack.add(CampaignGeneratorWorkflow.CHOOSE_SQUADRON);
        stateStack.add(CampaignGeneratorWorkflow.COMPLETE);
    }

    public void goToNextStep() throws PWCGException
    {
        if (stateIndex < (stateStack.size()-1))
        {
            ++stateIndex;
        }
        currentStep = stateStack.get(stateIndex);
    }
    
    public void goToPreviousStep() throws PWCGException
    {   
        if (stateIndex > 0)
        {
            --stateIndex;
        }
        currentStep = stateStack.get(stateIndex);        
    }

    public boolean isComplete()
    {
        if ((currentStep != CampaignGeneratorWorkflow.COMPLETE))
        {
            return false;
        }
        else if (!PWCGStringValidator.isValidDescriptor(campaignGeneratorDO.getCampaignName()))
        {
            return false;
        }
        else if (!PWCGStringValidator.isValidName(campaignGeneratorDO.getPlayerPilotName()))
        {
            return false;
        }
        else if (!(campaignGeneratorDO.getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE) &&
                !(PWCGStringValidator.isValidDescriptor(campaignGeneratorDO.getCoopUser())))
        {
            return false;
        }

        
        return true;
    }

    public CampaignGeneratorWorkflow getCurrentStep()
    {
        return currentStep;
    }

    public boolean isProfileComplete()
    {
        if (campaignGeneratorDO.getCampaignMode() == CampaignMode.CAMPAIGN_MODE_NONE)
        {
            return false;
        }
        else if (campaignGeneratorDO.getCampaignName().isEmpty())
        {
            return false;
        }
        else if (campaignGeneratorDO.getService() == null)
        {
            return false;
        }
        else if (!PWCGStringValidator.isValidDescriptor(campaignGeneratorDO.getCampaignName()))
        {
            return false;
        }

        return true;
    }
}
