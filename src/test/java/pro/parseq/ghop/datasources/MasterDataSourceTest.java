package pro.parseq.ghop.datasources;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pro.parseq.ghop.entities.Band;
import pro.parseq.ghop.entities.Contig;
import pro.parseq.ghop.entities.ReferenceGenome;
import pro.parseq.ghop.entities.Track;
import pro.parseq.ghop.utils.GenomicCoordinate;
import pro.parseq.ghop.utils.Query;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MasterDataSourceTest {

	private static final String GENOME = "GRCh37.p13";

	private static final String CHROMOSOMES_TRACK = "chromosomes";
	private static final String REGIONS_TRACK = "regions";

	private static final Track chromosomes = new Track(CHROMOSOMES_TRACK);
	private static final Track regions = new Track(REGIONS_TRACK);

	private static final String CHROMOSOMES_TRACK_BED = "/chromosomes.bed";
	private static final String REGIONS_TRACK_BED = "/regions.bed";

	private static final ReferenceGenome humanGenome = new ReferenceGenome(GENOME);
	private static final Contig contig_chr1 = new Contig(humanGenome, "chr1");
	private static final Contig contig_chr2 = new Contig(humanGenome, "chr2");
	private static final Contig contig_chr3 = new Contig(humanGenome, "chr3");
	private static final Contig contig_chr4 = new Contig(humanGenome, "chr4");

	/**
	 *    10 --1--- 20       20 --2-- 50 --3-- 70             0 --5-- 10 40 --6-- 50
	 *    10 -1Dup- 20                    60 ----4---- 140
	 * 0 ====chr1===== 100 0 ============chr2============ 150 0 ======chr4======= 50
	 */
	private static final GenomicCoordinate chr1_0 = new GenomicCoordinate(contig_chr1, 0);
	private static final GenomicCoordinate chr1_10 = new GenomicCoordinate(contig_chr1, 10);
	private static final GenomicCoordinate chr1_20 = new GenomicCoordinate(contig_chr1, 20);
	private static final GenomicCoordinate chr1_100 = new GenomicCoordinate(contig_chr1, 100);
	private static final GenomicCoordinate chr2_0 = new GenomicCoordinate(contig_chr2, 0);
	private static final GenomicCoordinate chr2_20 = new GenomicCoordinate(contig_chr2, 20);
	private static final GenomicCoordinate chr2_50 = new GenomicCoordinate(contig_chr2, 50);
	private static final GenomicCoordinate chr2_60 = new GenomicCoordinate(contig_chr2, 60);
	private static final GenomicCoordinate chr2_70 = new GenomicCoordinate(contig_chr2, 70);
	private static final GenomicCoordinate chr2_140 = new GenomicCoordinate(contig_chr2, 140);
	private static final GenomicCoordinate chr2_150 = new GenomicCoordinate(contig_chr2, 150);
	private static final GenomicCoordinate chr4_0 = new GenomicCoordinate(contig_chr4, 0);
	private static final GenomicCoordinate chr4_10 = new GenomicCoordinate(contig_chr4, 10);
	private static final GenomicCoordinate chr4_40 = new GenomicCoordinate(contig_chr4, 40);
	private static final GenomicCoordinate chr4_50 = new GenomicCoordinate(contig_chr4, 50);

	private static final Band chr1 = new Band.BandBuilder("chromosomes_0", chromosomes, chr1_0, chr1_100).build();
	private static final Band chr2 = new Band.BandBuilder("chromosomes_1", chromosomes, chr2_0, chr2_150).build();
	private static final Band chr4 = new Band.BandBuilder("chromosomes_2", chromosomes, chr4_0, chr4_50).build();

	private static final Band region1 = new Band.BandBuilder("regions_0", regions, chr1_10, chr1_20).build();
	private static final Band region1Dup = new Band.BandBuilder("regions_1", regions, chr1_10, chr1_20).build();
	private static final Band region2 = new Band.BandBuilder("regions_2", regions, chr2_20, chr2_50).build();
	private static final Band region3 = new Band.BandBuilder("regions_3", regions, chr2_50, chr2_70).build();
	private static final Band region4 = new Band.BandBuilder("regions_4", regions, chr2_60, chr2_140).build();
	private static final Band region5 = new Band.BandBuilder("regions_5", regions, chr4_0, chr4_10).build();
	private static final Band region6 = new Band.BandBuilder("regions_6", regions, chr4_40, chr4_50).build();

	@Autowired
	private DataSourceFactory dataSourceFactory;

	@Autowired
	private MasterDataSource masterDataSource;

	@Before
	public void setUp() throws Exception {

		masterDataSource.removeAll();
		masterDataSource.addDataSource(dataSourceFactory.newBedFileDataSourceInstance(
				chromosomes, getClass().getResourceAsStream(CHROMOSOMES_TRACK_BED), humanGenome));
		masterDataSource.addDataSource(dataSourceFactory.newBedFileDataSourceInstance(
				regions, getClass().getResourceAsStream(REGIONS_TRACK_BED), humanGenome));
	}

	@Test
	public void testMasterDataSource() throws Exception {

		Set<Track> tracks = new HashSet<>();
		tracks.add(regions);
		GenomicCoordinate coord = new GenomicCoordinate(contig_chr3, 100);
		Query query = new Query(coord, 0, 0, tracks);

		Set<Band> bands = masterDataSource.getBands(query);
		assertThat(bands).isEmpty();

		query = new Query(coord, 0, 1, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5).size().isEqualTo(1);

		query = new Query(coord, 0, 2, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5).size().isEqualTo(1);

		query = new Query(coord, 0, 3, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5, region6).size().isEqualTo(2);

		query = new Query(coord, 0, 4, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5, region6).size().isEqualTo(2);

		query = new Query(coord, 0, 5, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5, region6).size().isEqualTo(2);

		query = new Query(coord, 1, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region4).size().isEqualTo(1);

		query = new Query(coord, 2, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region3, region4).size().isEqualTo(2);

		query = new Query(coord, 3, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region3, region4).size().isEqualTo(2);

		query = new Query(coord, 4, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4).size().isEqualTo(3);

		query = new Query(coord, 5, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4).size().isEqualTo(3);

		query = new Query(coord, 6, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4).size().isEqualTo(5);

		query = new Query(coord, 7, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4).size().isEqualTo(5);

		query = new Query(coord, 8, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4).size().isEqualTo(5);

		coord = new GenomicCoordinate(contig_chr2, 50);
		query = new Query(coord, 0, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3).size().isEqualTo(2);

		query = new Query(coord, 1, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3).size().isEqualTo(2);

		query = new Query(coord, 2, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3).size().isEqualTo(4);

		query = new Query(coord, 3, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3).size().isEqualTo(4);

		query = new Query(coord, 4, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3).size().isEqualTo(4);

		query = new Query(coord, 0, 1, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4).size().isEqualTo(3);

		query = new Query(coord, 0, 2, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4).size().isEqualTo(3);

		query = new Query(coord, 0, 3, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4).size().isEqualTo(3);

		query = new Query(coord, 0, 4, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, region5).size().isEqualTo(4);

		query = new Query(coord, 0, 5, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, region5).size().isEqualTo(4);

		query = new Query(coord, 0, 6, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, region5, region6).size().isEqualTo(5);

		query = new Query(coord, 0, 7, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, region5, region6).size().isEqualTo(5);

		query = new Query(coord, 0, 8, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, region5, region6).size().isEqualTo(5);

		query = new Query(coord, 0, 9, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, region5, region6).size().isEqualTo(5);

		query = new Query(coord, 1, 9, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, region5, region6).size().isEqualTo(5);

		query = new Query(coord, 2, 9, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, region5, region6).size().isEqualTo(7);

		query = new Query(coord, 3, 9, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, region5, region6).size().isEqualTo(7);

		query = new Query(coord, 4, 9, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, region5, region6).size().isEqualTo(7);

		query = new Query(coord, 5, 9, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, region5, region6).size().isEqualTo(7);

		coord = new GenomicCoordinate(contig_chr2, 55);
		query = new Query(coord, 0, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region3).size().isEqualTo(1);

		query = new Query(coord, 1, 1, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4).size().isEqualTo(3);

		query = new Query(coord, 2, 2, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4).size().isEqualTo(3);

		query = new Query(coord, 2, 3, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4).size().isEqualTo(3);

		coord = new GenomicCoordinate(contig_chr2, 70);
		query = new Query(coord, 0, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region3, region4).size().isEqualTo(2);

		query = new Query(coord, 1, 1, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region3, region4).size().isEqualTo(2);

		tracks.add(chromosomes);
		coord = new GenomicCoordinate(contig_chr3, 100);
		query = new Query(coord, 0, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).isEmpty();

		query = new Query(coord, 0, 1, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5, chr4).size().isEqualTo(2);

		query = new Query(coord, 0, 2, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5, chr4).size().isEqualTo(2);

		query = new Query(coord, 0, 3, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5, region6, chr4).size().isEqualTo(3);

		query = new Query(coord, 0, 4, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5, region6, chr4).size().isEqualTo(3);

		query = new Query(coord, 0, 5, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5, region6, chr4).size().isEqualTo(3);

		query = new Query(coord, 0, 6, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region5, region6, chr4).size().isEqualTo(3);

		query = new Query(coord, 1, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(chr2).size().isEqualTo(1);

		query = new Query(coord, 2, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region4, chr2).size().isEqualTo(2);

		query = new Query(coord, 3, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region3, region4, chr2).size().isEqualTo(3);

		query = new Query(coord, 4, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region3, region4, chr2).size().isEqualTo(3);

		query = new Query(coord, 5, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, chr2).size().isEqualTo(4);

		query = new Query(coord, 6, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, chr2).size().isEqualTo(4);

		query = new Query(coord, 7, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, chr2).size().isEqualTo(4);

		query = new Query(coord, 8, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region2, region3, region4, chr1, chr2).size().isEqualTo(5);

		query = new Query(coord, 9, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, chr1, chr2).size().isEqualTo(7);

		query = new Query(coord, 10, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, chr1, chr2).size().isEqualTo(7);

		query = new Query(coord, 11, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, chr1, chr2).size().isEqualTo(7);

		query = new Query(coord, 12, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, chr1, chr2).size().isEqualTo(7);

		query = new Query(coord, 13, 0, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, chr1, chr2).size().isEqualTo(7);

		query = new Query(coord, 50, 50, tracks);
		bands = masterDataSource.getBands(query);
		assertThat(bands).contains(region1, region1Dup, region2, region3, region4, region5, region6, chr1, chr2, chr4).size().isEqualTo(10);
	}
}