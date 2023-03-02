package dev.dfonline.codeclient.dev.DevInventory;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DevInventoryGroup {

    private final int index;
    private final String id;
    private final ItemStack icon;
    private final Text name;
    private final boolean topHalf;
    private boolean hasSearchBar = true;
    private ItemsProvider itemsProvider = null;

    private static int global = 0;
    public DevInventoryGroup(String id, String name, ItemStack icon, boolean topHalf) {
        this.index = global;
        this.id = id;
        this.icon = icon;
        this.name = Text.of(name);
        this.topHalf = topHalf;
        if(!topHalf && TOP_HALF == -1) TOP_HALF = global + 1;


        GROUPS[index] = this;
        global++;
    }
    private DevInventoryGroup disableSearch() {
        this.hasSearchBar = false;
        return this;
    }
    private DevInventoryGroup setItemsProvider(ItemsProvider itemsProvider) {
        this.itemsProvider = itemsProvider;
        return this;
    }

    public List<ItemStack> searchItems(String query) {
        if(this.itemsProvider == null) return null;
        return this.itemsProvider.run(query);
    }
    public Text getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public boolean hasSearchBar() {
        return hasSearchBar;
    }

    public boolean isTopHalf() {
        return this.topHalf;
    }
    public int getPlace() {
        return this.index - (isTopHalf() ? 0 : TOP_HALF - 1);
    }

    public int getColumn() {
        return getPlace() % 7;
    }
    public int getRow() { return getPlace() / 7; }



    public int getDisplayX(int originX) {
        return originX + getColumn() * TAB_WIDTH;
    }
    public int getDisplayY(int originY, int menuHeight) {
        int multiplier = TAB_HEIGHT * (isTopHalf() ? -1 : 1);
        int y = (getRow() * multiplier) + originY + (isTopHalf() ? (TAB_HEIGHT * -1 - 4) : menuHeight);
        return y;
    }

    public static final int TAB_WIDTH = 28;
    public static final int TAB_HEIGHT = 28;
    

    public static int TOP_HALF = -1;
    public static final DevInventoryGroup[] GROUPS = new DevInventoryGroup[21];

    public static final DevInventoryGroup PLAYER_EVENT = new DevInventoryGroup("event", "Player Event", Items.DIAMOND_BLOCK.getDefaultStack(), true);
    public static final DevInventoryGroup PLAYER_IF = new DevInventoryGroup("if_player", "If Player", Items.OAK_PLANKS.getDefaultStack(), true);
    public static final DevInventoryGroup PLAYER_ACTION = new DevInventoryGroup("player_action", "Player Action", Items.COBBLESTONE.getDefaultStack(), true);
    public static final DevInventoryGroup ENTITY_EVENT = new DevInventoryGroup("entity_event", "Entity Event", Items.GOLD_BLOCK.getDefaultStack(), true);
    public static final DevInventoryGroup ENTITY_IF = new DevInventoryGroup("if_entity", "If Entity", Items.BRICKS.getDefaultStack(), true);
    public static final DevInventoryGroup ENTITY_ACTION = new DevInventoryGroup("game_action", "Entity Action", Items.MOSSY_COBBLESTONE.getDefaultStack(), true);
    public static final DevInventoryGroup SEARCH = new DevInventoryGroup("search", "Search All", Items.COMPASS.getDefaultStack(), true);
    public static final DevInventoryGroup VAR_SET = new DevInventoryGroup("set_var", "Set Variable", Items.IRON_BLOCK.getDefaultStack(), true);
    public static final DevInventoryGroup VAR_IF = new DevInventoryGroup("if_var", "If Variable", Items.OBSIDIAN.getDefaultStack(), true);
    public static final DevInventoryGroup GAME_ACTION = new DevInventoryGroup("game_action", "Game Action", Items.NETHERRACK.getDefaultStack(), true);
    public static final DevInventoryGroup GAME_IF = new DevInventoryGroup("if_game", "If Game", Items.RED_NETHER_BRICKS.getDefaultStack(), true);
    public static final DevInventoryGroup CONTROL = new DevInventoryGroup("control", "Control", Items.COAL_BLOCK.getDefaultStack(), true);
    public static final DevInventoryGroup SELECT_OBJECT = new DevInventoryGroup("select_obj", "Select Object", Items.PURPUR_BLOCK.getDefaultStack(), true);
    public static final DevInventoryGroup REPEAT = new DevInventoryGroup("repeat", "Repeat", Items.PRISMARINE.getDefaultStack(), true);
    public static final DevInventoryGroup SOUNDS = new DevInventoryGroup("sound", "Sounds", Items.NAUTILUS_SHELL.getDefaultStack(), false);
    public static final DevInventoryGroup PARTICLES = new DevInventoryGroup("particle", "Particles", Items.WHITE_DYE.getDefaultStack(), false);
    public static final DevInventoryGroup POTIONS = new DevInventoryGroup("potion", "Potions", Items.DRAGON_BREATH.getDefaultStack(), false);
    public static final DevInventoryGroup GAME_VALUES = new DevInventoryGroup("game_value", "Game Values", Items.NAME_TAG.getDefaultStack(), false);
    public static final DevInventoryGroup OTHERS = new DevInventoryGroup("others", "Other Items", Items.IRON_INGOT.getDefaultStack(), false).disableSearch();
    public static final DevInventoryGroup CODE_VAULT = new DevInventoryGroup("code_vault", "Code Vault", Items.ENDER_CHEST.getDefaultStack(), false);
    public static final DevInventoryGroup INVENTORY = new DevInventoryGroup("inventory", "Inventory", Items.CHEST.getDefaultStack(), false).disableSearch();

    interface ItemsProvider {
        List<ItemStack> run(String query);
    }

    private static ArrayList<ItemStack> emptyMenu() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            items.add(Items.AIR.getDefaultStack());
        }
        return items;
    }

    static {
        OTHERS.setItemsProvider(query -> {
            ArrayList<ItemStack> items = emptyMenu();
            try {
                items.set(0,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:book\",tag:{CustomModelData:5000,PublicBukkitValues:{\"hypercube:varitem\":'{\"id\":\"txt\",\"data\":{\"name\":\"text\"}}'},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"A value item used for \"},{\"italic\":false,\"color\":\"aqua\",\"text\":\"text\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"parameters.\"}],\"text\":\"\"}','{\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"light_purple\",\"text\":\"How to set:\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Type the text in chat while\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"holding this item.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"E.g. \\'\"},{\"italic\":false,\"color\":\"white\",\"text\":\"Sample Text\"},{\"italic\":false,\"color\":\"gray\",\"text\":\"\\' or \\'\"},{\"italic\":false,\"color\":\"white\",\"text\":\"&cRed\"},{\"italic\":false,\"color\":\"gray\",\"text\":\"\\'\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"aqua\",\"text\":\"Text\"}],\"text\":\"\"}'}}}")));
                items.set(1,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:nautilus_shell\",tag:{CustomModelData:5000,PublicBukkitValues:{\"hypercube:varitem\":'{\"id\":\"snd\",\"data\":{\"sound\":\"Pling\",\"pitch\":1.0,\"vol\":2.0}}'},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"A value item used for \"},{\"italic\":false,\"color\":\"blue\",\"text\":\"sound\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"parameters.\"}],\"text\":\"\"}','{\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"light_purple\",\"text\":\"How to set:\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Right-click this item to select\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"a sound effect.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Shift right-click to select\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"a variant of the sound.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"To add a pitch & volume, type\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"them in one chat line, in order.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Left-click to preview it.\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"blue\",\"text\":\"Sound\"}],\"text\":\"\"}'}}}")));
                items.set(2,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:prismarine_shard\",tag:{CustomModelData:5000,PublicBukkitValues:{\"hypercube:varitem\":'{\"id\":\"vec\",\"data\":{\"x\":0.0,\"y\":0.0,\"z\":0.0}}'},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"A value item used for \"},{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"#2AFFAA\",\"text\":\"vector\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"parameters.\"}],\"text\":\"\"}','{\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"light_purple\",\"text\":\"How to set:\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Use the /vector command or\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"type \"},{\"italic\":false,\"color\":\"aqua\",\"text\":\"\\\\\"<x> <y> <z>\\\\\"\"},{\"italic\":false,\"color\":\"gray\",\"text\":\" to set the\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"components. Left-click to\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"preview it.\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"#2AFFAA\",\"text\":\"Vector\"}],\"text\":\"\"}'}}}")));
                items.set(3,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:paper\",tag:{CustomModelData:5000,PublicBukkitValues:{\"hypercube:varitem\":'{\"id\":\"loc\",\"data\":{\"isBlock\":false,\"loc\":{\"x\":0.0,\"y\":0.0,\"z\":0.0,\"pitch\":0.0,\"yaw\":0.0}}}'},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"A value item used for \"},{\"italic\":false,\"color\":\"green\",\"text\":\"location\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"parameters.\"}],\"text\":\"\"}','{\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"light_purple\",\"text\":\"How to set:\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Right-click this item to set it\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"to your current location.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Left-click to set it to a block.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Shift left-click to teleport to it.\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"green\",\"text\":\"Location\"}],\"text\":\"\"}'}}}")));
                items.set(4,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:slime_ball\",tag:{CustomModelData:5000,PublicBukkitValues:{\"hypercube:varitem\":'{\"id\":\"num\",\"data\":{\"name\":\"0\"}}'},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"A value item used for \"},{\"italic\":false,\"color\":\"red\",\"text\":\"number\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"parameters.\"}],\"text\":\"\"}','{\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"light_purple\",\"text\":\"How to set:\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Type a number in chat while\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"holding this item.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"E.g. \"},{\"italic\":false,\"color\":\"white\",\"text\":\"4\"},{\"italic\":false,\"color\":\"gray\",\"text\":\" or \"},{\"italic\":false,\"color\":\"white\",\"text\":\"1.25\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"red\",\"text\":\"Number\"}],\"text\":\"\"}'}}}")));
                items.set(5,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:white_dye\",tag:{CustomModelData:5000,PublicBukkitValues:{\"hypercube:varitem\":'{\"id\":\"part\",\"data\":{\"particle\":\"Cloud\",\"cluster\":{\"amount\":1,\"horizontal\":0.0,\"vertical\":0.0},\"data\":{\"x\":1.0,\"y\":0.0,\"z\":0.0,\"motionVariation\":100}}}'},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"A value item used for \"},{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"#AA55FF\",\"text\":\"particle\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"parameters.\"}],\"text\":\"\"}','{\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"light_purple\",\"text\":\"How to set:\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Right-click this item to select\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"a particle effect.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Use the /particle command\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"to change its parameters.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Left-click to preview it.\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"#AA55FF\",\"text\":\"Particle\"}],\"text\":\"\"}'}}}")));
                items.set(6,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:dragon_breath\",tag:{CustomModelData:5000,PublicBukkitValues:{\"hypercube:varitem\":'{\"id\":\"pot\",\"data\":{\"pot\":\"Speed\",\"dur\":1000000,\"amp\":0}}'},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"A value item used for \"},{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"#FF557F\",\"text\":\"potion\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"effect parameters.\"}],\"text\":\"\"}','{\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"light_purple\",\"text\":\"How to set:\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Right-click this item to select\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"a potion effect.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Use the /potion command or\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"type \"},{\"italic\":false,\"color\":\"aqua\",\"text\":\"\\\\\"<amplifier> [duration]\\\\\"\"},{\"italic\":false,\"color\":\"gray\",\"text\":\"\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"to set its options. Left-click\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"to preview it.\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"#FF557F\",\"text\":\"Potion Effect\"}],\"text\":\"\"}'}}}")));

                items.set(9,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:magma_cream\",tag:{CustomModelData:5000,PublicBukkitValues:{\"hypercube:varitem\":'{\"id\":\"var\",\"data\":{\"name\":\"&eVariable\",\"scope\":\"unsaved\"}}'},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"A value item name that refers\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"to an internally stored value.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"The value can be set using the\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"white\",\"text\":\"Set Variable\"},{\"italic\":false,\"color\":\"gray\",\"text\":\" block.\"}],\"text\":\"\"}','{\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"light_purple\",\"text\":\"How to set:\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Type a variable name in chat\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"while holding this item.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Shift right-click for options.\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"yellow\",\"text\":\"Variable\"}],\"text\":\"\"}'}}}")));
                items.set(10,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:name_tag\",tag:{CustomModelData:5000,PublicBukkitValues:{\"hypercube:varitem\":'{\"id\":\"g_val\",\"data\":{\"type\":\"Location\",\"target\":\"Default\"}}'},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"An automatically set value\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"which is set based on the\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"game\\'s current conditions\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"(e.g. a player\\'s location).\"}],\"text\":\"\"}','{\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"light_purple\",\"text\":\"How to set:\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Right-click to select a game\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"value and its target.\"}],\"text\":\"\"}'],Name:'{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"#FFD47F\",\"text\":\"Game Value\"}],\"text\":\"\"}'}}}")));

                items.set(8,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:written_book\",tag:{CustomModelData:0,HideFlags:127,PublicBukkitValues:{\"hypercube:item_instance\":\"18726d32-57ae-4305-b317-2b084283e704\"},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Right-click to open the\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"reference menu.\"}],\"text\":\"\"}'],Name:'{\"italic\":false,\"extra\":[{\"color\":\"gold\",\"text\":\"◆ \"},{\"color\":\"aqua\",\"text\":\"Reference Book \"},{\"color\":\"gold\",\"text\":\"◆\"}],\"text\":\"\"}'}}}")));
                items.set(17,ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:blaze_rod\",tag:{CustomModelData:0,HideFlags:127,PublicBukkitValues:{\"hypercube:item_instance\":\"de7f011e-8248-40e6-b04f-8315c4c7be4a\"},display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Shows two corresponding brackets\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"for a short amount of time.\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Right-Click a bracket to highlight\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"both.\"}],\"text\":\"\"}'],Name:'{\"italic\":false,\"color\":\"gold\",\"text\":\"Bracket Finder\"}'}}}")));
                items.set(26, ItemStack.fromNbt(NbtHelper.fromNbtProviderString("{Count:1b,id:\"minecraft:arrow\",tag:{CustomModelData:0,HideFlags:127,display:{Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"Click on a Condition block with this\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"to switch between \\'IF\\' and \\'IF NOT\\'.\"}],\"text\":\"\"}'],Name:'{\"italic\":false,\"color\":\"red\",\"text\":\"NOT Arrow\"}'}}}")));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return items;
        });

        PLAYER_EVENT.setItemsProvider(query -> {
            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < 90; i++) {
                items.add(Items.DIAMOND.getDefaultStack());
                items.add(Items.BLAZE_POWDER.getDefaultStack());
            }
            return items;
        });
    }
}