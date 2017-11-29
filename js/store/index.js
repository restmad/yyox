'use strict';

import {applyMiddleware, createStore} from 'redux';
import thunk from 'redux-thunk';
import createLogger from 'redux-logger';

import {persistStore, autoRehydrate} from 'redux-persist';
import {AsyncStorage} from 'react-native';
import reducers from '../reducers/index';

const logger = createLogger();

let middlewares = [
    thunk,
    logger
];

let createAppStore = applyMiddleware(...middlewares)(createStore);

export default function configureStore(onComplete: ()=>void){
    const store = autoRehydrate()(createAppStore)(reducers);
    let opt = {
        storage: AsyncStorage,
        transform: [],
        whitelist: [],
    };
    persistStore(store, opt, onComplete);
    return store;
}


