import java.util.ArrayList;
import java.util.List;

import bwapi.Color;
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
		//Mapping.reset();
		Einheiten.bekommeAlleArbeiter();
		fillBuildings();
		//Mapping.sortbuildTileList();
		Mapping.scan(supply);
		Enemy.chokePoint();
	}


	public void onFrame()
	{
		try{
			Buildings.produziereEinheit();
			Einheiten.gatherMinerals2();
			Einheiten.gatherGas();			
			//Mapping.listeAufraemen(supply);
			Einheiten.bekommeAlleArbeiter();
			Einheiten.attackWhenSeen();
			testbuild2();
			AttackUnits.AttackUnitList();
			AttackUnits.schwadron();
			AttackUnits.TrackPosition();
			AttackUnits.attack();
			AttackUnits.tank_Siege_Mode();
			AttackUnits.taketanks();
			AttackUnits.stimpacks();

			AttackUnits.moveToChokePoint();
			Enemy.saveEnemyUnits();
			Enemy.saveEnemyPosition();
			//Einheiten.attackFirstUnit();
			
			AttackUnits.spidermines();
			Enemy.Center();
			
			
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
		buildings.add(2, UnitType.Terran_Refinery);
		buildings.add(3, UnitType.Terran_Academy);
		buildings.add(4,UnitType.Terran_Factory);
		buildings.add(5, UnitType.Terran_Machine_Shop);
		buildings.add(6,UnitType.Terran_Armory);
		buildings.add(7, UnitType.Terran_Academy);
	}
		
	@Override
	public void onUnitDestroy(Unit aUnit) {
	
		if(Mapping.scout != null && aUnit.getID() == Mapping.scout.getID()){
			Mapping.scout = null;
		}
	}
	
	public static List<UnitType> getBuildings() {
		return buildings;
	}

	public static void setBuildings(List<UnitType> buildings) {
		Core.buildings = buildings;
	}
	/*
	 * tvt tactic:
	 * at 9/10 scv build supply
	 * at 11/18 Barrack + 1 Marine
	 * Refinery
	 * 25/18 Supply
	 * 15/26 Factory + machine shop
	 * 23/26 supply
	 * michineshop==1 research speed and mines
	 * 28/34 supply
	 */
	
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
		&& selbst().allUnitCount(bauer)<=11)
		{
			Buildings.produziereEinheit(bauer);
		}
		if(selbst().allUnitCount(supply)>=1
		&& selbst().allUnitCount(bauer)>=11
		&& selbst().allUnitCount(barrack)<2)
		{
			Buildings.baueGeb(1);
			Buildings.baueGeb(2);
		}
		if(selbst().allUnitCount(Marine)<=8
		&& selbst().allUnitCount(barrack)>=1)
		{
			Buildings.produziereEinheit(Marine);
		}
		if(selbst().allUnitCount(barrack)>=1
		&& selbst().allUnitCount(supply)<4)
		{
			Buildings.baueGeb(0);
		}
		if(selbst().allUnitCount(bauer)<20
		&& selbst().allUnitCount(UnitType.Terran_Refinery)>=1)
		{
			Buildings.produziereEinheit(bauer);
		}
		if(selbst().allUnitCount(bauer)>=15
		&& selbst().allUnitCount(UnitType.Terran_Factory)<1)
		{
			Buildings.baueGeb(4);
		}
		if(selbst().allUnitCount(UnitType.Terran_Factory)>=1
		&& (selbst().supplyTotal()-selbst().supplyUsed()<4))
		{
			Buildings.baueGeb(0);
		}
		if(selbst().allUnitCount(UnitType.Terran_Factory)>=1
		&& selbst().allUnitCount(UnitType.Terran_Machine_Shop)<2)
		{
			Buildings.baueGeb(5);			
		}
		if(selbst().allUnitCount(UnitType.Terran_Factory)>=1
				&& selbst().allUnitCount(UnitType.Terran_Academy)<1)
		{
			Buildings.baueGeb(7);
		}
		if(selbst().allUnitCount(UnitType.Terran_Academy)>=1)
		{
			Buildings.erforsche(TechType.Stim_Packs);
		}
		if((selbst().allUnitCount(UnitType.Terran_Machine_Shop)>=1)
			&& selbst().allUnitCount(UnitType.Terran_Vulture)<4)
		{
			
			Buildings.produziereEinheit(UnitType.Terran_Vulture);
			//Vulture speed boost
			Buildings.erforsche(UpgradeType.Ion_Thrusters);
		}
		if(selbst().allUnitCount(UnitType.Terran_Machine_Shop)>=1
		&& selbst().allUnitCount(UnitType.Terran_Factory)<2)
		{
			Buildings.baueGeb(4);
		}
		if(selbst().allUnitCount(Marine)>=6
		&& selbst().allUnitCount(UnitType.Terran_Academy)==1
		&& selbst().allUnitCount(UnitType.Terran_Medic)<=2)
		{
			Buildings.produziereEinheit(UnitType.Terran_Medic);
		}
		if(selbst().allUnitCount(UnitType.Terran_Machine_Shop)>=1
		&& selbst().allUnitCount(UnitType.Terran_Armory)<1)
		{
			Buildings.baueGeb(6);
		}
		
		if(selbst().getUpgradeLevel(UpgradeType.Ion_Thrusters)>0)
		{
			//Vulture Spider mines
			Buildings.erforsche(TechType.Tank_Siege_Mode);
		}
		
		if(selbst().hasResearched(TechType.Spider_Mines))
		{
			AttackUnits.spidermines();
		}
		if(selbst().allUnitCount(UnitType.Terran_Armory)<=1)
		{
			Buildings.erforsche(UpgradeType.Terran_Vehicle_Weapons);
		}
		if(selbst().getUpgradeLevel(UpgradeType.Terran_Vehicle_Weapons)>0)
		{

			Buildings.erforsche(UpgradeType.Terran_Vehicle_Plating);
		}
		if(!AttackUnits.Marines.isEmpty())
		{
			Mapping.scout();
		}
		if(selbst().allUnitCount(UnitType.Terran_Siege_Tank_Tank_Mode)<=6)
		{
			Buildings.produziereEinheit(UnitType.Terran_Siege_Tank_Tank_Mode);
		}
		
		return false;
	
	}

	private static UnitType supply = null;
	private static UnitType barrack=null;
	private static UnitType bauer = null;
	private static UnitType Marine =null;
	private static UnitType Fire =null;
	private static UnitType medic = null;
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
			Marine = UnitType.Terran_Marine;
			Fire = UnitType.Terran_Firebat;
			medic = UnitType.Terran_Medic;
		}
			
		else
		{
			supply = UnitType.Protoss_Pylon;
			barrack = UnitType.Protoss_Gateway;
			bauer = UnitType.Protoss_Probe;
		}
			
	}

	
}