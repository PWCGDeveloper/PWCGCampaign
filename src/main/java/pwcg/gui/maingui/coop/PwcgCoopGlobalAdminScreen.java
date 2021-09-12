package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.ButtonGroup;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;
import javax.swing.SwingConstants;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.ButtonFactory;

public class PwcgCoopGlobalAdminScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private PwcgThreePanelUI pwcgThreePanel;
    private ButtonGroup buttonGroup = new ButtonGroup();

    public PwcgCoopGlobalAdminScreen()
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.pwcgThreePanel = new PwcgThreePanelUI(this);
    }
    
    public void makePanels() 
    {
        try
        {        	
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgCoopGlobalAdminScreen);
            this.setImageFromName(imagePath);

            pwcgThreePanel.setLeftPanel(makeNavigatePanel());
            pwcgThreePanel.setRightPanel(makeCoopAdminActionSelectPanel());
            pwcgThreePanel.setCenterPanel(makeBlankCenterPanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	public Pane makeBlankCenterPanel() throws PWCGException  
    {       
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel blankPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        blankPanel.setLayout(new BorderLayout());
                
        return blankPanel;
    }

    public Pane makeNavigatePanel() throws PWCGException  
    {
        Pane navPanel = new Pane(new BorderLayout());
        navPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        Button finishedButton = ButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished with coop player administration", this);
        buttonPanel.add(finishedButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    public Pane makeCoopAdminActionSelectPanel() throws PWCGException  
    {
        Pane configPanel = new Pane(new BorderLayout());
        configPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        

        Label label = ButtonFactory.makeMenuLabelLarge("Select Coop Admin Action:");
        buttonPanel.add(label);

        Label spacer = ButtonFactory.makeMenuLabelLarge("   ");
        buttonPanel.add(spacer);

        buttonPanel.add(makeActionSelectRadioButton("Show Coop Participant Information"));
        buttonPanel.add(makeActionSelectRadioButton("Add Coop User"));
        buttonPanel.add(makeActionSelectRadioButton("Remove Coop User"));
        
        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
    }

    private RadioButton  makeActionSelectRadioButton(String buttonText) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        RadioButton  button = new RadioButton (buttonText);
        button.setActionCommand(buttonText);
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

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("Finished"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
                return;
            }

            if (action.equalsIgnoreCase("Show Coop Participant Information"))
            {
                CoopPersonaInfoPanel coopPersonaInfoPanel = new CoopPersonaInfoPanel();
                coopPersonaInfoPanel.makePanels();                
                pwcgThreePanel.setCenterPanel(coopPersonaInfoPanel);
            }
            else if (action.equalsIgnoreCase("Add Coop User"))
            {
                CoopCreateUserPanel coopCreateUser = new CoopCreateUserPanel();
                coopCreateUser.makePanels();
                pwcgThreePanel.setCenterPanel(coopCreateUser);
            }
            else if (action.contains("Remove Coop User"))
            {
                CoopUserRemovePanel coopUserRemove = new CoopUserRemovePanel();
                coopUserRemove.makePanels();
                coopUserRemove.loadPanels();
                pwcgThreePanel.setCenterPanel(coopUserRemove);
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}


