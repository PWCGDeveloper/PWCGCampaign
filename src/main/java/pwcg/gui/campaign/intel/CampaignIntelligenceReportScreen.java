package pwcg.gui.campaign.intel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class CampaignIntelligenceReportScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

	private Campaign campaign;
	private CampaignIntelligenceSquadronDetailsPanel squadronDetailsRightPanel;
	private Side side;
	private JPanel contentPanel;
	
	public CampaignIntelligenceReportScreen(Campaign campaign)
	{
        super("");
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.setOpaque(false);
        
        try 
        {
            SquadronMember referencePlayer = campaign.findReferencePlayer();
            side = referencePlayer.determineCountry(campaign.getDate()).getSide();
        }
        catch (Exception  e)
        {
            side = Side.ALLIED;
        }
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
        this.add(makeContentPanels(), constraints);
	}

    private JPanel makeNavigatePanel() throws PWCGException  
    {       
        CampaignIntelligenceControlPanel navPanel = new CampaignIntelligenceControlPanel(this);
        navPanel.makeIntelNavPanel();
        return navPanel;
    }

    private JPanel makeContentPanels() throws PWCGException
    {
        contentPanel = new JPanel(new GridLayout(0, 2));
        contentPanel.setOpaque(false);

        makeContent();
        
        return contentPanel;
    }

    private void makeContent() throws PWCGException
    {
        contentPanel.removeAll();
        
        JPanel squadronListPanel = makeCenterPanel();
        contentPanel.add(squadronListPanel);

        JPanel squadronDetailsPanel = makeRightPanel();
        contentPanel.add(squadronDetailsPanel);
        
        this.revalidate();
        this.repaint();
    }

    private JPanel makeCenterPanel() throws PWCGException  
    {
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
            else if (action.contains("Friendly"))
            {
                SquadronMember referencePlayer = campaign.findReferencePlayer();
                side = referencePlayer.determineCountry(campaign.getDate()).getSide();
                makeContent();
            }
            else if (action.contains("Enemy"))
            {
                SquadronMember referencePlayer = campaign.findReferencePlayer();
                side = referencePlayer.determineCountry(campaign.getDate()).getSide().getOppositeSide();
                makeContent();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

}
