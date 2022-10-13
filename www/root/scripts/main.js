import VanillaRouter from './router.js';
import Swiper from 'https://cdn.jsdelivr.net/npm/swiper@8/swiper-bundle.esm.browser.min.js';
import {startWatch, stopWatch} from './util.js';

let currentContext = {};
const router = new VanillaRouter({type: history, routes: {
	'/': 'main',
	'/dashboard': 'dashboard',
	'/lst_thing': 'listThings',
	'/del_thing': 'deleteThing',
	'/mod_thing': 'modifyThing',
	'/add_thing': 'addThing'
}}).listen().on('route', async e => {
	fetch('pages/' + e.detail.route + '.html')
		.then(response => response.text())
		.then(htmlData => {
			document.getElementById('main-content').innerHTML = htmlData;
			if(e.detail.route === 'dashboard'){
				const swiper = new Swiper('.mySwiper', {
                    spaceBetween: 2,
                    navigation: {
                        nextEl: '.swiper-button-next',
                        prevEl: '.swiper-button-prev',
                    },
                });
                let prevBtn = document.getElementById('swiper-button-prev');
                let nextBtn = document.getElementById('swiper-button-next');
                swiper.on('reachEnd', function(){
                     nextBtn.classList.add('swiper-button-disabled');
                     prevBtn.classList.remove('swiper-button-disabled');
                });
                
                swiper.on('reachBeginning', function(){
                     nextBtn.classList.remove('swiper-button-disabled');
                     prevBtn.classList.add('swiper-button-disabled');
                });
				
				if(navigator.geolocation){
					navigator.geolocation.getCurrentPosition( // success callback, error callback, options
						(position) => {
							document.getElementById("accuracy").innerHTML = position.coords.accuracy;
							document.getElementById("altitude").innerHTML = position.coords.altitude;
							document.getElementById("latitude").innerHTML = position.coords.latitude;
							document.getElementById("longitude").innerHTML = position.coords.longitude;
							document.getElementById("altitude-accuracy").innerHTML = position.coords.altitudeAccuracy;
							document.getElementById("speed").innerHTML = position.coords.speed;
							document.getElementById("heading").innerHTML = position.coords.heading;
							document.getElementById("timestamp").innerHTML = position.timestamp;
						}, 
						(err) => {
							switch(err.code){
								case err.PERMISSION_DENIED: 
									console.log("User denied access to geolocation");
									break;
								case err.POSITION_UNAVAILABLE:
									console.log("Location information is unavailable");
									break;
								case err.TIMEOUT:
									console.log("Request to get user location timed out");
									break;
								case err.UNKNOWN_ERROR:
									console.log("An unknown error occured.");
									break;
							}
							
						}, 
						{
							enableHightAccuracy: false,
							timeout: 15000,
							maximumAge: 0 
						}
					);
				}
			}
		})
		.catch(e => console.log(e));
});

if("serviceWorker" in navigator){
	window.addEventListener("load", () => {
		navigator.serviceWorker.register("/scripts/serviceWorker.js")
		.then(res => console.log("serviceWorker registered"))
		.catch(err => console.log("serviceWorker not registered", err));
	});
}