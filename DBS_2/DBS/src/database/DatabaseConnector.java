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
    }

    /*
     * Function finds user in database and then evaluate if password is matching.
     * If password are matching, it return value corresponding to it.
     */
    public int checkUser(String userName, String password) throws SQLException
    {
        int returning_value;
        // In case connection is not established
        if(connection == null) {
            System.out.println("Problem");
        }
        connection.setAutoCommit(false);
        // Try to get user from table
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM player WHERE player_name = ?");
            prepstatement.setString(1, userName);
            returnedValue = prepstatement.executeQuery();

            connection.commit();
            connection.setAutoCommit(true);

            // If something was found it evaluate the password.
            // If not there is not a player with given name
            if (returnedValue.next()) {
                if (password.equals(returnedValue.getString("player_password"))) {
                    returning_value = returnedValue.getInt("player_id");
                    prepstatement.close();
                    return returning_value;
                } else {
                    System.out.print("login failed!");
                    return 0;
                }
            } else
                return 0;
        }
        catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            prepstatement.close();
            connection.setAutoCommit(true);
            return -1;
        }
    }

    // This function adds a new user into database
    public int addUser(String userPassw, String userName, String userEmail) throws SQLException
    {
        connection.setAutoCommit(false);
        PreparedStatement prepstatement;
        try {
            prepstatement = connection.prepareStatement("INSERT INTO player " +
                    "(player_name,player_password,email,no_characters,premium_points) VALUES (?,?,?,0,0)");
            prepstatement.setString(1, userName);
            prepstatement.setString(2, userPassw);
            prepstatement.setString(3, userEmail);
            prepstatement.executeUpdate();
            connection.commit();

        } catch (SQLException e)
        {
            connection.rollback();
            System.out.print("Failed to add user");
            e.printStackTrace();
            connection.setAutoCommit(true);
            return -1;
        }
        connection.setAutoCommit(true);
        prepstatement.close();
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

    public int addCharacter(String charName, String charClass, String charRace, int id) throws SQLException
    {
        PreparedStatement prepstatement;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("INSERT INTO game_character " +
                    "(character_name,character_xp,class,race,game_money,player_owner) VALUES (?,0,?,?,0,?)");
            prepstatement.setString(1, charName);
            prepstatement.setString(2, charClass);
            prepstatement.setString(3, charRace);
            prepstatement.setInt(4, id);
            prepstatement.executeUpdate();

            prepstatement = connection.prepareStatement("UPDATE player SET no_characters=(no_characters+1)" +
                    "WHERE player_id = ?");
            prepstatement.setInt(1, id);
            prepstatement.execute();

            connection.commit();
        } catch (SQLException e)
        {
            connection.rollback();
            System.out.print("Failed to add user");
            e.printStackTrace();
            connection.setAutoCommit(true);
            return -1;
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return 1;
    }

    public int addQuest(int id_q, int c_id) throws SQLException {
        int cnt = 0;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT COUNT(*) FROM character_quest WHERE questid = ? AND char_id = ?");
            prepstatement.setInt(1, id_q);
            prepstatement.setInt(2, c_id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                cnt = returnedValue.getInt(1);
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
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Cannot add quest");
            }
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
        return 1;
    }

    public void deleteQuest(int id_q, int c_id) throws SQLException{
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("DELETE FROM character_quest WHERE questid = ? AND char_id = ? ");
            prepstatement.setInt(1, id_q);
            prepstatement.setInt(2, c_id);
            prepstatement.execute();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
    }

    public void decreaseXP(int id) throws SQLException {
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("UPDATE game_character SET character_xp=(character_xp/100*90) " +
                    "WHERE character_id = ?");
            prepstatement.setInt(1, id);
            prepstatement.execute();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
    }


    public ArrayList<ArrayList<String>> getCharacters(int id ,int offset) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM game_character WHERE player_owner = ? LIMIT ?,12");
            prepstatement.setInt(1, id);
            prepstatement.setInt(2, offset);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return  a;
    }

    public List<Integer> getQuests(int id) throws SQLException {
        List<Integer> l = new ArrayList<>();
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.
                    prepareStatement("SELECT questid FROM character_quest WHERE char_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                l.add(returnedValue.getInt(1));
            }
            connection.commit();
        }  catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return l;
    }


    public ArrayList<ArrayList<String>> searchInTable(String searchValue) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM game_character WHERE character_name = ?");
            prepstatement.setString(1, searchValue);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }


    public ArrayList<ArrayList<String>> getOneCharacter(int id) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM game_character WHERE character_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }

    public int deleteCharacter(int c_id, int p_id) throws SQLException {
        connection.setAutoCommit(false);
        PreparedStatement prepstatement;
        try {
            prepstatement = connection.prepareStatement("DELETE FROM character_item WHERE cid = ?");
            prepstatement.setInt(1, c_id);
            prepstatement.execute();

            prepstatement = connection.prepareStatement("DELETE FROM character_quest WHERE char_id = ?");
            prepstatement.setInt(1, c_id);
            prepstatement.execute();

            prepstatement = connection.prepareStatement("DELETE FROM game_character WHERE character_id = ?");
            prepstatement.setInt(1, c_id);
            prepstatement.execute();

            prepstatement = connection.prepareStatement("UPDATE player SET no_characters=(no_characters-1)" +
                    "WHERE player_id = ?");
            prepstatement.setInt(1, p_id);
            prepstatement.execute();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.out.print("Failed to delete character");
            e.printStackTrace();
            return -1;
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return 1;
    }

    public ArrayList<ArrayList<String>> bestPlayers(int offset) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT player_name,character_name,AVG(character_xp) as AvgExperience, no_characters as NoCharacters, character_xp, player_id\n" +
                            "FROM game_character \n" +
                            "INNER JOIN player ON player_owner = player_id \n" +
                            "GROUP BY player_name\n" +
                            "HAVING character_xp = (SELECT MAX(character_xp) FROM game_character WHERE player_owner = player_id)\n" +
                            "ORDER BY 3 DESC\n"+
                            "LIMIT ?,100");
            prepstatement.setInt(1,offset);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }


    public ArrayList<ArrayList<String>> bestGuild(int offset) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT guild_name as Guild_Name, number_of_players as no_players, SUM(game_money) as AMOUNT_OF_GOLD\n" +
                            "FROM guild g\n" +
                            "INNER JOIN game_character c ON c.guild_id = g.guild_id\n" +
                            "group by guild_name\n" +
                            "order by 3 DESC\n" +
                            "LIMIT ?,100");
            prepstatement.setInt(1,offset);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }


    public String getNameOfQuest(int id) throws SQLException {
        String lst = "";
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT quest_name FROM quest WHERE quest_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                lst = returnedValue.getString(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return lst;
    }

    public String getQuestInfo(String name) throws SQLException {
        String lst = "";
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.
                    prepareStatement("SELECT quest_description FROM quest WHERE quest_name = ?");
            prepstatement.setString(1, name);
            returnedValue =  prepstatement.executeQuery();
            while (returnedValue.next()) {
                lst = returnedValue.getString(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return lst;
    }

    public int getQuestID(String name) throws SQLException {
        int q_id = 0;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.
                    prepareStatement("SELECT quest_id FROM quest WHERE quest_name = ?");
            prepstatement.setString(1, name);
            returnedValue =  prepstatement.executeQuery();
            while (returnedValue.next()) {
                q_id = returnedValue.getInt(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return q_id;
    }

    public  List<Integer> getItems(int id) throws SQLException {
        List<Integer> l = new ArrayList<>();
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.
                    prepareStatement("SELECT itemid FROM character_item WHERE cid = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                l.add(returnedValue.getInt(1));
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return l;
    }

    public ArrayList<ArrayList<String>> getItemInfo(int id) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT * FROM item WHERE item_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }

    public int getItemId(String name, int level) throws SQLException {
        connection.setAutoCommit(false);
        int i_id = 0;
        try {
            prepstatement = connection.prepareStatement("SELECT item_id FROM item WHERE item_name = ? AND item_level = ?");
            prepstatement.setString(1, name);
            prepstatement.setInt(2, level);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                i_id = returnedValue.getInt(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return i_id;
    }

    public void destroyItem(int i_id, int c_id, int lvl) throws SQLException {
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("DELETE FROM character_item WHERE itemid = ? AND cid = ? ");
            prepstatement.setInt(1, i_id);
            prepstatement.setInt(2, c_id);
            prepstatement.execute();
            addMoney(lvl, c_id);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
    }

    private void addMoney(int amount, int id) throws SQLException {
        prepstatement = connection.prepareStatement("UPDATE game_character SET game_money=(game_money + ?*1000)" +
                "WHERE character_id = ?");
        prepstatement.setInt(1, amount);
        prepstatement.setInt(2, id);
        prepstatement.execute();
    }

    public void buffItem(int i_id, int c_id, int lvl) throws SQLException {
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("UPDATE character_item SET itemid=(itemid+1000)" +
                    "WHERE cid = ? AND itemid = ?");
            prepstatement.setInt(1, c_id);
            prepstatement.setInt(2, i_id);
            prepstatement.execute();
            // strhne 5000 korun za kazdy level itemu
            addMoney(-(5*lvl), c_id);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
    }

    public int getMoney(int id) throws SQLException{
        int money = 0;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT game_money FROM game_character WHERE character_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                money = returnedValue.getInt(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return money;
    }

    private static ArrayList<ArrayList<String>> rsToString(ResultSet resultSet) throws SQLException {
        ArrayList<ArrayList<String>> stringResult = new ArrayList<>();
        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>();
            int i = 1;
            do {
                String column = resultSet.getString(i);
                i++;
                row.add(column);
            } while (i <= resultSet.getMetaData().getColumnCount());

                stringResult.add(row);
        }
        return stringResult;
    }
}
