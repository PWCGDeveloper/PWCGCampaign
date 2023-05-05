package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignHomeSquadronPlaque extends JPanel
{
    private static final long serialVersionUID = 1L;
    
    public CampaignHomeSquadronPlaque()  
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);        
    }


    public void makeDescPanel(int squadronId) throws PWCGException 
    {
        Date campaignDate = CampaignHomeContext.getCampaign().getDate();
        
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignHomeSquadronPlaque);
        ImageResizingPanel plaquePanel = new ImageResizingPanel(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);
        plaquePanel.setLayout(new BorderLayout());
        plaquePanel.setBorder(PwcgBorderFactory.createPlaqueBackgroundBorder());

        JPanel descGridPanel = new JPanel(new GridLayout(0, 1));
        descGridPanel.setOpaque(false);
        
        Font font = PWCGMonitorFonts.getPrimaryFont();
                
        String spacing = "         ";
                
        descGridPanel.add(PWCGLabelFactory.makeDummyLabel());
        descGridPanel.add(PWCGLabelFactory.makeDummyLabel());

        Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);

        String assignedToString = InternationalizationManager.getTranslation("Assigned to");
        JLabel lAssignedTo = PWCGLabelFactory.makeTransparentLabel(assignedToString, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lAssignedTo);

        String squadString = spacing + squadron.determineDisplayName(campaignDate);
        JLabel lSquad = PWCGLabelFactory.makeTransparentLabel(squadString, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lSquad);

        String airfieldAtString = InternationalizationManager.getTranslation("Stationed at");
        airfieldAtString += " " + squadron.determineCurrentAirfieldName(campaignDate);
        JLabel lAirfieldAt = PWCGLabelFactory.makeTransparentLabel(airfieldAtString, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lAirfieldAt);

        String airfieldString = spacing + squadron.determineCurrentAirfieldName(campaignDate);
        JLabel lAirfield = PWCGLabelFactory.makeTransparentLabel(airfieldString, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lAirfield);
        

        String dateString = spacing + DateUtils.getDateString(campaignDate);
        JLabel lDate = PWCGLabelFactory.makeTransparentLabel(dateString, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lDate);


        PlaneType aircraftType = squadron.determineBestPlane(campaignDate);
        if (aircraftType != null)
        {
            String aircraftString = spacing + InternationalizationManager.getTranslation("Flying the");
            aircraftString += " " + aircraftType.getDisplayName();
            JLabel lAircraft = PWCGLabelFactory.makeTransparentLabel(aircraftString, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);

            descGridPanel.add(lAircraft);
        }
        
        descGridPanel.add(PWCGLabelFactory.makeDummyLabel());
        descGridPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        plaquePanel.add(descGridPanel, BorderLayout.NORTH);
        
        this.add(plaquePanel, BorderLayout.CENTER);
    }

}
