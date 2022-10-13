import {Broker,Observable,ComputedObservable} from './mvp.js';

function hello(msg){
    console.log('hello: ' + msg);
}

function welcome(msg){
    console.log('welcome: ' + msg);
}

function init() {
    let broker = new Broker();
    
    //Test Singleton Design Pattern
    let anotherBroker = new Broker();
    
    if(broker === anotherBroker){
        console.log('yay it\'s the same broker');
    }
    
    //Test Publish/Subscribe Communication Pattern
    broker.subscribe('greeting',hello); // Enregistre le callback hello dans le tableau associé au topic greeting
    broker.subscribe('greeting',welcome);
    
    let spanishGreeting = msg => console.log('Hola ' + msg);
    broker.subscribe('greeting',spanishGreeting);
    
    broker.publish('greeting','ismail');
    
    broker.unsubscribe('greeting',spanishGreeting);
    
    let germanGreeting = msg => console.log('Guten tag ' + msg);
    broker.subscribe('greeting',germanGreeting);
    
    broker.publish('greeting','Ahmed'); 
    
}

document.addEventListener('DOMContentLoaded',init);
