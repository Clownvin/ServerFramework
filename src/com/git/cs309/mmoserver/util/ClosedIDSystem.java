package com.git.cs309.mmoserver.util;

import com.git.cs309.mmoserver.Config;

public final class ClosedIDSystem {
	
	public static final class IDTag {
		private final int id;
		private volatile boolean inUse = false;
		
		private IDTag(final int id) {
			this.id = id;
		}
		
		public int getID() {
			if (!inUse) {
				throw new RuntimeException("Object disobeyed IDTag contract.");
			}
			return id;
		}
		
		public void returnTag() {
			ClosedIDSystem.returnTag(this);
		}
		
		@Override
		public boolean equals(Object other) {
			return other instanceof IDTag && ((IDTag) other).id == id;
		}
	}
	
	private static final CycleArrayList<IDTag> TAG_STACK = new CycleArrayList<>(Config.MAX_ENTITIES);
	
	static {
		for (int i = 0; i < Config.MAX_ENTITIES; i++) {
			TAG_STACK.add(new IDTag(i));
		}
	}
	
	private ClosedIDSystem() {
		// Static util class, doesn't need to be instantiated.
	}
	
	public static IDTag getTag() {
		IDTag popped = TAG_STACK.remove();
		popped.inUse = true;
		return popped;
	}
	
	private static void returnTag(final IDTag tag) {
		assert(!TAG_STACK.contains(tag));
		tag.inUse = false;
		TAG_STACK.add(tag);
	}
}
