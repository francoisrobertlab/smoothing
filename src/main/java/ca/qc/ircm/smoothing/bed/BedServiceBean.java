package ca.qc.ircm.smoothing.bed;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class BedServiceBean implements BedService {
    @Inject
    private BedParser bedParser;

    protected BedServiceBean() {
    }

    protected BedServiceBean(BedParser bedParser) {
	this.bedParser = bedParser;
    }

    @Override
    public BedTrack parseFirstTrack(File file) throws IOException {
	class FirstTrackBedHandler implements BedHandler {
	    private BedTrack firstTrack;

	    @Override
	    public void handleTrack(ParsedBedTrack track) {
		if (firstTrack == null) {
		    firstTrack = track;
		}
	    }

	    @Override
	    public void handleAnnotation(ParsedBedAnnotation annotation, ParsedBedTrack track) {
	    }

	    @Override
	    public boolean handleInvalid() {
		return true;
	    }
	}
	FirstTrackBedHandler handler = new FirstTrackBedHandler();
	bedParser.parse(file, handler);
	return handler.firstTrack;
    }
}
