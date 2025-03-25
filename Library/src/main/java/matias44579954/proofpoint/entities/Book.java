package matias44579954.proofpoint.entities;

import java.util.Objects;

public class Book {
    private Integer id;
    private String title;
    private String authorName;
    private Integer publicationYear;

    public Book(Integer id, String title, String authorName, Integer publicationYear) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.publicationYear = publicationYear;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Override
    public String toString() {
        return "- " +
                "TITLE: " + title + "  |  " +
                "AUTHOR NAME: " + authorName + "  |  " +
                "PUBLICATION YEAR: " + publicationYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
                Objects.equals(authorName, book.authorName) &&
                Objects.equals(publicationYear, book.publicationYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authorName, publicationYear);
    }
}
