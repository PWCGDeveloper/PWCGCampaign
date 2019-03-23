package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.CombatReportBuilder;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.AAREventPanel;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class AARCombatReportPanel extends AAREventPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private SquadronMember referencePlayer = null;
    private AARCoordinator aarCoordinator;

    private CombatReportPanel campaignCombatReportGUI = null;

	public AARCombatReportPanel()
	{
        super();
        this.shouldDisplay = true;
        this.aarCoordinator = AARCoordinator.getInstance();
		this.campaign = PWCGContextManager.getInstance().getCampaign();
		this.referencePlayer = PWCGContextManager.getInstance().getReferencePlayer();
	}

	public void makePanel()  
	{
        try
        {
            createCombatReportGUI();
            createPostCombatReportTabs();

        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
	
    private void createPostCombatReportTabs()
    {
        ImageResizingPanel postCombatPanel = new ImageResizingPanel(ContextSpecificImages.imagesMisc() + "PaperFull.jpg");
        postCombatPanel.setLayout(new BorderLayout());
        postCombatPanel.add(campaignCombatReportGUI, BorderLayout.CENTER);
        this.add(postCombatPanel, BorderLayout.CENTER);
    }

    private void createCombatReportGUI() throws PWCGException
    {
        CombatReport combatReport = startAARProcess ();
        
        campaignCombatReportGUI = new CombatReportPanel (combatReport);
        campaignCombatReportGUI.makeGUI();
    }

	private CombatReport startAARProcess() throws PWCGException 
	{                
	    CombatReportBuilder combatReportBuilder = new CombatReportBuilder(campaign, referencePlayer, aarCoordinator);
        CombatReport combatReport = combatReportBuilder.createCombatReport();
		return combatReport;
	}

	public void finished() 
	{
		try
		{
	        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
			campaignCombatReportGUI.writeCombatReport(campaign);
		}
		catch (Exception e)
		{	
			Logger.logException(e);
		}
	}

}
