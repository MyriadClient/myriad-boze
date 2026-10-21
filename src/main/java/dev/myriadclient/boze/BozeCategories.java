package dev.myriadclient.boze;

import dev.boze.api.client.module.BaseModule;
import dev.myriadclient.api.module.Tag;
import dev.myriadclient.api.module.Tags;

import java.util.Locale;
import java.util.Map;

/**
 * Which Myriad category a Boze module belongs in. Boze's API doesn't say which of its GUI categories a module is in,
 * so this guesses: first from the module class's package (a {@code ...combat...} package is a combat module), then
 * from a table of module names, and otherwise the Boze category, where everything unplaced is still easy to find.
 * A module in the wrong window is only a cosmetic miss; add it to {@link #NAMES} to fix it.
 */
final class BozeCategories {
	private BozeCategories() {
	}

	/** Package segments that name a category, normalised, to shared tags. */
	private static final Map<String, Tag> PACKAGES = Map.ofEntries(
		Map.entry("combat", Tags.COMBAT),
		Map.entry("ghost", Tags.COMBAT),
		Map.entry("movement", Tags.MOVEMENT),
		Map.entry("travel", Tags.MOVEMENT),
		Map.entry("render", Tags.RENDER),
		Map.entry("visual", Tags.RENDER),
		Map.entry("visuals", Tags.RENDER),
		Map.entry("player", Tags.PLAYER),
		Map.entry("world", Tags.WORLD),
		Map.entry("misc", Tags.MISC),
		Map.entry("miscellaneous", Tags.MISC),
		Map.entry("chat", Tags.MISC)
	);

	/**
	 * Boze category names (an addon's {@code createTag}) to shared categories. "Client" is one of Boze's GUI
	 * categories but also a package segment of everything Boze ({@code dev.boze.client...}), so it's only here.
	 */
	private static final Map<String, Tag> CATEGORY_NAMES = Map.of("client", Tags.MISC);

