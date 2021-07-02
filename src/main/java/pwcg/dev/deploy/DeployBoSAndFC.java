package pwcg.dev.deploy;

public class DeployBoSAndFC extends DeployBase
{
    static public void main (String[] args)
    {
        BoSDeploy bosDeploy = new BoSDeploy();
        bosDeploy.doDeploy();
        
        FCDeploy fcDeploy = new FCDeploy();
        fcDeploy.doDeploy();
    }
}
