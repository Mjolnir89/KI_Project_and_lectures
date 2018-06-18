import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import bwapi.Color;
import bwapi.Order;
import bwapi.Position;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Region;


public class Buildings {
		
	public static void reset()
	{
		neededTechs.clear();
		neededUpgrades.clear();
	}
	private static HashMap<UnitType,Integer> needUnits = new HashMap<UnitType,Integer>();
	private static List<UpgradeType> neededUpgrades = new ArrayList<>();
	
	public static void braucheUpgrade(UpgradeType aUpgrade, int Anzahl)
	{
		for(int i=0; i<Anzahl;i++)
		{
			neededUpgrades.add(aUpgrade);
		}
	}
	private static List<TechType> neededTechs = new ArrayList<>();
	public static Position holeSammelpunkt()
	{
		return mSammelpunkt;
	}
	private static Position mSammelpunkt = Position.Unknown;
	public static void setzeSammelPunkt(Position sammeln)
	{
		mSammelpunkt = sammeln;
		if(mSammelpunkt != null && mSammelpunkt.isValid())
		{
			for(Unit einheit : Core.selbst().getUnits())
			{
				if(einheit.canSetRallyPoint())
				{
					einheit.setRallyPoint(mSammelpunkt);
				}
			}
		}
	}
	public static void braucheTech(TechType aTech)
	{
		
		neededTechs.add(aTech);
		
	}

	public static void erforsche(TechType tech)
	{
		for(Unit vEinheit : Core.selbst().getUnits())
		{
			if(vEinheit.isIdle()
			&& vEinheit.canResearch(tech)
			&& tech.gasPrice() <= Core.selbst().gas()
			&& tech.mineralPrice() <= Core.selbst().minerals())
			{
			 vEinheit.research(tech);
			}
		}
		
	}
	public static boolean erforsche(UpgradeType upgrade)
	{
		for(Unit vEinheit : Core.selbst().getUnits())
		{
			if(vEinheit.isIdle()
			&& vEinheit.canUpgrade(upgrade)
			&& upgrade.gasPrice() <= Core.selbst().gas()
			&& upgrade.mineralPrice()<= Core.selbst().minerals())
			{
				return vEinheit.upgrade(upgrade);
			}
		}
		return false;
	}
	

	public static TilePosition Center()
	{
		TilePosition headquarter=null;
		Unit Headquarter = null;
		for(Unit PotentialHeadquarter: Core.selbst().getUnits())
		{
			if(PotentialHeadquarter.getType().isResourceDepot())
			{
					Headquarter=PotentialHeadquarter;
					headquarter=Headquarter.getTilePosition();
				}
			}
		return headquarter;
	}
	private static int lastFrameCount=100;
	public static void delaytimer(int frame)
	{		
		if(Core.Spiel().getFrameCount() == lastFrameCount)
		{
			lastFrameCount = lastFrameCount+frame;
			return;
		}
	}

	private static List<UnitType> gebaude = new ArrayList<>();
	
	public static int getCurrentMinerals()
	{
		int reservedMinerals=0;
		
		for(UnitType aBuilding: gebaude)
		{
			reservedMinerals += aBuilding.mineralPrice();
		}
		return Core.selbst().minerals() - reservedMinerals;
	}

	public static boolean baueGeb(int nummer)
	{
		UnitType geb = Core.buildings.get(nummer);
		Unit bauer = Einheiten.getArbeiter().get(1);
		Unit bauer2 = Einheiten.getArbeiter().get(2);
		TilePosition buildtile= Mapping.getNextFromList(geb);
		if(!geb.isBuilding() 
		|| geb.mineralPrice()> getCurrentMinerals()
		|| geb.gasPrice() > Core.selbst().gas())
		{
			return false;
		}
			
		if(buildtile!=null )
		{	
			if((nummer==0 || nummer==6)&& bauer.getOrder() != Order.PlaceBuilding)
			{
				
				//System.out.println(bauer.getID()+"\t"+buildtile+"\t"+geb+"\t"+Center().getDistance(buildtile));
				//bauer.move(buildtile.toPosition());
				bauer.build(geb, buildtile);
			}
				
			else if((nummer==1 || nummer==4) && bauer2.getOrder() != Order.PlaceBuilding)
			{
				bauer2.build(geb,buildtile);
				//7System.out.println(bauer2.getID()+"\t"+buildtile+"\t"+geb+"\t"+Center().getDistance(buildtile));
				
			}
			else if(nummer == 7 )
			{
				Unit bauer3 = Einheiten.getArbeiter().get(3);
				bauer3.build(geb, buildtile);
			}
			//Extension von Factories
			else if(nummer==5)
			{
				for(Unit aUnit : Core.selbst().getUnits())
				{
					if (aUnit.getType() == UnitType.Terran_Factory && aUnit.canBuildAddon())
					{
						aUnit.buildAddon(UnitType.Terran_Machine_Shop);
					}
				}
			}
		}
		if(nummer == 2)
		{
			for(Unit aUnit: Core.Spiel().neutral().getUnits())
			{
				if(aUnit.getType() == UnitType.Resource_Vespene_Geyser)
				{
					bauer.build(geb, aUnit.getTilePosition());
				}
			}
		}

		return false;
	}
	static boolean startetBuilding=false;
	
	public static boolean startProgress(Unit unit)
	{
		if(unit.getOrder() == Order.PlaceBuilding)
		{
			return true;
		}
		else return false;
	}

	
	//Nicht im Farmweg bauen
	public static boolean sindKeineRessourcenInDerNaehe(Position position)
	{
		for(Unit einheit : Core.Spiel().getUnitsInRadius(position, 3*32))
		{
			if(einheit.getType().isMineralField()
			|| einheit.getType() == UnitType.Resource_Vespene_Geyser
			|| einheit.getType() == Core.selbst().getRace().getRefinery())
			{
				return false;
			}
		}
		return true;
	}
	//Vermeidung von Einmauern von eigenen Einheiten
	public static boolean sindKeineGebaudeInDerNahe(Position position)
	{
		for(Unit einheit : Core.Spiel().getUnitsInRadius(new Position(position.getX()+2,position.getY()), 2*32))
		{
			if(einheit.getType() == UnitType.Buildings
			|| einheit.getType() == UnitType.Terran_Barracks
			|| einheit.getType() == UnitType.Terran_Factory
			|| einheit.getType() == UnitType.Terran_Supply_Depot
			|| einheit.getType() == UnitType.Terran_Command_Center)
			{
				//System.out.println(einheit.getUnitsInRadius(2*32));
				return false;
			}
		}
		return true;
	}
	public static boolean produziereEinheit(UnitType einheit)
	{
		for(Unit building : Core.selbst().getUnits())
		{
			if(building.isIdle()
			&& building.canTrain(einheit)
			&& einheit.gasPrice()<= Core.selbst().gas()
			&& einheit.mineralPrice()<= Core.selbst().minerals())
			{
				return building.train(einheit);
			}
		}
		return false;
	}

	public static void produziereEinheit()
	{	
		for(UnitType needUnit: needUnits.keySet())
		{
			if(produziereEinheit(needUnit))
			{
				return;
			}
			
		}
	}

	public static boolean isinproduction(UnitType type)
	{
		if(needUnits.containsKey(type) && needUnits.get(type) >0)
		return true;
		else return false;
	}
	
}
