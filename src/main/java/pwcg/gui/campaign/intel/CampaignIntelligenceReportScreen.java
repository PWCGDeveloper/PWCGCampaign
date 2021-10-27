package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignIntelligenceReportScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

	private Campaign campaign;
	private CampaignIntelligenceSquadronDetailsPanel squadronDetailsRightPanel;
	
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
        this.add(formSquadronDetails(), constraints);
	}

    private JPanel formSquadronDetails() throws PWCGException
    {
        JPanel squadronIntelPanel = new JPanel(new GridLayout(0, 2));
        squadronIntelPanel.setOpaque(false);

        JPanel squadronListPanel = makeCenterPanel();
        squadronIntelPanel.add(squadronListPanel);

        JPanel squadronDetailsPanel = makeRightPanel();
        squadronIntelPanel.add(squadronDetailsPanel);
        
        return squadronIntelPanel;
    }

    private JPanel makeNavigatePanel() throws PWCGException  
    {       
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.setLayout(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        JButton finished = PWCGButtonFactory.makeTranslucentMenuButton("Finished Reading", "IntelFinished", "Leave Intel", this);
        buttonPanel.add(finished);
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    private JPanel makeCenterPanel() throws PWCGException  
    {
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        Side side = referencePlayer.determineCountry(campaign.getDate()).getSide().getOppositeSide();
        
        CampaignIntelligenceSquadronListPanel squadronSelectionCenterPanel = new CampaignIntelligenceSquadronListPanel(campaign, this,side);
        squadronSelectionCenterPanel.makePanel();
        return squadronSelectionCenterPanel;
    }

    private JPanel makeRightPanel() throws PWCGException  
    {
        squadronDetailsRightPanel = new CampaignIntelligenceSquadronDetailsPanel(campaign);
        squadronDetailsRightPanel.makePanel();
        return squadronDetailsRightPanel;
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
            else if (action.contains("SquadronSelected"))
            {
                int beginIndex = action.indexOf(":");
                ++beginIndex;
                String squadronIdString = action.substring(beginIndex).trim();
                int squadronId = Integer.valueOf(squadronIdString).intValue();
                squadronDetailsRightPanel.setSquadronIntelText(squadronId);
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

}
