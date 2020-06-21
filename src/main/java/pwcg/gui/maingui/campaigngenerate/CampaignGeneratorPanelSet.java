package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignInitialWriter;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.CampaignMainGUI;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGFrame;

public class CampaignGeneratorPanelSet extends JPanel implements ActionListener
{    
    private static final long serialVersionUID = 1L;

    private CampaignMainGUI mainGUI = null;
    private PwcgThreePanelUI pwcgThreePanel;
    private JButton profileFinishedButton = null;
    private JButton createCampaignButton = null;
    private CampaignGeneratorProfileGUI campaignProfileUI = null;
    private CampaignGeneratorDataEntryGUI campaignGeneratorDataEntryGUI = null;
    private CampaignGeneratorState campaignGeneratorState;

    private CampaignGeneratorDO campaignGeneratorDO = new CampaignGeneratorDO();

    public CampaignGeneratorPanelSet(CampaignMainGUI mainGUI)
    {
        super();
        this.setLayout(new BorderLayout());
        this.mainGUI = mainGUI;

        pwcgThreePanel = new PwcgThreePanelUI(this);

        campaignProfileUI = new CampaignGeneratorProfileGUI(this);
        campaignGeneratorDataEntryGUI = new CampaignGeneratorDataEntryGUI(this);
        campaignGeneratorState = new CampaignGeneratorState(campaignGeneratorDO);
    }

    public void makePanels() 
    {
        try
        {
            pwcgThreePanel.setCenterPanel(makeCampaignProfilePanel());
            pwcgThreePanel.setRightPanel (makeProceedButtonPanel());

            this.add(BorderLayout.WEST, makeButtonPanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getSideImageMain("CampaignGenNav.jpg");
        
        ImageResizingPanel configPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        configPanel.setLayout(new BorderLayout());
        configPanel.setOpaque(true);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setOpaque(false);
         
        createCampaignButton = PWCGButtonFactory.makeMenuButton("Create Campaign", "Create Campaign", this);
        buttonPanel.add(createCampaignButton);
        createCampaignButton.setEnabled(false);
        
        JLabel dummyLabel3 = new JLabel("     ");       
        dummyLabel3.setOpaque(false);
        buttonPanel.add(dummyLabel3);
        
        JButton cancelChanges = PWCGButtonFactory.makeMenuButton("Cancel", "Cancel", this);
        buttonPanel.add(cancelChanges);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return configPanel;
    }

    private JPanel makeProceedButtonPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getSideImageMain("CampaignGenNav.jpg");
        
        ImageResizingPanel configPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        configPanel.setLayout(new BorderLayout());
        configPanel.setOpaque(true);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setOpaque(false);
        
        profileFinishedButton = PWCGButtonFactory.makeMenuButton("Complete Campaign Data Entry", "Complete Campaign Data Entry", this);
        buttonPanel.add(profileFinishedButton);
        profileFinishedButton.setEnabled(false);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return configPanel;
    }

    public JPanel makeCampaignProfilePanel() throws PWCGException 
    {
        campaignProfileUI.makePanels();
        return campaignProfileUI;
    }

    public JPanel makeCampaignDataEntryPanel() throws PWCGException 
    {
        campaignGeneratorDataEntryGUI.makePanels();
        return campaignGeneratorDataEntryGUI;
    }
    
    public List<ArmedService> getArmedServices() throws PWCGException
    {
        List<ArmedService> allServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
        return allServices;
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
            else if (action.equalsIgnoreCase("Complete Campaign Data Entry"))
            {
                proceedToCampaignPilotInput();
            }
            else if (action.equalsIgnoreCase("Create Campaign"))
            {
                buildNewCampaign();
                return;
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void proceedToCampaignPilotInput() throws PWCGException
    {
        campaignGeneratorState.buildStateStack();

        pwcgThreePanel.setCenterPanel(makeCampaignDataEntryPanel());
        pwcgThreePanel.setRightPanel (makeProfileInfoPanel());

        CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
    }

    private void buildNewCampaign() throws PWCGException, PWCGUserException, Exception
    {        
        Campaign campaign = makeCampaign();
         
        campaign.open(campaignGeneratorDO.getCampaignName());                    
        PWCGContext.getInstance().setCampaign(campaign);
        
        CampaignHomeGUI campaignGUI = new CampaignHomeGUI (mainGUI, campaign);
        PWCGFrame.getInstance().setPanel(campaignGUI);

        SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (SquadronMember player : players.getSquadronMemberList())
        {
            campaignGeneratorDO.createCoopUserAndPersona(campaign, player);
        }
    }

    private JPanel makeProfileInfoPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getSideImageMain("CampaignGenNav.jpg");
        CampaignGeneratorProfileInfoGUI profileInfoPanel = new CampaignGeneratorProfileInfoGUI(this, imagePath);
        profileInfoPanel.makePanels();
        return profileInfoPanel;
    }

    public void changeService(ArmedService service) throws PWCGException
    {
        campaignGeneratorDO.setService(service);                
        this.add(BorderLayout.CENTER, campaignProfileUI);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
        evaluateCompletionState();
    }

    private Campaign makeCampaign() throws PWCGUserException, Exception
    {        
        ArmedService service = campaignGeneratorDO.getService();
        String campaignName = campaignGeneratorDO.getCampaignName();
        String playerName = campaignGeneratorDO.getPlayerPilotName();
        String region = campaignGeneratorDO.getRegion();
        String squadronName = campaignGeneratorDO.getSquadName();
        String rank = campaignGeneratorDO.getRank();
        Date startDate = campaignGeneratorDO.getStartDate();
        CampaignMode campaignMode =  campaignGeneratorDO.getCampaignMode();

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignMode(campaignMode);
        generatorModel.setCampaignDate(startDate);
        generatorModel.setCampaignName(campaignName);
        generatorModel.setPlayerName(playerName);
        generatorModel.setPlayerRank(rank);
        generatorModel.setPlayerRegion(region);
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);

        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();
        
        CampaignInitialWriter.doInitialCampaignWrite(campaign);
        
        return campaign;
    }

    public void setCampaignProfileParameters(CampaignMode campaignMode, String campaignName)
    {
        campaignGeneratorDO.setCampaignMode(campaignMode);
        campaignGeneratorDO.setCampaignName(campaignName);
        evaluateCompletionState();
    }
    
    public CampaignGeneratorDO getCampaignGeneratorDO()
    {
        return campaignGeneratorDO;
    }
    
    public CampaignGeneratorState getCampaignGeneratorState()
    {
        return campaignGeneratorState;
    }

    public void evaluateCompletionState()
    {
        profileFinishedButton.setEnabled(false);
        createCampaignButton.setEnabled(false);
        
        if (campaignGeneratorState.isComplete())
        {
            createCampaignButton.setEnabled(true);
        }
        else if (campaignGeneratorState.isProfileComplete())
        {
            profileFinishedButton.setEnabled(true);
        }
    }
}
