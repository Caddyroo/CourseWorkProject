package actors;

public abstract class Plant extends Actor{
	
	private int age;
	
	private boolean alive;
	
	protected abstract void incrementAge();
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
