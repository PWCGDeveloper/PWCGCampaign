package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ButtonGroup;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
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
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;

public class CampaignNewsStandScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private List<Newspaper> newspaperToDate = new ArrayList<Newspaper>();
    private PwcgThreePanelUI pwcgThreePanel;
    private ButtonGroup buttonGroup = new ButtonGroup();

    public CampaignNewsStandScreen(Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.pwcgThreePanel = new PwcgThreePanelUI(this);
    }

    public void makePanels() throws PWCGException 
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignNewsScreen);
        this.setImageFromName(imagePath);

        pwcgThreePanel.setLeftPanel(makeNewsLeftPanel());
        pwcgThreePanel.setCenterPanel(makeBlankCenterPanel());
        pwcgThreePanel.setRightPanel(makeNewsRightPanel());
    }


    private Pane makeNewsLeftPanel() throws PWCGException  
    {

        Pane squadronLogPanel = new Pane(new BorderLayout());
        squadronLogPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        Button finishedButton = ButtonFactory.makeTranslucentMenuButton("Finished Reading", "Finished", "Leave News Rack", this);
        buttonPanel.add(finishedButton);

        squadronLogPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return squadronLogPanel;
    }

    public Pane makeBlankCenterPanel()  
    {       
        Pane blankPanel = new Pane(new BorderLayout());
        blankPanel.setOpaque(false);
        blankPanel.setLayout(new BorderLayout());
        return blankPanel;
    }

    public Pane makeNewsRightPanel() throws PWCGException  
    {
        Pane configPanel = new Pane(new BorderLayout());
        configPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
                
        buttonPanel.add(ButtonFactory.makeMenuLabelLarge("Newspapers:"));
        addNewspapersButtonPanel(buttonPanel);

        buttonPanel.add(ButtonFactory.makeMenuLabelLarge("   "));
        buttonPanel.add(ButtonFactory.makeMenuLabelLarge("   "));
        buttonPanel.add(ButtonFactory.makeMenuLabelLarge("   "));

        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
    }

    private void addNewspapersButtonPanel(Pane buttonPanel) throws PWCGException
    {
        ICountry playerCountry = CountryFactory.makeCountryByCountry(campaign.getReferencePlayer().getCountry());
        newspaperToDate = PWCGContext.getInstance().getNewspaperManager().getNewpapersToDate(playerCountry.getSide(), campaign.getDate());
        for (Newspaper newspaper : newspaperToDate)
        {
            buttonPanel.add(makeNewspaperRadioButton(newspaper));
        }
    }
    
    private RadioButton  makeNewspaperRadioButton(Newspaper newspaper) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        String newspaperDateActionCommand = DateUtils.getDateStringYYYYMMDD(newspaper.getNewspaperEventDate());
        
        RadioButton  button = new RadioButton (getPrettyDate(newspaper));
        button.setActionCommand(newspaperDateActionCommand);
        button.setAlignment(SwingConstants.LEFT );
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
