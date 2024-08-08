package store.progress;

import javafx.animation.Timeline;

import java.text.DecimalFormat;

public abstract class AbstractProgressListener implements ProgressListener {

	final DecimalFormat format = new DecimalFormat("#.##");
	String message;
	double progress;

	@Override
	public void notify(double progress, String message) {
		double value = progress;
		value = value * 100;
		value = (double) ((int) value);
		value = value / 100;
		this.progress = value;
		this.message = message;
		change(value, message);
		System.out.println("[" + format.format(progress * 100) + "]: " + this.message);
	}

	public void pluginNotify(String message) {
		change(progress, this.message + message);
	}

	public abstract void change(double progress, String message);

}
