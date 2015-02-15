
public class Timer {
	private float duration;
	private float timePassed;
	private boolean finished;
	private boolean paused;
	
	public Timer(float duration){
		this.duration = duration;
		timePassed = 0;
		finished = false;
		paused = true;
	}
	
	public void setPause(boolean flag){
		paused = flag;
	}
	
	public boolean isFinished(float delta){
		if(paused) return false;
		timePassed += delta;
		if(timePassed >= duration){
			timePassed -= duration;
			finished = true;
		}
		boolean res = finished;
		finished = false;
		return res;
	}
	
	
}
