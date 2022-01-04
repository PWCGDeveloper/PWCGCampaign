package pwcg.gui.campaign.intel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import edu.cmu.relativelayout.Binding;
import edu.cmu.relativelayout.BindingFactory;
import edu.cmu.relativelayout.RelativeConstraints;
import edu.cmu.relativelayout.RelativeLayout;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.crewmember.CrewMember;
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
	private CampaignIntelligenceCompanyDetailsPanel squadronDetailsRightPanel;
	private Side side;
	private JPanel contentPanel;
	
	public CampaignIntelligenceReportScreen(Campaign campaign)
	{
        super("");
        this.setLayout(new RelativeLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.setOpaque(false);
        
        try 
        {
            CrewMember referencePlayer = campaign.findReferencePlayer();
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

        JPanel navPanel  = makeNavigatePanel();
        JPanel contentPanels  = makeContentPanels();

        Binding navPanelBinding = BindingFactory.getBindingFactory().directLeftEdge();
        RelativeConstraints navPanelConstraints = new RelativeConstraints();
        navPanelConstraints.addBinding(navPanelBinding);
        this.add(navPanel, navPanelConstraints);
        
        Binding centerPanelBinding = BindingFactory.getBindingFactory().directlyRightOf(navPanel);
        RelativeConstraints centerPanelConstraints = new RelativeConstraints();
        centerPanelConstraints.addBinding(centerPanelBinding);
        this.add(contentPanels, centerPanelConstraints);
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
        CampaignIntelligenceCompanyListPanel squadronSelectionCenterPanel = new CampaignIntelligenceCompanyListPanel(campaign, this,side);
        squadronSelectionCenterPanel.makePanel();
        return squadronSelectionCenterPanel;
    }

    private JPanel makeRightPanel() throws PWCGException  
    {
        squadronDetailsRightPanel = new CampaignIntelligenceCompanyDetailsPanel(campaign);
        squadronDetailsRightPanel.makePanel();
        return squadronDetailsRightPanel;
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
                CrewMember referencePlayer = campaign.findReferencePlayer();
                side = referencePlayer.determineCountry(campaign.getDate()).getSide();
                makeContent();
            }
            else if (action.contains("Enemy"))
            {
                CrewMember referencePlayer = campaign.findReferencePlayer();
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
