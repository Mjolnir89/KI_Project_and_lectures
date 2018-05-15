import java.util.ArrayList;
import java.util.List;

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
		Mapping.startscan(supply);
		Einheiten.bekommeAlleArbeiter();
		fillBuildings();
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
			//Einheiten.attackFirstUnit();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[]args)
	{
		new Core().run();
	}
	static List<UnitType> buildings = new ArrayList<>();
	public static void fillBuildings()
	{
		buildings.add(0, supply);
		buildings.add(1, barrack);
		buildings.add(2, supply);
		buildings.add(3, barrack);
		buildings.add(4, supply);
		buildings.add(5, supply);
		buildings.add(6, barrack);
		buildings.add(7, supply);
	}
		

	
	public static List<UnitType> getBuildings() {
		return buildings;
	}

	public static void setBuildings(List<UnitType> buildings) {
		Core.buildings = buildings;
	}

	public  boolean testbuild2()
	{
		
		if(!Buildings.isinproduction(bauer)
		&& selbst().allUnitCount(bauer)<8)
		{
			Buildings.produziereEinheit(bauer);
		}
		if(selbst().allUnitCount(bauer)>=8
		&& selbst().allUnitCount(supply)<1)
		{
			Buildings.baueGeb(0);
		}
		if(selbst().allUnitCount(supply)==1
		&& !Buildings.isinproduction(bauer)
		&& selbst().allUnitCount(bauer)<=12)
		{
			Buildings.produziereEinheit(bauer);
		}
		if(selbst().allUnitCount(supply)==1
		&& selbst().allUnitCount(bauer)>=9
		&& selbst().allUnitCount(barrack)<=1)
		{
			Buildings.baueGeb(1);	
		}
		if(selbst().allUnitCount(barrack)==1
		&& selbst().allUnitCount(supply)<=8)
		{
			Buildings.baueGeb(0);
		}
		if(selbst().allUnitCount(barrack)<=3
		&& selbst().allUnitCount(supply)==3)
		{
			Buildings.baueGeb(1);
		}
		if(selbst().allUnitCount(barrack)==1
		&& selbst().allUnitCount(bauer)<=18)
		{
			Buildings.produziereEinheit(bauer);
		}
		if(selbst().allUnitCount(barrack)>1
		&& selbst().allUnitCount(supply)<=10)
		{
			Buildings.baueGeb(0);
		}
		if(!Buildings.isinproduction(UnitType.Terran_Marine))
		{
			Buildings.produziereEinheit(UnitType.Terran_Marine);
		}
		Buildings.test();
		if(selbst().allUnitCount(UnitType.Terran_Marine)>=6)
		{
			//AttackUnits.AttackUnitList();
			//for(Unit aUnit : AttackUnits.getMarines())
			//{
			//	aUnit.attack(Einheiten.enemyBase);
			//}	
		}
		return false;
	
	}

	private static UnitType supply = null;
	private static UnitType barrack=null;
	private static UnitType bauer = null;
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
			barrack = UnitType.Protoss_Gateway;
			bauer = UnitType.Protoss_Probe;
		}
			
	}

	
}