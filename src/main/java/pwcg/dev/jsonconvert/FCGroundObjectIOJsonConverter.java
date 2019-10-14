package pwcg.dev.jsonconvert;

import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.Block;
import pwcg.core.exception.PWCGException;

public class FCGroundObjectIOJsonConverter extends GroundObjectIOJsonConverter{

	List<List<Block>> railroadsJson = null;

    public static void main(String[] args) throws PWCGException
    {
    	PWCGContext.setProduct(PWCGProduct.FC);

    	FCGroundObjectIOJsonConverter jsonConverter = new FCGroundObjectIOJsonConverter();
    	jsonConverter.convert("Arras");
    }
 }
