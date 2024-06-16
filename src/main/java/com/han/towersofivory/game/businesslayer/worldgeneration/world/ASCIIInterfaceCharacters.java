package com.han.towersofivory.game.businesslayer.worldgeneration.world;

/**
 * ASCIIInterfaceCharacters is an enumeration of the different characters used in the ASCII interface of the dungeon.
 * Each character represents a different element of the dungeon, such as a room, corridor, door, world border, player, monster, stair, or chest.
 * Each enum value can hold multiple characters, and methods are provided to retrieve a specific character or all characters.
 */
public enum ASCIIInterfaceCharacters {
    ROOM('.'),
    ROOM_BORDER('╔', '╗', '╚', '╝', '═', '║'),
    ROOM_CORNER('╔', '╗', '╚', '╝'),
    SPACE(' '),
    CORRIDOR('░'),
    DOOR('-', '¦'),
    PLAYER('©'),
    OTHERPLAYER('P'),
    MONSTER('¤'),
    STAIRS('^', 'v'),
    CHEST('Ω'),
    UNKNOWN('?'),
    GOLD_PLATING('₲'),
    ABSENCE('∅'),
    C4_MODEL('₵'),
    ITEM(GOLD_PLATING.getCharacter(), ABSENCE.getCharacter(), C4_MODEL.getCharacter(), CHEST.getCharacter()),
    OBJECTS(OTHERPLAYER.getCharacter(), MONSTER.getCharacter(), GOLD_PLATING.getCharacter(), ABSENCE.getCharacter(), C4_MODEL.getCharacter(), CHEST.getCharacter());

    private final char[] characters;

    /**
     * Constructor for ASCIIInterfaceCharacters.
     *
     * @param characters The characters that represent this element in the ASCII interface.
     */
    ASCIIInterfaceCharacters(char... characters) {
        this.characters = characters;
    }

    /**
     * Gets all characters that represent walkable tiles in the ASCII interface.
     *
     * @return The characters.
     */
    public static char[] getWalkableCharacters() {
        String walkableCharactersBuilder =
                String.valueOf(ROOM.getCharacters()) +
                        String.valueOf(CORRIDOR.getCharacters()) +
                        String.valueOf(DOOR.getCharacters());

        return walkableCharactersBuilder.toCharArray();
    }

    /**
     * Gets all characters that represent unwalkable tiles in the ASCII interface.
     *
     * @return The characters.
     */
    public static char[] getUnwalkableCharacters() {
        String unwalkableCharactersBuilder =
                String.valueOf(ROOM_BORDER.getCharacters()) +
                        String.valueOf(ROOM_CORNER.getCharacters()) +
                        String.valueOf(SPACE.getCharacters()) +
                        String.valueOf(PLAYER.getCharacters()) +
                        String.valueOf(MONSTER.getCharacters()) +
                        String.valueOf(STAIRS.getCharacters()) +
                        String.valueOf(CHEST.getCharacters())+
                        String.valueOf(GOLD_PLATING.getCharacters())+
                        String.valueOf(ABSENCE.getCharacters())+
                        String.valueOf(C4_MODEL.getCharacters());

        return unwalkableCharactersBuilder.toCharArray();
    }

    /**
     * Gets the first character that represents this element in the ASCII interface.
     *
     * @return The character.
     */
    public char getCharacter() {
        return characters[0];
    }

    /**
     * Gets a specific character that represents this element in the ASCII interface.
     *
     * @param index The index of the character to retrieve.
     * @return The character.
     */
    public char getCharacter(int index) {
        return characters[index];
    }

    /**
     * Gets all characters that represent this element in the ASCII interface.
     *
     * @return The characters.
     */
    public char[] getCharacters() {
        return characters;
    }

    /**
     * Gets all characters that represent interactables in the ASCII interface.
     *
     * @return The characters.
     */
    public static char[] getInteractableCharacters() {
        String interactableCharactersBuilder =
                String.valueOf(PLAYER.getCharacters()) +
                String.valueOf(OTHERPLAYER.getCharacters()) +
                String.valueOf(MONSTER.getCharacters()) +
                String.valueOf(STAIRS.getCharacters()) +
                String.valueOf(CHEST.getCharacters()) +
                String.valueOf(GOLD_PLATING.getCharacters())+
                String.valueOf(ABSENCE.getCharacters())+
                String.valueOf(C4_MODEL.getCharacters());

        return interactableCharactersBuilder.toCharArray();
    }
}
