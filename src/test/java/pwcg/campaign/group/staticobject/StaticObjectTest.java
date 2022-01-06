package pwcg.campaign.group.staticobject;

import java.util.Date;

import org.junit.jupiter.api.Test;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleRequestDefinition;

public class StaticObjectTest {
    static final Country[] bos_countries = {Country.BRITAIN, Country.GERMANY, Country.ITALY, Country.RUSSIA, Country.USA}; 
    static final Country[] fc_countries = {Country.BELGIUM, Country.BRITAIN, Country.FRANCE, Country.GERMANY, Country.USA}; 

    @Test
    public void staticObjectTestBoS() throws PWCGException 
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        runTest(bos_countries);
    }

    @Test
    public void staticObjectTestFC() throws PWCGException 
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        runTest(fc_countries);
    }

    private void runTest(Country[] countries) throws PWCGException
    {
        for (Date date = DateUtils.getBeginningOfGame(); date.before(DateUtils.getEndOfWar()); date = DateUtils.advanceTimeDays(date, 30))
        {
            for (Country country : countries)
            {
                VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(country, date, VehicleClass.StaticAirfield);
                assert(PWCGContext.getInstance().getStaticObjectDefinitionManager().getVehicleDefinitionForRequest(requestDefinition) != null);
            }
        }
    }

}
