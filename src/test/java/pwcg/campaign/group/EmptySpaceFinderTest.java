package pwcg.campaign.group;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;

public class EmptySpaceFinderTest
{
    @Mock Mission mission;
    
    @Test
    public void findEmptySpaceAroundAirfield() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);
        AirfieldManager airfieldManager  = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        
        for (Airfield airfield : airfieldManager.getAllAirfields().values())
        {
            EmptySpaceFinder emptySpaceFinder = new EmptySpaceFinder(mission);
            List<HotSpot> hotSpots = emptySpaceFinder.findEmptySpaces(airfield.getBoundary(), 50);
            
            assert (hotSpots.size() > 0);

            CoordinateBox coordinateBox = CoordinateBox.coordinateBoxFromCoordinateList(airfield.getBoundary());
            for (HotSpot hotSpot : hotSpots)
            {
                assert(coordinateBox.isInBox(hotSpot.getPosition()));
            }
            
        }
    }
}
