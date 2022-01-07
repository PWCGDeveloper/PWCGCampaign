package pwcg.campaign.plane;

import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.BlockDefinition;
import pwcg.campaign.group.BlockDefinitionManager;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;

public class StaticPlane extends Block implements IStaticPlane
{
	String staticPlaneName;
	
	public StaticPlane (String staticPlaneName)
	{
		index = IndexGenerator.getInstance().getNextIndex();
		this.staticPlaneName = staticPlaneName;
		initialize();
	}

    private void initialize()
    {
        Coordinate coords = new Coordinate();
        Orientation ori = new Orientation();

        String model = "graphics\\blocks\\" + staticPlaneName + ".mgm";
        String script = "LuaScripts\\WorldObjects\\Blocks\\"  + staticPlaneName + ".txt";
        
        this.setName(staticPlaneName);
        this.setModel(model);
        this.setScript(script);
        this.setDesc(staticPlaneName);
        this.setDurability(25000);
        this.setDamageReport(50);
        this.setDamageThreshold(1);
        this.setDeleteAfterDeath(0);
        this.setLinkTrId(0);

        this.setPosition(coords);
        this.setOrientation(ori);

        BlockDefinition blockDefinition = BlockDefinitionManager.getInstance().getBlockDefinition(staticPlaneName);
        if (blockDefinition != null)
        {
            this.setDurability(blockDefinition.getDurability());
        }
    }
}
