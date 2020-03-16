package pwcg.gui.rofmap.editmap;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationSet;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.rofmap.MapPanelBase;

public class MapLocationEditor
{    
    private LocationSet cities = new LocationSet("MapLocations");
    private MapPanelBase mapPanel = null;
        
    public MapLocationEditor(MapPanelBase mapPanel)
    {
        this.mapPanel = mapPanel;
    }

    public void createCity(String mapName, MouseEvent e)
    {
        try
        {
            Point point = new Point();
            point.x = e.getX();
            point.y = e.getY();
            Coordinate coordinate = mapPanel.pointToCoordinate(point);

            String cityName = JOptionPane.showInputDialog(mapPanel,
                    "City Name:", null);
            
            if (!(cityName == null) &&  !(cityName.equals("")))
            {            
                PWCGLocation city = new PWCGLocation();
                city.setName(cityName);
                city.setPosition(coordinate);
                city.setOrientation(new Orientation());
                cities.addLocation(city);
                
                LocationIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + "Arras" + "\\", "MapLocations.json", cities);
            }

        }
        catch (Exception exp)
        {
            PWCGLogger.logException(exp);
        }
    }
}
