package com.hungnguyen.qrcodescanner.utility;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Util {
	public static int index = 0;
	public static List<isSwipe> listSwipe;
	final static Set<String> protocols, protocolsWithHost;

	static {
		protocolsWithHost = new HashSet<String>(Arrays.asList(new String[] {
				"file", "ftp", "http", "https" }));
		protocols = new HashSet<String>(Arrays.asList(new String[] { "mailto",
				"news", "urn" }));
		protocols.addAll(protocolsWithHost);
	}

	public static boolean isURL(String str) {
		if (check(str))
			return true;
		String s = "http://" + str;
		if (check(str))
			return true;
		return false;
	}

	public static boolean check(String str) {
		int colon = str.indexOf(':');
		if (colon < 3)
			return false;
		String proto = str.substring(0, colon).toLowerCase();
		if (!protocols.contains(proto))
			return false;
		try {
			URI uri = new URI(str);
			if (protocolsWithHost.contains(proto)) {
				if (uri.getHost() == null)
					return false;
				String path = uri.getPath();
				if (path != null) {
					for (int i = path.length() - 1; i >= 0; i--) {
						if ("?<>:*|\"".indexOf(path.charAt(i)) > -1)
							return false;
					}
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static class isSwipe {
		public boolean isLeft;
		public boolean isRight;
		public isSwipe(boolean left,boolean right) {
			this.isLeft = left;
			this.isRight = right;
		}
		public isSwipe() {
			this.isLeft = false;
			this.isRight = false;
		}
	}
}
