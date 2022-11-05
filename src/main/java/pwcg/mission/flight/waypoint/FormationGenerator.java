package pwcg.mission.flight.waypoint;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuFormation;

public class FormationGenerator
{
    private static final int ECHELON_LEFT_ANGLE = 260;
    private static final int ECHELON_RIGHT_ANGLE = 110;
    private Campaign campaign;
    
    public FormationGenerator(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void generatePositionForPlaneInFormation(List<PlaneMcu> planes, int formationType) throws PWCGException
    {
        Coordinate leadPlaneCoords = planes.get(0).getPosition().copy();
        for (PlaneMcu plane : planes)
        {
            if (plane.getNumberInFormation() == 0)
            {
                continue;
            }

            if (formationType == McuFormation.FORMATION_LEFT)
            {
                createEchelonLeftFormation(leadPlaneCoords, plane);
            }
            else if (formationType == McuFormation.FORMATION_RIGHT)
            {
                createEchelonRightFormation(leadPlaneCoords, plane);
            }
            else 
            {
                createVeeFormation(leadPlaneCoords, plane);
            }
        }
    }

    private void createEchelonRightFormation(Coordinate leadPlaneCoords, PlaneMcu plane) throws PWCGException
    {
        createEchelonFormation(leadPlaneCoords, plane, ECHELON_RIGHT_ANGLE);
    }
    
    private void createEchelonLeftFormation(Coordinate leadPlaneCoords, PlaneMcu plane) throws PWCGException
    {
        createEchelonFormation(leadPlaneCoords, plane, ECHELON_LEFT_ANGLE);
    }
    
    private void createEchelonFormation(Coordinate leadPlaneCoords, PlaneMcu plane, int relativePlacementAngle) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int horizontalSpacing = productSpecific.getFormationHorizontalSpacing();
        int verticalSpacing = productSpecific.getFormationVerticalSpacing();
        
        int movementMultiplier = plane.getNumberInFormation() - 1;

        placePlaneInFormation(leadPlaneCoords, plane, relativePlacementAngle, horizontalSpacing, verticalSpacing, movementMultiplier);
    }    
    
    private void createVeeFormation(Coordinate leadPlaneCoords, PlaneMcu plane) throws PWCGException
    {
        if (plane.getNumberInFormation() == 0)
        {
            return;
        }

        int relativePlacementAngle = ECHELON_LEFT_ANGLE;
        if ((plane.getNumberInFormation() % 2) == 0)
        {
            relativePlacementAngle = ECHELON_RIGHT_ANGLE;
        }
        
        int movementMultiplier = plane.getNumberInFormation() / 2;

        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int horizontalSpacing = productSpecific.getFormationHorizontalSpacing();
        int verticalSpacing = productSpecific.getFormationVerticalSpacing();

        placePlaneInFormation(leadPlaneCoords, plane, relativePlacementAngle, horizontalSpacing, verticalSpacing, movementMultiplier);
    }
    

    private void placePlaneInFormation(
            Coordinate leadPlaneCoords, PlaneMcu plane, 
            int relativePlacementAngle, int horizontalSpacing,
            int verticalSpacing, int movementMultiplier) throws PWCGException
    {
        double absolutePlacementAngle = MathUtils.adjustAngle(plane.getOrientation().getyOri(), relativePlacementAngle);
        Coordinate planeInFormationCoords = MathUtils.calcNextCoord(campaign.getCampaignMap(), leadPlaneCoords, absolutePlacementAngle, horizontalSpacing * movementMultiplier);
        planeInFormationCoords.setYPos(leadPlaneCoords.getYPos() + (verticalSpacing * movementMultiplier));
        plane.setPosition(planeInFormationCoords);
    }
}
