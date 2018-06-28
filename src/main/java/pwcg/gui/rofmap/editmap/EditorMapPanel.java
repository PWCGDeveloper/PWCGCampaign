package pwcg.gui.rofmap.editmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapPanelBase;

public class EditorMapPanel extends MapPanelBase
{
	public static int DISPLAY_AIRFIELDS = 0;

    public static int EDIT_MODE_NONE = 0;

    public static int EDIT_MODE_CREATE_NEW_FRONT = 1;
    public static int EDIT_MODE_ADD_FRONT = 2;
    public static int EDIT_MODE_DELETE_FRONT = 3;
    public static int EDIT_MODE_EDIT_FRONT = 4;

	private static final long serialVersionUID = 1L;
	
	private ICountry country = CountryFactory.makeMapReferenceCountry(Side.ALLIED);
    private int editMode = EDIT_MODE_NONE;
    private Boolean[] whatToDisplay = new Boolean[8];

    private FrontLineEditor frontLineEditor = null;


	public EditorMapPanel(MapGUI parent) throws PWCGException  
	{
		super(parent);
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
    }

	public void paintComponent(Graphics g)
	{
		try
		{
			paintBaseMap(g);
			
			g.setColor(Color.black);
			
			if (whatToDisplay[0] == null)
			{
			    return;
			}
			
			if (whatToDisplay[DISPLAY_AIRFIELDS])
			{
		        AirfieldManager airfieldData =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager();
		        Map<String, IAirfield> allAF = airfieldData.getAllAirfields();
		        for (IAirfield af : allAF.values())
		        {
		            drawPointsByCountry(g, af.getPosition(), af.createCountry(parent.getMapDate()));
		        }        
			}
            
            for (FrontLinePoint frontLinePoint : frontLineEditor.getUserCreatedFrontLines())
            {
                drawPointsByCountry(g, frontLinePoint.getPosition(), frontLinePoint.getCountry());
            }
		}
		catch (Exception e)
		{
			Logger.logException(e);
		}
	}

    private void drawPointsByCountry(Graphics g, Coordinate coordinate, ICountry country) 
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
        
        drawPoints(g, coordinate, color);
    }

	private void drawPoints(Graphics g, Coordinate coordinate, Color color) 
	{
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        
        g.setColor(color);

        int size = 20;
        
        int halfWay = size / 2;

        Point point = super.coordinateToPoint(coordinate);

        Ellipse2D.Double circle = new Ellipse2D.Double(point.x - halfWay, point.y - halfWay, size, size);
        g2.fill(circle);
	}

	@Override
    protected void paintFrontLines(Graphics g, boolean drawPoints) throws PWCGException 
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
         
        // First draw the front
        Point prev = null;
        for (FrontLinePoint frontCoord : frontLineEditor.getUserCreatedFrontLines())
        {
            Point point = coordinateToPoint(frontCoord.getPosition());
            g2.setColor(ColorMap.PAPERPART_FOREGROUND);

            if (prev != null)
            {
                g2.draw(new Line2D.Float(prev.x, prev.y, point.x, point.y));
            }
            
            int size = 10;
            int halfWay = size / 2;
            Ellipse2D.Double circle = new Ellipse2D.Double(point.x - halfWay, point.y - halfWay, size, size);
            g2.fill(circle);

            prev = point;
        }
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
    public Point upperLeft()
    {
        return null;
    }

    public void resetFromActual()
    {
        repaintMap();
    }

    public void mirrorAlliedFrontLinesForAxis() throws PWCGException
    {
        FrontLineCreator frontLineCreator = new FrontLineCreator();
        List<FrontLinePoint> oppositeFrontLines = frontLineCreator.createAxisLines(frontLineEditor.getUserCreatedFrontLines());
        frontLineEditor.addAdditionalFrontLinePoints(oppositeFrontLines);
        repaintMap();
    }

}
