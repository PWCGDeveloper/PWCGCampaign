package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SquadronSkinExistsTest
{
    @Test
    public void skinLoaderInitializeBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Skin> missingFromDownload = new ArrayList<>();
        for (Squadron squadron : squadronManager.getAllSquadrons())
        {
            for (Skin skin : squadron.getSkins())
            {
                if (!skin.skinExists(Skin.PRODUCT_SKIN_DIR))
                {
                    missingFromDownload.add(skin);
                }
             }
        }
        
        for (Skin skin : missingFromDownload) {
            System.out.println("Missing skin plane: " + skin.getPlane() + "   name: " + skin.getSkinName() + "   Squadron: " + skin.getSquadId());
        }
    }
    
    @Test
    public void skinLoaderInitializeFCTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);

        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Skin> missingFromDownload = new ArrayList<>();
        for (Squadron squadron : squadronManager.getAllSquadrons())
        {
            for (Skin skin : squadron.getSkins())
            {
                if (!skin.isDefinedInGame()) 
                {
                    if (!skin.skinExists(Skin.PRODUCT_SKIN_DIR))
                    {
                        missingFromDownload.add(skin);
                    }
                }
             }
        }
        
        for (Skin skin : missingFromDownload) {
            System.out.println("Missing skin plane: " + skin.getPlane() + "   name: " + skin.getSkinName() + "   Squadron: " + skin.getSquadId());
        }

    }
}
