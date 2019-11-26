package pwcg.dev.jsonconvert;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

public class BoSGroundObjectIOJsonConverter extends GroundObjectIOJsonConverter{

    public static void main(String[] args) throws PWCGException
    {
    	PWCGContext.setProduct(PWCGProduct.BOS);

    	BoSGroundObjectIOJsonConverter jsonConverter = new BoSGroundObjectIOJsonConverter();
    	//jsonConverter.convert("Moscow");
    	//jsonConverter.convert("Stalingrad");
    	jsonConverter.convert("Bodenplatte");
    }
 }
