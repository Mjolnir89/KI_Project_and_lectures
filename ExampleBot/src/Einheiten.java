import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bwapi.Color;
import bwapi.Order;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;
import bwta.BWTA;
import bwta.BaseLocation;


public class Einheiten {

	
	public static void gatherGas()
	{	
		for(Unit einheit : Core.selbst().getUnits())
		{
			if((einheit.getType().isWorker())
				&& einheit.isGatheringMinerals()
				&& !einheit.isSelected())
				{
				Unit Headquarter = null;
				for(Unit PotentialHeadquarter: Core.selbst().getUnits())
				{
					if(PotentialHeadquarter.getType().isResourceDepot())
					{
						if(Headquarter == null
						|| Headquarter.getDistance(einheit)> PotentialHeadquarter.getDistance(einheit))
						{
							Headquarter=PotentialHeadquarter;
						}
					}
				}
				Unit gas=null;
				if(Headquarter !=null)
				{
					for(Unit PotentialGas : BWTA.getNearestBaseLocation(Headquarter.getPosition()).getGeysers())
					{
						if(gas == null
						|| gas.getDistance(einheit)> PotentialGas.getDistance(einheit))
						{
							gas=PotentialGas;
						}
					}
				}
				
				for(Unit unit: Core.selbst().getUnits())
				{
					if((unit.getType().isRefinery()) 
					&& (unit.isCompleted())
					&& (gas != null)
					&&(gasCount()<2))
					{
						
							arbeiter.get(5).gather(gas);
							arbeiter.get(6).gather(gas);
							arbeiter.get(7).gather(gas);
						
					}
				}
			}
		}
	}
	public static int gasCount()
	{
		int counter=0;
		for(Unit unit: arbeiter)
		{
			if(unit.getType() == UnitType.Terran_SCV  &&  unit.isGatheringGas())
			{
				counter++;
				
			}
		}
		return counter;
		
	}

