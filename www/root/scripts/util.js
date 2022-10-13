export function switchToSubdomain(url,subdomain,path){
    return `${window.location.protocol}//${window.location.host.replace(/^([^.])*/,subdomain)}/${path}`;
}

export function toggleFormElements(formId,bDisabled) {
    let f = document.getElementById(formId);
    let inputs = f.getElementsByTagName("input"); 
    for (var i = 0; i < inputs.length; i++) { 
        inputs[i].disabled = bDisabled;
    } 
    let selects = f.getElementsByTagName("select");
    for (var i = 0; i < selects.length; i++) {
        selects[i].disabled = bDisabled;
    }
    let textareas = f.getElementsByTagName("textarea"); 
    for (var i = 0; i < textareas.length; i++) { 
        textareas[i].disabled = bDisabled;
    }
    let buttons = f.getElementsByTagName("button");
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].disabled = bDisabled;
    }
}

var geoWatch;
let options = {
	enableHightAccuracy: false,
	timeout: 15000,
	maximumAge: 0
};

export function startWatch(sc, ec, op){
	if(!geoWatch){
		
		if("geolocation" in navigator && "watchPosition" in navigator.geolocation){
			let actualSuccessCallback = successCallback;
			if(sc !== null && sc !== undefined){
				actualSuccessCallback = sc;
			}
			let actualErrorCallback = errorCallback;
			if(sc !== null && sc !== undefined){
				actualErrorCallback = ec;
			}
			let actualOptions = options;
			if(op !== null && op !== undefined){
				actualOptions = op;
			}
			
			geoWatch = navigator.geolocation.watchPosition(actualSuccessCallback, actualErrorCallback, actualOptions);
		}
	}
}

function successCallback(position){
	
}
function errorCallback(error){
	
}


export function stopWatch(){
	navigator.geolocation.clearWatch(geoWatch);
	geoWatch = undefined;
	
}
