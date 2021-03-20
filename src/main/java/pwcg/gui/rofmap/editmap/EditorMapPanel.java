package pwcg.gui.rofmap.editmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.group.TownFinder;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapPanelBase;

public class EditorMapPanel extends MapPanelBase
{
	public static int DISPLAY_AIRFIELDS = 0;
	public static int DISPLAY_CITIES = 0;

    public static int EDIT_MODE_NONE = 0;

    public static int EDIT_MODE_CREATE_NEW_FRONT = 1;
    public static int EDIT_MODE_ADD_FRONT = 2;
    public static int EDIT_MODE_DELETE_FRONT = 3;
    public static int EDIT_MODE_EDIT_FRONT = 4;
    public static int EDIT_MODE_ADD_MAP_LOCATIONS = 5;

	private static final long serialVersionUID = 1L;
	
	private ICountry country = CountryFactory.makeMapReferenceCountry(Side.ALLIED);
    private int editMode = EDIT_MODE_NONE;
    private Boolean[] whatToDisplay = new Boolean[8];

    private FrontLineEditor frontLineEditor = null;
    private MapLocationEditor mapLocationEditor = null;


	public EditorMapPanel(MapGUI parent) throws PWCGException  
	{
		super(parent);
		mapLocationEditor = new MapLocationEditor(this);
	}

	public void setData() throws PWCGException 
	{
        setMapBackground(100);
		repaint();
	}

    private void repaintMap()
    {
        makeVisible(false);
        makeVisible(true);
        repaint();
    }

