package pwcg.dev.jsonconvert;

import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.core.exception.PWCGException;

public class BoSGroundObjectIOJsonConverter extends GroundObjectIOJsonConverter{

	List<List<Block>> railroadsJson = null;

    public static void main(String[] args) throws PWCGException
    {
    	PWCGContextManager.setRoF(false);

    	BoSGroundObjectIOJsonConverter jsonConverter = new BoSGroundObjectIOJsonConverter();
    	//jsonConverter.convert("Moscow");
    	//jsonConverter.convert("Stalingrad");
    	jsonConverter.convert("Kuban");
    }
 }
