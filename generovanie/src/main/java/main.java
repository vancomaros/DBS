import com.github.javafaker.Faker;
import com.opencsv.CSVWriter;
import java.util.Random;


import java.io.FileWriter;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        Faker faker = new Faker();

        String pathUsers = "C:\\Users\\Maroš\\Desktop\\test\\players.csv";
        CSVWriter writerPlayers = new CSVWriter(new FileWriter(pathUsers));
        String[] players = new String[6];

        String pathCharacters = "C:\\Users\\Maroš\\Desktop\\test\\characters.csv";
        CSVWriter writerCharacters = new CSVWriter(new FileWriter(pathCharacters));
        String[] characters = new String[10];

        players[0] = "id";
        players[1] = "name";
        players[2] = "password";
        players[3] = "email";
        players[4] = "no_characters";
        players[5] = "premium_points";
        writerPlayers.writeNext(players);

        characters[0] = "id";
        characters[1] = "name";
        characters[2] = "hours_played";
        characters[3] = "xp";
        characters[4] = "class";
        characters[5] = "race";
        characters[6] = "money";
        characters[7] = "guild_leader";
        characters[8] = "guild_id";
        characters[9] = "player_owner";

        writerCharacters.writeNext(characters);

        int user_id = 1;
        int character_id = 1;

        for (int i = 0; i < 100005; i++) {
            players[0] = String.valueOf(user_id);
            players[1] = faker.name().username();
            players[2] = faker.pokemon().name();
            players[3] = faker.internet().emailAddress();
            players[4] = "0";
            players[5] = "0";

            Random random = new Random();
            int randomInteger = random.nextInt(10);

            int counter = 0;
            for (int j = 0; j < randomInteger; j++) {

                characters[0] = Integer.toString(character_id);
                characters[1] = faker.name().firstName();

                Random lvl = new Random();
                int ranLvl = lvl.nextInt(100000);

                characters[2] = String.valueOf(ranLvl/100);
                characters[3] = String.valueOf(ranLvl);
                characters[4] = String.valueOf(faker.number().numberBetween(0, 5));
                characters[5] = String.valueOf(faker.number().numberBetween(0, 4));
                characters[6] = String.valueOf(faker.number().numberBetween(0, 20000));
                characters[7] = "0";
                characters[8] = "1";

                characters[9] = players[0];

                counter++;
                players[4] = Integer.toString(counter);

                if (characters[4].equals("0"))
                    characters[4] = "Warrior";
                else if (characters[4].equals("1"))
                    characters[4] = "Paladin";
                else if (characters[4].equals("2"))
                    characters[4] = "Mage";
                else if (characters[4].equals("3"))
                    characters[4] = "Hunter";
                else
                    characters[4] = "Rogue";

                if (characters[5].equals("0"))
                    characters[5] = "Human";
                else if (characters[5].equals("1"))
                    characters[5] = "Goblin";
                else if (characters[5].equals("2"))
                    characters[5] = "Orc";
                else
                    characters[5] = "Demon";

                writerCharacters.writeNext(characters);
                character_id++;
            }
            writerPlayers.writeNext(players);
            user_id++;
        }

        writerCharacters.close();
        writerPlayers.close();

    }
}
