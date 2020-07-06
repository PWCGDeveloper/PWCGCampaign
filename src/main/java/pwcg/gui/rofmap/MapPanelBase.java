package pwcg.gui.rofmap;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.FrontParameters;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ImagePanel;

public abstract class MapPanelBase extends ImagePanel implements ActionListener
{
    protected static final long serialVersionUID = 1L;

    protected double ratioHeight = 0.0;
    protected double ratioWidth = 0.0;
    protected Dimension mapSize;

    protected int scaleLevel = 100;

    MouseAdapter mouseClickListener = null;
    MouseMotionAdapter mouseMotionListener = null;

    protected boolean movementEnabled = false;
    protected Point mapScrollPositionStart = new Point();
    protected Point mapScrollPositionNow = new Point();

    protected MapGUI parent;

    public MapPanelBase(MapGUI parent) throws PWCGException
    {
        this.parent = parent;

        PWCGMouseClickListener mouseClickListener = new PWCGMouseClickListener(this);
        PWCGMouseMotionListener mouseMotionListener = new PWCGMouseMotionListener(this);
        MouseWheelListener mouseWheelListener = new PWCGMouseWheelListener(this);

        addMouseListener(mouseClickListener);
        addMouseMotionListener(mouseMotionListener);
        this.addMouseWheelListener(mouseWheelListener);
    }

