package dev.myriadclient.boze;

import dev.boze.api.addon.AddonModule;
import dev.myriadclient.api.module.Tags;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BozeCategoriesTest {
	static final class Named extends AddonModule {
		Named(String name) {
			super(name, "");
		}
	}

	@Test
	void knownNamesLandInSharedTags() {
		assertEquals(Tags.COMBAT, BozeCategories.of(new Named("AutoCrystal")));
		assertEquals(Tags.MOVEMENT, BozeCategories.of(new Named("ElytraFly")));
		assertEquals(Tags.RENDER, BozeCategories.of(new Named("Hole ESP")));
		assertEquals(Tags.PLAYER, BozeCategories.of(new Named("auto_tool")));
	}

	@Test
	void unknownNamesFallBack() {
		// The fallback tag is registered by the addon at startup; here nothing is, so null stands for it.
		assertNull(BozeCategories.of(new Named("SomethingNew")));
	}
}
