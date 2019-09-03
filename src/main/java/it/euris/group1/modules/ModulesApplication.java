package it.euris.group1.modules;

import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.entities.Type;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ModulesApplication {
	private static final Logger log = LoggerFactory.getLogger(ModulesApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ModulesApplication.class);
	}


	@Bean
	public CommandLineRunner demo(ModulesRepository repository) {
		List<Timestamp> timestamps = new ArrayList<Timestamp>(10);
		createTimestamps(timestamps);

		return args -> {
			//save some modules
			repository.save(new Module( "Jason", "Smith", LocalDate.of(2000, 1, 1), timestamps.get(0), 19, Type.OWNER));
			repository.save(new Module( "Alice", "Anderson", LocalDate.of(2000, 1, 1), timestamps.get(1), 19, Type.CHILD));
			repository.save(new Module( "Bob", "Anderson", LocalDate.of(1980, 2, 2), timestamps.get(2), 39, Type.SPOUSE));
			repository.save(new Module( "Bob", "Carlsen", LocalDate.of(1980, 3, 3), timestamps.get(3), 39, Type.SPOUSE));
			repository.save(new Module( "Dean", "Dobro", LocalDate.of(2000, 4, 4), timestamps.get(4), 19, Type.CHILD));
			repository.save(new Module( "Ester", "Effenbach", LocalDate.of(2010, 4, 5), timestamps.get(5), 9, Type.OWNER));
			repository.save(new Module( "Filip", "Fjord", LocalDate.of(1950, 6, 6), timestamps.get(6), 69, Type.SPOUSE));
			repository.save(new Module( "Greta", "Gunderson", LocalDate.of(1960, 7, 6), timestamps.get(7), 59, Type.CHILD));
			repository.save(new Module( "Hans", "Haskell", LocalDate.of(1975, 8, 8), timestamps.get(8), 44, Type.OWNER));
			repository.save(new Module( "Bob", "Anderson", LocalDate.of(1985, 9, 9), timestamps.get(9), 34, Type.SPOUSE));
		};

	}

    private List<Timestamp> createTimestamps(List<Timestamp> timestamps) {
        timestamps.add(Timestamp.valueOf("2015-01-01 12:00:00.000"));
        timestamps.add(Timestamp.valueOf("2019-02-02 01:01:01.001"));
        timestamps.add(Timestamp.valueOf("2018-03-03 02:02:01.002"));
        timestamps.add(Timestamp.valueOf("2017-04-04 03:03:03.003"));
        timestamps.add(Timestamp.valueOf("2018-05-05 04:03:04.004"));
        timestamps.add(Timestamp.valueOf("2017-06-06 15:05:05.005"));
        timestamps.add(Timestamp.valueOf("2016-07-07 15:06:06.006"));
        timestamps.add(Timestamp.valueOf("2014-08-08 17:07:07.007"));
        timestamps.add(Timestamp.valueOf("2013-09-09 12:00:00.000"));
        timestamps.add(Timestamp.valueOf("2016-08-08 00:00:00.000"));

        return timestamps;

    }
}

