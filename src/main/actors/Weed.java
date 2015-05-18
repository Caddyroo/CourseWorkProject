package actors;

import java.util.ArrayList;

import labClasses.ModelConstants;
import baseCode.Field;
import baseCode.Location;
import baseCode.RandomGenerator;

public class Weed extends Plant {
	
	public Weed(boolean generateAge) {
		if(generateAge == true) {
			this.setAge(RandomGenerator.getRandom().nextInt(ModelConstants.WEED_MAX_AGE));
		} else {
			this.setAge(0);
		}
		this.setAlive(true);
	}
	
	@Override
	protected void incrementAge() {
		int currentAge = this.getAge();
		if (currentAge < ModelConstants.WEED_MAX_AGE) {
			this.setAge(++currentAge);
		} else {
			this.setAlive(false);
		}
	}
		
	@Override
	public void act(Field field, ArrayList<Actor> newActors) {
		incrementAge();
		spreadIfOfAge(field, newActors);
	}
	
	private void spreadIfOfAge(Field field, ArrayList<Actor> newActors) {
		double rand = RandomGenerator.getRandom().nextDouble();
		Location freeLocation = field.freeAdjacentLocation(getLocation());
		if (getAge() >= ModelConstants.WEED_SPREADING_AGE
				&& rand < ModelConstants.WEED_CREATION_PROB	&& freeLocation != null) {
			Weed newWeed = new Weed(false);
			newWeed.setLocation(freeLocation);
			newActors.add(newWeed);
		}
	}

}
