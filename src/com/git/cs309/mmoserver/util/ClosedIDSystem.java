package com.git.cs309.mmoserver.util;

import com.git.cs309.mmoserver.Config;

/**
 * 
 * @author Clownvin
 * 
 *         <p>
 *         I'm actually pretty proud of this one. This is the ClosedIDSystem. It
 *         houses a finite number of IDTags, which can be distributed for use.
 *         The purpose behind this is to ensure completely that no two entities
 *         ever share the same unique ID, therefore making differentiation much
 *         easier. No other class can create more IDTags, ensuring the pool of
 *         legitimate tags isn't polluted with false tags.
 *         </p>
 *         <p>
 *         There is only one method this static utility provides, and that is
 *         getTag. Once a tag is finished being used, ie the entity it's
 *         attached to is removed, the IDTag MUST be returned into the system by
 *         calling returnTag on the IDTag itself. The ONLY way to mess up this
 *         system is if the tags aren't returned, so make sure they are. I even
 *         wrote a custom data type to house the tags so that removal doesn't
 *         take O(n) time, where n is the number of items in queue.
 * 
 */
public final class ClosedIDSystem {

	/**
	 * 
	 * @author Clownvin
	 *
	 *         IDTag used by the ClosedIDSystem. Only the ClosedIDSystem can
	 *         instantiate this type, to prevent list pollution and ensure all
	 *         tags are unique when created.
	 */
	public static final class IDTag {
		private final int id;
		private volatile boolean inUse = false;

		private IDTag(final int id) {
			this.id = id;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof IDTag && ((IDTag) other).id == id;
		}

		public int getID() {
			assert (inUse); // If this tag is not in use, then there is a problem. This means an object is still using this tag after returning it into system, which is a big no no.
			return id;
		}

		public void returnTag() {
			ClosedIDSystem.returnTag(this);
		}
	}

	private static final CycleQueue<IDTag> TAG_STACK = new CycleQueue<>(Config.MAX_ENTITIES); // List of all the IDTags ever.

	static { // Static block populates list with IDTags, each unique.
		for (int i = 0; i < Config.MAX_ENTITIES; i++) {
			TAG_STACK.add(new IDTag(i));
		}
	}

	/**
	 * Retrieves an IDTag from the front of queue.
	 * 
	 * @return "new" IDTag
	 */
	public static IDTag getTag() {
		IDTag popped = TAG_STACK.remove();
		popped.inUse = true;
		return popped;
	}

	/**
	 * Returns a tag to the queue. Can only be called by the IDTags themselves.
	 * 
	 * @param tag
	 *            tag to return.
	 */
	private static void returnTag(final IDTag tag) {
		assert (!TAG_STACK.contains(tag)); // Cannot contain two copies of the same ID, as that breaks the point of this class.
		tag.inUse = false; // set inUse flag to false.
		TAG_STACK.add(tag);
	}

	private ClosedIDSystem() {
		assert false;// Static util class, doesn't need to be instantiated.
	}
}
