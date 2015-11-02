/*******************************************************************************
 * jMP3 v0.2.1 - 10.10.2006 (w/Eolas fix & jQuery object replacement) an MP3
 * Player jQuery Plugin (http://www.sean-o.com/jquery/jmp3) by Sean O
 * 
 * An easy way make any MP3 playable directly on most any web site (to those
 * using Flash & JS), using the sleek Flash Single MP3 Player & the fantabulous
 * jQuery.
 * 
 * SIMPLE USAGE Example: $(youridorclass).jMP3();
 * 
 * ADVANCED USAGE Example: $("#sounddl").jmp3({ showfilename: "false",
 * backcolor: "000000", forecolor: "00ff00", width: 200, showdownload: "false"
 * });
 * 
 * HTML: <span class="mp3">sound.mp3</span>
 * 
 * NOTE: filename must be enclosed in tag. Various file paths can be set using
 * the filepath option.
 * 
 * Copyright (c) 2006 Sean O (http://www.sean-o.com) Licensed under the MIT
 * License: http://www.opensource.org/licenses/mit-license.php
 * 
 ******************************************************************************/
jQuery.fn.jmp3 = function(passedOptions) {
	// hard-wired options
	var playerpath = "resources/js/jmp3/"; // SET THIS FIRST: path to
											// singlemp3player.swf

	// passable options
	var options = {
		"filepath" : "", // path to MP3 file (default: current directory)
		"backcolor" : "", // background color
		"forecolor" : "ffffff", // foreground color (buttons)
		"width" : "25", // width of player
		"repeat" : "no", // repeat mp3?
		"volume" : "50", // mp3 volume (0-100)
		"autoplay" : "false", // play immediately on page load?
		"showdownload" : "true", // show download button in player
		"showfilename" : "true" // show .mp3 filename after player
	};

	// use passed options, if they exist
	if (passedOptions) {
		jQuery.extend(options, passedOptions);
	}

	// iterate through each object
	return this
			.each(function() {
				// filename needs to be enclosed in tag (e.g. <span
				// class='mp3'>mysong.mp3</span>)
				var filename = options.filepath + jQuery(this).html();
				// do nothing if not an .mp3 file
				var validfilename = filename.indexOf(".mp3");
				if (validfilename == -1) {
					return false;
				}
				// build the player HTML
				var mp3html = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" ';
				mp3html += 'width="' + options.width + '" height="20" ';
				mp3html += 'codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab">';
				mp3html += '<param name="movie" value="' + playerpath
						+ 'singlemp3player.swf?';
				mp3html += 'showDownload=' + options.showdownload + '&file='
						+ filename + '&autoStart=' + options.autoplay;
				mp3html += '&backColor=' + options.backcolor + '&frontColor='
						+ options.forecolor;
				mp3html += '&repeatPlay=' + options.repeat + '&songVolume='
						+ options.volume + '" />';
				mp3html += '<param name="wmode" value="transparent" />';
				mp3html += '<embed wmode="transparent" width="' + options.width
						+ '" height="20" ';
				mp3html += 'src="' + playerpath + 'singlemp3player.swf?'
				mp3html += 'showDownload=' + options.showdownload + '&file='
						+ filename + '&autoStart=' + options.autoplay;
				mp3html += '&backColor=' + options.backcolor + '&frontColor='
						+ options.forecolor;
				mp3html += '&repeatPlay=' + options.repeat + '&songVolume='
						+ options.volume + '" ';
				mp3html += 'type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />';
				mp3html += '</object>';
				// don't display filename if option is set
				if (options.showfilename == "false") {
					jQuery(this).html("");
				}
				jQuery(this).prepend(mp3html + "&nbsp;");

				// Eolas workaround for IE (Thanks Kurt!)
				if (jQuery.browser.msie) {
					this.outerHTML = this.outerHTML;
				}
			});
};
//添加此方法，解决jQuery1.9之后不支持$.browser.msie
(function(jQuery) {

	if (jQuery.browser)
		return;

	jQuery.browser = {};
	jQuery.browser.mozilla = false;
	jQuery.browser.webkit = false;
	jQuery.browser.opera = false;
	jQuery.browser.msie = false;

	var nAgt = navigator.userAgent;
	jQuery.browser.name = navigator.appName;
	jQuery.browser.fullVersion = '' + parseFloat(navigator.appVersion);
	jQuery.browser.majorVersion = parseInt(navigator.appVersion, 10);
	var nameOffset, verOffset, ix;

	// In Opera, the true version is after "Opera" or after "Version"
	if ((verOffset = nAgt.indexOf("Opera")) != -1) {
		jQuery.browser.opera = true;
		jQuery.browser.name = "Opera";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 6);
		if ((verOffset = nAgt.indexOf("Version")) != -1)
			jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);
	}
	// In MSIE, the true version is after "MSIE" in userAgent
	else if ((verOffset = nAgt.indexOf("MSIE")) != -1) {
		jQuery.browser.msie = true;
		jQuery.browser.name = "Microsoft Internet Explorer";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 5);
	}
	// In Chrome, the true version is after "Chrome"
	else if ((verOffset = nAgt.indexOf("Chrome")) != -1) {
		jQuery.browser.webkit = true;
		jQuery.browser.name = "Chrome";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 7);
	}
	// In Safari, the true version is after "Safari" or after "Version"
	else if ((verOffset = nAgt.indexOf("Safari")) != -1) {
		jQuery.browser.webkit = true;
		jQuery.browser.name = "Safari";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 7);
		if ((verOffset = nAgt.indexOf("Version")) != -1)
			jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);
	}
	// In Firefox, the true version is after "Firefox"
	else if ((verOffset = nAgt.indexOf("Firefox")) != -1) {
		jQuery.browser.mozilla = true;
		jQuery.browser.name = "Firefox";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);
	}
	// In most other browsers, "name/version" is at the end of userAgent
	else if ((nameOffset = nAgt.lastIndexOf(' ') + 1) < (verOffset = nAgt
			.lastIndexOf('/'))) {
		jQuery.browser.name = nAgt.substring(nameOffset, verOffset);
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 1);
		if (jQuery.browser.name.toLowerCase() == jQuery.browser.name
				.toUpperCase()) {
			jQuery.browser.name = navigator.appName;
		}
	}
	// trim the fullVersion string at semicolon/space if present
	if ((ix = jQuery.browser.fullVersion.indexOf(";")) != -1)
		jQuery.browser.fullVersion = jQuery.browser.fullVersion
				.substring(0, ix);
	if ((ix = jQuery.browser.fullVersion.indexOf(" ")) != -1)
		jQuery.browser.fullVersion = jQuery.browser.fullVersion
				.substring(0, ix);

	jQuery.browser.majorVersion = parseInt('' + jQuery.browser.fullVersion, 10);
	if (isNaN(jQuery.browser.majorVersion)) {
		jQuery.browser.fullVersion = '' + parseFloat(navigator.appVersion);
		jQuery.browser.majorVersion = parseInt(navigator.appVersion, 10);
	}
	jQuery.browser.version = jQuery.browser.majorVersion;
})(jQuery);