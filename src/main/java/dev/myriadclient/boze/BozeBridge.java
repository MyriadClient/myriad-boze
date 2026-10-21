package dev.myriadclient.boze;

import dev.boze.api.BozeInstance;
import dev.boze.api.addon.Addon;
import dev.boze.api.client.FriendManager;
import dev.boze.api.client.ModuleManager;
import dev.boze.api.client.module.BaseModule;
import dev.boze.api.internal.Instances;
import dev.myriadclient.api.Myriad;
import dev.myriadclient.api.addon.AddonContext;
import dev.myriadclient.api.module.ModuleGroup;
import dev.myriadclient.api.module.Tag;
import dev.myriadclient.api.module.Tags;
import dev.myriadclient.api.util.MyriadId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Finds Boze's modules and keeps their Myriad mirrors in step. Runs once per tick from {@link BozeAddon}:
 * <ul>
 *   <li>every tick, each mirror follows its Boze module's on/off state (a toggle from Boze's GUI or keybind shows in
 *       Myriad the same tick);</li>
 *   <li>every half second, settings and binds are read back from Boze, so edits made in Boze's own GUI or a profile it
 *       loaded appear in Myriad;</li>
 *   <li>every second, modules Boze gained since (its addons load after it) are bridged too, and the two friend
 *       lists are reconciled ({@link FriendSync}).</li>
 * </ul>
 * Reads are plain field getters, a few thousand per second at most, so this costs nothing measurable; Boze has no
 * change events for settings, so polling is the only way to notice its side.
 */
final class BozeBridge {
	private final AddonContext ctx;
	/** Boze module to its mirror; null for one that couldn't be bridged, so it isn't retried every second. */
	private final Map<BaseModule, BozeModule> bridged = new IdentityHashMap<>();
	private final List<BozeModule> mirrors = new ArrayList<>();
	/** A group for each of Boze's categories (as {@link BozeCategories} reads them), by name. */
	private final Map<String, ModuleGroup> groups = new HashMap<>();
	private final FriendSync friends = new FriendSync(
		new FriendSync.Side(() -> Myriad.friends().all(), n -> Myriad.friends().add(n), n -> Myriad.friends().remove(n)),
		new FriendSync.Side(FriendManager::getFriends, FriendManager::addFriend, FriendManager::delFriend));
	private int tick;
	private boolean announced;

	BozeBridge(AddonContext ctx) {
		this.ctx = ctx;
		// The shared ones first, in Boze's order, so the windows come in that order whatever loads first.
		for (Tag t : List.of(Tags.COMBAT, Tags.MOVEMENT, Tags.RENDER, Tags.PLAYER, Tags.WORLD, Tags.MISC, BozeAddon.fallbackTag())) group(t.name(), t.icon());
		discover();
		syncFriends();
	}

	/** Whether Boze has started and filled its module registry. */
	static boolean ready() {
		try {
			return Instances.getModules() != null && !ModuleManager.getClientModules().isEmpty();
		} catch (RuntimeException | LinkageError e) {
			return false;
		}
	}

	void tick() {
		tick++;
		for (BozeModule m : mirrors) m.pullState();
		if (tick % 10 == 0) for (BozeModule m : mirrors) m.pullSettings();
		if (tick % 20 == 0) {
			discover();
			syncFriends();
		}
	}

	private void syncFriends() {
		try {
			friends.sync();
		} catch (RuntimeException e) {
			ctx.logger().warn("Could not sync friends with Boze: {}", e.toString());
		}
	}

	private void discover() {
		List<BaseModule> all = new ArrayList<>(ModuleManager.getClientModules());
		all.addAll(ModuleManager.getAddonModules());
		int added = 0;
		for (BaseModule boze : all) {
			if (bridged.containsKey(boze)) continue;
			BozeModule mirror = null;
			try {
				String category = addonCategory(boze);
				Tag tag = category == null ? BozeCategories.of(boze) : tagOf(category);
				mirror = ctx.registerModule(group(category == null ? tag.name() : category, tag.icon()), new BozeModule(boze, tag));
				added++;
			} catch (RuntimeException e) {
				ctx.logger().warn("Could not bridge Boze module {}: {}", boze.getName(), e.toString());
			}
			bridged.put(boze, mirror);
			if (mirror != null) mirrors.add(mirror);
		}
		if (added == 0) return;
		ctx.logger().info("Bridged {} Boze modules ({} in all)", added, mirrors.size());
		if (!announced) {
			announced = true;
			Myriad.notifications().success("Boze", mirrors.size() + " Boze modules are in the menu");
		}
	}

	/** The group for Boze's category {@code name}: one window in the menu, as Boze has one in its GUI. */
	private ModuleGroup group(String name, String icon) {
		return groups.computeIfAbsent(name.toLowerCase(Locale.ROOT), n -> Myriad.groups().get(ctx.id(MyriadId.toPath(name)))
			.orElseGet(() -> ctx.registerGroup(name, icon)));
	}

	/** The category a module of one of Boze's own addons is in, if that addon declared one; null for anything else. */
	private static String addonCategory(BaseModule boze) {
		for (Addon addon : BozeInstance.INSTANCE.getAddons()) {
			if (!addon.modules.contains(boze)) continue;
			String name = addon.getCategory();
			return name == null || name.isBlank() ? null : name;
		}
		return null;
	}

	/** The tag for a Boze addon's category: the shared one it names, or the Boze tag. */
	private static Tag tagOf(String category) {
		Tag shared = BozeCategories.byName(category);
		return shared != null ? shared : BozeAddon.fallbackTag();
	}
}
