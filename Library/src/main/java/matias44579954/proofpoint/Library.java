package matias44579954.proofpoint;

import matias44579954.proofpoint.entities.Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Library {
    private static final int BOOK_TITLE_INDEX = 0, BOOK_AUTHOR_INDEX = 1;
    private static final int BOOK_PUBLICATION_YEAR_INDEX = 2;

    private static final String FILE_PATH = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "booksWithErrors.csv").toString();;

    record CsvRow(
            String title, String authorName,
            Integer publicationYear
    ){}

    //Receives a CSV file and returns a list where each element is a STRING that is a line of the CSV.
    private static List<String> rowList(String csvFilePath, int offset, int limit){
        Scanner sc = null;
        List<String> csvRows = new ArrayList<>();

        try {
            File file = new File(csvFilePath);
            sc = new Scanner(file);
            // Discard the header line if it has one.
            for (int i = 0; i < offset && sc.hasNextLine(); i++) {
                sc.nextLine();
            }
            // Add the CSV String lines to the csvRows list.
            for (int i = 0; i < limit && sc.hasNextLine(); i++) {
                csvRows.add(sc.nextLine());
            }

        } catch(FileNotFoundException fnfe){
            csvRows.clear();
            System.out.println("ERROR: " + fnfe.getMessage());
        }finally {
            if (sc != null) sc.close();
        }
        return csvRows;
    }

    // Converts each String line into a CsvRow record line, that is, into a different list.
    private static Function<String[], CsvRow> csvRowMapper = (arr) -> {
        Integer publicationYear;
        if (!validateYear(arr[BOOK_PUBLICATION_YEAR_INDEX])){
            publicationYear = 0;
        }
        else{
            publicationYear = Integer.parseInt(arr[BOOK_PUBLICATION_YEAR_INDEX]);
        }
        return new CsvRow(
                arr[BOOK_TITLE_INDEX],
                arr[BOOK_AUTHOR_INDEX],
                publicationYear
        );
    };

    //Gets the list of STRINGS with the CSV lines and converts it into a list of CsvRow records
    private static List<CsvRow> getDataCSV(){
        List<String> rows = new ArrayList<>();
        List<CsvRow> csvRows = new ArrayList<>();
        rows = rowList(FILE_PATH, 1, 211);
        for (String row : rows) {
            String[] values = row.split(",", -1);
            CsvRow csvRow = csvRowMapper.apply(values);
            csvRows.add(csvRow);
        }
        return csvRows;
    }

    private static Set<Book> dataProcessing(List<CsvRow> csvRows){
        Set<Book> books = new HashSet<>();
        Integer incID = 0;

        for (CsvRow csvRow: csvRows) {
            Boolean repeatedBook = books.stream().anyMatch(b -> ((b.getTitle().equals(csvRow.title)) && (b.getAuthorName().equals(csvRow.authorName)) && (b.getPublicationYear().equals(csvRow.publicationYear))));
            Book book;
            if (repeatedBook || (csvRow.title.equals(""))){
                continue;
            }
            if (csvRow.authorName.equals("")) {
                book = new Book(incID, csvRow.title, "Unknown Author", csvRow.publicationYear);
            }
            else {
                book = new Book(incID, csvRow.title, csvRow.authorName, csvRow.publicationYear);
            }
            books.add(book);
            incID += 1;
        }
        return books;
    }

    private static Boolean validateYear(String publicationYear){
        Integer test;
        int currentYear = java.time.Year.now().getValue();
        if (publicationYear == null || publicationYear.trim().isEmpty()){
            return false;
        }
        try {
            test = Integer.parseInt(publicationYear);
        } catch (NumberFormatException e){
            return false; // Returns false if the conversion fails
        }

        if (test <= 0 || test > currentYear){
            return false;
        }
        return true;
    }

    private static void groupByAuthor(Set<Book> books){
        Map<String, List<Book>> booksByAuthor = books.stream()
                .collect(Collectors.groupingBy(Book::getAuthorName));

        // Printing books grouped by author.
        booksByAuthor.forEach((author, bookList) -> {
            System.out.println("AUTHOR: " + author);
            bookList.forEach(System.out::println);
            System.out.println("\n-----\n");
        });
    }

    private static void groupByPublicationYear(Set<Book> books){
        Map<Integer, List<Book>> booksByYear = books.stream()
                .collect(Collectors.groupingBy(Book::getPublicationYear));

        // Printing books grouped by publication year.
        booksByYear.forEach((year, bookList) -> {
            System.out.println("YEAR: " + year);
            bookList.forEach(System.out::println);
            System.out.println("\n-----\n");
        });
    }

    public static void main(String[] args) throws Exception {
        List<CsvRow> csvRows = getDataCSV();
        System.out.println("\nCSV File processed correctly.\n");

        // dataProcessing is responsible for reading the CSV, mapping the data in memory, and creating the book instances.
        Set<Book> books = dataProcessing(csvRows);
        System.out.println("\nData porcessed correctly\n");

        // Book printing
        System.out.println("BOOKS IN LIBRARY: ");
        System.out.println("----------------------------------------------------------------------------------------------------------------\n");
        books.forEach(System.out::println);
        System.out.println("\n----------------------------------------------------------------------------------------------------------------");

        Integer opc;
        Scanner sc = null;
        String menu = "--------------------------------------------------------------------------\n" +
                "Enter a number according to the option you want to perform: \n" +
                "1- Books grouped by author.\n" +
                "2- Books grouped by publication year.\n" +
                "0- Quit.\n" +
                "--------------------------------------------------------------------------";

        try {
            sc = new Scanner(System.in);
            System.out.println(menu);
            opc = sc.nextInt();

            while(opc != 0) {
                switch (opc) {
                    case 1:
                        groupByAuthor(books);
                        break;
                    case 2:
                        groupByPublicationYear(books);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("ERROR: incorrect option, try again...");
                }
                System.out.println(menu);
                opc = sc.nextInt();
            }
        } catch (InputMismatchException e){
            System.out.println("ERROR: you must enter an integer:   " + e.getMessage());
            sc.nextLine(); // Clean buffer
        } finally {
            if (sc != null) sc.close();
        }
    }
}
