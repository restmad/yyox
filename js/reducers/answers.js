'use strict';

import {Process} from '../net/net';
import Type from '../actions/type';

const defaultState = JSON.parse(JSON.stringify(require('./answerList.json')));

const myState = JSON.parse(JSON.stringify(defaultState));

const initialState = (function(){
    var state = JSON.parse(JSON.stringify(defaultState));
    for (var i=0; i<state.data.length; i++) {
        var d = state.data[i];
        d.list = [];
    }
    return state;
})();

function dealDefaultState() {
    var state = JSON.parse(JSON.stringify(defaultState));
    for (var i=0; i<state.data.length; i++) {
        var d = state.data[i];
        d.list = [];
    }
    return state;
}

function reloadSectionData(data, section) {
    if (data[section].list.length > 0) {
        data[section].list = [];
    } else {
        data[section].list = myState.data[section].list;
    }
    return data
}

export default function answersReducer(state=initialState, action) {

    switch(action.type){
        case Type.SWITCHSECTION:
        {
            return {
                ...state,
                data: reloadSectionData(action.data, action.section)
            }
        }
        default:
            return state;
    }
}