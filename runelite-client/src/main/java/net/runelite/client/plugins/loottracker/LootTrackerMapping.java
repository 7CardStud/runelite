/*
 * Copyright (c) 2020, Anthony <https://github.com/while-loop>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.loottracker;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;

@Getter
public enum LootTrackerMapping
{
	CLUE_SCROLL_BEGINNER("Clue scroll (beginner)", ItemID.CLUE_SCROLL_BEGINNER),
	CLUE_SCROLL_EASY("Clue scroll (easy)", ItemID.CLUE_SCROLL_EASY),
	CLUE_SCROLL_MEDIUM("Clue scroll (medium)", ItemID.CLUE_SCROLL_MEDIUM),
	CLUE_SCROLL_HARD("Clue scroll (hard)", ItemID.CLUE_SCROLL_HARD),
	CLUE_SCROLL_ELITE("Clue scroll (elite)", ItemID.CLUE_SCROLL_ELITE),
	CLUE_SCROLL_MASTER("Clue scroll (master)", ItemID.CLUE_SCROLL_MASTER);

	private final String name;
	private final int baseId;

	LootTrackerMapping(String name, int baseId)
	{
		this.name = name;
		this.baseId = baseId;
	}

	private static final ImmutableMap<String, LootTrackerMapping> MAPPINGS;

	static
	{
		ImmutableMap.Builder<String, LootTrackerMapping> map = ImmutableMap.builder();
		for (LootTrackerMapping mapping : values())
		{
			map.put(mapping.name, mapping);
		}
		MAPPINGS = map.build();
	}

	public static int map(int itemId, ItemManager itemManager)
	{
		ItemComposition itemComp = itemManager.getItemComposition(itemId);
		if (itemComp == null || Strings.isNullOrEmpty(itemComp.getName()))
		{
			return itemId;
		}

		if (!MAPPINGS.containsKey(itemComp.getName()))
		{
			return itemId;
		}

		return MAPPINGS.get(itemComp.getName()).baseId;
	}

	public static ItemStack map(ItemStack item, ItemManager itemManager)
	{
		int baseId = map(item.getId(), itemManager);
		if (baseId == item.getId())
		{
			return item;
		}

		return new ItemStack(baseId, item.getQuantity(), item.getLocation());
	}
}
