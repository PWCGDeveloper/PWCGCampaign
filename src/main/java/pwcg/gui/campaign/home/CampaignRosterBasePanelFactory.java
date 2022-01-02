package pwcg.gui.campaign.home;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public abstract class CampaignRosterBasePanelFactory
{
    protected ActionListener parent = null;
	protected Campaign campaign = null;
	protected CrewMember referencePlayer = null;
	protected List<CrewMember> sortedCrewMembers = null;
    protected JPanel chalkboardPanel = null;
    protected JPanel crewMemberListPanel = null;
    protected boolean excludeAces = false;

	public CampaignRosterBasePanelFactory(ActionListener parent) throws PWCGException  
	{
	    this.parent = parent;
		this.campaign = PWCGContext.getInstance().getCampaign();
		this.referencePlayer = campaign.findReferencePlayer();
	}
	
	abstract public void makeCrewMemberList() throws PWCGException ;

	abstract public void makeCampaignHomePanels() throws PWCGException, PWCGException  ;

    public JPanel getChalkboardPanel()
    {
        return chalkboardPanel;
    }

    public JPanel getCrewMemberListPanel()
    {
        return crewMemberListPanel;
    }

    public void setExcludeAces(boolean excludeAces)
    {
        this.excludeAces = excludeAces;
    }
    
    
}
