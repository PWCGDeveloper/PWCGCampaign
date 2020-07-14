package pwcg.mission.flight.plane;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlightInformation;

public class FlightElementSizeCalculator 
{

    public static int calculateElementSizeForFighters(IFlightInformation flightInformation) throws PWCGException
    {        
        Campaign campaign = flightInformation.getCampaign();
        Squadron squadron = flightInformation.getSquadron();
        
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            int minimum = 2;
            return minimum + RandomNumberGenerator.getRandom(4);
        }
        
        if (squadron.getCountry().getCountry() != Country.GERMANY)
        {
            return 2;
        }
        else if (squadron.getCountry().getCountry() != Country.RUSSIA)
        {
            if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19430401")))
            {
                return 3;
            }
            else
            {
                return 2;
            }
        }
        else
        {
            if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19420301")))
            {
                return 3;
            }
            else
            {
                return 2;
            }   
        }
    }
}
