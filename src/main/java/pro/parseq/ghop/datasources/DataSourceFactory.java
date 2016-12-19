package pro.parseq.ghop.datasources;

import java.io.InputStream;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pro.parseq.ghop.entities.ReferenceGenome;
import pro.parseq.ghop.entities.Track;
import pro.parseq.ghop.utils.GenomicCoordinate;

@Component
public class DataSourceFactory {

	@Autowired
	private Comparator<GenomicCoordinate> comparator;

	public BedFileDataSource newBedFileDataSourceInstance(Track track,
			InputStream bedFile, ReferenceGenome referenceGenome) {
		return new BedFileDataSource(track, bedFile, referenceGenome, comparator);
	}
}
