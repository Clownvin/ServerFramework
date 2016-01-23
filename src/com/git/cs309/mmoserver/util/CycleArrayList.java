package com.git.cs309.mmoserver.util;

public final class CycleArrayList<T> {
	private final Object[] array;
	private volatile int takeIndex = 0;
	private volatile int placeIndex = 0;
	private volatile int size = 0;

	public CycleArrayList(final int totalAmount) {
		array = new Object[totalAmount];
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean contains(Object o) {
		synchronized (array) {
			for (Object obj : array) {
				if (obj.equals(o)) {
					return true;
				}
			}
		}
		return false;
	}

	public Object[] toArray() {
		synchronized (array) {
			Object[] object  = new Object[size];
			for (int i = takeIndex, j = 0; j < size; i++, j++) {
				i %= array.length;
				object[j] = array[i];
			}
			return object;
		}
	}

	@SuppressWarnings({ "hiding", "unchecked" }) // Safe to assume that array will ONLY contain types implementing T
	public <T> T[] toArray(T[] a) {
		Object[] objArray = toArray();
		if (a.length > objArray.length) {
			for (int i = 0; i < objArray.length; i++) {
				a[i] = (T) objArray[i];
			}
		} else {
			for (int i = 0; i < a.length; i++) {
				a[i] = (T) objArray[i];
			}
		}
		return a;
	}

	public boolean add(T e) {
		ensureCorners();
		synchronized (array) {
			if (size == array.length) {
				return false;
			}
			array[placeIndex++] = e;
			++size;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public T remove() {
		ensureCorners();
		synchronized (array) {
			if (size == 0) {
				throw new ArrayIndexOutOfBoundsException("Array is empty, so there is nothing to remove.");
			}
			T returnVal = (T) array[takeIndex++];
			--size;
			return returnVal;
		}
	}
	
	private void ensureCorners() {
		takeIndex %= array.length;
		placeIndex %= array.length;
	}

	public void clear() {
		synchronized (array) {
			for (int i = 0; i < array.length; i++) {
				array[i] = null;
			}
		}
		size = 0;
		takeIndex = 0;
		placeIndex = 0;
	}

	@SuppressWarnings("unchecked")
	public T get(int index) {
		index = (index + takeIndex) % array.length;
		return (T) array[index];
	}
}