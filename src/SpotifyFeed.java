import java.sql.*;

public class SpotifyFeed {
    private Connection connection;
    private final String url = "jdbc:oracle:thin:@oracle.fil.univ-lille1.fr:1521:filora";

    private Statement statement;
    private CallableStatement inserer_ligne ;

    public SpotifyFeed(String login, String pwd) {
        try {
            connection = DriverManager.getConnection(url, login, pwd);
            statement = connection.createStatement();
            this.inserer_ligne = this.connection.prepareCall("{call CARIN.inserer_ligne(?,?)}");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void lireTableTest(){
        try {
            String query = "select * from information   ";
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while(resultSet.next()) {

                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i) + " ");
                }
                System.out.println();
            }
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void lireTableTestAmeliore(String s){
        try {
            String query = "select * from CARON.TABLE_TEST where texte like '" + s +"'";
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while(resultSet.next()) {

                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = resultSet.getString(i);
                    System.out.println(columnValue + " " + rsmd.getColumnName(i) + " ");
                }
            }
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void insererLigne(String line) {
        try {
            String value = "22";
            inserer_ligne.setString(value, line);
            inserer_ligne.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
