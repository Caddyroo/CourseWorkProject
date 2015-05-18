package actors;

import java.util.ArrayList;
import java.util.Iterator;

import baseCode.Field;
import baseCode.Location;

public class Farmer extends Actor {

	@Override
	public void act(Field field, ArrayList<Actor> actors) {
		Location location = field.freeAdjacentLocation(this.getLocation());
		if (location != null) {
			field.clearLocation(this.getLocation());
			field.place(this, location);
			this.setLocation(location);
		}
		pestControl(field);
		tryToPlantNewBean(field, actors);
	}
	
	public void pestControl(Field field) {
		Iterator<Location> it = field.adjacentLocations(this.getLocation());
		while (it.hasNext()) {
			Actor adjacentActor = field.getObjectAt(it.next());
			if (adjacentActor instanceof Weed) {
				((Weed) adjacentActor).setAlive(false);
			}
		}
	}
	
	
	public void tryToPlantNewBean(Field field, ArrayList<Actor> newActors) {
		Location freeLocation = field.freeAdjacentLocation(getLocation()); 
		if(freeLocation != null) {
			BeanPlant bean = new BeanPlant(false);
			bean.setLocation(freeLocation);
			newActors.add(bean);
		}
	}

}
