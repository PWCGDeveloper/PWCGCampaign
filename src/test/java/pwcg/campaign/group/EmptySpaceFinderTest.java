package pwcg.campaign.group;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;

@ExtendWith(MockitoExtension.class)
public class EmptySpaceFinderTest
{
    @Mock Mission mission;
    @Mock Campaign campaign;
    
    @Test
    public void findEmptySpaceAroundAirfield() throws PWCGException
    {
        Mockito.when(mission.getCampaignMap()).thenReturn(FrontMapIdentifier.BODENPLATTE_MAP);
        
        PWCGContext.setProduct(PWCGProduct.BOS);
        AirfieldManager airfieldManager  = PWCGContext.getInstance().getMap(FrontMapIdentifier.BODENPLATTE_MAP).getAirfieldManager();
        
        for (Airfield airfield : airfieldManager.getAllAirfields().values())
        {
            EmptySpaceFinder emptySpaceFinder = new EmptySpaceFinder(mission);
            List<HotSpot> hotSpots = emptySpaceFinder.findEmptySpaces(FrontMapIdentifier.BODENPLATTE_MAP, airfield.getBoundary(FrontMapIdentifier.BODENPLATTE_MAP), 50);
            
            Assertions.assertTrue (hotSpots.size() > 0);

            CoordinateBox coordinateBox = CoordinateBox.coordinateBoxFromCoordinateList(airfield.getBoundary(FrontMapIdentifier.BODENPLATTE_MAP));
            for (HotSpot hotSpot : hotSpots)
            {
                assert(coordinateBox.isInBox(hotSpot.getPosition()));
            }
            
        }
    }
}
