package ca.qc.ircm.smoothing.bed;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BedTrackDefault implements BedTrack {
    public Color color;
    public Map<String, String> allParameters = new HashMap<>();

    public BedTrackDefault() {
    }

    public BedTrackDefault(String name, String description) {
	allParameters.put(BedTrackParameters.NAME.name, name);
	allParameters.put(BedTrackParameters.DESCRIPTION.name, description);
    }

    public BedTrackDefault(Map<String, String> allParameters) {
	this.allParameters = allParameters;
    }

    @Override
    public Type getType() {
	String rawType = allParameters.get(BedTrackParameters.TYPE.name);
	BedTrack.Type ret = null;
	for (BedTrack.Type type : BedTrack.Type.values()) {
	    if (type.value.equals(rawType)) {
		ret = type;
	    }
	}
	if (ret == null) {
	    return Type.BED;
	}
	return ret;
    }

    @Override
    public String getName() {
	return allParameters.get(BedTrackParameters.NAME.name);
    }

    public void setName(String name) {
	allParameters.put(BedTrackParameters.NAME.name, name);
    }

    @Override
    public String getDescription() {
	return allParameters.get(BedTrackParameters.DESCRIPTION.name);
    }

    public void setDescription(String description) {
	allParameters.put(BedTrackParameters.DESCRIPTION.name, description);
    }

    @Override
    public String getDatabase() {
	return allParameters.get(BedTrackParameters.DATABASE.name);
    }

    public void setDatabase(String database) {
	allParameters.put(BedTrackParameters.DATABASE.name, database);
    }

    @Override
    public Color getColor() {
	return parseColor(allParameters.get(BedTrackParameters.COLOR.name));
    }

    public void setColor(Color color) {
	allParameters.put(BedTrackParameters.COLOR.name,
		color.getRed() + "," + color.getGreen() + "," + color.getBlue());
    }

    @Override
    public Map<String, String> getAllParameters() {
	return allParameters;
    }

    public void setAllParameters(Map<String, String> otherParameters) {
	this.allParameters = otherParameters;
    }

    private Color parseColor(String colorValue) {
	if (colorValue == null)
	    return null;

	Pattern pattern = Pattern.compile("(\\d+)(?:,(\\d+),(\\d+))?");
	Matcher matcher = pattern.matcher(colorValue);
	if (matcher.matches()) {
	    int red = Integer.parseInt(matcher.group(1));
	    int green;
	    if (matcher.group(2) == null) {
		green = Integer.parseInt(matcher.group(1));
	    } else {
		green = Integer.parseInt(matcher.group(2));
	    }
	    int blue;
	    if (matcher.group(3) == null) {
		blue = Integer.parseInt(matcher.group(1));
	    } else {
		blue = Integer.parseInt(matcher.group(3));
	    }
	    try {
		return new Color(red, green, blue);
	    } catch (IllegalArgumentException e) {
		return null;
	    }
	} else {
	    return null;
	}
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder("BedTrackDefault_");
	builder.append(getType());
	builder.append("_");
	builder.append(getName());
	builder.append("_");
	builder.append(getDescription());
	return builder.toString();
    }
}