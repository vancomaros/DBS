package database;

import java.sql.*;
import java.util.*;

public class DatabaseConnector {

    private Connection connection = null;
    private String url = "jdbc:mysql://localhost:3306/dbs_projekt?characterEncoding=latin1";
    private String name = "root";
    private String passw = "1999";
    private Statement statement = null;
    private ResultSet returnedValue = null;
    private PreparedStatement prepstatement = null;
    private String lst;
    private int q_id;
    public void DatabseInit()
    {

        // Checks if driver is available
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            e.printStackTrace();
            return;
        }

        // Connecting into database
        try {
            connection = DriverManager.getConnection(url, name, passw);
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        //Testing connection
/*        if (connection != null) {
            System.out.println("Connection established\n");
        } else {
            System.out.println("Problem with connection");
        }
*/
    }

    /*
     * Function finds user in database and then evaluate if password is matching.
     * If password are matching, it return value corresponding to it.
     */
    public int checkUser(String userName, String password)
    {
        // In case connection is not established
        if(connection == null) {
            System.out.println("Problem");
        }

        // Try to get user from table
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM player WHERE player_name = ?");
            prepstatement.setString(1, userName);
            returnedValue = prepstatement.executeQuery();

            // If something was found it evaluate the password.
            // If not there is not a player with given name
            if (returnedValue.next()) {
                if (password.equals(returnedValue.getString("player_password"))) {
      //              System.out.print("login Successful");
                    return returnedValue.getInt("player_id");
                } else {
                    System.out.print("login failed!");
                    return 0;
                }
            } else
                return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // This function adds a new user into database
    public int addUser(String userPassw, String userName, String userEmail)
    {

        PreparedStatement prepstatement = null;
        try {
            prepstatement = connection.prepareStatement("INSERT INTO player " +
                    "(player_name,player_password,email,no_characters,premium_points) VALUES (?,?,?,0,0)");
            prepstatement.setString(1, userName);
            prepstatement.setString(2, userPassw);
            prepstatement.setString(3, userEmail);
            prepstatement.executeUpdate();

        } catch (SQLException e)
        {
            System.out.print("Failed to add user");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /*
     * Closing connection
     */
    public void connectionClose()
    {
        try {
            if (returnedValue != null)
                returnedValue.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
    //        System.out.print("Connection closed\n");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Closing connection failed!");
        }
    }

    public ResultSet getFromQuery(String quer)
    {
        try {
            prepstatement = connection
                    .prepareStatement(quer);
            returnedValue = prepstatement.executeQuery();
            return returnedValue;
        } catch (SQLException e)
        {
            System.out.println("Problem with player table");
        }
        return null;
    }

    public int addCharacter(String charName, String charClass, String charRace, int id)
    {
        {

            PreparedStatement prepstatement = null;
            try {
                prepstatement = connection.prepareStatement("INSERT INTO game_character " +
                        "(character_name,character_xp,class,race,game_money,player_owner) VALUES (?,0,?,?,0,?)");
                prepstatement.setString(1, charName);
                prepstatement.setString(2, charClass);
                prepstatement.setString(3, charRace);
                prepstatement.setInt(4, id);
                prepstatement.executeUpdate();

            } catch (SQLException e)
            {
                System.out.print("Failed to add user");
                e.printStackTrace();
                return -1;
            }
            return 1;
        }
    }

    public int addQuest(int id_q, int c_id) throws SQLException {
        prepstatement = null;
        int cnt = 0;
        try {
            prepstatement = connection.prepareStatement("SELECT COUNT(*) FROM character_quest WHERE questid = ? AND char_id = ?");
            prepstatement.setInt(1, id_q);
            prepstatement.setInt(2, c_id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                cnt = returnedValue.getInt(1);
            }
        } catch(SQLException e) {
            System.out.println(e + " nejde to");
        }

        if (cnt != 0) {
            return -1;
        }

        try {
            prepstatement = connection.prepareStatement("INSERT INTO character_quest" +
                    "(questid, char_id) VALUES (?, ?)");
            prepstatement.setInt(1, id_q);
            prepstatement.setInt(2, c_id);
            prepstatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cannot add quest");
        }
        return 1;
    }

    public void deleteQuest(int id_q, int c_id) throws SQLException{
        prepstatement = null;
        try {
            prepstatement = connection.prepareStatement("DELETE FROM character_quest WHERE questid = ? AND char_id = ? ");
            prepstatement.setInt(1, id_q);
            prepstatement.setInt(2, c_id);
            prepstatement.execute();
        } catch(SQLException e) {
            System.out.println("Cannot delete quest");
        }
    }

    public void decreaseXP(int id) throws SQLException {
        prepstatement = null;
        try {
            prepstatement = connection.prepareStatement("UPDATE game_character SET character_xp=(character_xp/100*90) " +
                    "WHERE character_id = ?");
            prepstatement.setInt(1, id);
            prepstatement.execute();
        } catch (SQLException e) {
            System.out.println("Cannot decrease XP" + e);
        }
    }

    public ResultSet getCharacters(int id ,int offset) throws SQLException {
        prepstatement = connection
                .prepareStatement("SELECT * FROM game_character WHERE player_owner = ? LIMIT ?,12");
        prepstatement.setInt(1, id);
        prepstatement.setInt(2, offset);
        returnedValue = prepstatement.executeQuery();
        return  returnedValue;
    }

    public List<Integer> getQuests(int id) throws SQLException {
        prepstatement = connection.
                prepareStatement("SELECT questid FROM character_quest WHERE char_id = ?");
        prepstatement.setInt(1, id);
        returnedValue = prepstatement.executeQuery();
        List<Integer> l = new ArrayList<>();
        while (returnedValue.next()) {
            l.add(returnedValue.getInt(1));
        }
        return l;
    }

    public ResultSet searchInTable(String searchValue) throws SQLException {
        prepstatement = connection
                .prepareStatement("SELECT * FROM game_character WHERE character_name = ?");
        prepstatement.setString(1, searchValue);
        returnedValue = prepstatement.executeQuery();
        return returnedValue;
    }

    public ResultSet getOneCharacter(int id) throws SQLException {
        prepstatement = connection
                .prepareStatement("SELECT * FROM game_character WHERE character_id = ?");
        prepstatement.setInt(1, id);
        returnedValue = prepstatement.executeQuery();
        return returnedValue;
    }

    public int deleteCharacter(int id) {
        PreparedStatement prepstatement = null;
        try {
            prepstatement = connection.prepareStatement("DELETE FROM game_character WHERE character_id = ?");
            prepstatement.setInt(1, id);
            prepstatement.execute();
        } catch (SQLException e) {
            System.out.print("Failed to delete character");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public ResultSet bestPlayers(int offset) throws SQLException {
        prepstatement = connection
                .prepareStatement("SELECT player_name,character_name,AVG(character_xp) as AvgExperience, no_characters as NoCharacters, character_xp, player_id\n" +
                        "FROM game_character \n" +
                        "INNER JOIN player ON player_owner = player_id \n" +
                        "GROUP BY player_name\n" +
                        "HAVING character_xp = (SELECT MAX(character_xp) FROM game_character WHERE player_owner = player_id)\n" +
                        "ORDER BY 3 DESC\n"+
                        "LIMIT ?,100");
        prepstatement.setInt(1,offset);
        ResultSet resultSet = prepstatement.executeQuery();
        return resultSet;
    }

    public ResultSet bestGuild(int offset) throws SQLException {
        prepstatement = connection
                .prepareStatement("SELECT guild_name as Guild_Name, number_of_players as no_players, SUM(game_money) as AMOUNT_OF_GOLD\n" +
                        "FROM guild g\n" +
                        "INNER JOIN game_character c ON c.guild_id = g.guild_id\n" +
                        "group by guild_name\n" +
                        "order by 3 DESC\n" +
                        "LIMIT ?,100");
        prepstatement.setInt(1,offset);
        return prepstatement.executeQuery();
    }

    public String getNameOfQuest(int id) throws SQLException{
        prepstatement = connection
                .prepareStatement("SELECT quest_name FROM quest WHERE quest_id = ?");
        prepstatement.setInt(1, id);
        returnedValue = prepstatement.executeQuery();
        while (returnedValue.next()) {
            lst = returnedValue.getString(1);
        }
        return lst;
    }

    public String getQuestInfo(String name) throws SQLException {
        prepstatement = connection.
                prepareStatement("SELECT quest_description FROM quest WHERE quest_name = ?");
        prepstatement.setString(1, name);
        returnedValue =  prepstatement.executeQuery();
        while (returnedValue.next()) {
            lst = returnedValue.getString(1);
        }
        return lst;
    }

    public int getQuestID(String name) throws SQLException {
        prepstatement = connection.
                prepareStatement("SELECT quest_id FROM quest WHERE quest_name = ?");
        prepstatement.setString(1, name);
        returnedValue =  prepstatement.executeQuery();
        while (returnedValue.next()) {
            q_id = returnedValue.getInt(1);
        }
        return q_id;
    }

    public  List<Integer> getItems(int id) throws SQLException {
        prepstatement = connection.
                prepareStatement("SELECT itemid FROM character_item WHERE cid = ?");
        prepstatement.setInt(1, id);
        returnedValue = prepstatement.executeQuery();
        List<Integer> l = new ArrayList<>();
        while (returnedValue.next()) {
            l.add(returnedValue.getInt(1));
        }
        return l;
    }

    public ResultSet getItemInfo(int id) throws SQLException {
        prepstatement = connection.prepareStatement("SELECT * FROM item WHERE item_id = ?");
        prepstatement.setInt(1, id);
        return prepstatement.executeQuery();
    }

    public int getItemId(String name, int level) throws SQLException {
        int i_id = 0;
        prepstatement = connection.prepareStatement("SELECT item_id FROM item WHERE item_name = ? AND item_level = ?");
        prepstatement.setString(1, name);
        prepstatement.setInt(2, level);
        returnedValue = prepstatement.executeQuery();
        while (returnedValue.next()) {
            i_id = returnedValue.getInt(1);
        }
        return i_id;
    }

    public void destroyItem(int i_id, int c_id, int lvl) throws SQLException {
        prepstatement = connection.prepareStatement("DELETE FROM character_item WHERE itemid = ? AND cid = ? ");
        prepstatement.setInt(1, i_id);
        prepstatement.setInt(2, c_id);
        prepstatement.execute();
        addMoney(lvl, c_id);
    }

    public void addMoney(int amount, int id) throws SQLException {
        prepstatement = connection.prepareStatement("UPDATE game_character SET game_money=(game_money + ?*1000)" +
                "WHERE character_id = ?");
        prepstatement.setInt(1, amount);
        prepstatement.setInt(2, id);
        prepstatement.execute();
    }

    public void buffItem(int i_id, int c_id, int lvl) throws SQLException {
        prepstatement = connection.prepareStatement("UPDATE character_item SET itemid=(itemid+7500)" +
                "WHERE cid = ? AND itemid = ?");
        prepstatement.setInt(1, c_id);
        prepstatement.setInt(2, i_id);
        prepstatement.execute();
        // strhne 5000 korun za kazdy level itemu
        addMoney(-(5*lvl), c_id);
    }

    public int getMoney(int id) throws SQLException{
        int money = 0;
        prepstatement = connection.prepareStatement("SELECT game_money FROM game_character WHERE character_id = ?");
        prepstatement.setInt(1, id);
        returnedValue = prepstatement.executeQuery();
        while (returnedValue.next()) {
            money = returnedValue.getInt(1);
        }
        return money;
    }
}