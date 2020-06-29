package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

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

public class CampaignHomeSquadronPlaque extends JPanel
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

        JPanel descGridPanel = new JPanel(new GridLayout(0, 1));
        descGridPanel.setOpaque(false);
        
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        
        Color fg = ColorMap.PLAQUE_GOLD;
        
        String spacing = "         ";
        
        
        JLabel lDummy1 = new JLabel("", JLabel.LEFT);
        lDummy1.setOpaque(false);
        descGridPanel.add(lDummy1);
      
        JLabel lDummy2 = new JLabel("", JLabel.LEFT);
        lDummy2.setOpaque(false);
        descGridPanel.add(lDummy2);

        Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        String squadString = spacing + "Assigned to " + squadron.determineDisplayName(campaign.getDate());
        JLabel lSquad = new JLabel(squadString, JLabel.LEFT);
        lSquad.setFont(font);
        lSquad.setForeground(fg);
        descGridPanel.add(lSquad);
        
        String airfieldAtString = spacing + "Stationed at ";
        JLabel lAirfieldAt = new JLabel(airfieldAtString, JLabel.LEFT);
        lAirfieldAt.setFont(font);
        lAirfieldAt.setForeground(fg);
        descGridPanel.add(lAirfieldAt);
        
        String airfieldString = spacing + squadron.determineCurrentAirfieldName(campaign.getDate());
        JLabel lAirfield = new JLabel(airfieldString, JLabel.LEFT);
        lAirfield.setFont(font);
        lAirfield.setForeground(fg);
        descGridPanel.add(lAirfield);

        JLabel lDate = new JLabel(spacing + DateUtils.getDateString(campaign.getDate()), JLabel.LEFT);
        lDate.setFont(font);
        lDate.setForeground(fg);
        descGridPanel.add(lDate);
        
        PlaneType aircraftType = squadron.determineBestPlane(campaign.getDate());
        if (aircraftType != null)
        {
            String aircraftString = "Flying the " + aircraftType.getDisplayName();
            JLabel lAircraft = new JLabel(spacing + aircraftString, JLabel.LEFT);
            lAircraft.setFont(font);
            lAircraft.setForeground(fg);
            descGridPanel.add(lAircraft);
        }
        
        JLabel lDummy3 = new JLabel("", JLabel.LEFT);
        lDummy3.setOpaque(false);
        descGridPanel.add(lDummy3);
        
        JLabel lDummy4 = new JLabel("", JLabel.LEFT);
        lDummy4.setOpaque(false);
        descGridPanel.add(lDummy4);
        
        plaquePanel.add(descGridPanel, BorderLayout.NORTH);
        
        this.add(plaquePanel, BorderLayout.CENTER);
    }

}
