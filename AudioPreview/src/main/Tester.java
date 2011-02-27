package main;

public class Tester {
	public static void main(String[] args) throws InterruptedException {
		SoundWrapper wrapper = new SoundWrapper(2);
		wrapper.initializeSource("Footsteps.wav");
		wrapper.initializeSource("FancyPants.wav");
		wrapper.singlePlay(0);
		Thread.sleep(10000);
		wrapper.singlePlay(1);
		Thread.sleep(5000);
		wrapper.setSourceSPos(0, 1, 10);
		while (true) {
		}
	}
}
