import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		for(Unit unit: Core.selbst().getUnits())
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
	public static void attackFirstUnit()
	{
		Unit enemyUnit=null;
		
		for(Unit aUnit: Core.Spiel().getAllUnits())
		{
			if(aUnit.getPlayer().isEnemy(Core.selbst()))
			{
				enemyUnit=aUnit;
			}
		}
		if(enemyUnit !=null)
		{
			for(Unit aUnit : Core.selbst().getUnits())
			{
				if(aUnit.getType().canAttack() && aUnit.isIdle())
				{
					aUnit.attack(enemyUnit.getPosition());
				}
			}
		}
	}
	static Unit scout=null;
	static Position enemyBase=null;
	/*public static void scout()
	{
		BaseLocation unexploredLocation=null;
		for(BaseLocation aLocation: BWTA.getStartLocations())
		{
			if(!Core.Spiel().isExplored(aLocation.getTilePosition()))
			{
				unexploredLocation=aLocation;		
				enemyBase=aLocation.getPosition();
			}
		}
		if(scout== null || scout.getHitPoints()<=0)
		{
			for(Unit aUnit: Core.selbst().getUnits())
			{
				if(aUnit.isIdle() && aUnit.getType() == UnitType.Terran_Marine)
				{
					scout=aUnit;
				}
			}
		}
		if(scout!=null && unexploredLocation !=null && scout.isIdle())
		{
			scout.move(unexploredLocation.getPosition());
			
		}		
	}
	*/
	private static List<Unit> arbeiter = new ArrayList<>();
	public static void bekommeAlleArbeiter()
	{
		Unit bauer=null;
		
		for(Unit aUnit:Core.selbst().getUnits())
		{
			if((aUnit.getType() == UnitType.Terran_SCV) ||
			(aUnit.getType()== UnitType.Protoss_Probe))
			{
				bauer = aUnit;
				arbeiter.add(bauer);
				Buildings.delaytimer(120);
			}
		}
		
	}
	static Unit bauer = null;
	static Unit bauer2 = null;
	public static Unit getscv(UnitType geb)
	{
		bauer = arbeiter.get(1);
		/*
		for(Unit aUnit: arbeiter)
		{
			if(aUnit != null && aUnit.isIdle() || aUnit.isGatheringMinerals())
			{
				bauer = aUnit;
			}
			if(bauer != null)
				break;
			
		}
		*/
		return bauer;
	}
	
	public static List<Unit> getArbeiter() {
		return arbeiter;
	}
	public static void setArbeiter(List<Unit> arbeiter) {
		Einheiten.arbeiter = arbeiter;
	}
}
