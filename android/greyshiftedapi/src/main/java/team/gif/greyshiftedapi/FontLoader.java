package team.gif.greyshiftedapi;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Copyright (C) 2017 Nathan Post. All rights reserved.
 * Created by Nathan Post on 9/3/2016.
 * This class belongs to the Squares project.
 */
public class FontLoader {

	public static final int INFINITY =   0;
	public static final int MANIFESTO =   1;

	private static final int FONT_COUNT = 2;//The number of fonts listed above

	private static boolean fontsLoaded = false;

	private static Typeface[] fonts = new Typeface[2];

	private static String[] fontPath = {
			"fonts/Infinity.ttf", //font 0
			"fonts/MANIFESTO.ttf" //font 1
	};


	/**
	 * Returns a loaded custom font based on it's identifier.
	 *
	 * @param context - the current context
	 * @param fontIdentifier = the identifier of the requested font
	 *
	 * @return Typeface object of the requested font.
	 */
	public static Typeface getTypeface(Context context, int fontIdentifier) {
		if (!fontsLoaded) {
			loadFonts(context);
		}
		return fonts[fontIdentifier];
	}


	private static void loadFonts(Context context) {
		for (int i = 0; i < FONT_COUNT; i++) {
			fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
		}
		fontsLoaded = true;

	}
}
