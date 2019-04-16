package core;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty title;
    private final SimpleStringProperty isbn;
    private final SimpleStringProperty author;
    
        public int getId () { return id.get(); }
        public String getTitle () { return title.get(); }
        public String getIsbn () { return isbn.get(); }
        public int getQuantity () { return quantity.get(); }
        public String getAuthor () { return author.get(); }

        public void setId (int v) { id.set(v); }
        public void setTitle (String v) { title.set(v); }
        public void setIsbn (String v) { isbn.set(v); }
        public void setQuantity (int v) { quantity.set(v); }
        public void setAuthor (String v) { author.set(v); }
        
        public Book (Object[] b) {
            this.id = new SimpleIntegerProperty((int) b[0]) ;
            this.isbn = new SimpleStringProperty((String) b[1]) ; 
            this.title = new SimpleStringProperty((String) b[2]) ; 
            this.author = new SimpleStringProperty((String) b[3]) ;
            this.quantity = new SimpleIntegerProperty((int) b[4]) ;
        }
}

