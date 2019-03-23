package pwcg.gui.campaign;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public abstract class CampaignRosterBasePanelFactory
{
    protected ActionListener parent = null;
	protected Campaign campaign = null;
	protected SquadronMember referencePlayer = null;
	protected List<SquadronMember> sortedPilots = null;
    protected JPanel chalkboardPanel = null;
    protected JPanel pilotListPanel = null;
    protected boolean excludeAces = false;

	public CampaignRosterBasePanelFactory(ActionListener parent) throws PWCGException  
	{
	    this.parent = parent;
		this.campaign = PWCGContextManager.getInstance().getCampaign();
		this.referencePlayer = PWCGContextManager.getInstance().getReferencePlayer();
	}
	
	abstract public void makePilotList() throws PWCGException ;

	abstract public void makeCampaignHomePanels() throws PWCGException, PWCGException  ;

    public JPanel getChalkboardPanel()
    {
        return chalkboardPanel;
    }

    public JPanel getPilotListPanel()
    {
        return pilotListPanel;
    }

    public void setExcludeAces(boolean excludeAces)
    {
        this.excludeAces = excludeAces;
    }
    
    
}
