package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CampaignModeFactory;
import pwcg.campaign.squadmember.ISquadronMemberReplacer;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.coop.CampaignAdminCoopPilotPanelSet;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;

public class NewPilotGeneratorUI extends JPanel implements ActionListener
{    
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private JButton newPilotCreateButton;
    private NewPilotDataEntryGUI dataEntry;
    private CampaignHomeGUI parent = null;
    private CampaignAdminCoopPilotPanelSet alternateParent = null;
    
    private NewPilotGeneratorDO newPilotGeneratorDO = new NewPilotGeneratorDO();
    private NewPilotState newPilotState;

    public NewPilotGeneratorUI(Campaign campaign, CampaignHomeGUI parent, CampaignAdminCoopPilotPanelSet alternateParent)
    {
        super();
        this.campaign = campaign;
        this.parent = parent;
        this.alternateParent = alternateParent;        
    }

    public void makePanels() throws PWCGException 
    {
        try
        {
            this.add(BorderLayout.WEST, makeButtonPanel());
            this.add(BorderLayout.CENTER, new CampaignGeneratorDataEntryEmpty());
            this.add(BorderLayout.EAST, makeServicePanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeServicePanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getSideImageMain("CampaignGenNav.jpg");
        
        ImageResizingPanel servicesPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        servicesPanel.setLayout(new BorderLayout());
        servicesPanel.setOpaque(true);

        PilotGenerationInfoGUI campaignChooseServiceGUI = new PilotGenerationInfoGUI(this, campaign);
        campaignChooseServiceGUI.makeServiceSelectionPanel();

        servicesPanel.add(campaignChooseServiceGUI, BorderLayout.NORTH);
     
        return servicesPanel;
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getSideImageMain("CampaignGenNav.jpg");
        
        ImageResizingPanel configPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        configPanel.setLayout(new BorderLayout());
        configPanel.setOpaque(true);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setOpaque(false);

        newPilotCreateButton = PWCGButtonFactory.makeMenuButton("Create Pilot", "Create Pilot", this);
        buttonPanel.add(newPilotCreateButton);
        newPilotCreateButton.setEnabled(false);
        
        JLabel dummyLabel3 = new JLabel("     ");       
        dummyLabel3.setOpaque(false);
        buttonPanel.add(dummyLabel3);
        
        JButton cancelChanges = PWCGButtonFactory.makeMenuButton("Cancel", "Cancel", this);
        buttonPanel.add(cancelChanges);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return configPanel;
    }

    public JPanel makeDataEntryPanel() throws PWCGException 
    {
        dataEntry = new NewPilotDataEntryGUI(campaign, this);
        dataEntry.makePanels();
        
        return dataEntry;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {        
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("Cancel"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("Create Pilot"))
            {
                createPilot();
                if (parent != null)
                {
                    parent.createCampaignHomeContext();
                    CampaignGuiContextManager.getInstance().popFromContextStack();
                }
                else if (alternateParent != null)
                {
                    alternateParent.makePanels();
                    CampaignGuiContextManager.getInstance().popFromContextStack();
                }
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void createPilot() throws PWCGUserException, Exception
    {
        String playerName = newPilotGeneratorDO.getPlayerPilotName();
        String squadronName = newPilotGeneratorDO.getSquadName();
        String rank = newPilotGeneratorDO.getRank();
        String coopuser = newPilotGeneratorDO.getCoopUser();

        ISquadronMemberReplacer squadronMemberReplacer = CampaignModeFactory.makeSquadronMemberReplacer(campaign);
        SquadronMember newSquadronMember = squadronMemberReplacer.createPersona(playerName, rank, squadronName, coopuser);
        
        campaign.write();        
        campaign.open(campaign.getCampaignData().getName());
        PWCGContext.getInstance().setCampaign(campaign);
        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            newPilotGeneratorDO.createCoopUserAndPersona(campaign, newSquadronMember);
        }
    }

    public void changeService(ArmedService service) throws PWCGException
    {
        newPilotGeneratorDO.setService(service);
        
        dataEntry = new NewPilotDataEntryGUI(campaign, this);
        dataEntry.makePanels();
        dataEntry.evaluateUI();
        
        this.add(BorderLayout.CENTER, dataEntry);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
    }

    public NewPilotGeneratorDO getNewPilotGeneratorDO()
    {
        return newPilotGeneratorDO;
    }

    public NewPilotState getNewPilotState()
    {
        return newPilotState;
    }

    public void evaluateCompletionState() throws PWCGException
    {
        newPilotCreateButton.setEnabled(false);
        createNewPilotState();
        if (newPilotState.isComplete())
        {
            newPilotCreateButton.setEnabled(true);
        }
    }

    private void createNewPilotState() throws PWCGException
    {
        if (newPilotState == null)
        {
            newPilotState = new NewPilotState(campaign, newPilotGeneratorDO);
            newPilotState.buildStateStack();
        }
    }
}