	public void paintComponent(Graphics g)
	{
		try
		{
	        //Graphics2D graphicsFromImage = (Graphics2D) image.getGraphics();

			paintBaseMap(g);
			
			g.setColor(Color.black);
			
			if (whatToDisplay[0] == null)
			{
			    return;
			}
			
	          
            if (whatToDisplay[DISPLAY_CITIES])
            {
                GroupManager groupManager =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
                TownFinder townFinder = groupManager.getTownFinder();
                LocationSet towns = townFinder.getTownLocations();
                for (PWCGLocation town : towns.getLocations())
                {
                    drawPointsWithName(g, town.getPosition(), town.getName());
                }
            }

			if (whatToDisplay[DISPLAY_AIRFIELDS])
			{
		        AirfieldManager airfieldData =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
		        Map<String, Airfield> allAF = airfieldData.getAllAirfields();
		        for (Airfield af : allAF.values())
		        {
		            drawCross(g, af.getPosition(), af.getName());
		        }
		        //writeFile(graphicsFromImage);
			}            

            Coordinate prevCoordinate = null;
            for (FrontLinePoint frontLinePoint : frontLineEditor.getUserCreatedFrontLines())
            {
                drawPointsByCountry(g, frontLinePoint.getPosition(), prevCoordinate, frontLinePoint.getCountry());
                prevCoordinate = frontLinePoint.getPosition().copy();
            }
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}

    private void drawPointsByCountry(Graphics g, Coordinate coordinate, Coordinate prevCoordinate, ICountry country) 
    {
        Color color = ColorMap.UNKNOWN;
                        
        if (country == null)
        {
            color = ColorMap.PAPER_FOREGROUND;
        }
        else if (country.getSide() == Side.AXIS)
        {
            color = ColorMap.AXIS_BLACK;
        }
        else if (country.getSide() == Side.ALLIED)
        {
            color = ColorMap.RUSSIAN_RED;
        }
        else if (country.isNeutral())
        {
            color = ColorMap.NEUTRAL;
        }
        
        drawPoints(g, coordinate, prevCoordinate, color);
    }

	private void drawPoints(Graphics g, Coordinate coordinate, Coordinate prevCoordinate, Color color) 
	{
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        
        g.setColor(color);

        int size = 20;
        
        int halfWay = size / 2;

        Point point = super.coordinateToPoint(coordinate);

        Ellipse2D.Double circle = new Ellipse2D.Double(point.x - halfWay, point.y - halfWay, size, size);
        g2.fill(circle);
        
        if (prevCoordinate != null)
        {
            Point prevPoint = super.coordinateToPoint(prevCoordinate);
            g2.drawLine(prevPoint.x,  prevPoint.y, point.x, point.y);
        }
	}


    private void drawPointsWithName(Graphics g, Coordinate coordinate, String name) 
    {
        Color color = ColorMap.BELGIAN_GOLD;
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18)); 
        drawPoints(g, coordinate, null, color);
        Graphics2D g2 = (Graphics2D) g;
        Point point = super.coordinateToPoint(coordinate);
        g2.drawString(name, point.x+15, point.y+3);

    }

    private void drawCross(Graphics g, Coordinate coordinate, String name) 
    {
        Color color = Color.WHITE;
        
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18)); 
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(3));
        
        g.setColor(color);

        Point point = super.coordinateToPoint(coordinate);

        g2.drawLine(point.x-3,  point.y-3, point.x+3, point.y+3);
        g2.drawLine(point.x-3,  point.y+3, point.x+3, point.y-3);
        
        g2.drawString(name, point.x+8, point.y+3);
    }

	@Override
    protected void paintFrontLines(Graphics g, boolean drawPoints) throws PWCGException 
    {
    }

    @Override
    public void mouseMovedCallback(MouseEvent e) 
    {       
    }

    @Override
    public void mouseDraggedCallback(MouseEvent e)
    {
        super.mouseDraggedCallback(e);
    }

    @Override
    public void leftClickReleasedCallback(MouseEvent e) throws PWCGException  
    {       
        if (editMode == EDIT_MODE_EDIT_FRONT)
        {
            frontLineEditor.releaseFrontPointToMove(e);
            repaintMap();
        }
        else if (editMode == EDIT_MODE_NONE)
        {
            super.leftClickReleasedCallback(e);
        }
    }

    @Override
    public void leftClickCallback(MouseEvent e) 
    {       
        try
        {
            if (editMode == EDIT_MODE_CREATE_NEW_FRONT)
            {
                frontLineEditor.createFrontPoint(e);
                repaintMap();
            }
            else if (editMode == EDIT_MODE_ADD_FRONT)
            {
                frontLineEditor.addFrontPointToLines(e);
                repaintMap();
            }
            else if (editMode == EDIT_MODE_EDIT_FRONT)
            {
                frontLineEditor.selectFrontPointToMove(e);
            }
            else if (editMode == EDIT_MODE_DELETE_FRONT)
            {
                frontLineEditor.deletePoint(e);
                repaintMap();
            }
            else if (editMode == EDIT_MODE_ADD_MAP_LOCATIONS)
            {
                mapLocationEditor.createCity(parent.getName(), e);
                repaintMap();
            }
            else if (editMode == EDIT_MODE_NONE)
            {
                super.leftClickCallback(e);
            }
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }
    }

    @Override
    public void rightClickCallback(MouseEvent e) 
    {       
        try
        {
            Point clickPoint = new Point();
            clickPoint.x = e.getX();
            clickPoint.y = e.getY();
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }
    }

	public Dimension getMapSize()
	{
		Dimension mapSize = new Dimension();
		mapSize.height =  image.getHeight(null);
		mapSize.width =  image.getWidth(null);
		return mapSize;
	}

	public void setWhatToDisplay(int displayItem, boolean displayIt)
	{
		this.whatToDisplay[displayItem] = displayIt;
	    		
		repaintMap();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
	}

	public ICountry getCountry() {
		return country;
	}

	public void setCountry(ICountry country) {
		this.country = country;
	}

    public void setEditMode(int editMode)
    {
        this.editMode = editMode;
    }

    public int getEditMode()
    {
       return editMode;
    }

    public FrontLineEditor getFrontLineCreator()
    {
        return frontLineEditor;
    }

    public void setFrontLineCreator(FrontLineEditor frontLineCreator)
    {
        this.frontLineEditor = frontLineCreator;
    }

    @Override
    public void rightClickReleasedCallback(MouseEvent e)
    {        
    }

    @Override
    public void centerClickCallback(MouseEvent e) 
    {
    }

    @Override
    public Point upperLeft()
    {
        return null;
    }

    public void resetFromActual()
    {
        repaintMap();
    }

    public void mirrorFrontLines() throws PWCGException
    {
        FrontLineCreator frontLineCreator = new FrontLineCreator();
        List<FrontLinePoint> modifiedFrontLines = frontLineCreator.createFrontLines(frontLineEditor.getUserCreatedFrontLines());
        frontLineEditor.replaceFrontLines(modifiedFrontLines);
        repaintMap();
    }

    public void writeFile(Graphics g) throws IOException
    {
        ImageIO.write(image, "jpg", new File("D:\\PWCG\\MAPFILE.jpg"));
    }
}
