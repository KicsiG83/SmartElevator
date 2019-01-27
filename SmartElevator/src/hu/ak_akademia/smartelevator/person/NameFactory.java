package hu.ak_akademia.smartelevator.person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import hu.ak_akademia.smartelevator.util.RandomNumberGenerator;

public class NameFactory {

    private static final String MALE_FIRST_NAMES_FILENAME = "resources/férfi-keresztnevek.txt";
    private static final String FEMALE_FIRST_NAMES_FILENAME = "resources/női-keresztnevek.txt";
    private static final String LAST_NAMES_FILENAME = "resources/vezetéknevek.txt";

    private static final List<String> maleFirstNames;
    private static final List<String> femaleFirstNames;
    private static final List<String> lastNames;

    private final RandomNumberGenerator randomNumberGenerator;

    static {
        try {
            maleFirstNames = Files.lines(Paths.get(MALE_FIRST_NAMES_FILENAME))
                    .collect(Collectors.toList());
            femaleFirstNames = Files.lines(Paths.get(FEMALE_FIRST_NAMES_FILENAME))
                    .collect(Collectors.toList());
            lastNames = Files.lines(Paths.get(LAST_NAMES_FILENAME))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("A nevek betöltése közben hiba lépett fel.", e);
        }
    }

    public NameFactory() {
        randomNumberGenerator = new RandomNumberGenerator();
    }

    public NameFactory(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public Name generateName() {
        return randomNumberGenerator.nextBoolean() ? generateMaleName() : generateFemaleName();
    }

    public Name generateMaleName() {
        String firstName = maleFirstNames.get(randomNumberGenerator.nextInt(maleFirstNames.size()));
        String lastName = lastNames.get(randomNumberGenerator.nextInt(lastNames.size()));
        return new Name(firstName, lastName);
    }

    public Name generateFemaleName() {
        String firstName = femaleFirstNames.get(randomNumberGenerator.nextInt(femaleFirstNames.size()));
        String lastName = lastNames.get(randomNumberGenerator.nextInt(lastNames.size()));
        return new Name(firstName, lastName);
    }

}