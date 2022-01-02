package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignInitialWriter;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.PwcgMainScreen;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignGeneratorScreen extends ImageResizingPanel implements ActionListener
{    
    private static final long serialVersionUID = 1L;

    private PwcgMainScreen mainGUI = null;
    private PwcgThreePanelUI pwcgThreePanel;
    private JButton profileFinishedButton = null;
    private JButton createCampaignButton = null;
    private CampaignGeneratorProfileGUI campaignProfileUI = null;
    private CampaignGeneratorDataEntryGUI campaignGeneratorDataEntryGUI = null;
    private CampaignGeneratorState campaignGeneratorState;

    private CampaignGeneratorDO campaignGeneratorDO = new CampaignGeneratorDO();

    public CampaignGeneratorScreen(PwcgMainScreen mainGUI)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

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
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignGeneratorScreen);
            this.setImageFromName(imagePath);

            pwcgThreePanel.setLeftPanel(makeButtonPanel());
            pwcgThreePanel.setCenterPanel(makeCampaignProfilePanel());
            pwcgThreePanel.setRightPanel (makeProceedButtonPanel());
            CampaignGuiContextManager.getInstance().pushToContextStack(this);
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setOpaque(false);
         
        createCampaignButton = PWCGButtonFactory.makeTranslucentMenuButtonGrayMenu("Create Campaign", "Create Campaign", "Create the campaign", this);
        buttonPanel.add(createCampaignButton);
        createCampaignButton.setEnabled(false);
 
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        JButton cancelChanges = PWCGButtonFactory.makeTranslucentMenuButtonGrayMenu("Cancel", "Cancel", "Cancel campaign creation", this);
        buttonPanel.add(cancelChanges);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
     
        return navPanel;
    }

    private JPanel makeProceedButtonPanel() throws PWCGException
    {
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setOpaque(false);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setOpaque(false);
        
        profileFinishedButton = PWCGButtonFactory.makeTranslucentMenuButtonGrayMenu("Complete Data Entry", "Complete", "Proceed to campaign data entry completion", this);
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
                CampaignGuiContextManager.getInstance().backToMain();
            }
            else if (action.equalsIgnoreCase("Complete"))
            {
                proceedToCampaignCrewMemberInput();
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

    private void proceedToCampaignCrewMemberInput() throws PWCGException
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

        CrewMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (CrewMember player : players.getCrewMemberList())
        {
            campaignGeneratorDO.createCoopUserAndPersona(campaign, player);
        }
        
        CampaignGuiContextManager.getInstance().backToMain();
        CampaignHomeScreen campaignHome = new CampaignHomeScreen (mainGUI, campaign);
        CampaignGuiContextManager.getInstance().pushToContextStack(campaignHome);
    }

    private JPanel makeProfileInfoPanel() throws PWCGException
    {
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
        String playerName = campaignGeneratorDO.getPlayerCrewMemberName();
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
