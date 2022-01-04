package pwcg.gui.campaign.crewmember;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.PictureManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.image.ImageCache;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePreviewPanel;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageScaledPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGButtonNoBackground;
import pwcg.gui.utils.PWCGJButton;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ToolTipManager;
import pwcg.gui.utils.UIUtils;

public class CampaignCrewMemberScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private CrewMember crewMember;
	private Company company;
    private Campaign campaign;
    private CampaignHomeScreen parent;

    protected String changeCrewMemberPictureAction = "";
    protected String changeSkinAction = "";
	protected String openMedalBoxAction = "";
    protected String openLogBookAction = "";
    protected JPanel centerPanel;

    public CampaignCrewMemberScreen(Campaign campaign, Company company, CrewMember crewMember, CampaignHomeScreen parent)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.crewMember = crewMember;
        this.company = company;
        this.parent = parent;
        this.campaign = campaign;

        changeCrewMemberPictureAction = "Change Picture";
		openMedalBoxAction = "Open Medal Box:";
		openLogBookAction = "View CrewMember Log:";
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignCrewMemberScreen);
        this.setImageFromName(imagePath);

        this.add(BorderLayout.WEST, makenavigationPanel());
        centerPanel = makeCenterPanel();
	    this.add(BorderLayout.CENTER, centerPanel);
	}

	public JPanel makeCenterPanel() throws PWCGException 
	{	
        JPanel campaignCrewMemberPanel = new JPanel(new BorderLayout());
        campaignCrewMemberPanel.setOpaque(false);

		JPanel crewMemberLogPanel = makeCrewMemberLog();
        JPanel picturePanel = makePicturePanel();
		JPanel crewMemberMedalBoxPanel = makeMedalBox();
		
        campaignCrewMemberPanel.add(crewMemberMedalBoxPanel,BorderLayout.NORTH);
        campaignCrewMemberPanel.add(picturePanel,BorderLayout.WEST);
		campaignCrewMemberPanel.add(crewMemberLogPanel,BorderLayout.CENTER);
				
		return campaignCrewMemberPanel;
	}	

    private JPanel makenavigationPanel() throws PWCGException  
    {
        JPanel crewMemberDesktopNavPanel = new JPanel(new BorderLayout());
        crewMemberDesktopNavPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "CrewMemberFinished", "Finished viewing crewMember", this);
        buttonPanel.add(finishedButton);

        crewMemberDesktopNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return crewMemberDesktopNavPanel;
    }

	private JPanel makeCrewMemberLog() throws PWCGException 
	{
		JPanel crewMemberLogPanel = new JPanel(new BorderLayout());
		crewMemberLogPanel.setOpaque(false);

        String imagePath = ContextSpecificImages.imagesMisc() + "CrewMemberLogBook.png";
        ImageScaledPanel crewMemberLogBorderPanel = new ImageScaledPanel(imagePath, 0.75);
		crewMemberLogBorderPanel.setLayout(new BorderLayout());
		crewMemberLogBorderPanel.setOpaque(false);

		Font font = PWCGMonitorFonts.getCrewMemberLogBookFont();

		Color bg = ColorMap.WOOD_BACKGROUND;
		Color fg = ColorMap.MAP_LOG_FOREGROUND;
		Color fadedFG = new Color(fg.getRed(),fg.getGreen(),fg.getBlue(),180);
		
		JPanel crewMemberLogBookGrid = new JPanel (new GridLayout(0, 2));
		crewMemberLogBookGrid.setOpaque(false);

		for (int i = 0; i < 1; ++i)
		{
	        for (int j = 0; j < 2; ++j)
	        {
    			crewMemberLogBookGrid.add(PWCGLabelFactory.makeDummyLabel());
	        }
		}
		
		JButton logButton = new PWCGButtonNoBackground("");
		logButton.setHorizontalAlignment(SwingConstants.LEFT);
		logButton.setForeground(fadedFG);
		logButton.setBackground(bg);
		logButton.setOpaque(false);
		logButton.setBorderPainted(false);
		logButton.setFocusPainted(false);
		logButton.setFont(font);
		logButton.setText("<html>               <br />               <br />               <br />               <br />               <br />               <br />               <br />               </html>");;
		String actionCommand = openLogBookAction + crewMember.getSerialNumber();
		logButton.addActionListener(this);
		logButton.setActionCommand(actionCommand);
        ToolTipManager.setToolTip(logButton, "View crewMember log book");

        crewMemberLogBookGrid.add(logButton);

		crewMemberLogBorderPanel.add(crewMemberLogBookGrid, BorderLayout.NORTH);

		crewMemberLogPanel.add(crewMemberLogBorderPanel, BorderLayout.CENTER);
        
		return crewMemberLogPanel;
	}

    private JPanel makePicturePanel() throws PWCGException 
    {
        JPanel crewMemberPicPanel = new JPanel(new BorderLayout());
        crewMemberPicPanel.setOpaque(false);
        
        PWCGJButton planeButton = makePlanePicture(campaign.getDate());
        PWCGJButton crewMemberButton = makeCrewMemberPicture();
        
        crewMemberPicPanel.add(crewMemberButton, BorderLayout.WEST);
        crewMemberPicPanel.add(planeButton, BorderLayout.SOUTH);
        
        return crewMemberPicPanel;
    }

	private PWCGJButton makeCrewMemberPicture() throws PWCGException 
	{
		Color bg = ColorMap.WOOD_BACKGROUND;
		ImageIcon imageIcon = null;  
		try 
		{
			String picPath = PictureManager.getPicturePath(crewMember);			
			imageIcon = ImageCache.getInstance().getRotatedImageIcon(picPath, 20);
		}
		catch (Exception ex) 
		{
            PWCGLogger.logException(ex);
		}

		PWCGJButton crewMemberPictureButton = PWCGButtonFactory.makeImageButton(imageIcon, bg,  changeCrewMemberPictureAction, "Press to set picture",  "Change crewMember picture", this);

		return crewMemberPictureButton;
	}

    private PWCGJButton makePlanePicture(Date date) throws PWCGException 
    {
        TankType bestPlane = null;
        if (company != null)
        {
            bestPlane = company.determineBestPlane(date);
        }
        
        String planePic = null;
        if (bestPlane != null)
        {
            planePic = bestPlane.getType();
        }

        Color bg = ColorMap.WOOD_BACKGROUND;
        
        ImageIcon imageIcon = null;  
        try 
        {
            if (planePic != null)
            {
                String picPath = PictureManager.getPlanePicturePath(planePic);          
                imageIcon = ImageIconCache.getInstance().getImageIconResized(picPath);
            }
        }
        catch (Exception ex) 
        {
            PWCGLogger.logException(ex);
        }

        String commandText =  changeSkinAction + crewMember.getSerialNumber();
        PWCGJButton planeSkinsButton = PWCGButtonFactory.makeImageButton(imageIcon, bg,  commandText, "Press to assign skins",  "Assign aircraft skins to this crewMember", this);

        return planeSkinsButton;
    }

	private JPanel makeMedalBox() throws PWCGException 
	{
		Color bg = ColorMap.WOOD_BACKGROUND;

		JPanel crewMemberPMedalBoxPanel = new JPanel(new BorderLayout());
		crewMemberPMedalBoxPanel.setOpaque(false);

        String imagePath = ContextSpecificImages.imagesMisc() + "CrewMemberMedalBox.png";
		ImageIcon imageIcon = ImageIconCache.getInstance().getImageIcon(imagePath);
        String commandText =  openMedalBoxAction + crewMember.getSerialNumber();
		PWCGJButton medalBoxButton = PWCGButtonFactory.makeImageButton(imageIcon, bg,  commandText, "View crewMember medals",  "View crewMember medals", this);

		crewMemberPMedalBoxPanel.add(medalBoxButton, BorderLayout.CENTER);
		return crewMemberPMedalBoxPanel;
	}

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.startsWith("View CrewMember Log"))
            {
                viewCrewMemberLog(action);
            }
            else if (action.equalsIgnoreCase("Change Picture"))
            {
                changeCrewMemberPicture();
            }
            else if (action.startsWith("Open Medal Box"))
            {
                openMedalBox(action);
            }
            else if (action.startsWith("CrewMemberFinished"))
            {
                parent.refreshInformation();
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void viewCrewMemberLog(String action) throws PWCGException 
    {
        SoundManager.getInstance().playSound("BookOpen.WAV");

        int index = action.indexOf(":");
        String crewMemberSerialNumberString = action.substring(index + 1);
        Integer serialNumber = Integer.valueOf(crewMemberSerialNumberString);

        CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
        if (crewMember == null)
        {
            crewMember = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(serialNumber);
        }
        
        if (crewMember != null)
        {
            CampaignCrewMemberLogScreen crewMemberLogPanel = new CampaignCrewMemberLogScreen(campaign, crewMember);
            crewMemberLogPanel.makePanels();
            
            CampaignGuiContextManager.getInstance().pushToContextStack(crewMemberLogPanel);
        }
    }

    private void openMedalBox(String action) throws PWCGException 
    {
        CrewMember crewMember = UIUtils.getCrewMemberFromAction(campaign, action);
        if (crewMember != null)
        {
            CampaignMedalScreen crewMemberMedalPanel = new CampaignMedalScreen(campaign, crewMember);
            crewMemberMedalPanel.makePanels();
            CampaignGuiContextManager.getInstance().pushToContextStack(crewMemberMedalPanel);
        }
    }

    private void changeCrewMemberPicture() throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByCountry(crewMember.getCountry());
        String countryName = country.getNationality();
        String picPath = ContextSpecificImages.imagesCrewMemberPictures() + countryName;
        File picDir = new File(picPath);

        CampaignChooseCrewMemberPicGUI picChooser = new CampaignChooseCrewMemberPicGUI(picDir);
        
        ImagePreviewPanel preview = new ImagePreviewPanel();
        picChooser.setAccessory(preview);
        picChooser.addPropertyChangeListener(preview);
        
        int returnVal = picChooser.showOpenDialog(null);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
        	String name = picChooser.getSelectedFile().getName();
        	crewMember.setPicName(name);
        }
        
        makePanels();
        
        this.add(BorderLayout.CENTER, centerPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
    }
}
