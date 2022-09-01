document.addEventListener("readystatechange", function(event) {
	if(event.target.readyState == "interactive") {
        
        document.querySelector("body").classList.add("js");

        // Photo Gallery Slideshow Plugin
		var gallery = tns({
			container: ".photo-gallery",
			controlsPosition: "bottom",
			nav: false,
			autoplayButtonOutput: false,
			items: 1,
			slideBy: "page",
			autoplay: true
		});

	
    }

});