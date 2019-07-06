package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.io.json.CoopPilotIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class ReferencePilotSelector extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private JComboBox<String> coopPilotSelector;
    private CampaignHomeGUI campaignHomeGui;
    private Campaign campaign;
    private Map<String, SquadronMember> coopPilotsInCampaign = new HashMap<>();

    public ReferencePilotSelector(Campaign campaign,CampaignHomeGUI campaignHomeGui)
    {
        super();
        this.campaign = campaign;
        this.campaignHomeGui = campaignHomeGui;
    }
    
    public void makePanels() 
    {
        try
        {
            setCenterPanel(makeCoopPilotSelectorPanel());
            setLeftPanel(makeNavigatePanel());
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeCoopPilotSelectorPanel() throws PWCGException
    {
        ImageResizingPanel centerPanel = null;
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
        centerPanel = new ImageResizingPanel(imagePath);
        centerPanel.setLayout(new BorderLayout());

        Font font = MonitorSupport.getPrimaryFontLarge();
        coopPilotSelector = new JComboBox<String>();
        coopPilotSelector.setOpaque(false);
        coopPilotSelector.setBackground(ColorMap.PAPER_BACKGROUND);
        coopPilotSelector.setFont(font);
        
        List<CoopPilot> coopPilots = CoopPilotIOJson.readCoopPilots();
        for (CoopPilot coopPilot : coopPilots)
        {
            if (campaign.getCampaignData().getName().equals(coopPilot.getCampaignName()))
            {
                SquadronMember coopSquadronMember = campaign.getPersonnelManager().getAnyCampaignMember(coopPilot.getSerialNumber());
                if (coopSquadronMember != null)
                {
                    if (coopSquadronMember.getPilotActiveStatus() >= SquadronMemberStatus.STATUS_CAPTURED)
                    {
                        String selectiontext = coopPilot.getPilotName() + ":" + coopPilot.getUsername();
                        coopPilotSelector.addItem(selectiontext);
                        coopPilotsInCampaign.put(selectiontext, coopSquadronMember);
                    }
                }
            }
        }

        if (coopPilotSelector.getItemCount() > 0)
        {
            coopPilotSelector.setSelectedIndex(0);
        }
        
        centerPanel.add(coopPilotSelector, BorderLayout.NORTH);
        
        return centerPanel;
    }

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigLeft.jpg");

        JPanel navPanel = new ImageResizingPanel(imagePath);
        navPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Accept Reference Pilot", "AcceptReferencePilot", this);
        buttonPanel.add(acceptButton);

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
                SquadronMember referencePlayer = coopPilotsInCampaign.get(coopPilotSelector.getSelectedItem());
                if (referencePlayer != null)
                {
                    PWCGContextManager.getInstance().setReferencePlayer(referencePlayer);
                }
                
                campaignHomeGui.clean();
                campaignHomeGui.createPilotContext();

                campaignHomeGui.enableButtonsAsNeeded();
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}


