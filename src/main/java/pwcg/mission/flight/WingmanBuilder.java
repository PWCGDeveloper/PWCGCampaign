package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.FlightElementSizeCalculator;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.group.WingmanMcuGroup;

public class WingmanBuilder
{
    private IFlight flight;

    public WingmanBuilder(IFlight flight)
    {
        this.flight = flight;
    }
    
    public void buildWingmenForFlight() throws PWCGException
    {
        if (!canCreateWingmen())
        {
            return;
        }
        
        for (FlightElement element : decomposeFlightIntoElements())
        {
            PlaneMcu leader = null;
            for (PlaneMcu plane : element.getPlanesInElement())
            {
                if (leader == null)
                {
                    leader = plane;
                }
                else
                {
                    if (plane.isActivePlayerPlane())
                    {
                        continue;
                    }
                    
                    WingmanMcuGroup wingmanCommands = new WingmanMcuGroup(flight, plane, leader);
                    wingmanCommands.buildWingmanCover();
                    plane.setWingman(wingmanCommands);
                }
            }
        }
    }

    private boolean  canCreateWingmen()
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return false;
        }
        
        if (!flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
        {
            return false;
        }
        
        if (flight.getFlightInformation().isVirtual())
        {
            return false;
        }

        return true;
    }
    
    private List<FlightElement> decomposeFlightIntoElements() throws PWCGException
    {
        List<FlightElement> elements = new ArrayList<>();
        int elementSize = FlightElementSizeCalculator.calculateElementSizeForFighters(flight.getFlightInformation());
        int numFullElements = flight.getFlightPlanes().getFlightSize() / elementSize;
        for (int elementIndex = 0; elementIndex < numFullElements; ++elementIndex)
        {
            FlightElement element = new FlightElement();
            for (int planeIndex = (elementSize * elementIndex); planeIndex < (elementSize * (elementIndex+1)); ++planeIndex)
            {
                element.addPlaneToElement(flight.getFlightPlanes().getPlanes().get(planeIndex));
            }
            elements.add(element);
        }
        return elements;
    }

}
