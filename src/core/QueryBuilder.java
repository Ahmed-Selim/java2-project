package core;

public interface QueryBuilder {
    String personTable = "CREATE TABLE IF NOT EXISTS person ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "email TEXT UNIQUE NOT NULL,"
                    + "password TEXT NOT NULL ,"
                    + "phone TEXT UNIQUE NOT NULL,"
                    + "gender TEXT NOT NULL,"
                    + "auth TEXT DEFAULT 'user',"
                    + "maxBorrow INTEGER DEFAULT 3,"
                    + "borrowed INTEGER DEFAULT 0,"
                    + "CHECK (gender IN ('male', 'female') "
                        + "AND ( auth is null OR (auth is not null AND auth IN ('admin', 'user') ) ) "
                        + "AND borrowed <= maxborrow) "
                    + ");" ;
      
    String  bookTable = "CREATE TABLE IF NOT EXISTS book ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "isbn TEXT UNIQUE NOT NULL,"
                    + "title TEXT UNIQUE NOT NULL,"
                    + "author TEXT NOT NULL ,"
                    + "quantity INTEGER DEFAULT 5,"
                    + "CHECK (quantity >= 0) "
                    + ");" ;
    
    String reportTable = "CREATE TABLE IF NOT EXISTS report ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "uid INTEGER REFERENCES person(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "bid INTEGER REFERENCES book(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "issueDate DATE DEFAULT (date('now')),"
                    + "dueDate DATE DEFAULT (date('now', '+3 days')) ,"
                    + "penalty INTEGER DEFAULT 3 ,"
                    + "UNIQUE (uid, bid)"
                    + ");" ;
    
    String initPerson = "insert or IGNORE into person (name, email, password, phone, gender, auth)" +
    "values ('Ahmed','ahmed','123456','0100','male','admin'),"
            + "('Medo','medo','123','0000','male','user'); " ,
           initBook = "insert or IGNORE into book (isbn, title, author, quantity) " +
    "values ('12345','java','Ahmed',10); " ,
            initReport = "insert or IGNORE into report (uid, bid) " +
    "values (1,1);";
    
}
