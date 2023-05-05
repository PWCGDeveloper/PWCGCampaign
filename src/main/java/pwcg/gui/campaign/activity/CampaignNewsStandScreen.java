package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.newspapers.Newspaper;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignNewsStandScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private List<Newspaper> newspaperToDate = new ArrayList<Newspaper>();
    private PwcgThreePanelUI pwcgThreePanel;
    private ButtonGroup buttonGroup = new ButtonGroup();

    public CampaignNewsStandScreen()
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.pwcgThreePanel = new PwcgThreePanelUI(this);
    }

    public void makePanels() throws PWCGException 
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignNewsScreen);
        this.setThemedImageFromName(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);

        pwcgThreePanel.setLeftPanel(makeNewsLeftPanel());
        pwcgThreePanel.setCenterPanel(makeBlankCenterPanel());
        pwcgThreePanel.setRightPanel(makeNewsRightPanel());
    }


    private JPanel makeNewsLeftPanel() throws PWCGException  
    {
        JPanel squadronLogPanel = new JPanel(new BorderLayout());
        squadronLogPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished Reading", "Finished", "Leave News Rack", this);
        buttonPanel.add(finishedButton);

        squadronLogPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return squadronLogPanel;
    }

    public JPanel makeBlankCenterPanel()  
    {       
        JPanel blankPanel = new JPanel(new BorderLayout());
        blankPanel.setOpaque(false);
        blankPanel.setLayout(new BorderLayout());
        return blankPanel;
    }

    public JPanel makeNewsRightPanel() throws PWCGException  
    {
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
                
        buttonPanel.add(PWCGLabelFactory.makeMenuLabelLarge("Newspapers"));
        addNewspapersButtonPanel(buttonPanel);

        buttonPanel.add(PWCGLabelFactory.makeMenuLabelLarge("   "));
        buttonPanel.add(PWCGLabelFactory.makeMenuLabelLarge("   "));
        buttonPanel.add(PWCGLabelFactory.makeMenuLabelLarge("   "));

        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
    }

    private void addNewspapersButtonPanel(JPanel buttonPanel) throws PWCGException
    {
        ICountry playerCountry = CountryFactory.makeCountryByCountry(CampaignHomeContext.getCampaign().getReferencePlayer().getCountry());
        newspaperToDate = PWCGContext.getInstance().getNewspaperManager().getNewpapersToDate(playerCountry.getSide(), CampaignHomeContext.getCampaign().getDate());
        for (Newspaper newspaper : newspaperToDate)
        {
            buttonPanel.add(makeNewspaperRadioButton(newspaper));
        }
    }
    
    private JRadioButton makeNewspaperRadioButton(Newspaper newspaper) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        String newspaperDateActionCommand = DateUtils.getDateStringYYYYMMDD(newspaper.getNewspaperEventDate());
        
        JRadioButton button = new JRadioButton(getPrettyDate(newspaper));
        button.setActionCommand(newspaperDateActionCommand);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);

        buttonGroup.add(button);

        return button;
    }
    
    private String getPrettyDate(Newspaper newspaper)
    {
        return DateUtils.getDateStringPretty(newspaper.getNewspaperEventDate());
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("Finished"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else
            {
                Newspaper newsPaper = getNewspaper(action);
                NewspaperUI campaignNewspaperUI = new NewspaperUI(newsPaper);
                campaignNewspaperUI.makePanels();
                pwcgThreePanel.setCenterPanel(campaignNewspaperUI);
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private Newspaper getNewspaper(String selectedNewspaperDateString) throws PWCGException
    {
        for (Newspaper newspaper : newspaperToDate)
        {
            String newspaperDateString = DateUtils.getDateStringYYYYMMDD(newspaper.getNewspaperEventDate());
            if (newspaperDateString.equals(selectedNewspaperDateString))
            {
                return newspaper;
            }
        }
        throw new PWCGException("No newspaper for date " + selectedNewspaperDateString);
    }

}
