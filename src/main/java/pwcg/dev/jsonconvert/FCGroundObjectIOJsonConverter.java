package pwcg.dev.jsonconvert;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

public class FCGroundObjectIOJsonConverter extends GroundObjectIOJsonConverter{

    public static void main(String[] args) throws PWCGException
    {
    	PWCGContext.setProduct(PWCGProduct.FC);

    	FCGroundObjectIOJsonConverter jsonConverter = new FCGroundObjectIOJsonConverter();
    	jsonConverter.convert("Arras");
    }
 }
