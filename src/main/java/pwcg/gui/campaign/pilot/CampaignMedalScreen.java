package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.medals.MedalText;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.image.ImageRetriever;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGFrame;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignMedalScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private SquadronMember pilot;
    private JTextArea medalTextArea;

    public CampaignMedalScreen(SquadronMember pilot)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.pilot = pilot;
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignMedalScreen);
        this.setImage(imagePath);

        this.add(BorderLayout.WEST, makenavigationPanel());
        this.add(BorderLayout.CENTER, makeCenterPanel());     
	}

    private JPanel makeCenterPanel() throws PWCGException
    {
        JPanel medalCenterPanel = new JPanel(new GridLayout(0,2));
        medalCenterPanel.setOpaque(false);
        medalCenterPanel.add(makeMedalBoxPanel());     
        medalCenterPanel.add(makePaperDollPanel());

        return medalCenterPanel;
    }

    private JPanel makenavigationPanel() throws PWCGException  
    {
        JPanel medalNavPanel = new JPanel(new BorderLayout());
        medalNavPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished admiring your medals", this);
        buttonPanel.add(finishedButton);

        medalNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return medalNavPanel;
    }

    public JPanel makeMedalBoxPanel() throws PWCGException 
    {   
        JPanel openMedalBoxPanel = new JPanel(new BorderLayout());
        openMedalBoxPanel.setOpaque(false);

        int numSpacers = calcNumSpacers();
        
        openMedalBoxPanel.add(SpacerPanelFactory.createVerticalSpacerPanel(numSpacers), BorderLayout.NORTH);
        openMedalBoxPanel.add(makeMedalBox(), BorderLayout.CENTER);
        makeMedalTextPanelPanel();
        //openMedalBoxPanel.add(makeMedalTextPanelPanel(), BorderLayout.SOUTH);

        return openMedalBoxPanel;
    }   
    
    private int calcPercentForRightSpacer()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            return 1;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            return 6;
        }
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return 10;
        }
        else
        {
            return 20;            
        }
    }

    private int calcNumSpacers()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            return 1;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            return 2;
        }
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return 3;
        }
        else
        {
            return 5;
        }
    }

	private JPanel makeMedalBox() throws PWCGException 
	{
		CampaignPilotMedalBox medalBoxPanel = new CampaignPilotMedalBox(this, pilot);
		medalBoxPanel.makePanels();

        JPanel pilotMedalBoxPanel = new JPanel(new BorderLayout());
        pilotMedalBoxPanel.setOpaque(false);
        pilotMedalBoxPanel.add(medalBoxPanel, BorderLayout.CENTER);
		return pilotMedalBoxPanel;
	}
    
    private JPanel makeMedalTextPanelPanel() throws PWCGException
    {
        List<Medal> pilotMedals = pilot.getMedals();
        if (!pilotMedals.isEmpty())
        {
            return makeTextPanelPanel(pilotMedals.get(0));
        }
        
        int percentForRightSpace = calcPercentForRightSpacer();
        return SpacerPanelFactory.makeSpacerPercentPanel(percentForRightSpace);
    }

	   
    private JPanel makeTextPanelPanel(Medal medal) throws PWCGException  
    {
        JPanel medalTextPanel = new JPanel(new BorderLayout());
        medalTextPanel.setOpaque(false);
       
        JPanel medalDescriptionPanel = formMedalTextPanel(medal);
        
        medalTextPanel.add(SpacerPanelFactory.createVerticalSpacerPanel(4), BorderLayout.NORTH);
        medalTextPanel.add(medalDescriptionPanel, BorderLayout.CENTER);
        medalTextPanel.add(SpacerPanelFactory.makeSpacerPercentPanel(20), BorderLayout.NORTH);

        return medalTextPanel;
    }


    private JPanel formMedalTextPanel(Medal medal) throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel medalTextPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        medalTextPanel.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());

        String medalText = MedalText.getTextForMedal(pilot, medal);

        medalTextArea = createMedalTextArea();
        medalTextArea.setText(medalText);
        medalTextPanel.add(medalTextArea, BorderLayout.WEST);

        return medalTextPanel;
    }

    private JTextArea createMedalTextArea() throws PWCGException
    {
        JTextArea medalTextArea;
        Font font = PWCGMonitorFonts.getTypewriterFont();
        medalTextArea = new JTextArea();
        medalTextArea.setFont(font);
        medalTextArea.setOpaque(false);
        medalTextArea.setLineWrap(true);
        medalTextArea.setWrapStyleWord(true);
        medalTextArea.setText("");
        medalTextArea.setPreferredSize(PwcgBorderFactory.createSideTextPreferredSize());

        return medalTextArea;
    }
    
    public void setMedalText(Medal medal)
    {
        medalTextArea.setText(MedalText.getTextForMedal(pilot, medal));
    }
    

    private JPanel makePaperDollPanel() throws PWCGException
    {
        BufferedImage paperDoll = buildPaperDollWithOverlay();
        ImageResizingPanel paperDollPanel = ImageResizingPanelBuilder.makeImageResizingPanel(paperDoll);
        paperDollPanel.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());
        
        return paperDollPanel;
    }

    private BufferedImage buildPaperDollWithOverlay() throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
        String imagePath = ContextSpecificImages.imagesPaperDoll() + "\\" + country.getCountryName();
        String paperDollPath = imagePath + "\\PaperDoll.png";
        BufferedImage paperDoll = ImageRetriever.getImageFromFile(paperDollPath);
        
        
        BufferedImage combinedPaperDollImage = paperDoll;
        for (Medal medal : pilot.getMedals())
        {
            try 
            {
                String medalPath = imagePath + "\\" + medal.getMedalName() + ".png";
                BufferedImage medalOverlay = ImageRetriever.getImageFromFile(medalPath);
                if (medalOverlay != null)
                {
                    combinedPaperDollImage = combineOverlayWithMap(combinedPaperDollImage, medalOverlay);
                }
            }
            catch (Exception ex) 
            {
                PWCGLogger.logException(ex);
            }            
        }

        return combinedPaperDollImage;
    }

    private BufferedImage combineOverlayWithMap(BufferedImage paperDollImage, BufferedImage medalOverlay)
    {
        BufferedImage result = new BufferedImage(paperDollImage.getWidth(), paperDollImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();
        g.drawImage(paperDollImage, 0, 0, null);
        g.drawImage(medalOverlay, 0, 0, null);
        return result;
    }

    private JLabel makePaperDollSpacerLabel()
    {
        double width = PWCGFrame.getInstance().getBounds().getWidth();
        int numSpaces = width * .4;

        JLabel medalLabel = new JLabel(medalIcon);
        medalLabel.setOpaque(false);
        return medalLabel;
    }


	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			String action = ae.getActionCommand();
			
            if (action.startsWith("Finished"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
			
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}
