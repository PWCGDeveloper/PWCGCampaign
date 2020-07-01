package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.display.model.CombatReportBuilder;

public class AARCombatReportPanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private AARCoordinator aarCoordinator;
    private CombatReportPanel campaignCombatReportGUI = null;
    private boolean shouldDisplay = false;

	public AARCombatReportPanel()
	{
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.shouldDisplay = true;
        this.aarCoordinator = AARCoordinator.getInstance();
		this.campaign = PWCGContext.getInstance().getCampaign();
	}

	public void makePanel()  
	{
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
            this.setImage(imagePath);

            createCombatReportGUI();
            JPanel eventTabPane =createPostCombatReportTabs();
            this.add(eventTabPane, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
	
    private JPanel createPostCombatReportTabs()
    {
        JPanel postCombatPanel = new JPanel(new BorderLayout());
        postCombatPanel.setOpaque(false);

        postCombatPanel.add(campaignCombatReportGUI, BorderLayout.CENTER);
        return postCombatPanel;
    }

    private void createCombatReportGUI() throws PWCGException
    {
        CombatReport combatReport = startAARProcess ();
        
        campaignCombatReportGUI = new CombatReportPanel (combatReport);
        campaignCombatReportGUI.makeGUI();
    }

	private CombatReport startAARProcess() throws PWCGException 
	{                
        SquadronMember referencePlayer = campaign.findReferencePlayer();
	    CombatReportBuilder combatReportBuilder = new CombatReportBuilder(campaign, referencePlayer, aarCoordinator);
        CombatReport combatReport = combatReportBuilder.createCombatReport();
		return combatReport;
	}

	public void finished() 
	{
		try
		{
	        Campaign campaign = PWCGContext.getInstance().getCampaign();
			campaignCombatReportGUI.writeCombatReport(campaign);
		}
		catch (Exception e)
		{	
			PWCGLogger.logException(e);
		}
	}

    @Override
    public boolean isShouldDisplay()
    {
        return shouldDisplay;
    }

    @Override
    public JPanel getPanel()
    {
        return this;
    }
}
