package labClasses;

import java.util.ArrayList;
import java.util.Collections;

import actors.Actor;
import actors.BeanPlant;
import actors.Farmer;
import actors.Plant;
import actors.Weed;
import baseCode.Field;
import baseCode.Location;
import baseCode.RandomGenerator;
import baseCode.SimulatorView;

/**
 * @author James Caddick
 * 
 * Class used as the main class to initialise the simulation
 *  and control its running throughout its lifecycle.
 *
 */
public class Simulation {

	private Field field;
	private SimulatorView view;
	private ArrayList<Actor> actors;
	private int step;
	
	public Simulation(int depth, int width) {
		//check if suitable depth and width are provided, otherwise set as default
		if (depth <= 0) {
			depth = ModelConstants.DEFAULT_FIELD_DEPTH;
		}
		if (width <= 0) {
			width = ModelConstants.DEFAULT_FIELD_WIDTH;
		}
		
		//initialise the field and the view of the field to the checked depth and width
		field = new Field(depth, width);
		view = new SimulatorView(depth, width);
		
		//setting the colours of the actors in the view using their constants.
		view.setColor(Farmer.class, ModelConstants.FARMER_COLOUR);
		view.setColor(Weed.class, ModelConstants.WEED_COLOUR);
		view.setColor(BeanPlant.class, ModelConstants.BEAN_COLOUR);
		
		//Initializing the array which will hold the data about all the actors on the board.
		actors = new ArrayList<Actor>();
		
		//Initializing the static RandomGenerator with the seed value.
		RandomGenerator.initialiseWithSeed(ModelConstants.RANDOM_SEED);
	}
	
	/**
	 * Initial population of the field using the depth and width and random number generator.
	 * @author James Caddick
	 */
	private void populate() {
		//ensure the field has been cleared before initial population begins.
		field.clear();
		for(int x = 0; x < field.getDepth(); x++) {
			for(int y = 0; y < field.getWidth(); y++) {
				//get random double 
				Double random = RandomGenerator.getRandom().nextDouble();
				if (random < ModelConstants.FARMER_CREATION_PROB) {
					Farmer farmer = new Farmer();
					placeActor(farmer, x, y);
				} else if (random < ModelConstants.WEED_CREATION_PROB + ModelConstants.FARMER_CREATION_PROB) {
					Weed weed = new Weed(true);
					placeActor(weed, x, y);
				} else if (random < ModelConstants.BEAN_CREATION_PROB + ModelConstants.WEED_CREATION_PROB + ModelConstants.FARMER_CREATION_PROB) {
					BeanPlant beanPlant = new BeanPlant(true);
					placeActor(beanPlant, x, y);
				}	
			}
		}
	}
	
	/**
	 * Place a provided actor onto the field according to X and Y coordinates and add it to the list of actors.
	 * @param actor
	 * @param x
	 * @param y
	 */
	private void placeActor(Actor actor, int x, int y) {
		Location location = new Location(x, y);
		actor.setLocation(location);
		actors.add(actor);
		field.place(actor, location);
	}
	
	/**
	 * Begins the simulation process and calls simulateOneStep for the required number of steps.
	 * @param int
	 * @author James Caddick
	 * @return void
	 */
	private void simulate(int numberOfSteps) throws InterruptedException {
		for (int i = 0; i < numberOfSteps; i++) {
			simulateOneStep();
			Thread.sleep(50);
		}
	}
	
	/**
	 * Used to simulate one step for all actors in the actors array,
	 * this includes passing the field and an array to the act method
	 * so that new actors can be added to this and actors can evaluate their position
	 * in the field.
	 * 
	 * @param ArrayList
	 * @author James Caddick
	 * @return void
	 */
	private void simulateOneStep() {
		Collections.shuffle(actors, RandomGenerator.getRandom());
		ArrayList<Plant> deadPlants = new ArrayList<Plant>();
		ArrayList<Actor> newActors = new ArrayList<Actor>();
		
		//run act for each actor and then check if the actor should still take part.
		for(Actor actor : actors) {
			actor.act(field, newActors);
			if(actor instanceof Plant && !((Plant)actor).isAlive()) {
				deadPlants.add((Plant) actor);
			}
		}
		
		addNewActors(newActors);
		removePlantsIfDead(deadPlants);
		step++;
		view.showStatus(step, field);
	}
	
	/**
	 * This method removes plant objects from the actors array and from the field
	 * given a list of plant objects which it takes as a parameter.
	 * 
	 * @param ArrayList
	 * @author James Caddick
	 * @return void
	 */
	private void removePlantsIfDead(ArrayList<Plant> deadPlants) {
		for (Plant plant : deadPlants) {
			actors.remove(plant);
			field.clearLocation(plant.getLocation());
		}
	}
	
	/**
	 * This method adds actor objects to the actors array and its place on the field
	 * given a list of actor objects which it takes as a parameter.
	 * 
	 * @param ArrayList
	 * @author James Caddick
	 * @return void
	 */
	private void addNewActors(ArrayList<Actor> newActors) {
		for(Actor actor : newActors) {
			actors.add(actor);
			field.place(actor, actor.getLocation());
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Simulation sim = new Simulation(50, 50);
		sim.populate();
		sim.simulate(1000);
	}
}
