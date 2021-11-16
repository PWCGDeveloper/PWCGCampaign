package integration.campaign.io.json;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.SkirmishProfileIOJson;
import pwcg.campaign.skirmish.SkirmishProfile;
import pwcg.campaign.skirmish.SkirmishProfileType;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class SkirmishProfileIOJsonTest
{
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance();
        Map<SkirmishProfileType, SkirmishProfile> skirmishProfiles = SkirmishProfileIOJson.readJson();
        Assertions.assertTrue (skirmishProfiles.size() == 9);
    }
}