	static private List<Unit> minerals = new ArrayList<>();
	static int i = 0;
	public static void gatherMinerals2() {
		for (Unit einheit : Core.selbst().getUnits()) {
			if (einheit.getType().isWorker() && einheit.isIdle()
					&& !einheit.isSelected()) {
				Unit Headquarter = null;
				for (Unit PotentialHeadquarter : Core.selbst().getUnits()) {
					if (PotentialHeadquarter.getType().isResourceDepot()) {
						if (Headquarter == null
								|| Headquarter.getDistance(einheit) > PotentialHeadquarter
										.getDistance(einheit)) {
							Headquarter = PotentialHeadquarter;
						}
					}
				}
				// sollte nur einmal aufgerufen werden beim start
				if (Headquarter != null) {
					for (Unit Potentialminiralkristall : BWTA
							.getNearestBaseLocation(Buildings.Center())//getNearestBaseLocation(Headquarter.getPosition())
							.getMinerals()) {
						if (!minerals.contains(Potentialminiralkristall))
							minerals.add(Potentialminiralkristall);

					}
					
					einheit.gather(minerals.get(i%7));
					i++;
					if(i>=8) i = 0;
					}
			
				}
			
			}
			
		}
	public static boolean attackFirstUnit()
	{
		Unit enemyUnit=null;
		
		for(Unit aUnit: Core.Spiel().getAllUnits())
		{
			if(aUnit.getPlayer().isEnemy(Core.selbst()) && aUnit.isVisible())
			{
				enemyUnit=aUnit;
			}
		}
		if(enemyUnit !=null && enemyUnit.isVisible() && !AttackUnits.Marines.isEmpty())
		{
			for(Unit aUnit : AttackUnits.Marines)
			{
				if(aUnit.getType().canAttack())
				{
					aUnit.attack(Enemy.enemieUnits.get(0).getPosition());
					
				}
			}
			return true;
		}
		
		return false;
	}
	public static void attackWhenSeen()
	{
		for(Unit aUnit:AttackUnits.Marines)
		{
			int i=0;
			if(!Enemy.enemieUnits.isEmpty() 
			&& Enemy.enemieUnits.get(0).isVisible()
			&& Enemy.enemieUnits.get(0).getPosition().getDistance(Mapping.chokePoint())<3)
			{
				if(aUnit.getType() == UnitType.Terran_Marine)
				{
					Core.Spiel().drawBoxMap(aUnit.getUnitsInWeaponRange(WeaponType.Gauss_Rifle).get(0).getX()*32, 
							aUnit.getUnitsInWeaponRange(WeaponType.Gauss_Rifle).get(0).getY()*32, 
							aUnit.getUnitsInWeaponRange(WeaponType.Gauss_Rifle).get(0).getX()*32+32,
							aUnit.getUnitsInWeaponRange(WeaponType.Gauss_Rifle).get(0).getY()*32+32, Color.Green);
					aUnit.attack(aUnit.getUnitsInWeaponRange(WeaponType.Gauss_Rifle).get(0));
				}
				else if(aUnit.getType() == UnitType.Terran_Vulture)
				{
					Core.Spiel().drawBoxMap(aUnit.getUnitsInWeaponRange(WeaponType.Fragmentation_Grenade).get(0).getX()*32, 
							aUnit.getUnitsInWeaponRange(WeaponType.Fragmentation_Grenade).get(0).getY()*32, 
							aUnit.getUnitsInWeaponRange(WeaponType.Fragmentation_Grenade).get(0).getX()*32+32,
							aUnit.getUnitsInWeaponRange(WeaponType.Fragmentation_Grenade).get(0).getY()*32+32, Color.Green);
					aUnit.attack(aUnit.getUnitsInWeaponRange(WeaponType.Fragmentation_Grenade).get(0));
				}
				//fragmentation granade für vulture
				
			}
			else if((!Enemy.enemieUnits.isEmpty()
			&& Enemy.enemieUnits.get(0).isVisible()
			&& Enemy.enemieUnits.get(0).getPosition().getDistance(Mapping.chokePoint())<3)
			&& Enemy.enemieUnits.get(i).getHitPoints()<=0)
			{
				aUnit.attack(Enemy.chokePoint().toPosition());
			}
		}
		for(Unit aUnit:AttackUnits.tanks)
		{
			int i=0;
			if(!Enemy.enemieUnits.isEmpty() 
			&& Enemy.enemieUnits.get(0).getPosition().getDistance(Mapping.chokePoint())<3)
			{
				aUnit.attack(aUnit.getUnitsInWeaponRange(WeaponType.Arclite_Cannon).get(0));
				Core.Spiel().drawBoxMap(aUnit.getUnitsInWeaponRange(WeaponType.Arclite_Cannon).get(0).getX()*32, 
				aUnit.getUnitsInWeaponRange(WeaponType.Arclite_Cannon).get(0).getY()*32, 
				aUnit.getUnitsInWeaponRange(WeaponType.Arclite_Cannon).get(0).getX()*32+32,
				aUnit.getUnitsInWeaponRange(WeaponType.Arclite_Cannon).get(0).getY()*32+32, Color.Green);
				System.out.println(aUnit.getGroundWeaponCooldown());
			}
			else if((!Enemy.enemieUnits.isEmpty() 
			&& Enemy.enemieUnits.get(0).getPosition().getDistance(Mapping.chokePoint())<3)
			&& Enemy.enemieUnits.get(i).getHitPoints()<=0)
			{
				aUnit.attack(Enemy.chokePoint().toPosition());
			}
		}
	}
	//List fuer ARbeiter, die fertig produziert sind
	private static List<Unit> arbeiter = new ArrayList<>();
	public static void bekommeAlleArbeiter()
	{
		Unit bauer=null;
		
		for(Unit aUnit:Core.selbst().getUnits())
		{
			if((aUnit.getType() == UnitType.Terran_SCV) && aUnit.isCompleted()
			&& !arbeiter.contains(aUnit))
			{
				bauer = aUnit;
				arbeiter.add(bauer);
				Buildings.delaytimer(120);
			}
			else if(aUnit.getHitPoints()<=0)
			{
				arbeiter.remove(aUnit);
			}
		}
		
	}
	
	public static List<Unit> getArbeiter() {
		return arbeiter;
	}
	public static void setArbeiter(List<Unit> arbeiter) {
		Einheiten.arbeiter = arbeiter;
	}
}
