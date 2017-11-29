'use strict';

import React, { Component } from 'react';
import { Provider } from 'react-redux';

import configureStore from './store/index';

let store = configureStore();

import Root from './root';

import {NativeModules} from 'react-native';
const RNBridgeManager = NativeModules.RNBridgeManager;

export default class App extends Component{
    constructor(){
        super();
        this.state = {
            store: configureStore(()=>{
                this.setState({isLoading: false})
            })
        }
    }
    render(){
        if(this.state.isLoading){
            console.log('loading app');
            return null;
        }
        //RNBridgeManager.
        return (
            <Provider store={store}>
                <Root />
            </Provider>
        )
    }
}
