package com.velcro.base.util.uuid;


public final class UUIDUtils {

	private UUIDUtils() {
	}

	// ---------------------------------------------------------------------------

	public static final String randomUUID() {
		return randomUUID(false);
	}

	public static final String randomUUID(boolean longVersion) {
		//String s = UUID.randomUUID().toString().toLowerCase();
		//	return longVersion ? s : s.replaceAll("-", "");
		return UUIDHexGenerator.getUUID();
	}

}
