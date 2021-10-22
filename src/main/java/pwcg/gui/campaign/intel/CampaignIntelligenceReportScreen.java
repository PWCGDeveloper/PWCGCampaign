package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
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
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignIntelligenceReportScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

	private JTabbedPane tabs = new JTabbedPane();
	private Campaign campaign;

	public CampaignIntelligenceReportScreen(Campaign campaign)
	{
        super("");
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.setOpaque(false);
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignIntelligenceReportScreen);
        this.setImageFromName(imagePath);

        
        GridBagConstraints constraints = initializeGridbagConstraints();

        constraints.weightx = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(makeNavigatePanel(), constraints);

        constraints.weightx = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.add(makeCenterPanel(), constraints);
        
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridy = 0;
        this.add(SpacerPanelFactory.makeDocumentSpacerPanel(1400), constraints);
	}

	private JPanel makeNavigatePanel() throws PWCGException  
	{		
        JPanel intelPanel = new JPanel(new BorderLayout());
		intelPanel.setOpaque(false);

		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
		
        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished Reading", "IntelFinished", "Leave Intel", this);
		buttonPanel.add(acceptButton);
		
		intelPanel.add(buttonPanel, BorderLayout.NORTH);
		
		return intelPanel;
	}

	private JPanel makeCenterPanel() throws PWCGException 
	{
        JPanel intelPanel = new JPanel(new BorderLayout());
        intelPanel.setOpaque(false);

        Color tabBG = ColorMap.PAPER_BACKGROUND;
        tabs.setBackground(tabBG);
        tabs.setOpaque(false);
        
        CampaignIntelligenceEnemySquadronsGUI enemySquadronsGUI = new CampaignIntelligenceEnemySquadronsGUI();
        ImageToDisplaySizer.setDocumentSizeWithMultiplier(enemySquadronsGUI, 2);
        tabs.addTab("Enemy Squadrons", enemySquadronsGUI);      
        
        CampaignIntelligenceFriendlySquadronsGUI friendlySquadronsGUI = new CampaignIntelligenceFriendlySquadronsGUI();
        ImageToDisplaySizer.setDocumentSizeWithMultiplier(friendlySquadronsGUI, 2);
        tabs.addTab("Friendly Squadrons", friendlySquadronsGUI);        
                    
        for (int i = 0; i < tabs.getTabCount(); ++i)
        {
            tabs.setBackgroundAt(i, tabBG);
        }

        intelPanel.add(tabs, BorderLayout.CENTER);
		
		return intelPanel;
	}

    private GridBagConstraints initializeGridbagConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipadx = 3;
        constraints.ipady = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        Insets margins = new Insets(0, 50, 50, 0);
        constraints.insets = margins;
        return constraints;
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
