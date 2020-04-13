import com.github.javafaker.Faker;
import com.opencsv.CSVWriter;
import java.util.Random;


import java.io.FileWriter;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {

        int[] guildes = new int[50];
        for (int i = 0; i < 50; i++) {
            guildes[i] = 0;
        }

        Faker faker = new Faker();

        String pathUsers = "C:\\Users\\Maroš\\Desktop\\test\\players.csv";
        CSVWriter writerPlayers = new CSVWriter(new FileWriter(pathUsers));
        String[] players = new String[6];

        String pathCharacters = "C:\\Users\\Maroš\\Desktop\\test\\characters.csv";
        CSVWriter writerCharacters = new CSVWriter(new FileWriter(pathCharacters));
        String[] characters = new String[10];

        String pathGuild = "C:\\Users\\Maroš\\Desktop\\test\\guild.csv";
        CSVWriter writerGuild = new CSVWriter(new FileWriter(pathGuild));
        String[] guild = new String[3];

        String pathQuest = "C:\\Users\\Maroš\\Desktop\\test\\quest.csv";
        CSVWriter writerQuest = new CSVWriter(new FileWriter(pathQuest));
        String[] quest = new String[7];

        String pathLocation = "C:\\Users\\Maroš\\Desktop\\test\\location.csv";
        CSVWriter writerLocation = new CSVWriter(new FileWriter(pathLocation));
        String[] location = new String[6];

        String char_quest = "C:\\Users\\Maroš\\Desktop\\test\\character_quest.csv";
        CSVWriter writerCharQue = new CSVWriter(new FileWriter(char_quest));
        String[] charQue = new String[3];

        String pathItem = "C:\\Users\\Maroš\\Desktop\\test\\item.csv";
        CSVWriter writerItem = new CSVWriter(new FileWriter(pathItem));
        String[] item = new String[5];

        String char_item = "C:\\Users\\Maroš\\Desktop\\test\\character_item.csv";
        CSVWriter writerCharIt = new CSVWriter(new FileWriter(char_item));
        String[] charIt = new String[3];

        players[0] = "id";
        players[1] = "name";
        players[2] = "password";
        players[3] = "email";
        players[4] = "no_characters";
        players[5] = "premium_points";
        writerPlayers.writeNext(players);

        characters[0] = "character_id";
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

        guild[0] = "id";
        guild[1] = "no_of_players";
        guild[2] = "rank";
        writerGuild.writeNext(guild);

        quest[0] = "id";
        quest[1] = "name";
        quest[2] = "required level";
        quest[3] = "reward money";
        quest[4] = "reward xp";
        quest[5] = "description";
        quest[6] = "location";
        writerQuest.writeNext(quest);

        location[0] = "id";
        location[1] = "name";
        location[2] = "monsters";
        location[3] = "active event";
        location[4] = "description";
        location[5] = "playercount";
        writerLocation.writeNext(location);

        charQue[0] = "character_quest_id";
        charQue[1] = "questid";
        charQue[2] = "char_id";
        writerCharQue.writeNext(charQue);

        charIt[0] = "character_item_id";
        charIt[1] = "item_id";
        charIt[2] = "char_id";
        writerCharIt.writeNext(charIt);

        item[0] = "item_id";
        item[1] = "name";
        item[2] = "level";
        item[3] = "type";
        item[4] = "rarity";
        writerItem.writeNext(item);

        int user_id = 1;
        int character_id = 1;
        int guild_id = 1;
        int quest_id = 1;
        int location_id = 1;
        int character_quest_id = 1;
        int item_id = 0;
        int character_item_id = 1;

        for (int i = 0; i < 10; i++) {
            Random pls = new Random();
            int pl = pls.nextInt(66000);

            location[0] = String.valueOf(location_id);
            location[1] = faker.country().capital();
            location[2] = faker.animal().name() + ", " + faker.overwatch().hero() + ", " + faker.superhero().name();
            location[3] = "0";
            location[4] = faker.shakespeare().kingRichardIIIQuote();
            location[5] = String.valueOf(pl);
            location_id++;
            writerLocation.writeNext(location);
        }

        for (int i = 0; i < 7500; i++) {
            Random reward = new Random();
            int rew = reward.nextInt(10);

        quest[0] = String.valueOf(quest_id);
        quest[1] = faker.superhero().descriptor();
        quest[2] = String.valueOf(rew);
        quest[3] = String.valueOf(rew * 50);
        quest[4] = String.valueOf(rew * 25);
        quest[5] = faker.shakespeare().romeoAndJulietQuote();
        quest[6] = String.valueOf(rew);
        quest_id++;
        writerQuest.writeNext(quest);
        }

        for (int i = 0; i < 1000; i++) {
            item[0] = String.valueOf(item_id);
            item[1] = faker.color().name() + " " + faker.elderScrolls().creature();

            Random l = new Random();
            int lv = l.nextInt(4)
                    ;
            item[2] = String.valueOf(1);
            item[3] = String.valueOf(lv+1);
            if(i < 500) {
                item[4] = "common";
            }
            else if (i < 800) {
                item[4] = "rare";
            }
            else if (i < 950) {
                item[4] = "epic";
            }
            else item[4] = "legendary";

            item_id++;
            writerItem.writeNext(item);
        }

        for (int i = 0; i < 100005; i++) {
            players[0] = String.valueOf(user_id);
            players[1] = faker.name().username();
            players[2] = faker.pokemon().name();
            players[3] = faker.internet().emailAddress();
            players[4] = "0";
            players[5] = "0";

            Random random = new Random();
            int randomInteger = random.nextInt(35);
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
                if (i % 200 == 0 && j == 1) {
                    characters[7] = "1";
                }
                else characters[7] = "0";

                Random guild_number = new Random();
                int guildNumber = guild_number.nextInt(50);
                guildes[guildNumber]++;
                characters[8] = String.valueOf(guildNumber);

                characters[9] = players[0];

                counter++;
                players[4] = Integer.toString(counter);


                //class & race
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

                Random no_quest = new Random();
                int noQuest = no_quest.nextInt(5);

                for (int k = 0; k < noQuest; k++) {

                    Random which_quest = new Random();
                    int whichQuest = which_quest.nextInt(7500);

                    charQue[0] = String.valueOf(character_quest_id);
                    charQue[1] = String.valueOf(whichQuest);
                    charQue[2] = String.valueOf(character_id);
                    writerCharQue.writeNext(charQue);
                    character_quest_id++;
                }

                Random no_items = new Random();
                int noItems = no_items.nextInt(5);

                for (int k = 0; k < noItems; k++) {

                    Random which_item = new Random();
                    int whichItem = which_item.nextInt(1000);

                    charIt[0] = String.valueOf(character_item_id);
                    charIt[1] = String.valueOf(whichItem);
                    charIt[2] = String.valueOf(character_id);
                    writerCharIt.writeNext(charIt);
                    character_item_id++;
                }

                writerCharacters.writeNext(characters);
                character_id++;
            }
            writerPlayers.writeNext(players);
            user_id++;
        }

        writerQuest.close();
        writerLocation.close();
        writerCharacters.close();
        writerPlayers.close();
        writerCharQue.close();
        writerItem.close();
        writerCharIt.close();

        // making guildes for every character
        for (int i = 0; i < 50; i++) {
            guild[0] = String.valueOf(guild_id);
            guild[1] = String.valueOf(guildes[i]);
            guild[2] = String.valueOf(guild_id);

            guild_id++;
            writerGuild.writeNext(guild);
        }

        writerGuild.close();
    }
}
