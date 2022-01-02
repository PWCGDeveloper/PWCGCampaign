package pwcg.gui.campaign.crewmember;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignCrewMemberLogScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private CrewMember crewMember = null;
	
	private JPanel leftpage = null;
	private JPanel rightpage = null;
    private int pageNum = 1;
    private CrewMemberLogPages crewMemberLogPages;
    private Campaign campaign;
    private JPanel centerPanel = null;

    public CampaignCrewMemberLogScreen(Campaign campaign, CrewMember crewMember)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.crewMember = crewMember;  
        this.campaign = campaign;  
    }
	
	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignCrewMemberLogScreen);
        this.setImageFromName(imagePath);

        crewMemberLogPages = new CrewMemberLogPages(campaign, crewMember);
        crewMemberLogPages.makePages();
        
		this.add(BorderLayout.WEST, makeNavigationPanel());
		
		centerPanel = makeLogCenterPanel();
		this.add(BorderLayout.CENTER, centerPanel);
		
        makePages();        
	}

    private JPanel makeNavigationPanel() throws PWCGException  
    {
        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished Reading", "CrewMemberLogFinished", "Leave CrewMember Log", this);
        buttonPanel.add(finishedButton);

        JPanel logBookPanel = new JPanel(new BorderLayout());
        logBookPanel.setOpaque(false);
        logBookPanel.add(buttonPanel, BorderLayout.NORTH);        
        return logBookPanel;
    }

	private JPanel  makeLogCenterPanel() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.OpenCrewMemberLog);
        ImageResizingPanel campaignCrewMemberLogPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        campaignCrewMemberLogPanel.setLayout(new GridLayout(0,2));
        campaignCrewMemberLogPanel.setOpaque(false);

		return campaignCrewMemberLogPanel;
	}

	private void makePages() throws PWCGException  
	{
		centerPanel.removeAll();

		if (leftpage != null)
		{
			leftpage.removeAll();
			leftpage = null;
		}
		
		if (rightpage != null)
		{
			rightpage.removeAll();
			rightpage = null;
		}
		
		StringBuffer leftPageEntries = new StringBuffer("");
		int leftPageNum = pageNum;
		if (crewMemberLogPages.getPageCount() >= leftPageNum)
		{
			leftPageEntries = crewMemberLogPages.getPage(leftPageNum);
		}

        StringBuffer rightPageEntries = new StringBuffer("");
		int rightPageNum = pageNum + 1;
		if (crewMemberLogPages.getPageCount() >= (rightPageNum))
		{
			rightPageEntries = crewMemberLogPages.getPage(rightPageNum);
		}

		leftpage = makePage (leftPageEntries, leftPageNum);
		rightpage = makePage (rightPageEntries, rightPageNum);

		centerPanel.add(leftpage);
		centerPanel.add(rightpage);
	}

	private JPanel makePage(StringBuffer pageEntries, int pageNum) throws PWCGException 
	{
		JPanel campaignLogPanelBorder = new JPanel();
		campaignLogPanelBorder.setLayout(new BorderLayout());
		campaignLogPanelBorder.setOpaque(false);

		JPanel campaignLogPanel = new JPanel();
		campaignLogPanel.setLayout(new GridLayout(0,1));
		campaignLogPanel.setOpaque(false);

		Font font = PWCGMonitorFonts.getCursiveFont();

        final JTextArea logTextArea= new JTextArea();
        
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setEditable(false);
        logTextArea.setOpaque(false);
        logTextArea.setFont(font);
        logTextArea.setSize(logTextArea.getPreferredSize().width, 1);
        
        Insets margins = calculatePageMargins(pageNum);
        logTextArea.setMargin(margins);

		logTextArea.setText(pageEntries.toString());
		campaignLogPanel.add(logTextArea);
		
        final JTextArea bufferTextArea= new JTextArea();
        bufferTextArea.setOpaque(false);
        
		campaignLogPanelBorder.add(bufferTextArea, BorderLayout.WEST);
		campaignLogPanelBorder.add(campaignLogPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = makeButtonPanel(pageNum);
		campaignLogPanelBorder.add(buttonPanel, BorderLayout.SOUTH);
		
		return campaignLogPanelBorder;
	}

    private Insets calculatePageMargins(int pageNum)
    {
        Insets margins = PWCGMonitorBorders.calculateBorderMargins(35, 35, 35, 10);
        if (pageNum%2 == 0)
        {
            margins = PWCGMonitorBorders.calculateBorderMargins(25, 10, 35, 25);           
        }
        return margins;
    }

	private JPanel makeButtonPanel(int pageNum) throws PWCGException
	{
		JPanel buttonPanel = new JPanel(new GridLayout(0,2));
		buttonPanel.setOpaque(false);

		Color fg = ColorMap.PAPER_FOREGROUND;

		Font font = PWCGMonitorFonts.getPrimaryFont();

		makeLeftPage(pageNum, buttonPanel, fg, font);
		
		makeRightPage(pageNum, buttonPanel, fg, font);
		
		addWhiteSpace(buttonPanel);
		
		return buttonPanel;
	}


    private void makeLeftPage(int pageNum, JPanel buttonPanel, Color fg, Font font) throws PWCGException
    {
        if ((pageNum % 2) == 1)
		{
			if (pageNum > 1)
			{
                JButton prevButton = PWCGButtonFactory.makeTranslucentMenuButton("Previous Page", "Previous Page", "Go to the previous page", this);
                prevButton.setForeground(fg);   
                prevButton.setFont(font);
                buttonPanel.add(prevButton);
			}
		}
    }


    private void makeRightPage(int pageNum, JPanel buttonPanel, Color fg, Font font) throws PWCGException
    {
        if ((pageNum % 2) == 0)
		{
			if (pageNum  < crewMemberLogPages.getPageCount())
			{
		        JButton nextButton = PWCGButtonFactory.makeTranslucentMenuButton("Next Page", "Next Page", "Go to the next page", this);
				nextButton.setForeground(fg);	
				nextButton.setFont(font);
				buttonPanel.add(nextButton);
			}
		}
    }


    private void addWhiteSpace(JPanel buttonPanel)
    {
        for (int i = 0; i < 2; ++i)
		{
            JLabel dummyLeft = PWCGLabelFactory.makeDummyLabel();
            buttonPanel.add(dummyLeft);
            
            JLabel dummyRight = PWCGLabelFactory.makeDummyLabel();
            buttonPanel.add(dummyRight);
		}
    }

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("CrewMemberLogFinished"))
            {
                campaign.write();                
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("Next Page"))
			{
                nextPage();
			}
			else if (action.equalsIgnoreCase("Previous Page"))
			{
                previousPage();
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void previousPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum -= 2;
        makePages();
        centerPanel.setVisible(false);
        centerPanel.setVisible(true);
    }

    private void nextPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum += 2;
        makePages();
        centerPanel.setVisible(false);
        centerPanel.setVisible(true);
    }

}
