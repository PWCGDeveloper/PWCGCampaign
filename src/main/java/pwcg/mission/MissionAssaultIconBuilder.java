package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuIconFactory;
import pwcg.mission.target.AssaultDefinition;

public class MissionAssaultIconBuilder
{
    private List<ArrowIcon> assaultIcons = new ArrayList<>();

    public void createAssaultIcons(List<AssaultDefinition> assaultDefinitions) throws PWCGException
    {
        for (AssaultDefinition assaultDefinition : assaultDefinitions)
        {
            createWaypointIconsForAssault(assaultDefinition);
        }
    }

    private void createWaypointIconsForAssault(AssaultDefinition assaultDefinition) throws PWCGException
    {
        ArrowIcon arrowIcon = new ArrowIcon();
        double arrowOrientation = MathUtils.calcAngle(assaultDefinition.getAssaultPosition(), assaultDefinition.getDefensePosition());
        String assaultingCountryName = assaultDefinition.getAssaultingCountry().getCountryName();

        arrowIcon.tail = buildTail(assaultDefinition, arrowOrientation);
        arrowIcon.tail.setDesc(assaultingCountryName + " Arrow Tail");
        
        arrowIcon.preAssaultPoint = buildPreAssault(assaultDefinition, arrowOrientation);
        arrowIcon.preAssaultPoint.setDesc(assaultingCountryName + " Arrow Pre Assault");

        arrowIcon.assaultPoint = buildAssault(assaultDefinition, arrowOrientation);
        arrowIcon.assaultPoint.setDesc(assaultingCountryName + " Arrow Assault");

        arrowIcon.destinationPoint = buildDestination(assaultDefinition, arrowOrientation);        
        arrowIcon.destinationPoint.setDesc(assaultingCountryName + " Arrow Defense");

        arrowIcon.postDestinationPoint = buildPostDestination(assaultDefinition, arrowOrientation);
        arrowIcon.postDestinationPoint.setDesc(assaultingCountryName + " Arrow Post Defense");

        arrowIcon.head = buildHead(assaultDefinition, arrowOrientation);
        arrowIcon.head.setDesc(assaultingCountryName + " Arrow Head");

        linkIconsIntoArrow(arrowIcon);

        
        assaultIcons.add(arrowIcon);
    }
    
    private McuIcon buildTail(AssaultDefinition assaultDefinition, double arrowOrientation) throws PWCGException
    {
        double oppositeArrowOrientation = MathUtils.adjustAngle(arrowOrientation, 180);
        Coordinate tailPosition = MathUtils.calcNextCoord(assaultDefinition.getDefensePosition(), oppositeArrowOrientation, 9000.0);

        return McuIconFactory.buildAssaultIcon(tailPosition, arrowOrientation, assaultDefinition.getAssaultingCountry().getSide());
    }

    private McuIcon buildPreAssault(AssaultDefinition assaultDefinition, double arrowOrientation) throws PWCGException
    {
        double oppositeArrowOrientation = MathUtils.adjustAngle(arrowOrientation, 180);
        Coordinate preAssaultPosition = MathUtils.calcNextCoord(assaultDefinition.getDefensePosition(), oppositeArrowOrientation, 8500.0);
        
        double angleUp = MathUtils.adjustAngle(arrowOrientation, 90);
        preAssaultPosition = MathUtils.calcNextCoord(preAssaultPosition, angleUp, 500.0);

        return McuIconFactory.buildAssaultIcon(preAssaultPosition, arrowOrientation, assaultDefinition.getAssaultingCountry().getSide());
    }

    private McuIcon buildAssault(AssaultDefinition assaultDefinition, double arrowOrientation) throws PWCGException
    {
        double oppositeArrowOrientation = MathUtils.adjustAngle(arrowOrientation, 180);
        Coordinate assaultPosition = MathUtils.calcNextCoord(assaultDefinition.getDefensePosition(), oppositeArrowOrientation, 6000.0);
        
        double angleUp = MathUtils.adjustAngle(arrowOrientation, 90);
        assaultPosition = MathUtils.calcNextCoord(assaultPosition, angleUp, 3000.0);

        return McuIconFactory.buildAssaultIcon(assaultPosition, arrowOrientation, assaultDefinition.getAssaultingCountry().getSide());
    }

    private McuIcon buildDestination(AssaultDefinition assaultDefinition, double arrowOrientation) throws PWCGException
    {
        Coordinate defensePosition = assaultDefinition.getDefensePosition().copy();
        
        double angleUp = MathUtils.adjustAngle(arrowOrientation, 90);
        defensePosition = MathUtils.calcNextCoord(defensePosition, angleUp, 3000.0);

        return McuIconFactory.buildAssaultIcon(defensePosition, arrowOrientation, assaultDefinition.getAssaultingCountry().getSide());
    }

    private McuIcon buildPostDestination(AssaultDefinition assaultDefinition, double arrowOrientation) throws PWCGException
    {
        Coordinate preAssaultPosition = MathUtils.calcNextCoord(assaultDefinition.getDefensePosition(), arrowOrientation, 3000.0);
        return McuIconFactory.buildAssaultIcon(preAssaultPosition, arrowOrientation, assaultDefinition.getAssaultingCountry().getSide());
    }
    
    private McuIcon buildHead(AssaultDefinition assaultDefinition, double arrowOrientation) throws PWCGException
    {
        Coordinate headPosition = MathUtils.calcNextCoord(assaultDefinition.getDefensePosition(), arrowOrientation, 3000.0);
        
        double angleUp = MathUtils.adjustAngle(arrowOrientation, 90);
        headPosition = MathUtils.calcNextCoord(headPosition, angleUp, 1200.0);

        return McuIconFactory.buildAssaultIcon( headPosition, arrowOrientation, assaultDefinition.getAssaultingCountry().getSide());
    }


    private void linkIconsIntoArrow(ArrowIcon arrowIcon)
    {
        arrowIcon.tail.setTarget(arrowIcon.preAssaultPoint.getIndex());
        arrowIcon.preAssaultPoint.setTarget(arrowIcon.assaultPoint.getIndex());
        arrowIcon.assaultPoint.setTarget(arrowIcon.destinationPoint.getIndex());
        arrowIcon.destinationPoint.setTarget(arrowIcon.postDestinationPoint.getIndex());
        arrowIcon.postDestinationPoint.setTarget(arrowIcon.head.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        for (ArrowIcon assaultIcon : assaultIcons)
        {
            assaultIcon.tail.write(writer);
            assaultIcon.preAssaultPoint.write(writer);
            assaultIcon.assaultPoint.write(writer);
            assaultIcon.destinationPoint.write(writer);
            assaultIcon.postDestinationPoint.write(writer);
            assaultIcon.head.write(writer);
        }
    }

    private class ArrowIcon
    {
        McuIcon tail;
        McuIcon preAssaultPoint;
        McuIcon assaultPoint;
        McuIcon destinationPoint;
        McuIcon postDestinationPoint;
        McuIcon head;
    }
}
