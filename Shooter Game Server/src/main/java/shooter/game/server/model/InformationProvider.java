package shooter.game.server.model;

import game_lib.entities.Character;

import java.util.*;

/**
 * Created by Elvira on 18.12.16.
 */
public class InformationProvider {
    private Map<String, Character> allPlayers;
    private List<Character> activePlayers;

    public InformationProvider() {
        allPlayers = Collections.synchronizedMap(new HashMap<String, Character>());
        activePlayers = Collections.synchronizedList(new LinkedList<Character>());
    }

    public Character getCharacter(String token) {
        return allPlayers.get(token);
    }

    public List<Character> getAllActiveCharacters() {
        return activePlayers;
    }

    public void addCharacter(Character character, String token) {
        allPlayers.put(token, character);
    }

    public void addToActive(Character character) {
        activePlayers.add(character);
    }

    public void removeFromActive(String login) {
        for (int i = 0; i < activePlayers.size(); i++) {
            if (activePlayers.get(i).login.equals(login)) {
                activePlayers.remove(i);
            }
        }
    }

    public void removeFromAllPlayers(String s) {
        Set<Map.Entry<String, Character>> entries = allPlayers.entrySet();
        for (Map.Entry<String, Character> k :
                entries) {
            if(k.getValue().login.equals(s)){
                allPlayers.remove(k.getKey());
                return;
            }
        }
    }

    public int getActivePlayersCount() {
        return activePlayers.size();
    }

    public void upKiller(String login) {
        Character character;
        Set<Map.Entry<String, Character>> entries = allPlayers.entrySet();
        for (Map.Entry<String, Character> k :
                entries) {
            if(k.getValue().login.equals(login)){
                character = allPlayers.get(k.getKey());
                character.health += 20;
                character.arsenal.shotgunBulletCount += 5;
                character.arsenal.rifleBulletCount += 10;
                return;
            }
        }
    }

    public boolean isLoginExist(String login){
        Character character = new Character();
        character.login = login;
        return allPlayers.containsValue(character);
    }

    public boolean isUserPlaying(String login){
        Character character = new Character();
        character.login = login;
        return activePlayers.contains(character);
    }
}
