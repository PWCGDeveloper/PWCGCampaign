package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.tank.PwcgRole;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.maingui.campaigngenerate.NewCrewMemberState.CrewMemberGeneratorWorkflow;
import pwcg.gui.utils.PWCGJButton;
import pwcg.gui.utils.PWCGLabelFactory;

public class NewCrewMemberDataEntryGUI extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    private static final Color jComboBoxBackgroundColor = ColorMap.PAPER_BACKGROUND;
    private static final Color textBoxBackgroundColor = ColorMap.CHALK_FOREGROUND;
    private static final Color labelColorSelected = ColorMap.BRITISH_RED;
    private static final Color labelColorNotSelected = ColorMap.CHALK_FOREGROUND;
    
	private static Font font = null;
	
    private JTextField playerCrewMemberNameTextBox;
    private JTextField coopUserNameTextBox;
    
    private JTextArea squadronTextBox;
	private JComboBox<String> cbRole;
	private JComboBox<String> cbRank;
	private JComboBox<String> cbSquadron;
	private JComboBox<String> cbCoopUser;
    
    private JLabel lSquad;
    private JLabel lRank;
    private JLabel lCoopUser;
    private JLabel lRole;
    private JLabel lPlayerName;

    private CampaignNewCrewMemberScreen parent;
    private Campaign campaign;

	public NewCrewMemberDataEntryGUI(Campaign campaign, CampaignNewCrewMemberScreen parent) 
	{
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.parent = parent;
	}
	

	public void makePanels() throws PWCGException 
	{
	    font = PWCGMonitorFonts.getPrimaryFontLarge();

		try
		{			
			GridBagConstraints labelConstraints = new GridBagConstraints();
			labelConstraints.fill = GridBagConstraints.HORIZONTAL;
			labelConstraints.weightx = 0.1;
			labelConstraints.ipadx = 1;
			labelConstraints.ipady = 0;
			
			GridBagConstraints dataConstraints = new GridBagConstraints();
			dataConstraints.fill = GridBagConstraints.HORIZONTAL;
			dataConstraints.weightx = 0.3;
			dataConstraints.ipadx = 2;
			dataConstraints.ipady = 0;
			
			GridBagLayout campaignGenerateLayout = new GridBagLayout();
			JPanel campaignGeneratePanel = new JPanel(campaignGenerateLayout);
			campaignGeneratePanel.setOpaque(false);

			int rowCount = 0;
			for (int i = 0; i < 3; ++i)
			{
			    rowCount = spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, i);
			}
	        
            rowCount = createPlayerCrewMemberNameWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            
            rowCount = createCoopUserWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount = createCampaignRoleWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
			rowCount = createRankWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
			rowCount = createSquadronWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
			createNextStepWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

			rowCount = spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
			
			this.add(campaignGeneratePanel, BorderLayout.NORTH);
	          
            JPanel squadronPanel = createSquadronInfoPanel ();
            this.add(squadronPanel, BorderLayout.SOUTH);
            
            evaluateUI();
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private int createSquadronWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lSquad = createCampaignGenMenuLabel("Squadron", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lSquad, labelConstraints);

        cbSquadron = new JComboBox<String>();
        cbSquadron.setOpaque(false);
        cbSquadron.setBackground(jComboBoxBackgroundColor);
        cbSquadron.setActionCommand("SquadronChanged");
        cbSquadron.addActionListener(this);
        cbSquadron.setFont(font);

        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbSquadron, dataConstraints);
        
        spacerColumn (campaignGeneratePanel, 3, rowCount);
        ++rowCount;
        return rowCount;
    }

    private int createRankWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lRank = createCampaignGenMenuLabel("CrewMember Rank", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lRank, labelConstraints);

        cbRank = new JComboBox<String>();
        cbRank.setOpaque(false);
        cbRank.setBackground(jComboBoxBackgroundColor);
        cbRank.setFont(font);
        
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbRank, dataConstraints);

        cbRank.setSelectedIndex(cbRank.getItemCount()-1);
        cbRank.setActionCommand("RankChanged");
        cbRank.addActionListener(this);

        spacerColumn (campaignGeneratePanel, 3, rowCount);
        
        int serviceId = parent.getNewCrewMemberGeneratorDO().getService().getServiceId();
        Date campaignDate = campaign.getDate();
        ArmedService dateCorrectedService = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId, campaignDate);
        
        makeRankChoices(dateCorrectedService);
        

        ++rowCount;
        return rowCount;
    }

    private int createCoopUserWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            spacerColumn (campaignGeneratePanel, 0, rowCount + 0);
            
            lCoopUser = createCampaignGenMenuLabel("Coop User", labelConstraints, campaignGeneratePanel, rowCount);
            campaignGeneratePanel.add(lCoopUser, labelConstraints);

            cbCoopUser = new JComboBox<String>();
            for (CoopUser coopUsername : CoopUserManager.getIntance().getAllCoopUsers())
            {
                cbCoopUser.addItem(coopUsername.getUsername());
            }

            cbCoopUser.setOpaque(false);
            cbCoopUser.setBackground(jComboBoxBackgroundColor);
            cbCoopUser.setSelectedIndex(0);
            cbCoopUser.setActionCommand("CoopUserChanged");
            cbCoopUser.addActionListener(this);
            cbCoopUser.setFont(font);

            dataConstraints.gridx = 2;
            dataConstraints.gridy = rowCount;
            campaignGeneratePanel.add(cbCoopUser, dataConstraints);

            coopUserNameTextBox = new JTextField(50);
            coopUserNameTextBox.setFont(font);
            coopUserNameTextBox.setBackground(textBoxBackgroundColor);
            
            DocumentListener campaignNameTextBoxListener = new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateFieldState();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateFieldState();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateFieldState();
                }

                protected void updateFieldState() {
                    String coopUserNameFromTextBox = coopUserNameTextBox.getText();
                    parent.getNewCrewMemberGeneratorDO().setCoopUser(coopUserNameFromTextBox);
                }
            };
            coopUserNameTextBox.getDocument().addDocumentListener(campaignNameTextBoxListener);

            dataConstraints.gridx = 3;
            dataConstraints.gridy = rowCount;
            campaignGeneratePanel.add(coopUserNameTextBox, dataConstraints);

            ++rowCount;
        }
        
        return rowCount;
    }

	private int createNextStepWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        JLabel lNextStep = createCampaignGenMenuLabel("Next/Previous Step", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lNextStep, labelConstraints);

        Color fgColor = ColorMap.CHALK_FOREGROUND;

        String nextDisplayText = InternationalizationManager.getTranslation("Next Step");
        PWCGJButton nextStepButton = new PWCGJButton(nextDisplayText);      
        nextStepButton.setActionCommand("NextStep");
        nextStepButton.setOpaque(false);
        nextStepButton.setHorizontalAlignment(SwingConstants.LEFT);
        nextStepButton.addActionListener(this);
        nextStepButton.setBorderPainted(false);
        nextStepButton.setFocusPainted(false);
        nextStepButton.setForeground(fgColor);
        nextStepButton.setFont(font);
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(nextStepButton, dataConstraints);

        ++rowCount;

        String previousDisplayText = InternationalizationManager.getTranslation("Previous Step");
        PWCGJButton previousStepButton = new PWCGJButton(previousDisplayText);      
        previousStepButton.setActionCommand("PreviousStep");
        previousStepButton.setOpaque(false);
        previousStepButton.setHorizontalAlignment(SwingConstants.LEFT);
        previousStepButton.addActionListener(this);
        previousStepButton.setBorderPainted(false);
        previousStepButton.setFocusPainted(false);
        previousStepButton.setForeground(fgColor);
        previousStepButton.setFont(font);
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(previousStepButton, dataConstraints);
        
        ++rowCount;
        return rowCount;
    }

    private int createCampaignRoleWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lRole = createCampaignGenMenuLabel("Role", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lRole, labelConstraints);
        
        cbRole = new JComboBox<String>();
        
        setRolesInUI();
        
        cbRole.setOpaque(false);
        cbRole.setBackground(jComboBoxBackgroundColor);
        cbRole.setSelectedIndex(0);
        cbRole.addActionListener(this);
        cbRole.setActionCommand("RoleChanged");
        cbRole.setFont(font);

        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbRole, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount);

        ++rowCount;
        return rowCount;
    }

    private void setRolesInUI() throws PWCGException
    {
        final ActionListener[] actionListeners = cbRole.getActionListeners();
        for (final ActionListener listener : actionListeners)
        {
            cbRole.removeActionListener(listener);
        }   
        
        cbRole.removeAllItems();
        
        List<PwcgRoleCategory> availableRoles = getRoleCategoriesForService();
        if (availableRoles.size() > 0)
        {
            for (PwcgRoleCategory roleCategory : availableRoles)
            {
                cbRole.addItem(roleCategory.getRoleCategoryDescription());
            }
        }
        else
        {
            cbRole.addItem(PwcgRole.ROLE_FIGHTER.getRoleDescription());
        }
        
        cbRole.addActionListener(this);
    }

    private List<PwcgRoleCategory> getRoleCategoriesForService() throws PWCGException
    {
        Map<String, PwcgRoleCategory> rolesSorted = new TreeMap<>();
        
        Date date = campaign.getDate();
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getPlayerCompaniesByService(parent.getNewCrewMemberGeneratorDO().getService(), date);
        
        for (Company squadron : squadronsForService)
        {            
            PwcgRoleCategory primaryRoleCategory = squadron.determineSquadronPrimaryRoleCategory(date);

            rolesSorted.put(primaryRoleCategory.getRoleCategoryDescription(), primaryRoleCategory);
        }
        
        List<PwcgRoleCategory> roles = new ArrayList<>();
        roles.addAll(rolesSorted.values());
        
        return roles;
    }

    private int createPlayerCrewMemberNameWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);

        lPlayerName = createCampaignGenMenuLabel("CrewMember Name", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lPlayerName, labelConstraints);

        playerCrewMemberNameTextBox = new JTextField(50);
        playerCrewMemberNameTextBox.setFont(font);
        playerCrewMemberNameTextBox.setBackground(textBoxBackgroundColor);
        
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(playerCrewMemberNameTextBox, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount + 0);

        ++rowCount;
        return rowCount;
    }

    private JLabel createCampaignGenMenuLabel(String labelText, GridBagConstraints labelConstraints, JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        String displayText = InternationalizationManager.getTranslation(labelText);
        displayText += ": ";
        JLabel menuLabel = PWCGLabelFactory.makeTransparentLabel(displayText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);

        labelConstraints.gridx = 1;
        labelConstraints.gridy = rowCount;
        
        return menuLabel;
    }

	private int spacerFullRow (GridBagConstraints labelConstraints, GridBagConstraints dataConstraints, JPanel panel, int rowCount)
	{		
		labelConstraints.gridx = 0;
		labelConstraints.gridy = rowCount;
		panel.add(PWCGLabelFactory.makeDummyLabel(), labelConstraints);
		
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        panel.add(PWCGLabelFactory.makeDummyLabel(), dataConstraints);

        ++rowCount;
        return rowCount;
	}

    private JPanel createSquadronInfoPanel () throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        
        JPanel squadronPanel = new JPanel(new GridLayout(0,3));
        squadronPanel.setOpaque(false);

        squadronPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        // Squadron info
        squadronTextBox = new JTextArea();
        squadronTextBox.setBackground(bgColor);
        squadronTextBox.setForeground(fgColor);
        squadronTextBox.setFont(font);
        squadronTextBox.setEditable(false);
        squadronTextBox.setLineWrap(true);
        squadronTextBox.setWrapStyleWord(true);
        squadronTextBox.setText("");
        squadronTextBox.setOpaque(false);
        squadronPanel.add(squadronTextBox);

        squadronPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        return squadronPanel;
    }

	private void spacerColumn (JPanel panel, int column, int row)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.2;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.gridx = column;
		constraints.gridy = row;

		panel.add(PWCGLabelFactory.makeDummyLabel(), constraints);
	}

	public void evaluateUI() throws PWCGException 
	{
	    initializeWidgets();

        if (parent.getNewCrewMemberState().getCurrentStep() == CrewMemberGeneratorWorkflow.CHOOSE_PLAYER_NAME)
        {
            lPlayerName.setForeground(labelColorSelected);
    	    playerCrewMemberNameTextBox.setEnabled(true);

        }

        if (parent.getNewCrewMemberState().getCurrentStep() == CrewMemberGeneratorWorkflow.CHOOSE_COOP_USER)
        {
            this.lCoopUser.setForeground(labelColorSelected);
            cbCoopUser.setEnabled(true);
            coopUserNameTextBox.setEnabled(true);
        }

	    if (parent.getNewCrewMemberState().getCurrentStep() == CrewMemberGeneratorWorkflow.CHOOSE_ROLE)
	    {
	        setRolesInUI();
	        
	        String selectedRole = parent.getNewCrewMemberGeneratorDO().getRole().getRoleDescription();
	        
	        cbRole.setSelectedItem(selectedRole);
            
	        lRole.setForeground(labelColorSelected);
            cbRole.setEnabled(true);
	    }

	    if (parent.getNewCrewMemberState().getCurrentStep() == CrewMemberGeneratorWorkflow.CHOOSE_RANK)
	    {
	        cbRank.setSelectedItem(parent.getNewCrewMemberGeneratorDO().getRank());
	        lRank.setForeground(labelColorSelected);
            cbRank.setEnabled(true);
	    }

	    if (parent.getNewCrewMemberState().getCurrentStep() == CrewMemberGeneratorWorkflow.CHOOSE_SQUADRON)
	    {
	        lSquad.setForeground(labelColorSelected);
	        
            int serviceId = parent.getNewCrewMemberGeneratorDO().getService().getServiceId();
            ArmedService dateCorrectedService = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId, campaign.getDate());
            
	        makeSquadronChoices(dateCorrectedService);

	        String squadronName = (String)cbSquadron.getSelectedItem();
	        String squadronInfo = getSquadronInfo(campaign.getDate(), squadronName);
	        this.squadronTextBox.setText(squadronInfo);

            cbSquadron.setEnabled(true);
	    }
	    
	    if (parent.getNewCrewMemberState().isComplete())
	    {
	        parent.evaluateCompletionState();
	    }
	}

    private void initializeWidgets() throws PWCGException
    {
        parent.evaluateCompletionState();

        if (lCoopUser != null)
        {
            lCoopUser.setForeground(labelColorNotSelected);
            cbCoopUser.setEnabled(false);
            coopUserNameTextBox.setEnabled(false);
        }

	    playerCrewMemberNameTextBox.setEnabled(false);
	    cbRole.setEnabled(false);
        cbRank.setEnabled(false);
        cbSquadron.setEnabled(false);
        
        lPlayerName.setForeground(labelColorNotSelected);

        lRole.setForeground(labelColorNotSelected);
        lRank.setForeground(labelColorNotSelected);
        lSquad.setForeground(labelColorNotSelected);
    }

    private String getSquadronInfo(Date campaignDate, String squadronName) throws PWCGException 
    {
        if (squadronName == null)
        {
            return "";
        }

        Company company = PWCGContext.getInstance().getCompanyManager().getCompanyByName(squadronName, campaignDate);
        return company.determineSquadronInfo(campaignDate);
    }

	private void makeRankChoices(ArmedService dateCorrectedService) 
	{
		cbRank.removeActionListener(this);
		cbRank.removeAllItems();

		IRankHelper ranks = RankFactory.createRankHelper();
		List<String> rankList = ranks.getRanksByService(dateCorrectedService);
		for (int i = 0; i < rankList.size(); ++i)
		{
			cbRank.addItem(rankList.get(i));
			PWCGLogger.log(LogLevel.DEBUG, "Add Rank = " + rankList.get(i));
		}
		
		cbRank.addActionListener(this);
	}

	private void makeSquadronChoices(ArmedService dateCorrectedService) throws PWCGException 
	{
	    try
	    {
    		cbSquadron.removeAllItems();
            CampaignGeneratorCompanyFilter squadronFilter = new CampaignGeneratorCompanyFilter();
	        String selectedRole = (String)cbRole.getSelectedItem();
	        
	        FrontMapIdentifier campaignMap = PWCGContext.getInstance().getCurrentMap().getMapIdentifier();
            List<String> squadronNames = squadronFilter.makeSquadronChoices(campaign.getDate(), dateCorrectedService, campaignMap, selectedRole, parent.getNewCrewMemberGeneratorDO().isCommandRank());
            
            for (String squadronName : squadronNames)
            {
				cbSquadron.addItem(squadronName);
    		}
	    }
	    catch (Exception exp)
	    {
            PWCGLogger.logException(exp);
            throw exp;
	    }
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String playerName = (String)playerCrewMemberNameTextBox.getText();
 
            if (ae.getActionCommand().equalsIgnoreCase("RoleChanged"))
            {
                String roleDesc = (String)cbRole.getSelectedItem();
                PwcgRole role = PwcgRole.getRoleFromDescription(roleDesc);
                parent.getNewCrewMemberGeneratorDO().setRole(role);
            }
			else if (ae.getActionCommand().equalsIgnoreCase("RankChanged"))
			{
		        String rank = (String)cbRank.getSelectedItem();
		        parent.getNewCrewMemberGeneratorDO().setRank(rank);
			}
			else if (ae.getActionCommand().equalsIgnoreCase("CoopUserChanged"))
			{
		        String coopUserFromDropDown = (String)cbCoopUser.getSelectedItem();
                coopUserNameTextBox.setText(coopUserFromDropDown);
		        parent.getNewCrewMemberGeneratorDO().setCoopUser(coopUserFromDropDown);
			}
            else if (ae.getActionCommand().equalsIgnoreCase("SquadronChanged"))
            {
                String squadronName = (String)cbSquadron.getSelectedItem();
                if (squadronName != null)
                {
                    parent.getNewCrewMemberGeneratorDO().setSquadName(squadronName);
                    String squadronInfo = getSquadronInfo(campaign.getDate(), squadronName);
                    this.squadronTextBox.setText(squadronInfo);
                }
            }
            else if (ae.getActionCommand().equalsIgnoreCase("NextStep"))
            {
                parent.getNewCrewMemberState().goToNextStep();
                evaluateUI() ;
            }
            else if (ae.getActionCommand().equalsIgnoreCase("PreviousStep"))
            {
                parent.getNewCrewMemberState().goToPreviousStep();
                evaluateUI() ;
            }
            
            revalidate();
            repaint();
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}
