import {Observable,ComputedObservable} from './mvp.js';

const bindings = {};
const app = ()=>{
    bindings.forename = new Observable('forename','Walid');
    bindings.surname = new Observable('surname','Hamdi');
    bindings.fullname = new ComputedObservable('fullname',() => `${bindings.forename.value} ${bindings.surname.value}`.trim(),[bindings.forename,bindings.surname]);
    console.log(new Date('1995-12-17T03:24:00').toISOString().split('T')[0]);
    bindings.birthDate = new Observable('birthDate',new Date('1995-12-17T03:24:00').toISOString().split('T')[0]);
    bindings.age = new ComputedObservable('age',()=> new Date(new Date() - new Date(`${bindings.birthDate.value}`)).getFullYear() - 1970,[bindings.birthDate]);

    bindings.ageCategory = new ComputedObservable('ageCategory',()=>{
        let age = `${bindings.age.value}`;
        if(age < 11){
            return 'enfant';
        }else if(age < 18){
            return 'adolescent';
        }else if(age < 31){
            return 'junior';
        }else if(age < 65){
            return 'adulte';
        }else{
            return 'senior';
        }
    },[bindings.age]);
    
    applyBindings();
};

setTimeout(app,0);

const applyBindings = () => {
    document.querySelectorAll("[data-bind]").forEach(elem => {
        const obs = bindings[elem.getAttribute("data-bind")];
        bindValue(elem,obs);
    });
};

const bindValue = (input,observable) => {
    input.value = observable.value;
    observable.register(() => input.value = observable.value);
	let callback = () => observable.value = input.value;
    input.onkeyup = callback;
	input.onchange = callback;
}

