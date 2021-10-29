package pwcg.gui.campaign.personnel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import edu.cmu.relativelayout.Binding;
import edu.cmu.relativelayout.BindingFactory;
import edu.cmu.relativelayout.RelativeConstraints;
import edu.cmu.relativelayout.RelativeLayout;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignReferencePilotSelectorScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private JComboBox<String> squadronMemberSelector;
    private CampaignHomeScreen campaignHomeGui;
    private Campaign campaign;
    private Map<String, SquadronMember> coopSquadronMembersInCampaign = new HashMap<>();

    public CampaignReferencePilotSelectorScreen(Campaign campaign,CampaignHomeScreen campaignHomeGui)
    {
        super("");
        this.setLayout(new RelativeLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.campaignHomeGui = campaignHomeGui;
    }
    
    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignReferencePilotSelectorScreen);
            this.setImageFromName(imagePath);
            
            Binding navPanelBinding = BindingFactory.getBindingFactory().directLeftEdge();
            RelativeConstraints navPanelConstraints = new RelativeConstraints();
            navPanelConstraints.addBinding(navPanelBinding);
            JPanel navPanel  = makeNavigatePanel();
            this.add(navPanel, navPanelConstraints);
            
            Binding centerPanelBinding = BindingFactory.getBindingFactory().directlyRightOf(navPanel);
            RelativeConstraints centerPanelConstraints = new RelativeConstraints();
            centerPanelConstraints.addBinding(centerPanelBinding);
            JPanel centerPanel  = makeReferencePilotSelectorPanel();
            this.add(centerPanel, centerPanelConstraints);
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeReferencePilotSelectorPanel() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        squadronMemberSelector = new JComboBox<String>();
        squadronMemberSelector.setOpaque(false);
        squadronMemberSelector.setBackground(ColorMap.PAPER_BACKGROUND);
        squadronMemberSelector.setFont(font);
        
        SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (SquadronMember player : players.getSquadronMemberList())
        {
            if (player.getPilotActiveStatus() >= SquadronMemberStatus.STATUS_CAPTURED)
            {
                String selectiontext = player.getNameAndRank();
                squadronMemberSelector.addItem(selectiontext);
                coopSquadronMembersInCampaign.put(selectiontext, player);
            }
        }

        if (squadronMemberSelector.getItemCount() > 0)
        {
            squadronMemberSelector.setSelectedIndex(0);
        }
        
        JPanel gridPanel = new JPanel(new GridLayout(0,3));
        gridPanel.setOpaque(false);

        for(int i = 0; i < 5; ++i)
        {
            gridPanel.add(PWCGLabelFactory.makeDummyLabel());
            gridPanel.add(PWCGLabelFactory.makeDummyLabel());
            gridPanel.add(PWCGLabelFactory.makeDummyLabel());
        }

        gridPanel.add(PWCGLabelFactory.makeDummyLabel());
        gridPanel.add(squadronMemberSelector);
        gridPanel.add(PWCGLabelFactory.makeDummyLabel());

        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel centerPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(gridPanel, BorderLayout.NORTH);
        this.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        ImageToDisplaySizer.setDocumentSize(centerPanel);

        return centerPanel;
    }

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept", "AcceptReferencePilot", "Make this pilot the reference pilot", this);
        buttonPanel.add(acceptButton);

        JButton cancelButton = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", "CancelReferencePilot", "Do not change the reference pilot", this);
        buttonPanel.add(cancelButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("AcceptReferencePilot"))
            {
                SquadronMember referencePlayer = coopSquadronMembersInCampaign.get(squadronMemberSelector.getSelectedItem());
                if (referencePlayer != null)
                {
                    campaign.getCampaignData().setReferencePlayerSerialNumber(referencePlayer.getSerialNumber());
                    campaign.write();
                }
                
                campaignHomeGui.refreshInformation();
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("CancelReferencePilot"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}


