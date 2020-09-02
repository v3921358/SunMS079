package constants;

public class LoadingOption {

    public enum SQLInfo {

        Default(1, ServerConstants.SQL_PORT, ServerConstants.SQL_USER, ServerConstants.SQL_PASSWORD, ServerConstants.SQL_DATABASE);
        //Alpha(3, "3306", "root", "e11a64a31f1ea8b", "alpha");
        private final String port, user, pass, db;
        private final int id;

        private SQLInfo(int id, String port, String user, String pass, String db) {
            this.id = id;
            this.port = port;
            this.user = user;
            this.pass = pass;
            this.db = db;
        }

        public String getPort() {
            return port;
        }

        public String getUser() {
            return user;
        }

        public String getPass() {
            return pass;
        }

        public String getDb() {
            return db;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(id).append(". ").append(this.name()).append("\r\n");
            sb.append("Port :: ").append(port).append("\r\n");
            sb.append("User :: ").append(user).append("\r\n");
            sb.append("Password :: ").append(pass).append("\r\n");
            sb.append("Database :: ").append(db).append("\r\n");
            sb.append("\r\n");
            return sb.toString();
        }

        public static SQLInfo getById(int id) {
            for (SQLInfo i : values()) {
                if (i.id == id) {
                    return i;
                }
            }
            return Default;
        }
    }

    public final static String[] ipSetting = {"127.0.0.1", "87.69.227.144"};

}