    public static void preloadMaps()
    {
        try
        {
            String mapPrefix = PWCGContext.getInstance().getCurrentMap().getMapName() + "Map";

            MapLoader mapLoader = new MapLoader(mapPrefix);
            mapLoader.loadPrimaryMap();
            
            Thread mapLoadThread = new Thread(mapLoader);
            mapLoadThread.start();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    public void setMapBackground(int zoom)
    {
        preloadMaps();
        scaleLevel = zoom;

        String mapImageName = PWCGContext.getInstance().getCurrentMap().getMapName() + "Map";
        if (scaleLevel < 100)
        {
            mapImageName += "0";
        }
        mapImageName += scaleLevel;
        super.setMapImage(mapImageName);
        
        if (image == null)
        {
            ErrorDialog.internalError("Map failed to load - did you install the map pack?  If not, get the map pack from the PWCG web site.");
        }
        else
        {
            mapSize = getImageSize();
            setPreferredSize(mapSize);
            setMinimumSize(mapSize);
            setMaximumSize(mapSize);
            setSize(mapSize);
            setLayout(null);

            FrontParameters frontParameters = PWCGContext.getInstance().getCurrentMap().getFrontParameters();

            ratioWidth = mapSize.width / frontParameters.getzMax();
            ratioHeight = mapSize.height / frontParameters.getxMax();

            MapScroll mapScroll = parent.getMapScroll();
            mapScroll.setScrollRange();

            refresh();
        }
    }

    public void increaseZoom()
    {
        if (scaleLevel == 50)
        {
            setMapBackground(75);
        }
        else if (scaleLevel == 75)
        {
            setMapBackground(100);
        }
        else if (scaleLevel == 100)
        {
            setMapBackground(125);
        }
        else if (scaleLevel == 125)
        {
            setMapBackground(150);
        }
        else if (scaleLevel == 150)
        {
        }
    }

    public void decreaseZoom()
    {
        if (scaleLevel == 50)
        {
        }
        else if (scaleLevel == 75)
        {
            setMapBackground(50);
        }
        else if (scaleLevel == 100)
        {
            setMapBackground(75);
        }
        else if (scaleLevel == 125)
        {
            setMapBackground(100);
        }
        else if (scaleLevel == 150)
        {
            setMapBackground(125);
        }
    }

    public Point coordinateToPoint(Coordinate rofCoord)
    {
        Point point = new Point();
        if (image != null)
        {
            point.x = Double.valueOf((rofCoord.getZPos() * ratioWidth)).intValue();
            point.y = mapSize.height - Double.valueOf((rofCoord.getXPos() * ratioHeight)).intValue();
        }

        return point;
    }

    public Coordinate pointToCoordinate(Point point) throws PWCGException
    {
        Coordinate coord = new Coordinate();

        double x = Double.valueOf(point.x) / ratioWidth;
        int invertedY = mapSize.height - point.y;
        double y = Double.valueOf(invertedY) / ratioHeight;

        Point coordAsInt = new Point();
        coordAsInt.x = Double.valueOf(x).intValue();
        coordAsInt.y = Double.valueOf(y).intValue();

        coord.setZPos(coordAsInt.x);
        coord.setXPos(coordAsInt.y);

        return coord;
    }

    public Dimension getMapSize()
    {
        return getImageSize();
    }

    protected void paintBaseMapWithMajorGroups(Graphics g) throws PWCGException
    {
        paintBaseMap(g);
    }

    protected void paintBaseMap(Graphics g) throws PWCGException
    {
        g.drawImage(image, 0, 0, null);

        paintFrontLines(g, false);
    }

    protected void paintFrontLines(Graphics g, boolean drawPoints) throws PWCGException
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(parent.getMapDate());
        List<FrontLinePoint> frontLinesAllied = frontLinesForMap.getFrontLines(Side.ALLIED);
        Point prev = null;
        for (FrontLinePoint frontCoord : frontLinesAllied)
        {
            Point point = coordinateToPoint(frontCoord.getPosition());
            g2.setColor(ColorMap.RUSSIAN_RED);

            if (prev != null && shouldDraw(prev.x, prev.y, point.x, point.y))
            {
                g2.draw(new Line2D.Float(prev.x, prev.y, point.x, point.y));
            }

            prev = point;
        }

        List<FrontLinePoint> frontLinesAxis = frontLinesForMap.getFrontLines(Side.AXIS);
        prev = null;
        for (FrontLinePoint frontCoord : frontLinesAxis)
        {
            Point point = coordinateToPoint(frontCoord.getPosition());
            g2.setColor(ColorMap.AXIS_BLACK);

            if (prev != null && shouldDraw(prev.x, prev.y, point.x, point.y))
            {
                g2.draw(new Line2D.Float(prev.x, prev.y, point.x, point.y));
            }

            prev = point;
        }
    }

    private boolean shouldDraw(int x, int y, int x2, int y2) throws PWCGException
    {
        Point point1 = new Point();
        point1.x = x;
        point1.y = y;
        Coordinate coord1 = this.pointToCoordinate(point1);

        Point point2 = new Point();
        point2.x = x2;
        point2.y = y2;
        Coordinate coord2 = this.pointToCoordinate(point2);

        double distance = MathUtils.calcDist(coord1, coord2);

        if (distance < 20000.0)
        {
            return true;
        }

        return false;
    }

    public Point getBestPoint(List<Point> usedPoints, Point point)
    {
        Point bestPoint = new Point();
        bestPoint.x = point.x;
        bestPoint.y = point.y;

        boolean good = true;
        int numTries = 0;
        do
        {
            for (Point usedPoint : usedPoints)
            {
                if (Math.abs(usedPoint.y - bestPoint.y) < 30)
                {
                    if (Math.abs(usedPoint.x - bestPoint.x) < 70)
                    {
                        good = false;
                        bestPoint.y += 30;

                        break;
                    }
                }
            }

            ++numTries;

            if (numTries > 3)
            {
                good = true;
            }
        }
        while (!good);

        return bestPoint;
    }

    public void refresh()
    {
        this.revalidate();
        this.repaint();
    }

    public void makeVisible(boolean isVisible)
    {
        this.setVisible(isVisible);
    }

    public abstract void paintComponent(Graphics g);

    public abstract void mouseMovedCallback(MouseEvent e);

    public void mouseDraggedCallback(MouseEvent mouseEvent)
    {
        if (movementEnabled)
        {
            mapScrollPositionNow.x = mouseEvent.getX();
            mapScrollPositionNow.y = mouseEvent.getY();

            int xMovement = Double.valueOf((mapScrollPositionNow.x - mapScrollPositionStart.x) * 0.75).intValue();
            int yMovement = Double.valueOf((mapScrollPositionNow.y - mapScrollPositionStart.y) * 0.75).intValue();

            parent.getMapScroll().moveScrollBarPosition(xMovement, yMovement);

            mapScrollPositionStart.x = mouseEvent.getX();
            mapScrollPositionStart.y = mouseEvent.getY();            
        }

    }

    public void leftClickCallback(MouseEvent mouseEvent)
    {
        movementEnabled = true;
        mapScrollPositionStart.x = mouseEvent.getX();
        mapScrollPositionStart.y = mouseEvent.getY();
    }

    public void leftClickReleasedCallback(MouseEvent mouseEvent) throws PWCGException
    {
        movementEnabled = false;
    }

    protected void drawArrow(Graphics g, int x1, int y1, int x2, int y2)
    {
        final int ARR_SIZE = 10;

        Graphics2D g2d = (Graphics2D) g.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g2d.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g2d.drawLine(0, 0, len, 0);
        g2d.fillPolygon(new int[]
        { len, len - ARR_SIZE, len - ARR_SIZE, len }, new int[]
        { 0, -ARR_SIZE, ARR_SIZE, 0 }, 4);
    }

    public abstract void rightClickCallback(MouseEvent e);

    public abstract void rightClickReleasedCallback(MouseEvent e);

    public abstract Point upperLeft();
}
