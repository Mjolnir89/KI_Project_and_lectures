import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bwapi.Order;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
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
						
							einheit.gather(gas);
						
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
	public static void gatherMinerals()
	{
		for(Unit einheit : Core.selbst().getUnits())
		{
			if(einheit.getType().isWorker()
			&& einheit.isIdle()
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
				Unit miniralkristall = null;
				if(Headquarter !=null)
				{
					for(Unit Potentialminiralkristall : BWTA.getNearestBaseLocation(Headquarter.getPosition()).getMinerals())
					{
						if(miniralkristall == null
						|| miniralkristall.getDistance(einheit)> Potentialminiralkristall.getDistance(einheit))
						{
							miniralkristall=Potentialminiralkristall;
						}
					}
				}
				if(miniralkristall == null)
				{
					for(Unit Potentialminiralkristall: Core.Spiel().neutral().getUnits())
					{
						if(Potentialminiralkristall.getType().isMineralField())
						{
							if(miniralkristall == null
							|| miniralkristall.getDistance(einheit)> Potentialminiralkristall.getDistance(einheit))
							{
								System.out.println(Potentialminiralkristall.getPosition());
								miniralkristall= Potentialminiralkristall;
							}
						}
					}
				}
				if(miniralkristall != null)
				{
					einheit.gather(miniralkristall);				
				}
						
			}
		}
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
							.getNearestBaseLocation(Headquarter.getPosition())
							.getMinerals()) {
						if (!minerals.contains(Potentialminiralkristall))
							minerals.add(Potentialminiralkristall);

					}
					
					einheit.gather(minerals.get(i%8));
					System.out.println(i%8);
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
		if(enemyUnit !=null && enemyUnit.isVisible())
		{
			for(Unit aUnit : AttackUnits.Marines)
			{
				if(aUnit.getType().canAttack() )
				{
					aUnit.attack(enemyUnit.getPosition());
					
				}
			}
			return true;
		}
		return false;
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
