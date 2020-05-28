package pwcg.gui.campaign.pilot;

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
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGJButton;

public class CampaignPilotLogPanel extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private SquadronMember pilot = null;
	
	private JPanel leftpage = null;
	private JPanel rightpage = null;
    private int pageNum = 1;
    private PilotLogPages pilotLogPages;
    private Campaign campaign;

    public CampaignPilotLogPanel(Campaign campaign, SquadronMember pilot)
    {
        super();

        this.pilot = pilot;  
        this.campaign = campaign;  
    }


	public void makeVisible(boolean visible) 
	{
	}
	
	public void makePanels() throws PWCGException  
	{
        pilotLogPages = new PilotLogPages(campaign, pilot);
        pilotLogPages.makePages();
        
		setLeftPanel(makeNavigationPanel());
		setCenterPanel(makeLogCenterPanel());
		
        makePages();        
	}

    private JPanel makeNavigationPanel() throws PWCGException  
    {
        String imagePath = getSideImage(campaign, "PilotInfoNav.jpg");

        ImageResizingPanel journalPanel = new ImageResizingPanel(imagePath);
        journalPanel.setLayout(new BorderLayout());
        journalPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeMenuButton("Finished Reading", "PilotLogFinished", this);
        buttonPanel.add(finishedButton);

        journalPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return journalPanel;
    }

	private JPanel  makeLogCenterPanel() throws PWCGException  
	{
        String imagePath = ContextSpecificImages.imagesMisc() + "PilotLog.jpg";
        ImageResizingPanel campaignPilotLogPanel = new ImageResizingPanel(imagePath);
        campaignPilotLogPanel.setLayout(new GridLayout(0,2));
        campaignPilotLogPanel.setOpaque(false);

		return campaignPilotLogPanel;
	}

	private void makePages() throws PWCGException  
	{
		getCenterPanel().removeAll();

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
		if (pilotLogPages.getPageCount() >= leftPageNum)
		{
			leftPageEntries = pilotLogPages.getPage(leftPageNum);
		}

        StringBuffer rightPageEntries = new StringBuffer("");
		int rightPageNum = pageNum + 1;
		if (pilotLogPages.getPageCount() >= (rightPageNum))
		{
			rightPageEntries = pilotLogPages.getPage(rightPageNum);
		}

		leftpage = makePage (leftPageEntries, leftPageNum);
		rightpage = makePage (rightPageEntries, rightPageNum);

		getCenterPanel().add(leftpage);
		getCenterPanel().add(rightpage);
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

		Color bg = ColorMap.PAPER_BACKGROUND;
		Color fg = ColorMap.PAPER_FOREGROUND;

	      Font font = PWCGMonitorFonts.getPrimaryFont();

		makeLeftPage(pageNum, buttonPanel, bg, fg, font);
		
		makeRightPage(pageNum, buttonPanel, bg, fg, font);
		
		addWhiteSpace(buttonPanel);
		
		return buttonPanel;
	}


    private void makeLeftPage(int pageNum, JPanel buttonPanel, Color bg, Color fg, Font font)
    {
        if ((pageNum % 2) == 1)
		{
			if (pageNum > 1)
			{
				PWCGJButton prevButton = new PWCGJButton("Previous Page");
				prevButton.addActionListener(this);
				buttonPanel.add (prevButton);
				prevButton.setOpaque(false);
				prevButton.setBackground(bg);
				prevButton.setForeground(fg);	
				prevButton.setBorderPainted(false);
				prevButton.setFont(font);
				prevButton.setHorizontalAlignment(SwingConstants.CENTER);
				
				buttonPanel.add(prevButton);
			}
		}
    }


    private void makeRightPage(int pageNum, JPanel buttonPanel, Color bg, Color fg, Font font)
    {
        if ((pageNum % 2) == 0)
		{
			if (pageNum  < pilotLogPages.getPageCount())
			{
				PWCGJButton nextButton = new PWCGJButton("Next Page");
				nextButton.addActionListener(this);
				buttonPanel.add (nextButton);
				nextButton.setOpaque(false);
				nextButton.setBackground(bg);	
				nextButton.setForeground(fg);	
				nextButton.setBorderPainted(false);
				nextButton.setFont(font);
				nextButton.setHorizontalAlignment(SwingConstants.CENTER);

				buttonPanel.add(nextButton);
			}
		}
    }


    private void addWhiteSpace(JPanel buttonPanel)
    {
        for (int i = 0; i < 2; ++i)
		{
            JLabel dummyLeft = PWCGButtonFactory.makeDummy();
            buttonPanel.add(dummyLeft);
            
            JLabel dummyRight = PWCGButtonFactory.makeDummy();
            buttonPanel.add(dummyRight);
		}
    }

	/**
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("PilotLogFinished"))
            {
                finishedWithCampaignScreen();
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
        getCenterPanel().setVisible(false);
        getCenterPanel().setVisible(true);
    }

    private void nextPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum += 2;
        makePages();
        getCenterPanel().setVisible(false);
        getCenterPanel().setVisible(true);
    }

}
