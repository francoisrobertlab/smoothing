package ca.qc.ircm.smoothing.bed;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

    @Override
    public int countFirstTrackData(File file) throws IOException {
	class FirstTrackBedHandler implements BedHandler {
	    private BedTrack firstTrack;
	    private int count;

	    @Override
	    public void handleTrack(ParsedBedTrack track) {
		if (firstTrack == null) {
		    firstTrack = track;
		}
	    }

	    @Override
	    public void handleAnnotation(ParsedBedAnnotation annotation, ParsedBedTrack track) {
		if (track.equals(firstTrack)) {
		    count++;
		}
	    }

	    @Override
	    public boolean handleInvalid() {
		return true;
	    }
	}
	FirstTrackBedHandler handler = new FirstTrackBedHandler();
	bedParser.parse(file, handler);
	return handler.count;
    }

    @Override
    public int countFirstTrackChromosomes(File file) throws IOException {
	class FirstTrackBedHandler implements BedHandler {
	    private BedTrack firstTrack;
	    private Set<String> chromosomes = new HashSet<>();

	    @Override
	    public void handleTrack(ParsedBedTrack track) {
		if (firstTrack == null) {
		    firstTrack = track;
		}
	    }

	    @Override
	    public void handleAnnotation(ParsedBedAnnotation annotation, ParsedBedTrack track) {
		if (track.equals(firstTrack) && annotation.getChromosome() != null) {
		    chromosomes.add(annotation.getChromosome());
		}
	    }

	    @Override
	    public boolean handleInvalid() {
		return true;
	    }
	}
	FirstTrackBedHandler handler = new FirstTrackBedHandler();
	bedParser.parse(file, handler);
	return handler.chromosomes.size();
    }
}