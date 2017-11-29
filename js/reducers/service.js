'use strict';

import {Process} from '../net/net';
import Type from '../actions/type';

const initialState = {
    payload: [
        {
            title: '1.邮客与其他转运公司相比的优势在哪里？',
            desc: '依托强大的清关和仓库信息化管理能力，邮客主打稳定快速服务，为客户提供高性价比的服务；而且邮客有诸多的优惠客户政策，最大效应地让利给客户。更着重于售后服务与运输服务细节。 另外，邮客在全球各地都有自己的物流服务，能够帮助用户实现全球海淘。'
        },
        {
            title: '2.邮客海外是你们自己的仓库吗？',
            desc: '是的，为保证服务质量，邮客海淘所有的海外仓库都是自建仓库。'
        },
        {
            title: '3.转运物品有何限制？可以运奢侈品等贵重物品吗？',
            desc: '转运限制见禁运物品，另外不定期根据各国海关以及航空要求会有调整，请关注运费服务页和公告。一线奢侈品等不承运，其他贵重物品请购买保险，并注意承运物品价格上限。如您不清楚您的物品是否可转运，请先行联系客服。切勿自行想象。'
        },
        {
            title: '4. 在邮客充值后金额有使用期限吗？充值金额可以抵扣运费吗？',
            desc: '邮客不设定充值金额使用期限；充值金额可直接抵扣运费及其附加费、增值服务费、补缴税金等。'
        },
        {
            title: '5. 邮客转运超出承诺时效如何处理？',
            desc: '邮客各线路转运时效请参考运费服务；如遇到我们操作问题延误时间，邮客会给出相应补偿公告。'
        }
    ],
    data: null,
    status: null,
};

export default function serviceReducer(state=initialState, action){

    switch(action.type){
        case Type.GETSERVICEMAIN:
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
                        data: action.data,
                        status: Process.FETCH_SUCCESS
                    };

                case Process.FETCH_FAIL:
                    return {
                        ...state,
                        status: Process.FETCH_FAIL
                    };

                default:
                    return state;
            }
        }
        default:
            return state;
    }

}