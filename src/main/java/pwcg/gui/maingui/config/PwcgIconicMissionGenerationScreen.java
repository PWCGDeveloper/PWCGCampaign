package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.ButtonGroup;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;
import javax.swing.SwingConstants;

import pwcg.campaign.skirmish.IconicMissionsManager;
import pwcg.campaign.skirmish.IconicSingleMission;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.iconicbattles.IconicBattlesGUI;
import pwcg.gui.iconicbattles.IconicBattlesGenerator;
import pwcg.gui.iconicbattles.IconicBattlesGeneratorData;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;

public class PwcgIconicMissionGenerationScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private Map<String, IconicBattlesGUI> iconicBattleGUIs = new HashMap<String, IconicBattlesGUI>();
    private String selectedIconicBattle;
    private PwcgThreePanelUI pwcgThreePanel;
    private ButtonGroup buttonGroup = new ButtonGroup();

    public PwcgIconicMissionGenerationScreen()
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.pwcgThreePanel = new PwcgThreePanelUI(this);
    }
    
    public void makePanels() throws PWCGException 
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgGlobalConfigurationScreen);
        this.setImageFromName(imagePath);
        
        pwcgThreePanel.setLeftPanel(makeNavigatePanel());
        pwcgThreePanel.setCenterPanel(makeBlankCenterPanel());
        pwcgThreePanel.setRightPanel(makeCategoryPanel());
    }

    public Pane makeBlankCenterPanel()  
    {       
        Pane blankPanel = new Pane(new BorderLayout());
        blankPanel.setOpaque(false);
        blankPanel.setLayout(new BorderLayout());
        return blankPanel;
    }

    public Pane makeNavigatePanel() throws PWCGException  
    {
        Pane navPanel = new Pane(new BorderLayout());
        navPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        Button cancelButton = ButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished with iconic missions", this);
        buttonPanel.add(cancelButton);

        Button generateMissionButton = ButtonFactory.makeTranslucentMenuButton("Generate Mission", "Generate Mission", "Generate selected iconic mission", this);
        buttonPanel.add(generateMissionButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    public Pane makeCategoryPanel() throws PWCGException  
    {
        Pane configPanel = new Pane(new BorderLayout());
        configPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        
        
        buttonPanel.add(ButtonFactory.makeMenuLabelLarge("Iconic Missions:"));
        addMissionsForMapToButtonPanel(buttonPanel, "Stalingrad");
        addMissionsForMapToButtonPanel(buttonPanel, "Kuban");
        addMissionsForMapToButtonPanel(buttonPanel, "Rhine");
        buttonPanel.add(ButtonFactory.makeMenuLabelLarge("   "));
        buttonPanel.add(ButtonFactory.makeMenuLabelLarge("   "));
        buttonPanel.add(ButtonFactory.makeMenuLabelLarge("   "));

        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
    }

    private void addMissionsForMapToButtonPanel(Pane buttonPanel, String mapName) throws PWCGException
    {
        buttonPanel.add(ButtonFactory.makeMenuLabelLarge("   "));
        for (IconicSingleMission iconicMission : IconicMissionsManager.getInstance().getIconicMissionsForMapByDate(mapName))
        {
            buttonPanel.add(makeCategoryRadioButton(iconicMission.getMapName() + ": " + iconicMission.getCampaignName(), iconicMission.getDateString()));
        }
    }
    
    private RadioButton  makeCategoryRadioButton(String buttonText, String battleDate) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        RadioButton  button = new RadioButton (buttonText);
        button.setActionCommand(battleDate);
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

    IconicBattlesGUI createIconicBattlePanel(String battleKey) throws PWCGException 
    {        
        IconicBattlesGUI selectedBattle = new IconicBattlesGUI (battleKey);
        selectedBattle.makeGUI();
        
        return selectedBattle;
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
            else if (action.equalsIgnoreCase("Generate Mission"))
            {
                if (iconicBattleGUIs.containsKey(selectedIconicBattle))
                {
                    IconicBattlesGUI selectedIconicBattleGUI = iconicBattleGUIs.get(selectedIconicBattle);
                    IconicBattlesGeneratorData iconicBattleData = selectedIconicBattleGUI.getIconicBattleData();
                    IconicBattlesGenerator iconicBattlesGenerator = new IconicBattlesGenerator(iconicBattleData);
                    iconicBattlesGenerator.generateIconicMission();
                }
            }
            else
            {
                selectedIconicBattle = action;
                IconicBattlesGUI newIconicBattlePanel = null;
                if (iconicBattleGUIs.containsKey(selectedIconicBattle))
                {
                    newIconicBattlePanel = iconicBattleGUIs.get(selectedIconicBattle);
                }
                else
                {
                    newIconicBattlePanel = createIconicBattlePanel(selectedIconicBattle);
                    iconicBattleGUIs.put(selectedIconicBattle, newIconicBattlePanel);
                }

                pwcgThreePanel.setCenterPanel(newIconicBattlePanel);
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}


