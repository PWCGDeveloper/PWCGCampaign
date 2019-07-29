package pwcg.gui.maingui.campaigngenerate;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.core.exception.PWCGException;

public interface IPilotGeneratorUI 
{
    void changeService(ArmedService service) throws PWCGException;
    void enableCompleteAction(boolean enabled);
    public List<ArmedService> getArmedServices() throws PWCGException;
}
