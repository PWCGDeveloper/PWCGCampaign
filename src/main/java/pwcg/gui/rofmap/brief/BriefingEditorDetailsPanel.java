package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import javafx.scene.text.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;

import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.utils.ScrollBarWrapper;

public class BriefingEditorDetailsPanel
{
    private Pane waypointPanel = new Pane(new BorderLayout());
    private WaypointEditorSet waypointEditors = new WaypointEditorSet();
    private boolean isEditable = false;

    public BriefingEditorDetailsPanel (boolean isEditable)
    {
        this.isEditable = isEditable;
    }
    
    public void buildWaypointPanel(BriefingFlight activeBriefingFlight) throws PWCGException
    {
        waypointPanel.setOpaque(false);
        
        GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.ipadx = 3;
		constraints.ipady = 3;
		GridBagLayout gridBagLayout = new GridBagLayout();
		Pane waypointDetailsPanel = new Pane(gridBagLayout);
		waypointDetailsPanel.setOpaque(false);

		createMissionParametersHeader(constraints, waypointDetailsPanel);
	            
        BriefingMapPoint previousMapPoint = null;
	    for (BriefingMapPoint briefingMapPoint : activeBriefingFlight.getBriefingFlightParameters().getBriefingMapMapPoints())
	    {
	        IWaypointDetails waypointViewer = buildWaypointViewer(briefingMapPoint.getWaypointID());
	        waypointViewer.initializeWPEdit(previousMapPoint, briefingMapPoint);
	    	
	        constraints.gridy = constraints.gridy + 1;

			constraints.gridx = 0;
			waypointDetailsPanel.add(waypointViewer.getDescriptionField(), constraints);
            
            constraints.gridx = 1;
            waypointDetailsPanel.add(waypointViewer.getAltitudeField(), constraints);
            
            constraints.gridx = 2;
            waypointDetailsPanel.add(waypointViewer.getCruisingSpeedField(), constraints);
            
            constraints.gridx = 3;
            waypointDetailsPanel.add(waypointViewer.getDistanceField(), constraints);
        
            constraints.gridx = 4;
            waypointDetailsPanel.add(waypointViewer.getHeadingField(), constraints);
            
            waypointEditors.addWaypointEditor(waypointViewer);
            
            previousMapPoint = briefingMapPoint;
	    }	    
	    
        JScrollPane waypointScrollPane = ScrollBarWrapper.makeScrollPane(waypointDetailsPanel);
        waypointPanel.add(waypointScrollPane, BorderLayout.NORTH);
    }
    
    private IWaypointDetails buildWaypointViewer(long waypointID)
    {
        if (isEditable)
        {
            return new WaypointEditor(waypointID);
        }
        else
        {
            return new WaypointViewer(waypointID);
        }
    }

    private void createMissionParametersHeader(GridBagConstraints constraints, Pane panel) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getTypewriterFont();

        Label wpName = new Label ("WP");      
        wpName.setFont(font);
        
        wpName.setAlignment(Label.CENTER);
        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(wpName, constraints);
        
        Label altLabel = new Label ("Alt (Meters)");
        altLabel.setFont(font);

        altLabel.setAlignment(Label.CENTER);
        constraints.weightx = 0.2;
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(altLabel, constraints);

        Label cruiseSpeedLabel = new Label ("Cruise Speed");
        cruiseSpeedLabel.setFont(font);

        cruiseSpeedLabel.setAlignment(Label.CENTER);
        constraints.weightx = 0.2;
        constraints.gridx = 2;
        constraints.gridy = 0;
        panel.add(cruiseSpeedLabel, constraints);

        Label distLabel = new Label ("Dist (Km)");
        distLabel.setFont(font);

        distLabel.setAlignment(Label.CENTER);
        constraints.weightx = 0.2;
        constraints.gridx = 3;
        constraints.gridy = 0;
        panel.add(distLabel, constraints);
        
        Label headingLabel = new Label ("Heading");
        headingLabel.setFont(font);
        
        headingLabel.setAlignment(Label.CENTER);
        constraints.weightx = 0.2;
        constraints.gridx = 4;
        constraints.gridy = 0;
        panel.add(headingLabel, constraints);
    }

    public Pane getWaypointPanel()
    {
        return waypointPanel;
    }

    public WaypointEditorSet getWaypointEditors()
    {
        return waypointEditors;
    }
    
    
}
