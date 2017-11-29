'use strict';

import React, {Component} from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    ListView,
    Image,
    TouchableOpacity,
    Dimensions,
    TextInput,
    Keyboard,
    RefreshControl
} from 'react-native';

import { PixelRatio } from 'react-native';

import {NativeModules} from 'react-native';
const RNBridgeManager = NativeModules.RNBridgeManager;

import NavBar from './components/navbar';
import commonStyles from '../styles/common';
import BottomTextBtn from './components/bottomTextBtn';

import TabNavigator from 'react-native-tab-navigator';

import {getServiceMain} from '../actions/service';

import { connect } from 'react-redux';

import Price from './price';

import YJAutoScrollView from './components/YJAutoScrollView';

var {height, width} = Dimensions.get('window');
import {unionOfArray} from '../helper';
import {WebApi, Process} from '../net/net';

class Service extends Component {

    constructor(props){
        super(props);

        this.ds = new ListView.DataSource({
            rowHasChanged: (r1, r2) => r1 !== r2
        });

        this.btnDataSource = (new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2})).cloneWithRows(
            [
                {'icon': require('../imgs/service/jiage.png'), 'title': '价格'},
                {'icon': require('../imgs/service/fuwu.png'), 'title': '服务'},
                {'icon': require('../imgs/service/baozhang-.png'), 'title': '保障'},
                {'icon': require('../imgs/service/zaixiangongdan.png'), 'title': '在线工单'},
                {'icon': require('../imgs/service/zaixiankefu.png'), 'title': '在线客服'}
            ]
        );

        this.adDatas = [{imgsrc: require('../imgs/service/server_banner.png')}];
    }

    //componentWillMount () {
    //    this.keyboardWillChangeFrame = Keyboard.addListener('keyboardWillChangeFrame', this.keyboardFrameWillChanged.bind(this));
    //}
    //
    //componentWillUnmount () {
    //    this.keyboardWillChangeFrame.remove();
    //}
    //
    //keyboardFrameWillChanged (keyboard) {
    //    let input = this.refs.mainListView.refs.searchInput;
    //    input.measure((x, y, w, h, px, py)=>{
    //        if (keyboard.endCoordinates.screenY < height) {
    //            let disY = keyboard.endCoordinates.screenY - (y+h);
    //            this.refs.mainListView.scrollTo({x:0, y:disY, animated: true})
    //        }
    //    })
    //}

    _generateTabItem(title, icon, selectedIcon, component) {
        return(
            <TabNavigator.Item
                selected={'服务' === title}
                title={title}
                renderIcon={() => icon && <Image source={icon} style={styles.tabItemIconStyle} />}
                renderSelectedIcon={() => selectedIcon && <Image source={selectedIcon} style={styles.tabItemIconStyle} />}
                selectedTitleStyle={{color: '#248df5'}}
                onPress={() => {title === '服务' || RNBridgeManager.changeItem(title)}}
            >
                {component}
            </TabNavigator.Item>
        );
    }

    _renderRow(rowData) {
        return (
            <View style={styles.cell}>
                <Text style={styles.cellTitle}>{rowData.title}</Text>
                <Text style={styles.cellDesc}>{rowData.desc}</Text>
            </View>
        )
    }

    _renderBtnsRow(rowData) {
        return (
            <TouchableOpacity style={styles.btn} onPress={()=>{
                                            this.btnTitle = rowData.title;
                                            this.pressOnBtn();
            }}>
                <Image resizeMode={'center'} source={rowData.icon} style={{width: 35, height: 35, marginTop: 15, marginBottom: 10}} />
                <Text style={{fontSize: 12, color: '#323131', marginBottom: 15}}>{rowData.title}</Text>
            </TouchableOpacity>
        )
    }

    _btnsView() {
        return (
            <View style={[styles.headerViewStyle, {height: (this.adDatas.length == 0 ? 0 : 139) + 170 + 10 + 55}]}>
                {
                    this.adDatas.length > 0 ?
                        (<YJAutoScrollView style={styles.autoScrollViewStyle}
                                           dataArr={this.adDatas}
                                           hasIndicator={false}
                                           onPress={(i)=>{
                                                this.props.router.toBannerDetail({uri: WebApi.BANNERDETAILAPI})
                                           }}
                        />)
                        : null
                }
                <ListView dataSource={this.btnDataSource}
                          renderRow={this._renderBtnsRow.bind(this)}
                          style={styles.btnsView}
                          enableEmptySections={true}
                          contentContainerStyle={styles.btnsViewContainer}
                          scrollEnabled={false}
                />
                {this._searchInput()}
            </View>
        );
    }

    _footerBtnView() {
        return (
            <View style={styles.footerContainerStyle}>
                <TouchableOpacity style={styles.footerBtnStyle}
                                  onPress={ ()=>{
                                        this.props.router.toAnswers()
                                  }}
                >
                    <Text style={styles.footerTextStyle}>更多问题</Text>
                </TouchableOpacity>
            </View>
        )
    }

    _searchInput() {
        return (
            <View style={styles.searchContainerStyle}
                  ref="searchInput">
                <TouchableOpacity style={styles.searchInputContainerStyle}
                                  onPress={ ()=>{
                                        this.props.router.toAnswers()
                                  }}>
                    <Text style={styles.searchInputStyle}>常见问题搜索</Text>
                </TouchableOpacity>
            </View>
        )
    }

    _listView() {
        this.ds = this.ds.cloneWithRows(this.props.dataSource);
        return (
            <ListView dataSource={this.ds}
                      renderRow={this._renderRow.bind(this)}
                      style={styles.listView}
                      renderHeader={()=>this._btnsView()}
                      renderFooter={()=>this._footerBtnView()}
                      enableEmptySections={true}
                      ref="mainListView"
                      keyboardShouldPersistTaps={false}
                      keyboardDismissMode='on-drag'
            />
        )
    }

    _mainView() {
        return (
            <View style={commonStyles.container}>
                <NavBar style={styles.navBarStyle} title='邮客服务' />
                {this._listView()}
            </View>
        )
    }

    pressOnBtn() {
        RNBridgeManager.clickOnBtns(this.btnTitle, (res)=> {
            if (res == true) {
                if (this.btnTitle.match('价格')) {
                    this.props.router.toPrice()
                } else if (this.btnTitle.match('服务')) {
                    this.props.router.toSubServer({uri: WebApi.SEVERAPI})
                } else if (this.btnTitle.match('保障')) {
                    this.props.router.toProtect({uri: WebApi.SAFEGUARDAPI})
                }
            }
        });
    }

    render() {
        let component =
            <TabNavigator tabBarStyle={styles.tabbarStyle}>
                {this._generateTabItem('运单', require('../imgs/tab/tabbar_order.png'), require('../imgs/tab/tabbar_service_selected.png'), null)}
                {this._generateTabItem('海淘', require('../imgs/tab/tabbar_shop.png'), require('../imgs/tab/tabbar_shop_selected.png'), null)}
                {this._generateTabItem('服务', require('../imgs/tab/tabbar_service.png'), require('../imgs/tab/tabbar_service_selected.png'), this._mainView())}
                {this._generateTabItem('我的', require('../imgs/tab/tabbar_mine.png'), require('../imgs/tab/tabbar_mine_selected.png'), null)}
            </TabNavigator>;
        return component;
    }
}

