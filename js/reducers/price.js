'use strict';

const initialState = {
    params: {
        warehouse: null,
        transform: null,
        weight: null
    },
    datas: null,
    status: null,
    netMsg: null,
    price: null
};

import {Net, Process, Response} from '../net/net';
import Type from '../actions/type';
export default function priceReducer(state=initialState, action){
    switch(action.type){
        case Type.CHANGEPRICEPARAMS:
        {
            switch (action.name) {
                case 'warehouse':
                    return {
                        ...state,
                        params: {
                            warehouse: action.params,
                            transform: state.params.transform,
                            weight: state.params.weight
                        }
                    };
                case 'transform':
                    return {
                        ...state,
                        params: {
                            transform: action.params,
                            warehouse: state.params.warehouse,
                            weight: state.params.weight
                        }
                    };
                case 'weight':
                    return {
                        ...state,
                        params: {
                            transform: state.params.transform,
                            warehouse: state.params.warehouse,
                            weight: action.params
                        }
                    };
                default:
                    return state;
            }
        }
        case Type.GETPRICEBASEDATA:
        {
            switch (action.name) {
                case Process.FETCH_BEGIN:
                    return {
                        ...state,
                        status: Process.FETCH_BEGIN,
                        params: {
                            warehouse: null,
                            transform: null,
                            weight: null
                        },
                        price: null
                    };

                case Process.FETCH_SUCCESS:
                    return {
                        ...state,
                        datas: action.payload,
                        status: Process.FETCH_SUCCESS,
                        netMsg: action.msg
                    };

                case Process.FETCH_FAIL:
                    return {
                        ...state,
                        status: Process.FETCH_FAIL,
                        netMsg: action.msg
                    };

                default:
                    return state;
            }
        }
        case Type.CALCULATEPRICE:
        {
            switch (action.name) {
                case Process.FETCH_BEGIN:
                    return {
                        ...state,
                        status: Process.FETCH_BEGIN
                    };

                case Process.FETCH_SUCCESS:
                    return {
                        ...state,
                        price: action.payload,
                        status: Process.FETCH_SUCCESS,
                        netMsg: action.msg
                    };

                case Process.FETCH_FAIL:
                    return {
                        ...state,
                        status: Process.FETCH_FAIL,
                        netMsg: action.msg
                    };

                default:
                    return state;
            }
        }
        default:
            return state;
    }

}