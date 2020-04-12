package pwcg.gui.maingui.campaigngenerate;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.PWCGStringValidator;

public class NewPilotState
{
    private PilotGeneratorWorkflow currentStep = PilotGeneratorWorkflow.CHOOSE_PLAYER_NAME;
    private Campaign campaign;
    private NewPilotGeneratorDO newPilotGeneratorDO;
    private List<PilotGeneratorWorkflow> stateStack = new ArrayList<>();
    private int stateIndex = 0;
    
    public enum PilotGeneratorWorkflow
    {
        CHOOSE_PLAYER_NAME,
        CHOOSE_COOP_USER,
        CHOOSE_REGION,
        CHOOSE_ROLE,
        CHOOSE_RANK,
        CHOOSE_SQUADRON,
        COMPLETE
    }
    
    public NewPilotState(Campaign campaign, NewPilotGeneratorDO campaignGeneratorDO)
    {
        this.campaign = campaign;
        this.newPilotGeneratorDO = campaignGeneratorDO;
    }

    public void buildStateStack() throws PWCGException
    {
        stateStack.add(PilotGeneratorWorkflow.CHOOSE_PLAYER_NAME);
        
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            ICountry country = newPilotGeneratorDO.getService().getCountry();
            if (country.getCountry() == Country.GERMANY)
            {
                stateStack.add(PilotGeneratorWorkflow.CHOOSE_REGION);
            }
        }

        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            stateStack.add(PilotGeneratorWorkflow.CHOOSE_COOP_USER);
        }
        
        stateStack.add(PilotGeneratorWorkflow.CHOOSE_ROLE);
        stateStack.add(PilotGeneratorWorkflow.CHOOSE_RANK);
        stateStack.add(PilotGeneratorWorkflow.CHOOSE_SQUADRON);
        stateStack.add(PilotGeneratorWorkflow.COMPLETE);
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

    public PilotGeneratorWorkflow getCurrentStep()
    {
        return currentStep;
    }

    public boolean isComplete()
    {
        if ((currentStep != PilotGeneratorWorkflow.COMPLETE))
        {
            return false;
        }
        else if (!PWCGStringValidator.validateStringIsAlpha(newPilotGeneratorDO.getPlayerPilotName()))
        {
            return false;
        }
        else if (!(campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE) &&
                !(PWCGStringValidator.validateStringIsAlpha(newPilotGeneratorDO.getCoopUser())))
        {
            return false;
        }

        
        return true;
    }
}