const styles = StyleSheet.create({
    headerViewStyle: {
        width: width,
        borderBottomWidth: 1 / PixelRatio.get(),
        borderColor: '#ccc',
        borderStyle: 'solid'
    },
    autoScrollViewStyle: {
        height: 129,
        width: width,
        marginBottom: 10,
        borderBottomWidth: 1 / PixelRatio.get(),
        borderColor: '#ccc',
        borderStyle: 'solid'
    },
    listView: {

    },
    cell: {
        backgroundColor: 'white',
        borderColor: '#ccc',
        borderStyle: 'solid',
        borderBottomWidth: 1 / PixelRatio.get()
    },
    cellTitle: {
        color: '#2a2828',
        fontSize: 14,
        marginLeft: 20,
        marginTop: 15,
        marginRight: 20
    },
    cellDesc: {
        color: '#666666',
        fontSize: 12,
        marginLeft: 20,
        marginRight: 20,
        marginTop: 13,
        marginBottom: 15,
        lineHeight: 16
    },
    btnsViewContainer: {
        height: 170,
        flexDirection: 'row',
        flexWrap: 'wrap'
    },
    btnsView: {
        borderBottomWidth: 1 / PixelRatio.get(),
        borderTopWidth: 1 / PixelRatio.get(),
        borderColor: '#ccc',
        backgroundColor: 'white',
        borderStyle: 'solid',
        marginBottom: 10
    },
    btn: {
        width: width / 3,
        height: 80,
        alignItems: 'center'
    },
    tabItemIconStyle: {
        width: 30,
        height: 30
    },
    tabbarStyle: {
        backgroundColor: 'white'
    },
    navBarStyle: {
        backgroundColor: '#1b82d2'
    },
    searchContainerStyle: {
        backgroundColor: 'white',
        borderTopWidth: 1 / PixelRatio.get(),
        borderColor: '#ccc',
        height: 55
    },
    searchInputStyle: {
        fontSize: 12,
        color: '#999'
    },
    searchInputContainerStyle: {
        backgroundColor: '#eee',
        height: 28,
        marginLeft: 20,
        marginRight: 20,
        marginTop: 27/2,
        borderRadius: 14,
        justifyContent: 'center',
        alignItems: 'center'
    },
    footerBtnStyle: {
        backgroundColor: 'rgba(0, 0, 0, 0)',
        justifyContent: 'center',
        alignItems: 'center',
        height: 40,
        width: 130,
        borderColor: '#1e81d1',
        borderRadius: 5,
        borderWidth: 1 / PixelRatio.get(),
        marginTop: 20,
        marginBottom: 20
    },
    footerContainerStyle: {
        flex: 1,
        backgroundColor: 'rgba(0, 0, 0, 0)',
        justifyContent: 'center',
        alignItems: 'center',
    },
    footerTextStyle: {
        color: '#1e81d1',
        fontSize: 14,
    }
});

const mapStateToProps = (state) => {
    return {
        dataSource: state.serviceStore.payload,
        data: state.serviceStore.data,
        status: state.serviceStore.status
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        getServiceMain: (params)=> {
            dispatch(getServiceMain(params))
        }
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(Service);