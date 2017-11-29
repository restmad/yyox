'use strict';

import React, {Component} from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    ListView,
    TouchableOpacity,
    TouchableHighlight,
    TextInput,
    Image,
    Dimensions,
    Keyboard
} from 'react-native';

import { PixelRatio } from 'react-native';
var {height, width} = Dimensions.get('window');

import {Process, WebApi} from '../net/net';
import {NativeModules} from 'react-native';
const RNBridgeManager = NativeModules.RNBridgeManager;

import NavBar from './components/navbar';
import commonStyles from '../styles/common';

import { connect } from 'react-redux';

import {changePriceParams, calculatePrice, getPriceBaseData} from '../actions/price';
import {unionOfArray} from '../helper';

class Price extends Component {

    constructor(props){
        super(props);
        this.loading = false;
    }

    componentDidMount() {
        this.props.getPriceBaseData();
    }

    _checkParams() {
        let error = null;
        if (!this.props.params.warehouse) error = '请选择仓库';
        else if (!this.props.params.transform) error = '请选择运输服务';
        else if (!this.props.params.weight) error = '请输入重量';

        if (error) {
            RNBridgeManager.showError(error);
            return null;
        }
        let value = this.props.params.weight;
        if (value.length > 0) {
            value = parseFloat(value);
            if (isNaN(value)) {
                error = '只允许输入数字，请检查您的输入';
            }
        }

        if (error) {
            RNBridgeManager.showError(error);
            return null;
        }
        return {
            'orderType': this.props.params.transform.code,
            'orderDetail': {
                'estimatedWeight': value
            },
            'warehouseId': this.props.params.warehouse.id,
            'ordertypeId': this.props.params.transform.id,
            'leadId': this.props.params.transform.leadId
        };
    }

    _warehouses(transform) {
        if (!this.props.datas) return null;
        //if (!transform) {
            return this.props.datas;
        //}
        //return this.props.datas.filter(function(warehouse){
        //    for (var i=0; i<warehouse.orderTypeList.length; i++) {
        //        let orderType = warehouse.orderTypeList[i];
        //        if (orderType.id === transform.id) {
        //            return true;
        //        }
        //    }
        //    return false;
        //});
    }

    _transforms(warehouse) {
        if (!this.props.datas) return null;
        if (!warehouse) {
            let transforms = this.props.datas.reduce(function(c, w){
                return c.concat(w.orderTypeList);
            }, []);
            return unionOfArray(transforms, 'id');
        }
        return warehouse.orderTypeList;
    }

    _staticListView() {
        let warehouse = this.props.params.warehouse;
        let transform = this.props.params.transform;
        return (
            <View style={styles.staticListViewStyle}>
                <TouchableOpacity onPress={()=>{
                    let warehouses = this._warehouses(transform);
                    RNBridgeManager.showPickViewWithCallback(warehouses, (idx)=>{
                        if (warehouses) {
                            this.props.changePriceParams('warehouse', warehouses[idx])
                            if (transform) {
                                this.props.changePriceParams('transform', null)
                            }
                        }
                    })
                }}>
                    <View style={styles.staticRowStyle}>
                        <Text style={styles.staticRowTitleStyle}>{'仓库：'}</Text>
                        <TextInput style={[styles.staticRowInputStyle, {textAlign: 'right'}]}
                                   placeholder="请选择仓库"
                                   editable={false}
                                   value={warehouse ? warehouse.name : ''}
                        />
                        <Image resizeMode={'center'} style={styles.disclosureStyle} source={require('../imgs/public/cell_disclosure.png')}/>
                    </View>
                </TouchableOpacity>
                <TouchableOpacity onPress={()=>{
                    if (!warehouse) return RNBridgeManager.showError('请先选择仓库');
                    let transforms = this._transforms(warehouse);
                    RNBridgeManager.showPickViewWithCallback(transforms, (idx)=>{
                        if (transforms) {
                            this.props.changePriceParams('transform', transforms[idx])
                        }
                    })
                }}>
                <View style={styles.staticRowStyle}>
                    <Text style={styles.staticRowTitleStyle}>{'运输服务：'}</Text>
                    <TextInput style={[styles.staticRowInputStyle, {textAlign: 'right'}]}
                               placeholder="请选择运输服务"
                               editable={false}
                               value={transform ? transform.name : ''}
                    />
                    <Image resizeMode={'center'} style={styles.disclosureStyle} source={require('../imgs/public/cell_disclosure.png')}/>
                </View>
                </TouchableOpacity>
                <View style={styles.staticRowStyle}>
                    <Text style={styles.staticRowTitleStyle}>{'输入包裹重量(KG)：'}</Text>
                    <TextInput style={[styles.staticRowInputStyle, {textAlign: 'left'}]}
                               ref="warehouse"
                               placeholder="请输入包裹重量"
                               defaultValue={this.props.params ? this.props.params.weight ? this.props.params.weight.toString() : null : null}
                               onEndEditing={(e)=> {
                                    let value = e.nativeEvent.text;
                                   this.props.changePriceParams('weight', value)
                               }}
                    />
                </View>
                <View style={[styles.staticRowStyle, {height: 65}]}>
                    <Text style={[styles.staticRowTitleStyle, {fontSize: 15, color: '#333'}]}>{'您的运费：'}</Text>
                    <Text style={styles.staticRowPriceCategoryStyle}>{'RMB'}</Text>
                    <Text style={styles.staticRowPriceStyle}>{this.props.price ? this.props.price.shippingCostStr : '0.00'}</Text>
                </View>
            </View>
        )
    }

