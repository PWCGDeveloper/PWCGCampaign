package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceFinder;
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
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.coop.CampaignAdminCoopPilotPanelSet;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class NewPilotGeneratorUI extends PwcgGuiContext implements ActionListener, IPilotGeneratorUI
{    
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private JButton newPilotCreateButton;
    private CampaignGeneratorDataEntryGUI dataEntry;
    private CampaignHomeGUI parent = null;
    private CampaignAdminCoopPilotPanelSet alternateParent = null;
    
    private CampaignGeneratorDO campaignGeneratorDO = new CampaignGeneratorDO();
    private CampaignGeneratorState campaignGeneratorState;

    public NewPilotGeneratorUI(Campaign campaign, CampaignHomeGUI parent, CampaignAdminCoopPilotPanelSet alternateParent)
    {
        this.campaign = campaign;
        this.parent = parent;
        this.alternateParent = alternateParent;        
    }

    public void makePanels() throws PWCGException 
    {
        campaignGeneratorState = new CampaignGeneratorState(campaignGeneratorDO);
        campaignGeneratorState.buildStateStack();

        try
        {
            setRightPanel (makeServicePanel());
            setCenterPanel(new CampaignGeneratorDataEntryEmpty());
            setLeftPanel(makeButtonPanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
    private JPanel makeServicePanel() throws PWCGException
    {
        String imagePath = getSideImageMain("CampaignGenNav.jpg");
        
        ImageResizingPanel servicesPanel = new ImageResizingPanel(imagePath);
        servicesPanel.setLayout(new BorderLayout());
        servicesPanel.setOpaque(true);

        CampaignGeneratorChooseServiceGUI campaignChooseServiceGUI = new CampaignGeneratorChooseServiceGUI(this);
        campaignChooseServiceGUI.makeServiceSelectionPanel();

        servicesPanel.add(campaignChooseServiceGUI, BorderLayout.NORTH);
     
        return servicesPanel;
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        String imagePath = getSideImageMain("CampaignGenNav.jpg");
        
        ImageResizingPanel configPanel = new ImageResizingPanel(imagePath);
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
        dataEntry = new CampaignGeneratorDataEntryGUI(this);
        dataEntry.makePanels();
        
        return dataEntry;
    }

    public List<ArmedService> getArmedServices() throws PWCGException
    {
        ArmedServiceFinder armedServiceFinder = new ArmedServiceFinder();
        return armedServiceFinder.getArmedServices();
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
                    parent.createPilotContext();
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
        String playerName = campaignGeneratorDO.getPlayerPilotName();
        String squadronName = campaignGeneratorDO.getSquadName();
        String rank = campaignGeneratorDO.getRank();
        String coopuser = campaignGeneratorDO.getCoopUser();

        ISquadronMemberReplacer squadronMemberReplacer = CampaignModeFactory.makeSquadronMemberReplacer(campaign);
        SquadronMember newSquadronMember = squadronMemberReplacer.createPersona(playerName, rank, squadronName, coopuser);
        
        campaign.write();        
        campaign.open(campaign.getCampaignData().getName());
        PWCGContext.getInstance().setCampaign(campaign);
        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            campaignGeneratorDO.createCoopUserAndPersona(campaign, newSquadronMember);
        }
    }

    public void changeService(ArmedService service) throws PWCGException
    {
        campaignGeneratorDO.setService(service);
        campaignGeneratorDO.setCampaignName(campaign.getCampaignData().getName());
        campaignGeneratorDO.setStartDate(campaign.getDate());
        campaignGeneratorDO.setCampaignMode(campaign.getCampaignData().getCampaignMode());
        campaignGeneratorDO.setService(service);
        
        dataEntry = new CampaignGeneratorDataEntryGUI(this);
        dataEntry.makePanels();
        dataEntry.evaluateUI();
        
        CampaignGuiContextManager.getInstance().changeCurrentContext(null, dataEntry, null);        
    }

    @Override
    public void setCampaignProfileParameters(CampaignMode campaignMode, String campaignName)
    {
    }

    @Override
    public CampaignGeneratorDO getCampaignGeneratorDO()
    {
        return campaignGeneratorDO;
    }

    @Override
    public CampaignGeneratorState getCampaignGeneratorState()
    {
        return campaignGeneratorState;
    }

    @Override
    public void evaluateCompletionState()
    {
        newPilotCreateButton.setEnabled(false);
        if (campaignGeneratorState.isComplete())
        {
            newPilotCreateButton.setEnabled(true);
        }
    }
}
