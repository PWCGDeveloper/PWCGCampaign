package pwcg.gui.maingui.campaigngenerate;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.CampaignMode;
import pwcg.core.exception.PWCGException;

public interface IPilotGeneratorUI 
{
    void changeService(ArmedService service) throws PWCGException;
    public List<ArmedService> getArmedServices() throws PWCGException;
    public void setCampaignProfileParameters(CampaignMode campaignMode, String campaignName);
    CampaignGeneratorDO getCampaignGeneratorDO();
    CampaignGeneratorState getCampaignGeneratorState();
    void evaluateCompletionState();
}
