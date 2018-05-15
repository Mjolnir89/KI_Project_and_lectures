import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bwapi.Unit;
import bwapi.UnitType;


public class AttackUnits {
	
	
	
	public static  Unit[] attackMarines()
	{
		Unit Marine1=null;
		Unit Marine2=null;
		Unit Marine3=null;
		Unit Marine4=null;
		Unit Marine5=null;
		Unit Marine6=null;
		
		if(Marine1== null || Marine1.getHitPoints()<=0)
		{
			for(Unit aUnit: Core.selbst().getUnits())
			{
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining())
				{
					Marine1=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine2 !=Marine1)
				{
					Marine2=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine3 !=Marine2)
				{
					Marine3=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine4 !=Marine3)
				{
					Marine4=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine5 !=Marine4)
				{
					Marine5=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine6 !=Marine5)
				{
					Marine6=aUnit;
				}
				
			}
		}		
		return new Unit[] {Marine1,Marine2,Marine3,Marine4,Marine5,Marine6};
	}
	private static List<Unit> Marines = new ArrayList<>();
	public static  void AttackUnitList()
	{
		for(Unit aUnit: Core.selbst().getUnits())
		{
			if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
			&& !aUnit.isTraining())
			{
				getMarines().add(aUnit);
			}
			if(aUnit.getHitPoints()<=0)
			{
				getMarines().remove(aUnit);
			}
		}
	}
	public static void angreifen()
	{
		for(Unit aUnit : attackMarines())
		{
			if(aUnit.canAttack())
			{
				aUnit.attack(Einheiten.enemyBase);
			}
		}
	}
	public static List<Unit> getMarines() {
		return Marines;
	}
	public static void setMarines(List<Unit> marines) {
		Marines = marines;
	}
}
