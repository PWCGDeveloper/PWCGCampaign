package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignHomeSquadronPlaque extends Pane
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    
    public CampaignHomeSquadronPlaque(Campaign campaign)  
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaign;
    }


    public void makeDescPanel(int squadronId) throws PWCGException 
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignHomeSquadronPlaque);
        ImageResizingPanel plaquePanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        plaquePanel.setLayout(new BorderLayout());
        plaquePanel.setBorder(PwcgBorderFactory.createPlaqueBackgroundBorder());

        Pane descGridPanel = new Pane(new GridLayout(0, 1));
        descGridPanel.setOpaque(false);
        
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        
        Color fg = ColorMap.PLAQUE_GOLD;
        
        String spacing = "         ";
        
        
        Label lDummy1 = new Label("", Label.LEFT);
        lDummy1.setOpaque(false);
        descGridPanel.add(lDummy1);
      
        Label lDummy2 = new Label("", Label.LEFT);
        lDummy2.setOpaque(false);
        descGridPanel.add(lDummy2);

        Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        String squadString = spacing + "Assigned to " + squadron.determineDisplayName(campaign.getDate());
        Label lSquad = new Label(squadString, Label.LEFT);
        lSquad.setFont(font);
        lSquad.setForeground(fg);
        descGridPanel.add(lSquad);
        
        String airfieldAtString = spacing + "Stationed at ";
        Label lAirfieldAt = new Label(airfieldAtString, Label.LEFT);
        lAirfieldAt.setFont(font);
        lAirfieldAt.setForeground(fg);
        descGridPanel.add(lAirfieldAt);
        
        String airfieldString = spacing + squadron.determineCurrentAirfieldName(campaign.getDate());
        Label lAirfield = new Label(airfieldString, Label.LEFT);
        lAirfield.setFont(font);
        lAirfield.setForeground(fg);
        descGridPanel.add(lAirfield);

        Label lDate = new Label(spacing + DateUtils.getDateString(campaign.getDate()), Label.LEFT);
        lDate.setFont(font);
        lDate.setForeground(fg);
        descGridPanel.add(lDate);
        
        PlaneType aircraftType = squadron.determineBestPlane(campaign.getDate());
        if (aircraftType != null)
        {
            String aircraftString = "Flying the " + aircraftType.getDisplayName();
            Label lAircraft = new Label(spacing + aircraftString, Label.LEFT);
            lAircraft.setFont(font);
            lAircraft.setForeground(fg);
            descGridPanel.add(lAircraft);
        }
        
        Label lDummy3 = new Label("", Label.LEFT);
        lDummy3.setOpaque(false);
        descGridPanel.add(lDummy3);
        
        Label lDummy4 = new Label("", Label.LEFT);
        lDummy4.setOpaque(false);
        descGridPanel.add(lDummy4);
        
        plaquePanel.add(descGridPanel, BorderLayout.NORTH);
        
        this.add(plaquePanel, BorderLayout.CENTER);
    }

}
