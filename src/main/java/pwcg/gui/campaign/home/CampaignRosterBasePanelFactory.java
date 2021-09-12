package pwcg.gui.campaign.home;

import java.awt.event.ActionListener;
import java.util.List;

import javafx.scene.layout.Pane;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public abstract class CampaignRosterBasePanelFactory
{
    protected ActionListener parent = null;
	protected Campaign campaign = null;
	protected SquadronMember referencePlayer = null;
	protected List<SquadronMember> sortedPilots = null;
    protected Pane chalkboardPanel = null;
    protected Pane pilotListPanel = null;
    protected boolean excludeAces = false;

	public CampaignRosterBasePanelFactory(ActionListener parent) throws PWCGException  
	{
	    this.parent = parent;
		this.campaign = PWCGContext.getInstance().getCampaign();
		this.referencePlayer = campaign.findReferencePlayer();
	}
	
	abstract public void makePilotList() throws PWCGException ;

	abstract public void makeCampaignHomePanels() throws PWCGException, PWCGException  ;

    public Pane getChalkboardPanel()
    {
        return chalkboardPanel;
    }

    public Pane getPilotListPanel()
    {
        return pilotListPanel;
    }

    public void setExcludeAces(boolean excludeAces)
    {
        this.excludeAces = excludeAces;
    }
    
    
}
