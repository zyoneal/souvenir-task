package com.viktor.task;

import com.viktor.task.model.Producer;
import com.viktor.task.model.Souvenir;
import com.viktor.task.service.SouvenirService;
import com.viktor.task.service.impl.CSVProcessor;
import com.viktor.task.service.impl.SouvenirServiceImpl;
import com.viktor.task.utils.Menu;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;

import static com.viktor.task.utils.Constants.*;

public class Main {

  private static final String INPUT_OPTION = "i";

  private static final String DELIMITER_OPTION = "d";

  private static char specifiedDelimiter;

  private static final SouvenirService souvenirService = new SouvenirServiceImpl();

  public static void main(String[] args) throws InterruptedException {

    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse(getOptions(), args);
      specifiedDelimiter = getDelimiter(cmd);

      try (InputStream in = resolveInputStream(cmd)) {
        CSVProcessor csvProcessor = new CSVProcessor();
        List<Souvenir> souvenirs = csvProcessor.readSouvenirsFromFile(in, specifiedDelimiter);

        Menu menu = new Menu();
        Scanner scanner = new Scanner(System.in);
        while (true) {
          switch (menu.menuChooseMode()) {
            case EXIT:
              return;

            // ADD PRODUCER -> невозможно добавить отдельно производителя без сувенира. В моей иерархии главный сувенир.
            // сувенир имеет произодителя, а не произодитель имеет сувениры.

            case ADD_SOUVENIR:
              Souvenir newSouvenir;
              System.out.println("Please, input souvenir name:");
              String souvenirName = scanner.nextLine();
              System.out.println("Please, input producer name:");
              String pName = scanner.nextLine();
              System.out.println("Please input souvenir price:");
              Double souvenirPrice = scanner.nextDouble();
              Optional<Producer> any = souvenirService.findAllProducers(souvenirs).stream().filter(p -> p.getName().equals(pName)).findAny();
              if (any.isPresent()) {
                newSouvenir = new Souvenir(souvenirName, any.get(), addDate(), souvenirPrice);
              } else {
                System.out.println("Please, input producer country:");
                String pCountry = scanner.next();
                newSouvenir = new Souvenir(souvenirName, new Producer(pName, pCountry), addDate(), souvenirPrice);
              }
              souvenirs.add(newSouvenir);
              csvProcessor.writeSouvenirsToFile(args[1], souvenirs, specifiedDelimiter);
              break;
            case EDIT_PRODUCER:
              System.out.println("Please, input producer name:");
              String name = scanner.next();
              System.out.println("Please, input producer country");
              String country = scanner.next();
              Predicate <Producer> producerNamePredicate = pr -> pr.getName().equals(name);
              Predicate <Producer> producerCountryPredicate = pr -> pr.getCountry().equals(country);
              Optional<Producer> anyProducerWithThatName = souvenirs.stream().map(souvenir -> souvenir.getProducer())
                      .filter(producerNamePredicate.and(producerCountryPredicate))
                      .findAny();
              if (!anyProducerWithThatName.isPresent()) {
                System.out.println("No such element!!!\n");
                Thread.sleep(5000);
                break;
              }
              Producer producer = anyProducerWithThatName.get();
              System.out.println("===Change country of producer===");
              System.out.println("Please, input new country for producer: " + producer.getName());
              String newCountry = scanner.next();
              souvenirService.editCountryOfProducer(souvenirs, producer.getName(), newCountry);
              csvProcessor.writeSouvenirsToFile(args[1], souvenirs, specifiedDelimiter);
              break;
            case EDIT_SOUVENIR:
              System.out.println("Please, input producer name:");
              String prName = scanner.next();
              System.out.println("Please, input souvenir name:");
              String souvName = scanner.next();
              System.out.println("Please, input producer country");
              String c = scanner.next();
              Predicate<Souvenir> sN = s -> s.getName().equals(souvName);
              Predicate<Souvenir> pN = s -> s.getProducer().getName().equals(prName);
              Predicate <Souvenir> pC = s -> s.getProducer().getCountry().equals(c);
              Predicate<Souvenir> souvenirPredicate = sN.and(pN).and(pC);
              Optional<Souvenir> toEdit = souvenirs.stream()
                      .filter(souvenirPredicate)
                      .findAny();
              if (!toEdit.isPresent()) {
                System.out.println("No such element!!!\n");
                Thread.sleep(5000);
                break;
              }
              System.out.println("===Change price of souvenir===");
              System.out.println("Please, input new price for souvenir: " + souvName);
              double newPrice = scanner.nextDouble();
              souvenirService.editPriceOfSouvenir(souvenirs, souvenirPredicate, toEdit.get().getName(), newPrice);
              csvProcessor.writeSouvenirsToFile(args[1], souvenirs, specifiedDelimiter);
              break;
            case FIND_ALL_PRODUCERS:
              souvenirService.findAllProducers(souvenirs).forEach(System.out::println);
              break;
            case FIND_ALL_SOUVENIRS:
              souvenirService.findAllSouvenirs(souvenirs).forEach(System.out::println);
              break;
            case FIND_SOUVENIRS_IN_COUNTRY:
              System.out.println("Please, input country:");
              String ctr = scanner.next();
              souvenirService.findSouvenirsByCountry(souvenirs, ctr).forEach(System.out::println);
              break;
            case FIND_PRODUCERS_PRICES_LESS_THAN:
              System.out.println("Please, input price:");
              Double price = scanner.nextDouble();
              souvenirService.findProducersByPriceLessThan(souvenirs, price).forEach(System.out::println);
              break;
            case FIND_PRODUCERS_AND_THEIRS_SOUVENIRS:
              Map<Producer, List<Souvenir>> allInformationProducerAndHisSouvenirs = souvenirService.findAllInformationProducerAndHisSouvenirs(souvenirs);
              printMap(allInformationProducerAndHisSouvenirs);
              break;
            case FIND_SOUVENIR_IN_YEAR:
              System.out.println("Please, input souvenir:");
              String souvenir = scanner.next();
              System.out.println("Please, input year:");
              int year = scanner.nextInt();
              souvenirService.findProducersBySouvenirAndYear(souvenirs, souvenir, year).forEach(System.out::println);
              break;
            case FIND_SOUVENIRS_FOR_EACH_YEAR:
              Map<Year, List<Souvenir>> souvenirsByYear = souvenirService.findSouvenirsByYears(souvenirs);
              printMap(souvenirsByYear);
              break;
            case DELETE_PRODUCER_AND_HIS_SOUVENIRS:
              System.out.println("Please, input producer name:");
              String prodName = scanner.next();
              System.out.println("Please, input producer country:");
              String countryName = scanner.next();
              souvenirService.deleteProducerAndHisSouvenirs(souvenirs, new Producer(prodName, countryName));
              csvProcessor.writeSouvenirsToFile(args[1], souvenirs, specifiedDelimiter);
              break;
            default:
              System.out.println("Illegal option. Please try again");
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (ParseException e) {
      System.err.println(e.getMessage());
      HelpFormatter helper = new HelpFormatter();
      helper.printHelp("Usage:", getOptions());
    }
  }

  private static char getDelimiter(CommandLine cmd) {
    char delimiter = ',';
    if (cmd.hasOption(DELIMITER_OPTION)) {
      delimiter = cmd.getOptionValue(DELIMITER_OPTION).charAt(0);
    }
    return delimiter;
  }

  private static Options getOptions() {
    Options options = new Options();
    options.addOption(Option.builder(INPUT_OPTION)
            .longOpt("input")
            .hasArg()
            .required(true)
            .desc("set input csv file")
            .build());

    options.addOption(Option.builder(DELIMITER_OPTION)
            .longOpt("delimiter")
            .hasArg()
            .required(false)
            .desc("specify the delimiter character in CSV file")
            .build());
    return options;
  }

  private static InputStream resolveInputStream(CommandLine cmd) throws FileNotFoundException {
    return new FileInputStream(cmd.getOptionValue(INPUT_OPTION));
  }

  private static <T> void printMap(Map<T, List<Souvenir>> map) {
    TreeMap<T, List<Souvenir>> producerListTreeMap = new TreeMap<>(map);
    Set<T> keys = producerListTreeMap.keySet();
    for (T key : keys) {
      System.out.println(key + " | ==> |: " + map.get(key));
    }
  }

  private static LocalDate addDate() {
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter a date [yyyy-MM-dd]: ");
    String str = scan.nextLine();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(str, dtf);
  }

}
