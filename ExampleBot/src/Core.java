import java.util.ArrayList;

import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.PlayerType;
import bwapi.Position;
import bwapi.Race;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Region;


public class Core extends DefaultBWListener
{
	private Mirror mirror = new Mirror();
	
	private static Game mSpiel;
	public static Game Spiel(){return mSpiel;}
	
	private static Player mselbst;
	public static Player selbst(){return mselbst;}
	
	private static Player menemy;
	public static Player enemy(){return menemy;}
	
	public void onUnitComplete(Unit unit)
	{
		Buildings.setzeSammelPunkt(Buildings.holeSammelpunkt());
	}
	
	public void run()
	{
		mirror.getModule().setEventListener(this);
		mirror.startGame();
	}

	public void onStart()
	{
		Buildings.reset();
		mSpiel = mirror.getGame();
		mselbst = mSpiel.self();
		menemy = mSpiel.enemy();
		BWTA.readMap();
		BWTA.analyze();
		Spiel().enableFlag(1);
		Spiel().setLocalSpeed(20);
		defineRace();
		Mapping.reset();
		Mapping.startscan(barrack);
		Einheiten.bekommeAlleArbeiter();

	}
	public void onFrame()
	{
		try{
			Buildings.produziereEinheit();
			Einheiten.gatherMinerals();
			Einheiten.gatherGas();			
			Mapping.mapToListChange(Mapping.getBauPosition());
			//Mapping.listeAufraemen(supply);
			Einheiten.bekommeAlleArbeiter();
			
			testbuild2();
			AttackUnits.AttackUnitList();
			Einheiten.attackFirstUnit();
			Buildings.produziereEinheit(UnitType.Terran_Marine);
			if(testbuild2())
			{
				System.out.println("build finished");
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[]args)
	{
		new Core().run();
	}



	public void bot()
	{
		if(!Buildings.isinproduction(bauer)
		&& selbst().allUnitCount(bauer)<20)
		{
			Buildings.produziereEinheit(bauer);
		}
		if(Einheiten.getArbeiter().size()>9
		&& selbst().allUnitCount(supply)<1)
		{
			Buildings.baueGeb(supply);
		}
		if(Einheiten.getArbeiter().size()>=11
		&& selbst().allUnitCount(barrack)<1)
		{
			Buildings.baueGeb(barrack);
		}
		if(selbst().allUnitCount(barrack)>=1
		&& selbst().allUnitCount(supply)<=3)
		{
			Buildings.baueGeb(supply);
		}
		
		if(selbst().allUnitCount(supply)>=3
		&& selbst().allUnitCount(barrack)<=3
		&& !Buildings.isinproduction(barrack))
		{
			Buildings.baueGeb(barrack);
		}
		
		
		/*
		if(selbst().allUnitCount(UnitType.Terran_Marine)>=10)
		{
			for(Unit aUnit : AttackUnits.attackMarines())
			{
				if(aUnit != null)
				{
					aUnit.attack(Einheiten.enemybase);
				}
			}
			
		}
		*/
		//if(selbst().allUnitCount(UnitType.Terran_Marine)>=1)
		//{
		//	Einheiten.scout();
		//}
		
	}
	
	public  boolean testbuild2()
	{
		if(!Buildings.isinproduction(bauer)
		&& selbst().allUnitCount(bauer)<20)
		{
			Buildings.produziereEinheit(bauer);
		}
		
		if((selbst().allUnitCount(bauer)>=8)
		&&(selbst().allUnitCount(supply)<1)
		&&(!Buildings.isinproduction(supply)))
		{
			Buildings.baueGeb(supply);
		}		
		else if((selbst().allUnitCount(bauer)>=11)
		&& (!Buildings.isinproduction(barrack))
		&& (selbst().allUnitCount(barrack)<1))
		{			
			Buildings.baueGeb(barrack);		
		}
		else if((selbst().allUnitCount(supply)<=2)
		&&(!Buildings.isinproduction(supply))
		&& (selbst().allUnitCount(barrack)==1))
		{
			Buildings.baueGeb(supply);
		}
		else if((selbst().allUnitCount(supply)>=2)
		&& selbst().allUnitCount(barrack)<2)
		{
			Buildings.baueGeb(barrack);
		}
		else if((selbst().allUnitCount(supply)<=8)
		&& selbst().allUnitCount(barrack)>=2)
		{
			Buildings.baueGeb(supply);
		}
		else if((selbst().allUnitCount(supply)==8)
		&& selbst().allUnitCount(barrack)>=2)
		{
			return true;
		}
		if(!Buildings.isinproduction(UnitType.Terran_Marine))
		{
			Buildings.produziereEinheit(UnitType.Terran_Marine);
		}
		if(AttackUnits.getMarines().size()>=1 && Einheiten.enemyBase == null)
		{
			Einheiten.scout();
		}
		if(AttackUnits.getMarines().size()>=10)
		{
			AttackUnits.angreifen();
		}
		return false;
	
	}
	
	private UnitType supply = null;
	private UnitType barrack=null;
	private UnitType bauer = null;
	private void defineRace()
	{
		if(selbst().getRace() == Race.Zerg)
		{
			supply = UnitType.Zerg_Overlord;
		}
			
		else if(selbst().getRace() == Race.Terran)
		{
			supply = UnitType.Terran_Supply_Depot;
			barrack = UnitType.Terran_Barracks;
			bauer= selbst().getRace().getWorker();
		}
			
		else
		{
			supply = UnitType.Protoss_Pylon;
		}
			
	}

	
}