package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.io.json.CoopPilotIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.CampaignMainGUI;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGFrame;

public class CampaignGeneratorPanelSet extends PwcgGuiContext implements ActionListener
{    
    private static final long serialVersionUID = 1L;

    private CampaignMainGUI mainGUI = null;
    private JButton createCampaignButton = null;
    private CampaignGeneratorDataEntryGUI dataEntry = null;

    public CampaignGeneratorPanelSet(CampaignMainGUI mainGUI)
    {
        this.mainGUI = mainGUI;
    }

    public void makePanels() 
    {
        try
        {
            setCenterPanel(makeDataEntryPanel());
            setRightPanel (makeServicePanel());
            setLeftPanel(makeButtonPanel());
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeServicePanel() throws PWCGException
    {
        String imagePath = getSideImageMain("CampaignGenLeft.jpg");
        
        CampaignGeneratorChooseServiceGUI campaignChooseServiceGUI = new CampaignGeneratorChooseServiceGUI(this);
        campaignChooseServiceGUI.makeServiceSelectionPanel(imagePath);
        return campaignChooseServiceGUI;
    }

    private JPanel makeButtonPanel() throws PWCGException
    {
        String imagePath = getSideImageMain("CampaignGenNav.jpg");
        
        ImageResizingPanel configPanel = new ImageResizingPanel(imagePath);
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

    public void enableCreateCampaign(boolean enabled)
    {
        createCampaignButton.setEnabled(enabled);
    }

    public JPanel makeDataEntryPanel() throws PWCGException 
    {
        dataEntry = new CampaignGeneratorDataEntryGUI(this);
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
            else if (action.equalsIgnoreCase("Create Campaign"))
            {
                Campaign campaign = makeCampaign();
            
                CampaignGeneratorDO campaignGeneratorDO = dataEntry.getCampaignGeneratorDO();
                campaign.open(campaignGeneratorDO.getCampaignName());                    
                PWCGContextManager.getInstance().setCampaign(campaign);
                
                CampaignHomeGUI campaignGUI = new CampaignHomeGUI (mainGUI, campaign);
                PWCGFrame.getInstance().setPanel(campaignGUI);
                
                return;
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public void changeService(ArmedService service) throws PWCGException
    {
        CampaignGeneratorDO campaignGeneratorDO = new CampaignGeneratorDO();
        campaignGeneratorDO.setService(service);
        
        dataEntry = new CampaignGeneratorDataEntryGUI(this);
        dataEntry.setCampaignGeneratorDO(campaignGeneratorDO);
        dataEntry.makePanels();
        dataEntry.evaluateUI();
        
        CampaignGuiContextManager.getInstance().changeCurrentContext(null, dataEntry, null);
    }

    private Campaign makeCampaign() throws PWCGUserException, Exception
    {
        CampaignGeneratorDO campaignGeneratorDO = dataEntry.getCampaignGeneratorDO();
        
        ArmedService service = campaignGeneratorDO.getService();
        String campaignName = campaignGeneratorDO.getCampaignName();
        String playerName = campaignGeneratorDO.getPlayerName();
        String region = campaignGeneratorDO.getRegion();
        String squadronName = campaignGeneratorDO.getSquadName();
        String rank = campaignGeneratorDO.getRank();
        Date startDate = campaignGeneratorDO.getStartDate();
        boolean isCoop = campaignGeneratorDO.isCoop();

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(startDate);
        generatorModel.setCampaignName(campaignName);
        generatorModel.setPlayerName(playerName);
        generatorModel.setPlayerRank(rank);
        generatorModel.setPlayerRegion(region);
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        generatorModel.setCoop(isCoop);

        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();
        
        writeCampaign(campaign);
        
        return campaign;
    }
    

    private void writeCampaign(Campaign campaign) throws PWCGException
    {
        campaign.write();
        if (campaign.getCampaignData().isCoop())
        {
        	SquadronMember hostPilot = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList().get(0);
        	
        	CoopPilot hostPilotRecord = new CoopPilot();
        	hostPilotRecord.setApproved(true);
        	hostPilotRecord.setCampaignName(campaign.getCampaignData().getName());
        	hostPilotRecord.setNote("Auto create host pilot");
        	hostPilotRecord.setPilotName(hostPilot.getName());
        	hostPilotRecord.setPilotRank(hostPilot.getRank());
        	hostPilotRecord.setSerialNumber(hostPilot.getSerialNumber());
        	hostPilotRecord.setSquadronId(hostPilotRecord.getSquadronId());
        	hostPilotRecord.setUsername("Host");
        	
        	CoopPilotIOJson.writeJson(hostPilotRecord);
        }
        PWCGContextManager.getInstance().setCampaign(campaign);
    }
}
