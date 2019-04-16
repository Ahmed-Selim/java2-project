package core;

import java.util.Date;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Report {
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty userId;
    private final SimpleIntegerProperty bookId;
    private final SimpleStringProperty issueDate;
    private final SimpleStringProperty dueDate;
    
    public Report(int id, int userId, int bookId, String issueDate, String dueDate) {
        this.id = new SimpleIntegerProperty(id);
        this.userId = new SimpleIntegerProperty(userId);
        this.bookId = new SimpleIntegerProperty(bookId);
        this.issueDate = new SimpleStringProperty(issueDate);
        this.dueDate = new SimpleStringProperty(dueDate);
    }

    public int getId() {
        return id.get();
    }

    public int getUserId() {
        return userId.get();
    }

    public int getBookId() {
        return bookId.get();
    }

    public String getIssueDate() {
        return issueDate.get();
    }

    public String getDueDate() {
        return dueDate.get();
    }
    
}