	/** Module names (normalised: lower case, no spaces) to shared tags. Boze's naming follows the usual client vocabulary. */
	private static final Map<String, Tag> NAMES = Map.ofEntries(
		// Combat
		entry("autocrystal", Tags.COMBAT), entry("autoanchor", Tags.COMBAT), entry("anchoraura", Tags.COMBAT),
		entry("aura", Tags.COMBAT), entry("killaura", Tags.COMBAT), entry("trigger", Tags.COMBAT),
		entry("triggerbot", Tags.COMBAT), entry("aimassist", Tags.COMBAT), entry("surround", Tags.COMBAT),
		entry("holefill", Tags.COMBAT), entry("selftrap", Tags.COMBAT), entry("autotrap", Tags.COMBAT),
		entry("burrow", Tags.COMBAT), entry("autoweb", Tags.COMBAT), entry("autototem", Tags.COMBAT),
		entry("offhand", Tags.COMBAT), entry("automine", Tags.COMBAT), entry("packetmine", Tags.COMBAT),
		entry("pearlphase", Tags.COMBAT), entry("autopearl", Tags.COMBAT), entry("autobed", Tags.COMBAT),
		entry("bedaura", Tags.COMBAT), entry("cevbreaker", Tags.COMBAT), entry("pistonaura", Tags.COMBAT),
		entry("pistoncrystal", Tags.COMBAT), entry("autoarmor", Tags.COMBAT), entry("antiregear", Tags.COMBAT),
		entry("quiver", Tags.COMBAT), entry("bowaimbot", Tags.COMBAT), entry("criticals", Tags.COMBAT),
		entry("autohitcrystal", Tags.COMBAT), entry("holesnap", Tags.COMBAT), entry("obsidian", Tags.COMBAT),
		entry("autocity", Tags.COMBAT), entry("cityesp", Tags.RENDER), entry("fastbow", Tags.COMBAT),
		entry("autoexp", Tags.COMBAT), entry("mace", Tags.COMBAT), entry("maceaura", Tags.COMBAT),
		entry("autoeat", Tags.PLAYER),
		// Movement
		entry("elytrafly", Tags.MOVEMENT), entry("elytraflight", Tags.MOVEMENT), entry("autopilot", Tags.MOVEMENT),
		entry("elytraautopilot", Tags.MOVEMENT), entry("speed", Tags.MOVEMENT), entry("sprint", Tags.MOVEMENT),
		entry("step", Tags.MOVEMENT), entry("nofall", Tags.MOVEMENT), entry("velocity", Tags.MOVEMENT),
		entry("noslow", Tags.MOVEMENT), entry("fly", Tags.MOVEMENT), entry("flight", Tags.MOVEMENT),
		entry("jesus", Tags.MOVEMENT), entry("strafe", Tags.MOVEMENT), entry("autowalk", Tags.MOVEMENT),
		entry("reversestep", Tags.MOVEMENT), entry("boatfly", Tags.MOVEMENT), entry("entitycontrol", Tags.MOVEMENT),
		entry("antihunger", Tags.MOVEMENT), entry("safewalk", Tags.MOVEMENT), entry("parkour", Tags.MOVEMENT),
		entry("spider", Tags.MOVEMENT), entry("longjump", Tags.MOVEMENT), entry("timer", Tags.MOVEMENT),
		entry("elytrarecast", Tags.MOVEMENT), entry("packetfly", Tags.MOVEMENT), entry("pearlrecall", Tags.MOVEMENT),
		entry("inventorymove", Tags.MOVEMENT), entry("inventorywalk", Tags.MOVEMENT),
		// Player
		entry("fastuse", Tags.PLAYER), entry("fastplace", Tags.PLAYER), entry("fastbreak", Tags.PLAYER),
		entry("autotool", Tags.PLAYER), entry("autoreplenish", Tags.PLAYER), entry("stackreplenish", Tags.PLAYER),
		entry("reach", Tags.PLAYER), entry("xcarry", Tags.PLAYER), entry("antiaim", Tags.PLAYER),
		entry("autorespawn", Tags.PLAYER), entry("autolog", Tags.PLAYER), entry("autodisconnect", Tags.PLAYER),
		entry("middleclick", Tags.PLAYER), entry("nointeract", Tags.PLAYER), entry("portals", Tags.PLAYER),
		entry("armorupgrader", Tags.PLAYER), entry("autoupgrade", Tags.PLAYER), entry("autosmith", Tags.PLAYER),
		entry("autofish", Tags.PLAYER), entry("autoshulker", Tags.PLAYER), entry("autoremount", Tags.PLAYER),
		entry("invmanager", Tags.PLAYER), entry("inventorymanager", Tags.PLAYER), entry("autosign", Tags.PLAYER),
		entry("antiafk", Tags.PLAYER), entry("blink", Tags.PLAYER), entry("ghosthand", Tags.PLAYER),
		entry("multitask", Tags.PLAYER), entry("nopush", Tags.PLAYER), entry("wallinteract", Tags.PLAYER),
		// Render
		entry("esp", Tags.RENDER), entry("tracers", Tags.RENDER), entry("nametags", Tags.RENDER),
		entry("fullbright", Tags.RENDER), entry("freecam", Tags.RENDER), entry("freelook", Tags.RENDER),
		entry("norender", Tags.RENDER), entry("xray", Tags.RENDER), entry("chams", Tags.RENDER),
		entry("holeesp", Tags.RENDER), entry("blockesp", Tags.RENDER), entry("storageesp", Tags.RENDER),
		entry("voidesp", Tags.RENDER), entry("burrowesp", Tags.RENDER), entry("shaders", Tags.RENDER),
		entry("viewmodel", Tags.RENDER), entry("zoom", Tags.RENDER), entry("crosshair", Tags.RENDER),
		entry("breakesp", Tags.RENDER), entry("blockhighlight", Tags.RENDER), entry("skybox", Tags.RENDER),
		entry("logoutspots", Tags.RENDER), entry("trajectories", Tags.RENDER), entry("itemesp", Tags.RENDER),
		entry("pearlesp", Tags.RENDER), entry("cameraclip", Tags.RENDER), entry("noweather", Tags.RENDER),
		entry("ambience", Tags.RENDER), entry("capes", Tags.RENDER), entry("tooltips", Tags.RENDER),
		entry("betterchat", Tags.MISC), entry("crystalchams", Tags.RENDER), entry("hitparticles", Tags.RENDER),
		entry("popchams", Tags.RENDER), entry("totempops", Tags.RENDER),
		// World
		entry("scaffold", Tags.WORLD), entry("airplace", Tags.WORLD), entry("nuker", Tags.WORLD),
		entry("autobuild", Tags.WORLD), entry("baritone", Tags.WORLD), entry("printer", Tags.WORLD),
		entry("highwaybuilder", Tags.WORLD), entry("tunneler", Tags.WORLD), entry("avoid", Tags.WORLD),
		entry("autoreplace", Tags.WORLD), entry("packetplace", Tags.WORLD), entry("liquidfiller", Tags.WORLD),
		// Misc
		entry("antikick", Tags.MISC), entry("discordrpc", Tags.MISC), entry("notifier", Tags.MISC),
		entry("notifications", Tags.MISC), entry("spammer", Tags.MISC), entry("autoreconnect", Tags.MISC),
		entry("friends", Tags.MISC), entry("betterportals", Tags.MISC), entry("packetlogger", Tags.MISC),
		entry("visualrange", Tags.MISC), entry("autoreply", Tags.MISC), entry("chatmanager", Tags.MISC),
		entry("fakeplayer", Tags.MISC), entry("antispam", Tags.MISC), entry("announcer", Tags.MISC)
	);

	private static Map.Entry<String, Tag> entry(String name, Tag tag) {
		return Map.entry(name, tag);
	}

	static Tag of(BaseModule module) {
		Tag fromPackage = fromPackage(module.getClass().getName());
		if (fromPackage != null) return fromPackage;
		Tag fromName = NAMES.get(normalize(module.getName()));
		return fromName != null ? fromName : BozeAddon.fallbackTag();
	}

	/** The shared tag a Boze category name stands for, or null for one of Boze's own ("Addons", "Hud"). */
	static Tag byName(String name) {
		String n = normalize(name);
		Tag c = PACKAGES.get(n);
		return c != null ? c : CATEGORY_NAMES.get(n);
	}

	/** The tag named by the innermost package segment that names one, if any. */
	static Tag fromPackage(String className) {
		String[] parts = className.toLowerCase(Locale.ROOT).split("\\.");
		for (int i = parts.length - 2; i >= 0; i--) {
			Tag c = PACKAGES.get(parts[i]);
			if (c != null) return c;
		}
		return null;
	}

	static String normalize(String s) {
		return s.toLowerCase(Locale.ROOT).replace(" ", "").replace("_", "").replace("-", "");
	}
}
