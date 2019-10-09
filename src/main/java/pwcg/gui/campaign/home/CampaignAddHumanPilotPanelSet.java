package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberFactory;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignAddHumanPilotPanelSet extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;

	private Campaign campaign = null;
    private JComboBox<String> cbPilotRank;
    private JComboBox<String> cbReplacePilot;
    private JTextField playerNameTextBox;
    private Font font;

	public CampaignAddHumanPilotPanelSet  ()
	{
        super();
		this.campaign = PWCGContext.getInstance().getCampaign();
	}
	
	public void makeVisible(boolean visible) 
	{
	}

	public void makePanels() throws PWCGException  
	{
	    font = MonitorSupport.getPrimaryFontLarge();
	    setLeftPanel(makeAddPilotLeftPanel());
        setCenterPanel(makeAddPilotCenterPanel());
	}

	private JPanel makeAddPilotLeftPanel() throws PWCGException  
	{
        String imagePath = getSideImage("CampaignLeft.jpg");

		ImageResizingPanel leaverPanel = new ImageResizingPanel(imagePath);
		leaverPanel.setLayout(new BorderLayout());
		leaverPanel.setOpaque(false);

		JPanel leaveButtonPanel = new JPanel(new GridLayout(0,1));
		leaveButtonPanel.setOpaque(false);
		
        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Accept New Pilot", "Accept New Pilot", this);
        leaveButtonPanel.add(acceptButton);

        JButton rejectButton = PWCGButtonFactory.makeMenuButton("Reject New Pilot", "Reject New Pilot", this);
        leaveButtonPanel.add(rejectButton);
		
		leaverPanel.add(leaveButtonPanel, BorderLayout.NORTH);
		
		return leaverPanel;
	}

    private void createRankWidget() throws PWCGException
    {
        cbPilotRank = new JComboBox<String>();
        cbPilotRank.setOpaque(false);
        cbPilotRank.setBackground(ColorMap.PAPER_BACKGROUND);
        cbPilotRank.setFont(font);

        IRankHelper rankHelper = RankFactory.createRankHelper();
    	SquadronMember referencePlayer = PWCGContext.getInstance().getReferencePlayer();
        for (String rank : rankHelper.getRanksByService(referencePlayer.determineSquadron().determineServiceForSquadron(campaign.getDate())))
        {
            cbPilotRank.addItem(rank);
        }        

        cbPilotRank.setSelectedIndex(0);
    }


    private void createPlayerNameWidget() throws PWCGException
    {
        playerNameTextBox = new JTextField(50);
        playerNameTextBox.setFont(font);
        playerNameTextBox.setBackground(ColorMap.PAPER_BACKGROUND);
    }

    private JPanel makeAddPilotCenterPanel() throws PWCGException  
	{        
        ImageResizingPanel newPilotCenterPanel = null;
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
        newPilotCenterPanel = new ImageResizingPanel(imagePath);
        newPilotCenterPanel.setLayout(new BorderLayout());

        JPanel newPilotMainGridPanel = makeNewPilotMainGrid();
        newPilotCenterPanel.add(newPilotMainGridPanel, BorderLayout.NORTH);
		return newPilotCenterPanel;
	}

    private JPanel makeNewPilotMainGrid() throws PWCGException
    {
        JPanel newPilotInfoPanel = makePilotNamePanel();
        JPanel newPilotReplacePanel = makePilotReplacePanel();

        JPanel newPilotMainGridPanel = new JPanel(new GridLayout(0, 1));
        newPilotMainGridPanel.setOpaque(false);

        int numDummyRows = 8;
        for (int i = 0; i < numDummyRows; ++i)
        {
            addSpacer(newPilotMainGridPanel);
        }
        
        newPilotMainGridPanel.add(newPilotInfoPanel);
        newPilotMainGridPanel.add(newPilotReplacePanel);
        
        return newPilotMainGridPanel;
    }

    private JPanel makePilotNamePanel() throws PWCGException
    {
        createRankWidget();
        createPlayerNameWidget();

        JPanel newPilotInfoPanel = new JPanel(new GridLayout(1, 0));
        newPilotInfoPanel.setOpaque(false);

        JLabel lNewPilot = new JLabel("New Pilot:      ", JLabel.RIGHT);
        lNewPilot.setOpaque(false);
        lNewPilot.setFont(font);
        newPilotInfoPanel.add(lNewPilot);

        newPilotInfoPanel.add(cbPilotRank);
        newPilotInfoPanel.add(playerNameTextBox);
        
        addSpacer(newPilotInfoPanel);

        return newPilotInfoPanel;
    }

    private JPanel makePilotReplacePanel() throws PWCGException
    {
        makePilotReplaceCB();
        JPanel newPilotReplacePanel = new JPanel(new GridLayout(1, 0));
        newPilotReplacePanel.setOpaque(false);

        JLabel lPilotToReplace = new JLabel("Pilot To Remove:      ", JLabel.RIGHT);
        lPilotToReplace.setOpaque(false);
        lPilotToReplace.setFont(font);
        newPilotReplacePanel.add(lPilotToReplace);
        
        newPilotReplacePanel.add(cbReplacePilot);
        addSpacer(newPilotReplacePanel);
        addSpacer(newPilotReplacePanel);
        
        return newPilotReplacePanel;
    }

    private void makePilotReplaceCB() throws PWCGException
    {
        cbReplacePilot = new JComboBox<String>();
        cbReplacePilot.setOpaque(false);
        cbReplacePilot.setBackground(ColorMap.PAPER_BACKGROUND);
        cbReplacePilot.setFont(font);
        
    	SquadronMember referencePlayer = PWCGContext.getInstance().getReferencePlayer();
    	Map<Integer, SquadronMember> squadronMemberCollection = campaign.getPersonnelManager().getSquadronPersonnel(referencePlayer.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection();
        SquadronMembers aiSquadronMembers = SquadronMemberFilter.filterActiveAI(squadronMemberCollection, campaign.getDate());
        for (SquadronMember aiSquadronMember : aiSquadronMembers.sortPilots(campaign.getDate()))
        {
            cbReplacePilot.addItem(aiSquadronMember.getSerialNumber() + ":" + aiSquadronMember.getNameAndRank());
        }

        cbReplacePilot.setSelectedIndex(0);
    }

    private void addSpacer(JPanel panel)
    {
        JLabel lDummy = new JLabel("     ");
        lDummy.setOpaque(false);
        panel.add(lDummy);
    }

    public void actionPerformed(ActionEvent ae)
    {
        String action = ae.getActionCommand();

        try
        {
            if (action.contains("Accept"))
            {
                addNewPilot();
                SoundManager.getInstance().playSound("Stapling.WAV");
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.contains("Reject"))
            {
                SoundManager.getInstance().playSound("Stapling.WAV");
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void addNewPilot() throws PWCGException
    {
        String rank = (String)cbPilotRank.getSelectedItem();
        String playerName = playerNameTextBox.getText();
        String replaceInfo = (String)cbReplacePilot.getSelectedItem();
        String[] replaceInfoArray = replaceInfo.split(":");
        int serialNumberToReplace = new Integer(replaceInfoArray[0]);
        
    	SquadronMember referencePlayer = PWCGContext.getInstance().getReferencePlayer();
    	SquadronPersonnel playerSquadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(referencePlayer.getSquadronId());

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setPlayerRank(rank);
        generatorModel.setPlayerName(playerName);
        generatorModel.setService(referencePlayer.determineSquadron().determineServiceForSquadron(campaign.getDate()));
        
        SquadronMemberFactory squadronMemberFactory = new SquadronMemberFactory(campaign, referencePlayer.determineSquadron(), playerSquadronPersonnel);
        SquadronMember newPlayer = squadronMemberFactory.createPlayer(generatorModel);

        playerSquadronPersonnel.addSquadronMember(newPlayer);
        
        SquadronMember aiToRemove = playerSquadronPersonnel.getSquadronMember(serialNumberToReplace);
        playerSquadronPersonnel.removeSquadronMember(aiToRemove);
    }
}
