'use strict';

import {Net, Process, Api, Response} from '../net/net';
import Type from './type';

const changePriceParams = (name, params) => (dispatch, getState) => {
    return dispatch({type: Type.CHANGEPRICEPARAMS, name, params});
};

const calculatePrice = (params) => (dispatch, getState) => {
    dispatch({type: Type.CALCULATEPRICE, name: Process.FETCH_BEGIN});
    return Net.commonPostModel(Api.CALCULATEPRICEAPI, params, (res)=>{
        if (res.code == 0 || res.code == 999) {
            dispatch({type: Type.CALCULATEPRICE, name: Process.FETCH_SUCCESS, payload: res.data, msg: res.msg})//res.data
        } else {
            dispatch({type: Type.CALCULATEPRICE, name: Process.FETCH_FAIL, payload: null, msg: res.msg})
        }
    })
};

const getPriceBaseData = () => (dispatch, getState) => {
    dispatch({type: Type.GETPRICEBASEDATA, name: Process.FETCH_BEGIN});
    return Net.commonGetModel(Api.GETPRICEBASEDATAAPI, null, (res)=>{
        if (res.code == 0 || res.code == 999) {
            dispatch({type: Type.GETPRICEBASEDATA, name: Process.FETCH_SUCCESS, payload: res.data, msg: res.msg})//res.data
        } else {
            dispatch({type: Type.GETPRICEBASEDATA, name: Process.FETCH_FAIL, payload: null, msg: res.msg})
        }
    })
};

export {changePriceParams, calculatePrice, getPriceBaseData};