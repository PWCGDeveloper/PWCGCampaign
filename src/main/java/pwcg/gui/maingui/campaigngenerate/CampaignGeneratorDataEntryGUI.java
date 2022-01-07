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
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.tank.PwcgRole;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.maingui.campaigngenerate.CampaignGeneratorState.CampaignGeneratorWorkflow;
import pwcg.gui.utils.PWCGJButton;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignGeneratorDataEntryGUI extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    private static final Color jComboBoxBackgroundColor = ColorMap.PAPER_BACKGROUND;
    private static final Color textBoxBackgroundColor = ColorMap.CHALK_FOREGROUND;
    private static final Color labelColorSelected = ColorMap.BRITISH_RED;
    private static final Color labelColorNotSelected = ColorMap.CHALK_FOREGROUND;
    
    private static Font font = null;

    private JTextField playerNameTextBox;
    private JTextField coopUserNameTextBox;
    
    private JComboBox<String> cbCoopUser;
	private JComboBox<String> cbMap;
	private JComboBox<String> cbDate;
	private JComboBox<String> cbRole;
	private JComboBox<String> cbRank;
	private JComboBox<String> cbCompany;
    
    private JLabel lPlayerName;
    private JLabel lCoopUser;
    private JLabel lMap;
    private JLabel lDate;
    private JLabel lRole;
    private JLabel lRank;
    private JLabel lCompany;

    private JTextArea companyTextBox;

    private CampaignGeneratorScreen parent = null;

	public CampaignGeneratorDataEntryGUI(CampaignGeneratorScreen parent) 
	{
        super();
        this.setOpaque(false);
		this.setLayout(new BorderLayout());

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

            rowCount = createPlayerNameWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount = creatCoopUserWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount = createCampaignMapWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount = createCampaignStartDateWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

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
	          
            JPanel companyPanel = createSquadronInfoPanel ();
            this.add(companyPanel, BorderLayout.SOUTH);
            
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
        
        lCompany = createCampaignGenMenuLabel("Squadron", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lCompany, labelConstraints);

        cbCompany = new JComboBox<String>();
        cbCompany.setOpaque(false);
        cbCompany.setBackground(jComboBoxBackgroundColor);
        cbCompany.setActionCommand("CompanyChanged");
        cbCompany.addActionListener(this);
        cbCompany.setFont(font);

        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbCompany, dataConstraints);
        
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
        
        int serviceId = parent.getCampaignGeneratorDO().getService().getServiceId();
        Date campaignDate = parent.getCampaignGeneratorDO().getStartDate();
        ArmedService dateCorrectedService = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId, campaignDate);
        
        makeRankChoices(dateCorrectedService);

        ++rowCount;
        return rowCount;
    }

    private int createNextStepWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        JLabel lNextStep = createCampaignGenMenuLabel("Next/Previous Step", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lNextStep, labelConstraints);

        Color fgColor = ColorMap.CHALK_FOREGROUND;

        PWCGJButton nextStepButton = makeButton("Next Step", "NextStep", fgColor);        
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(nextStepButton, dataConstraints);
        ++rowCount;

        PWCGJButton previousStepButton = makeButton("Previous Step", "PreviousStep", fgColor);
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(previousStepButton, dataConstraints);
        ++rowCount;

        return rowCount;
    }

    private PWCGJButton makeButton(String displayText, String commandText, Color fgColor) throws PWCGException
    {
        displayText = InternationalizationManager.getTranslation(displayText);
        PWCGJButton nextStepButton = new PWCGJButton(displayText);      
        nextStepButton.setActionCommand(commandText);
        nextStepButton.setOpaque(false);
        nextStepButton.setHorizontalAlignment(SwingConstants.LEFT);
        nextStepButton.addActionListener(this);
        nextStepButton.setBorderPainted(false);
        nextStepButton.setFocusPainted(false);
        nextStepButton.setForeground(fgColor);
        nextStepButton.setFont(font);
        return nextStepButton;
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
        
        List<PwcgRoleCategory> availableRoles = getRolesForService();
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

    private List<PwcgRoleCategory> getRolesForService() throws PWCGException
    {
        Map<String, PwcgRoleCategory> rolesSorted = new TreeMap<>();
        
        Date date = parent.getCampaignGeneratorDO().getStartDate();
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companiesForService = companyManager.getPlayerCompaniesByService(parent.getCampaignGeneratorDO().getService(), date);
        
        for (Company company : companiesForService)
        {            
            PwcgRoleCategory primaryRoleCategory = company.determineSquadronPrimaryRoleCategory(date);

            rolesSorted.put(primaryRoleCategory.getRoleCategoryDescription(), primaryRoleCategory);
        }
        
        List<PwcgRoleCategory> roles = new ArrayList<PwcgRoleCategory>();
        roles.addAll(rolesSorted.values());
        
        return roles;
    }

    private int createPlayerNameWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);

        lPlayerName = createCampaignGenMenuLabel("Player Name:", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lPlayerName, labelConstraints);

        playerNameTextBox = new JTextField(50);
        playerNameTextBox.setFont(font);
        playerNameTextBox.setBackground(textBoxBackgroundColor);
        
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(playerNameTextBox, dataConstraints);

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

    private int creatCoopUserWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        if (parent.getCampaignGeneratorDO().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            spacerColumn (campaignGeneratePanel, 0, rowCount + 0);
            
            lCoopUser = createCampaignGenMenuLabel("Coop User", labelConstraints, campaignGeneratePanel, rowCount);
            campaignGeneratePanel.add(lCoopUser, labelConstraints);

            cbCoopUser = new JComboBox<String>();
            cbCoopUser.insertItemAt("", 0);
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
            
            makeCoopUserNameTextDocumentListener();

            dataConstraints.gridx = 3;
            dataConstraints.gridy = rowCount;
            campaignGeneratePanel.add(coopUserNameTextBox, dataConstraints);

            ++rowCount;
        }
        
        return rowCount;
    }


    private void makeCoopUserNameTextDocumentListener()
    {
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
                parent.getCampaignGeneratorDO().setCoopUser(coopUserNameFromTextBox);
            }
        };
        coopUserNameTextBox.getDocument().addDocumentListener(campaignNameTextBoxListener);
    }

    private int createCampaignMapWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
	        JPanel campaignGeneratePanel, int rowCount) throws PWCGException
	{
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lMap = createCampaignGenMenuLabel("Campaign Map", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lMap, labelConstraints);
        
        cbMap = new JComboBox<String>();
        cbMap.addItem("All Maps");
        for (PWCGMap map : PWCGContext.getInstance().getAllMaps())
        {
            cbMap.addItem(map.getMapName());
        }
        
        cbMap.setOpaque(false);
        cbMap.setBackground(jComboBoxBackgroundColor);
        cbMap.setSelectedIndex(0);
        cbMap.setActionCommand("MapChanged");
        cbMap.addActionListener(this);
        cbMap.setFont(font);

        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbMap, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount);

        ++rowCount;

        return rowCount;
	}

    private int createCampaignStartDateWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lDate = createCampaignGenMenuLabel("Campaign Start Date", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lDate, labelConstraints);
        
        cbDate = new JComboBox<String>();
        makeStartDateChoices();
        
        cbDate.setOpaque(false);
        cbDate.setBackground(jComboBoxBackgroundColor);
        cbDate.setSelectedIndex(0);
        cbDate.setActionCommand("DateChanged");
        cbDate.addActionListener(this);
        cbDate.setFont(font);

        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbDate, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount);

        ++rowCount;

        return rowCount;
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
        
        JPanel companyPanel = new JPanel(new GridLayout(0,3));
        companyPanel.setOpaque(false);

        companyPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        // Squadron info
        companyTextBox = new JTextArea();
        companyTextBox.setBackground(bgColor);
        companyTextBox.setForeground(fgColor);
        companyTextBox.setFont(font);
        companyTextBox.setEditable(false);
        companyTextBox.setLineWrap(true);
        companyTextBox.setWrapStyleWord(true);
        companyTextBox.setText("");
        companyTextBox.setOpaque(false);
        companyPanel.add(companyTextBox);

        companyPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        return companyPanel;
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

        if (parent.getCampaignGeneratorState().getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME)
        {
            lPlayerName.setForeground(labelColorSelected);
            playerNameTextBox.setEnabled(true);
        }

        else if (parent.getCampaignGeneratorState().getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_COOP_USER)
        {
            lCoopUser.setForeground(labelColorSelected);
            cbCoopUser.setEnabled(true);
            coopUserNameTextBox.setEnabled(true);
        }

        else if (parent.getCampaignGeneratorState().getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_ROLE)
	    {
	        setRolesInUI();
	        
	        String selectedRole = parent.getCampaignGeneratorDO().getRole().getRoleDescription();
	        
	        cbRole.setSelectedItem(selectedRole);
            
	        lRole.setForeground(labelColorSelected);
            cbRole.setEnabled(true);
	    }

        else if (parent.getCampaignGeneratorState().getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_MAP)
	    {
            makeMapChoices();
	        lMap.setForeground(labelColorSelected);
            cbMap.setEnabled(true);
	    }

        else if (parent.getCampaignGeneratorState().getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_DATE)
	    {
	    	makeStartDateChoices();
	    	lDate.setForeground(labelColorSelected);
            cbDate.setEnabled(true);
	    }

        else if (parent.getCampaignGeneratorState().getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_RANK)
	    {
	        cbRank.setSelectedItem(parent.getCampaignGeneratorDO().getRank());
	        lRank.setForeground(labelColorSelected);
            cbRank.setEnabled(true);
	    }

        else if (parent.getCampaignGeneratorState().getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_SQUADRON)
	    {
	        lCompany.setForeground(labelColorSelected);
	        
            int serviceId = parent.getCampaignGeneratorDO().getService().getServiceId();
            Date campaignDate = parent.getCampaignGeneratorDO().getStartDate();
            ArmedService dateCorrectedService = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId, campaignDate);
            
	        makeCompanyChoices(campaignDate, dateCorrectedService);

	        String companyName = (String)cbCompany.getSelectedItem();
	        String companyInfo = getSquadronInfo(campaignDate, companyName);
	        this.companyTextBox.setText(companyInfo);

            cbCompany.setEnabled(true);
	    }
	}

    private void initializeWidgets()
    {	    
        if (lCoopUser != null)
        {
            lCoopUser.setForeground(labelColorNotSelected);
            cbCoopUser.setEnabled(false);
            coopUserNameTextBox.setEnabled(false);
        }
        
	    playerNameTextBox.setEnabled(false);
        cbRole.setEnabled(false);
        cbMap.setEnabled(false);
        cbDate.setEnabled(false);
        cbRank.setEnabled(false);
        cbCompany.setEnabled(false);

        lPlayerName.setForeground(labelColorNotSelected);
        lRole.setForeground(labelColorNotSelected);
        lMap.setForeground(labelColorNotSelected);
        lDate.setForeground(labelColorNotSelected);
        lRank.setForeground(labelColorNotSelected);
        lCompany.setForeground(labelColorNotSelected);
    }

    private String getSquadronInfo(Date campaignDate, String companyName) throws PWCGException 
    {
        if (companyName == null)
        {
            return "";
        }

        Company company = PWCGContext.getInstance().getCompanyManager().getCompanyByName(companyName, campaignDate);
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

	private void makeStartDateChoices() throws PWCGException 
	{
	    try
	    {
    		cbDate.removeAllItems();
	    	for (String startDate : getDatesForMap())
	    	{
	    		cbDate.addItem(startDate);
	    	}
	    }
	    catch (Exception exp)
	    {
            PWCGLogger.logException(exp);
            throw exp;
	    }
	}
	
	private void makeMapChoices() throws PWCGException 
	{
	    try
	    {
	        cbMap.removeAllItems();
	        cbMap.addItem("All Maps");
	        for (PWCGMap map : PWCGContext.getInstance().getAllMaps())
	        {
	            if (map.isMapHasService(parent.getCampaignGeneratorDO().getService().getServiceId())) 
	            {
	                cbMap.addItem(map.getMapName());
	            }
	        }
	    }
	    catch (Exception exp)
	    {
	        PWCGLogger.logException(exp);
	        throw exp;
	    }
	}

	
    
    private List<String> getDatesForMap() throws PWCGException
    {
    	List<String> startDates = new ArrayList<>();
        for (String startDate : PWCGContext.getInstance().getCampaignStartDates())
        {
            Date date = DateUtils.getDateWithValidityCheck(startDate);            
            if (!parent.getCampaignGeneratorDO().getService().getServiceStartDate().after(date) && 
                 parent.getCampaignGeneratorDO().getService().getServiceEndDate().after(date))
            {
            	PWCGMap map = PWCGContext.getInstance().getMapByMapId(parent.getCampaignGeneratorDO().getFrontMap());
            	if (map == null)
            	{
                	startDates.add(startDate);
            	}
            	else
            	{
            	    
            		if (map.getFrontDatesForMap().isMapActive(date))
            		{
                    	startDates.add(startDate);
            		}
            	}

            }
        }
        
        return startDates;
    }

	private void makeCompanyChoices(Date campaignDate, ArmedService dateCorrectedService) throws PWCGException 
	{
	    try
	    {
    		cbCompany.removeAllItems();
            CampaignGeneratorCompanyFilter companyFilter = new CampaignGeneratorCompanyFilter();
	        String selectedRole = (String)cbRole.getSelectedItem();
            List<String> companyNames = companyFilter.makeSquadronChoices(
                    campaignDate, dateCorrectedService, parent.getCampaignGeneratorDO().getFrontMap(), selectedRole, parent.getCampaignGeneratorDO().isCommandRank());
            
            for (String companyName : companyNames)
            {
				cbCompany.addItem(companyName);
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
            String playerName = (String)playerNameTextBox.getText();
            parent.getCampaignGeneratorDO().setPlayerCrewMemberName(playerName);
            
            if (ae.getActionCommand().equalsIgnoreCase("CoopUserChanged"))
            {
                String coopUserFromDropDown = (String)cbCoopUser.getSelectedItem();
                coopUserNameTextBox.setText(coopUserFromDropDown);
                parent.getCampaignGeneratorDO().setCoopUser(coopUserFromDropDown);;
            }
            else if (ae.getActionCommand().equalsIgnoreCase("RoleChanged"))
            {
                String roleDesc = (String)cbRole.getSelectedItem();
                PwcgRole role = PwcgRole.getRoleFromDescription(roleDesc);
                parent.getCampaignGeneratorDO().setRole(role);
            }
            else if (ae.getActionCommand().equalsIgnoreCase("MapChanged"))
			{
		        String mapName = (String)cbMap.getSelectedItem();
		        PWCGMap map = PWCGContext.getInstance().getMapByMapName(mapName);
		        if (map == null)
		        {
				    parent.getCampaignGeneratorDO().setFrontMap(null);
		        }
		        else
		        {
				    parent.getCampaignGeneratorDO().setFrontMap(map.getMapIdentifier());
		        }
			}
            else if (ae.getActionCommand().equalsIgnoreCase("DateChanged"))
			{
                Date campaignDate =  getDateFromComboBox();
			    parent.getCampaignGeneratorDO().setStartDate(campaignDate);
			}
			else if (ae.getActionCommand().equalsIgnoreCase("RankChanged"))
			{
		        String rank = (String)cbRank.getSelectedItem();
		        parent.getCampaignGeneratorDO().setRank(rank);
			}
            else if (ae.getActionCommand().equalsIgnoreCase("CompanyChanged"))
            {
                String companyName = (String)cbCompany.getSelectedItem();
                if (companyName != null)
                {
                    parent.getCampaignGeneratorDO().setSquadName(companyName);
                    String companyInfo = getSquadronInfo(parent.getCampaignGeneratorDO().getStartDate(), companyName);
                    this.companyTextBox.setText(companyInfo);
                }
            }
            else if (ae.getActionCommand().equalsIgnoreCase("NextStep"))
            {
                parent.getCampaignGeneratorState().goToNextStep();
                evaluateUI();
                parent.evaluateCompletionState();
            }
            else if (ae.getActionCommand().equalsIgnoreCase("PreviousStep"))
            {
                parent.getCampaignGeneratorState().goToPreviousStep();
                evaluateUI();
                parent.evaluateCompletionState();
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


    private Date getDateFromComboBox() throws PWCGException 
	{
    	Date campaignDate = null;
        String dateStr = (String)cbDate.getSelectedItem();
        if (dateStr != null)
        {
        	campaignDate = DateUtils.getDateWithValidityCheck(dateStr);
        }
        
        return campaignDate;
	}
}
