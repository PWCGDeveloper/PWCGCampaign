package pwcg.core.utils;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.PWCGLocation;


public class PointsAlongCurve
{
    CubicCurve2D curve;
    FlatteningPathIterator fpi;
    double length;
    double curLength;
    Point2D curSrcPoint;
    Point2D curDstPoint;
    double segLength;

    public PointsAlongCurve(CubicCurve2D curve)
    {
        this.curve = (CubicCurve2D) curve.clone();
        reset();
        iterate(Double.POSITIVE_INFINITY);
        length = curLength;
        reset();
    }

    private void reset() {
        fpi = new FlatteningPathIterator(curve.getPathIterator(null), 0.5);
        curLength = 0;
        float[] coords = new float[6];
        int type = fpi.currentSegment(coords);
        assert(type == PathIterator.SEG_MOVETO);
        curSrcPoint = new Point2D.Float(coords[0], coords[1]);
        fpi.next();
        type = fpi.currentSegment(coords);
        assert(type == PathIterator.SEG_LINETO);
        curDstPoint = new Point2D.Float(coords[0], coords[1]);
        segLength = curSrcPoint.distance(curDstPoint);
    }

    private void iterate(double val)
    {
        if (val < curLength)
            reset();

        while (curLength + segLength < val)
        {
            fpi.next();
            if (fpi.isDone())
                return;

            curSrcPoint = curDstPoint;
            curLength += segLength;
            float[] coords = new float[6];
            int type = fpi.currentSegment(coords);
            assert(type == PathIterator.SEG_LINETO);
            curDstPoint = new Point2D.Float(coords[0], coords[1]);
            segLength = curDstPoint.distance(curSrcPoint);
        }

    }

    public PWCGLocation getPoint(double val) throws PWCGException
    {
        iterate(val);
        if (fpi.isDone())
            throw new IllegalArgumentException();
        double t = (val - curLength) / segLength;
        assert(t >= 0 && t < 1);
        Point2D point = new Point2D.Double((1 - t) * curSrcPoint.getX() + t * curDstPoint.getX(),
                (1 - t) * curSrcPoint.getY() + t * curDstPoint.getY());
        double orientation = Math.toDegrees(Math.atan2(curDstPoint.getY() - curSrcPoint.getY(), curDstPoint.getX() - curSrcPoint.getX()));

        PWCGLocation loc = new PWCGLocation();
        loc.getPosition().setXPos(point.getX());
        loc.getPosition().setYPos(point.getY());
        loc.getOrientation().setyOri(orientation);

        return loc;
    }

    public double getLength()
    {
        return length;
    }
}