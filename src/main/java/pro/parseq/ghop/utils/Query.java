package pro.parseq.ghop.utils;

import java.util.Set;

import pro.parseq.ghop.entities.Track;

/**
 * Data source query for objects class
 * 
 * @author Alexander Afanasyev <a href="mailto:aafanasyev@parseq.pro">aafanasyev@parseq.pro</a>
 */
public class Query {

	private final GenomicCoordinate coord;
	private final int left;
	private final int right;
	private final TrackSettings trackSettings;

	public Query(GenomicCoordinate coord, int left, int right, Set<Track> tracks) {

		this.coord = coord;
		this.left = left;
		this.right = right;

		trackSettings = new TrackSettings();
		tracks.stream().forEach(track -> trackSettings.addTrack(track));
	}

	public Query(GenomicCoordinate coord, int left, int right, TrackSettings trackSettings) {

		this.coord = coord;
		this.left = left;
		this.right = right;
		this.trackSettings = trackSettings;
	}

	public TrackSettings getTrackSettings() {
		return trackSettings;
	}

	public GenomicCoordinate getCoord() {
		return coord;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}
}