package pwcg.dev.jsonconvert.orig.io;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.gui.rofmap.editmap.FrontLineWriter;

public class WesternFrontPositionAdjuster
{    
    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.FC);

        WesternFrontPositionAdjuster fixer = new WesternFrontPositionAdjuster();
        fixer.fixFront("19170601");
        fixer.fixFront("19180329");
        fixer.fixFront("19180404");
        fixer.fixFront("19180429");
        fixer.fixFront("19180531");
        fixer.fixFront("19180604");
        fixer.fixFront("19180612");
        fixer.fixFront("19180918");
        fixer.fixFront("19180925");
        fixer.fixFront("19181004");
        fixer.fixFront("19181015");
        fixer.fixFront("19181101");
    }

    private void fixFront (String dateString) throws Exception 
    {
        List<FrontLinePoint> modifiedFrontLines = new ArrayList<>();
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.WESTERN_FRONT_MAP).getFrontLinesForMap(DateUtils.getDateYYYYMMDD(dateString));
        
        for (FrontLinePoint mapPoint : frontLinesForMap.getFrontLinesAllied())
        {
            Double before = mapPoint.getPosition().getXPos();
            System.out.println("Was :" + Double.valueOf(mapPoint.getPosition().getXPos()).intValue());
            Coordinate newCoordinates = recalculateX(mapPoint);
            Double now = newCoordinates.getXPos();
            System.out.println("Is  :" + Double.valueOf(newCoordinates.getXPos()).intValue() + " with a difference of " + Double.valueOf(before - now).intValue());
            mapPoint.setPosition(newCoordinates);
            modifiedFrontLines.add(mapPoint);
        }

        for (FrontLinePoint mapPoint : frontLinesForMap.getFrontLinesAxis())
        {
            Coordinate newCoordinates = recalculateX(mapPoint);
            mapPoint.setPosition(newCoordinates);
            modifiedFrontLines.add(mapPoint);
        }
    
        // Avoiding an accidental run and overwrite
        //writeData(dateString, modifiedFrontLines);
    }

    private Coordinate recalculateX(FrontLinePoint mapPoint) throws PWCGException
    {
        Point pointOnWrongMap = coordinateToPointOnWrongSize(mapPoint.getPosition());
        Coordinate correctedForRightMap = correctForRightMap(pointOnWrongMap);
        return correctedForRightMap;
    }
    

    public Point coordinateToPointOnWrongSize(Coordinate wrongCoord)
    {
        double ratioWidth = 4096.0 / 422400.0;
        double ratioHeight = 4096.0 / 345600.0;
        
        Point point = new Point();
        point.x = Double.valueOf((wrongCoord.getZPos() * ratioWidth)).intValue();
        point.y = 4096 - Double.valueOf((wrongCoord.getXPos() * ratioHeight)).intValue();
        return point;
    }

    public Coordinate correctForRightMap(Point point) throws PWCGException
    {
        double ratioWidth = 4096.0 / 422400.0;
        double ratioHeight = 3350.0 / 345600.0;

        Coordinate coord = new Coordinate();

        double x = Double.valueOf(point.x) / ratioWidth;
        int invertedY = 3350 - point.y;
        double y = Double.valueOf(invertedY) / ratioHeight;

        Point coordAsInt = new Point();
        coordAsInt.x = Double.valueOf(x).intValue();
        coordAsInt.y = Double.valueOf(y).intValue();

        coord.setZPos(coordAsInt.x);
        coord.setXPos(coordAsInt.y);

        return coord;
    }

    private void writeData(String datePath, List<FrontLinePoint> modifiedFrontLines) throws PWCGException 
    {
        FrontLineWriter frontLineWriter = new FrontLineWriter(modifiedFrontLines);
        String outputPath = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + PWCGContext.getInstance().getMap(FrontMapIdentifier.WESTERN_FRONT_MAP).getMapName() + "\\" + datePath + "\\";      
        frontLineWriter.finished(outputPath);
    }
}
