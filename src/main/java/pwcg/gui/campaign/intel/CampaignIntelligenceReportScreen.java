package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javax.swing.JTabbedPane;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignIntelligenceReportScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

	private JTabbedPane tabs = new JTabbedPane();
	private Campaign campaign;

	public CampaignIntelligenceReportScreen(Campaign campaign)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.setOpaque(false);
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignIntelligenceReportScreen);
        this.setImageFromName(imagePath);

        this.add(BorderLayout.WEST, makeNavigatePanel());
        this.add(BorderLayout.CENTER,  makeCenterPanel());
        this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(2000));
	}

	private Pane makeNavigatePanel() throws PWCGException  
	{		
        Pane intelPanel = new Pane(new BorderLayout());
		intelPanel.setOpaque(false);

		Pane buttonPanel = new Pane(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
		
        Button acceptButton = ButtonFactory.makeTranslucentMenuButton("Finished Reading", "IntelFinished", "Leave Intel", this);
		buttonPanel.add(acceptButton);
		
		intelPanel.add(buttonPanel, BorderLayout.NORTH);
		
		return intelPanel;
	}

	private Pane makeCenterPanel() throws PWCGException 
	{
        Pane intelPanel = new Pane(new BorderLayout());
        intelPanel.setOpaque(false);

        Color tabBG = ColorMap.PAPER_BACKGROUND;
        tabs.setBackground(tabBG);
        tabs.setOpaque(false);
        
        CampaignIntelligenceEnemySquadronsGUI enemySquadronsGUI = new CampaignIntelligenceEnemySquadronsGUI();
        tabs.addTab("Enemy Squadrons", enemySquadronsGUI);      
        
        CampaignIntelligenceFriendlySquadronsGUI friendlySquadronsGUI = new CampaignIntelligenceFriendlySquadronsGUI();
        tabs.addTab("Friendly Squadrons", friendlySquadronsGUI);        
                    
        for (int i = 0; i < tabs.getTabCount(); ++i)
        {
            tabs.setBackgroundAt(i, tabBG);
        }

        intelPanel.add(tabs, BorderLayout.CENTER);
		
		return intelPanel;
	}

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();

            if (action.equalsIgnoreCase("IntelFinished"))
            {
                campaign.write();                
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

}
