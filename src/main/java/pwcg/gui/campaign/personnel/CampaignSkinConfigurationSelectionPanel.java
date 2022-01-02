package pwcg.gui.campaign.personnel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.skin.Skin;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ScrollBarWrapper;


public class CampaignSkinConfigurationSelectionPanel extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    public static String NO_SKIN = "None";
            
    private String selectedPlane = "";
    
    private CampaignSkinConfigurationScreen parent;
    private JPanel skinsSelectionPanel = null;

    List<ButtonModel> aircraftButtonModels = new ArrayList<>();

    private ButtonGroup skinButtonGroup = new ButtonGroup();
    private List<ButtonModel> skinButtonModels = new ArrayList<>();

	public CampaignSkinConfigurationSelectionPanel(CampaignSkinConfigurationScreen parent) throws PWCGException
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        this.setImageFromName(imagePath);
        this.setBorder(BorderFactory.createEmptyBorder(30, 30, 50, 70));
        
        this.parent = parent;

        setLayout(new BorderLayout());
 	}

    public void makePanels() throws PWCGException  
    {
        removeAll();

        skinsSelectionPanel = makeSkinSelectionPanel();                        
        add(skinsSelectionPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    public JPanel makeSkinSelectionPanel() throws PWCGException 
    {
        JPanel skinSelectPanel = new JPanel(new BorderLayout());
        skinSelectPanel.setOpaque(false);
                
        JPanel skinSelectInnerPanel = new JPanel(new BorderLayout());
        skinSelectInnerPanel.setOpaque(false);
                
        JPanel skinSelectGrid = new JPanel(new GridLayout(0,2));
        skinSelectGrid.setOpaque(false);

        List<String> skinNames = new ArrayList<String>();
        addNoSkinListToSelection(skinNames);
        addSquadronSkinsToSelection(skinNames);
        addNonSquadronSkinsToSelection(skinNames);
        addLooseSkinsToSelection(skinNames);
        
        skinButtonGroup = new ButtonGroup();
        skinButtonModels = new ArrayList<ButtonModel>();
        String currentSkinNameForSelectedPlane = getSkinSelection();
        makeSkinButtons(skinSelectGrid, skinNames, currentSkinNameForSelectedPlane);
        
        skinSelectInnerPanel.add(skinSelectGrid, BorderLayout.NORTH);
        
        ScrollBarWrapper.createScrollPaneWithMax(skinSelectPanel, skinSelectInnerPanel, skinNames.size(), 20);
        
        return skinSelectPanel;
    }

    public void resetSkinSelectionPanel() throws PWCGException  
    {
        if (skinsSelectionPanel != null)
        {
            this.remove(skinsSelectionPanel);
        }
   
        skinsSelectionPanel = makeSkinSelectionPanel();
        
        add(skinsSelectionPanel, BorderLayout.CENTER);
        
        skinsSelectionPanel.revalidate();
        skinsSelectionPanel.repaint();
    }

    private void addNoSkinListToSelection(List<String> skinNames)
    {
        List<String>noSkinList = new ArrayList<String>();
        noSkinList.add(NO_SKIN);
        skinNames.addAll(noSkinList);
    }

    private void addSquadronSkinsToSelection(List<String> skinNames) throws PWCGException
    {
        SkinSessionManager skinSessionManager = parent.getSkinSessionManager();
        if (skinSessionManager.isSquadronSkinsSelected())
        {
            List<Skin> squadronSkins = skinSessionManager.getSquadronSkins(selectedPlane);
            List<String> squadronSkinNames = getStringNamesFromSkins(squadronSkins);
            skinNames.addAll(squadronSkinNames);
        }
    }

    private void addNonSquadronSkinsToSelection(List<String> skinNames) throws PWCGException
    {
        SkinSessionManager skinSessionManager = parent.getSkinSessionManager();
        if (skinSessionManager.isNonSquadronSkinsSelected())
        {
            List<Skin> nonSquadronSkins = skinSessionManager.getNonSquadronSkins(selectedPlane);
            List<String> nonSquadronSkinNames = getStringNamesFromSkins(nonSquadronSkins);
            skinNames.addAll(nonSquadronSkinNames);
        }
    }

    private void addLooseSkinsToSelection(List<String> skinNames) throws PWCGException
    {
        SkinSessionManager skinSessionManager = parent.getSkinSessionManager();
        if (skinSessionManager.isLooseSkinsSelected())
        {
            List<Skin> looseSkins = skinSessionManager.getLooseSkins(selectedPlane);
            List<String> looseSkinNames = getStringNamesFromSkins(looseSkins);
            skinNames.addAll(looseSkinNames);
        }
    }

	private List<String> getStringNamesFromSkins(List<Skin> skins)
	{
	    List<String> skinNames = new ArrayList<>();
	    for (Skin skin : skins)
	    {
	        skinNames.add(skin.getSkinName());
	    }
	    
	    return skinNames;
	}

    private String getSkinSelection()
    {
        SkinSessionManager skinSessionManager = parent.getSkinSessionManager();
        Skin skinForPlane = skinSessionManager.getSkinForCrewMemberAndPlane(selectedPlane);
        String currentSkinNameForSelectedPlane = "";
        if (skinForPlane != null)
        {
            currentSkinNameForSelectedPlane = skinForPlane.getSkinName();
        }
        else
        {
            currentSkinNameForSelectedPlane = NO_SKIN;
        }
        return currentSkinNameForSelectedPlane;
    }


    private void makeSkinButtons(JPanel skinSelectGrid, List<String> skinNames, String currentSkinNameForSelectedPlane) throws PWCGException
    {
        for (String skinName : skinNames)
        {
            // Add this skin to the skin button group and panel
            JRadioButton skinButton = makeRadioButton(skinName, "SelectSkin:" + skinName, ColorMap.PAPER_FOREGROUND);
            skinSelectGrid.add(skinButton);
            
            // Select the skin if the crewMember has one assigned and it is in this group
            if (skinName.equals(currentSkinNameForSelectedPlane))
            {
                skinButton.setSelected(true);
                
                skinButton.setForeground(ColorMap.BRITISH_RED);
            }
            
            skinButtonGroup.add(skinButton);
            skinButtonModels.add(skinButton.getModel());
        }
    }

    private JRadioButton makeRadioButton(String buttonText, String actionCommand, Color fgColor) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setActionCommand(actionCommand);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);

        return button;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.contains("SelectSkin:"))
            {
                int index = action.indexOf(":");
                String selectedSkinName = action.substring(index + 1);

                assignSkinToCrewMember(selectedSkinName);
                resetSkinSelectionPanel();
                
                CampaignSkinConfigurationCrewMemberPanel skinSelectionPanel = parent.getSkinControlPanel();
                skinSelectionPanel.resetCrewMemberInfoPanel();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void assignSkinToCrewMember(String selectedSkinName) throws PWCGException
    {
        Skin skin = null;
        SkinSessionManager skinSessionManager = parent.getSkinSessionManager();
        if (!selectedSkinName.equals(NO_SKIN))
        {
            skin = PWCGContext.getInstance().getSkinManager().getConfiguredSkinByName(selectedPlane, selectedSkinName);
            if (skin == null)
            {
                skin = new Skin();
                skin.setSkinName(selectedSkinName);
                ICountry country = CountryFactory.makeCountryByCountry(skinSessionManager.getCrewMember().getCountry());
                skin.setCountry(country.getCountryName());
                skin.setPlane(selectedPlane);
            }
        }

        skinSessionManager.updateSkinForPlane(selectedPlane, skin);
    }

    public void setSelectedPlane(String selectedPlane)
    {
        this.selectedPlane = selectedPlane;
    }
}
