package core;

public class Person {
    private int _id ;
    private String _name, _email, _password, _phone, _gender, _auth ;
    
        public int getId () { return _id; }
        public String getName () { return _name; }
        public String getEmail () { return _email; }
        public String getPassword () { return _password; }
        public String getPhone () { return _phone; }
        public String getGender () { return _gender; }
        public String getAuth () { return _auth; }

        public void setId (int v) { _id = v; }
        public void setName (String v) { _name = v; }
        public void setEmail (String v) { _email = v; }
        public void setPassword (String v) { _password = v; }
        public void setPhone (String v) { _phone = v; }
        public void setGender (String v) { _gender = v; }
        public void setAuth (String v) { _auth = v; }


    private static Person singleton = null;
        
        private Person (Object[] p) {
            _id = (int) p[0] ;
            _name = (String) p[1] ; 
            _email = (String) p[2] ; 
            _password = (String) p[3] ;
            _phone = (String) p[4] ;
            _gender = (String) p[5] ; 
            _auth = (String) p[6] ;
        } 
  
        public static Person getInstance() {
            return singleton; 
        }
        
        public static Person getInstance(String email, String password) {
            if (singleton == null) { 
                singleton = new Person(SQLite.getInstance().login(email, password)); 
            } 
            return singleton; 
        }
        
        
        public static void signOut () {
            singleton = null ;
        }
        
        public static boolean isLoggedIn () {
            return singleton != null ;
        }
}
