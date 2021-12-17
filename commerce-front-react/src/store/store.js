import React from 'react';
import { compose, createStore, applyMiddleware } from 'redux';
import {
    createStoreHook,
    createDispatchHook,
    createSelectorHook
} from 'react-redux'
import thunkMiddleware from 'redux-thunk';
import { createLogger } from 'redux-logger';
import rootReducer from '@store/reducers';

// convert object to string and store in localStorage
function saveToLocalStorage(state) {
    try {
        const serialisedState = JSON.stringify(state);
        localStorage.setItem("persistantState", serialisedState);
    } catch (e) {
        console.warn(e);
    }
}

// load string from localStarage and convert into an Object
// invalid output must be undefined
function loadFromLocalStorage() {
    try {
        const serialisedState = localStorage.getItem("persistantState");
        if (serialisedState === null) return undefined;
        return JSON.parse(serialisedState);
    } catch (e) {
        console.warn(e);
        return undefined;
    }
}


const loggerMiddleware = createLogger();

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

export const context = React.createContext(null)

// Export your custom hooks if you wish to use them in other files.
export const useStore = createStoreHook(context)
export const useDispatch = createDispatchHook(context)
export const useSelector = createSelectorHook(context)

export const store = createStore(
    rootReducer,
    loadFromLocalStorage(),
    composeEnhancer(
        applyMiddleware(
            thunkMiddleware,
            loggerMiddleware
        )
    )
);

// listen for store changes and use saveToLocalStorage to
// save them to localStorage
store.subscribe(() => saveToLocalStorage(store.getState()));