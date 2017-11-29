'use strict';

import Type from './type';
import {Net, Process, Response} from '../net/net';

const getServiceMain = (params) => (dispatch, getState) => {
    dispatch({type: Type.GETSERVICEMAIN, name: Process.FETCH_BEGIN});
    return Net.commonGetModel('http://10.0.0.19:8080/app/customer/searchInfoHistory', params, (res)=>{
        if (res.code == 0 || res.code == 999) {
            dispatch({type: Type.GETSERVICEMAIN, name: Process.FETCH_SUCCESS, data: null})//res.data
        } else {
            dispatch({type: Type.GETSERVICEMAIN, name: Process.FETCH_FAIL, data: null})
        }
    })
};

export {getServiceMain};