    _mainView() {
        return (
            <View style={commonStyles.container}
                  onStartShouldSetResponder={()=>{
                      Keyboard.dismiss();
                      return false;
                  }}>
                <NavBar title='价格'
                        back={true}
                        onPress={()=>this.props.router.pop()}
                        rightItem={
                            <TouchableOpacity style={{right: 10,
                                                        position: 'absolute',
                                                        top: 0,
                                                        height: 44,
                                                        zIndex: 2,
                                                        justifyContent: 'center'}}
                                              onPress={()=>{
                                                  this.props.router.toPriceRule({uri: WebApi.CALCULATERULE});
                                              }}>
                                <Text style={{color: 'white', fontSize: 14}}>计价规则</Text>
                            </TouchableOpacity>
                        }
                />
                {this._staticListView()}
                <TouchableHighlight style={{width: width, height: 49, position: 'absolute', bottom: 0}}
                                    onPress={()=>{
                                        let params = this._checkParams();
                                        if (params) {
                                            this.props.calculatePrice(params);
                                        }
                                    }}
                >
                    <Text style={styles.bottomBtnStyle}>{'计算运费'}</Text>
                </TouchableHighlight>
            </View>
        )
    }

    render() {
        if (this.props.status === Process.FETCH_BEGIN) {
            RNBridgeManager.showLoading(null);
            this.loading = true;
        } else if (this.props.status === Process.FETCH_FAIL && this.loading) {
            RNBridgeManager.showError(this.props.netMsg);
            this.loading = false;
        } else {
            RNBridgeManager.hideHUD();
        }
        return this._mainView();
    }
}

const styles = StyleSheet.create({
    staticListViewStyle: {

    },
    staticRowStyle: {
        backgroundColor: 'white',
        height: 50,
        borderBottomColor: '#ccc',
        borderBottomWidth: 1 / PixelRatio.get(),
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        paddingLeft: 20,
        paddingRight: 20,
    },
    staticRowInputStyle: {
        fontSize: 13,
        height: 50,
        marginRight: 10,
        flexGrow: 1,
        marginLeft: 10
    },
    staticRowTitleStyle: {
        color: '#666',
        fontSize: 13,
        minWidth: 120,
        textAlign: 'left'
    },
    disclosureStyle: {
        width: 8,
        height: 13
    },
    staticRowPriceStyle: {
        fontSize: 20,
        color: '#fc9b31',
        marginRight: 0
    },
    staticRowPriceCategoryStyle: {
        fontSize: 15,
        color: '#fc9b31',
        flexGrow: 1,
        textAlign: 'right',
        paddingTop: 5
    },
    bottomBtnStyle: {
        color: 'white',
        fontSize: 15,
        textAlign: 'center',
        flex: 1,
        lineHeight: 49,
        backgroundColor: '#1a6bc6'
    }
});

const mapStateToProps = (state) => {
    return {
        params: state.priceStore.params,
        datas: state.priceStore.datas,
        status: state.priceStore.status,
        netMsg: state.priceStore.netMsg,
        price: state.priceStore.price
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        changePriceParams: (name, text)=> {
            dispatch(changePriceParams(name, text))
        },
        getPriceBaseData: ()=> {
            dispatch(getPriceBaseData())
        },
        calculatePrice: (params)=> {
            dispatch(calculatePrice(params))
        }
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(Price);