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
import pwcg.campaign.factory.CampaignModeFactory;
import pwcg.campaign.squadmember.ISquadronMemberReplacer;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class NewPilotGeneratorUI extends PwcgGuiContext implements ActionListener, IPilotGeneratorUI
{    
    private static final long serialVersionUID = 1L;

    private JButton newPilotCreateButton = null;
    private NewPilotDataEntryGUI dataEntry = null;
    private Campaign campaign;

    public NewPilotGeneratorUI(Campaign campaign)
    {
    	this.campaign = campaign;
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

    public void enableCompleteAction(boolean enabled)
    {
        newPilotCreateButton.setEnabled(enabled);
    }

    public JPanel makeDataEntryPanel() throws PWCGException 
    {
        dataEntry = new NewPilotDataEntryGUI(this);
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
                CampaignGuiContextManager.getInstance().popFromContextStack();
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
        campaignGeneratorDO.setCampaignName(campaign.getCampaignData().getName());
        campaignGeneratorDO.setStartDate(campaign.getDate());
        
        dataEntry = new NewPilotDataEntryGUI(this);
        dataEntry.setCampaignGeneratorDO(campaign, campaignGeneratorDO);
        dataEntry.makePanels();
        dataEntry.evaluateUI();
        
        CampaignGuiContextManager.getInstance().changeCurrentContext(null, dataEntry, null);
    }

    private void createPilot() throws PWCGUserException, Exception
    {
        CampaignGeneratorDO campaignGeneratorDO = dataEntry.getCampaignGeneratorDO();
        String playerName = campaignGeneratorDO.getPlayerPilotName();
        String squadronName = campaignGeneratorDO.getSquadName();
        String rank = campaignGeneratorDO.getRank();
        String coopuser = campaignGeneratorDO.getCoopUser();

        ISquadronMemberReplacer squadronMemberReplacer = CampaignModeFactory.makeSquadronMemberReplacer(campaign);
        squadronMemberReplacer.createPilot(playerName, rank, squadronName, coopuser);
        campaign.write();
    }
}
