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
    Keyboard
} from 'react-native';

import { PixelRatio } from 'react-native';

import {NativeModules} from 'react-native';
const RNBridgeManager = NativeModules.RNBridgeManager;

import NavBar from './components/navbar';
import commonStyles from '../styles/common';

import TabNavigator from 'react-native-tab-navigator';

import {getAnswers, switchSection} from '../actions/answers';

import { connect } from 'react-redux';

var {height, width} = Dimensions.get('window');

class Answers extends Component {

    constructor(props){
        super(props);

        this.ds = new ListView.DataSource({
            getSectionHeaderData: (dataBlob, sectionID) => dataBlob[sectionID],
            getRowData: (dataBlob, sectionID, rowID) => dataBlob[sectionID + ":" + rowID],
            rowHasChanged: (r1, r2) => r1 !== r2,
            sectionHeaderHasChanged: (s1, s2) => s1 !== s2
        });
    }

    _renderRow(rowData) {
        return (
            <View style={styles.cell}>
                <Text style={styles.cellTitle}>{rowData.title}</Text>
                <Text style={styles.cellDesc}>{rowData.desc}</Text>
            </View>
        )
    }

    _renderSection(sectionData, sectionId) {
        return (
            <TouchableOpacity style={styles.sectionHeader} onPress={(e)=>{
                this.props.switchSection(sectionId)
            }}>
                <Text style={styles.sectionTitleStyle}>{sectionData.category}</Text>
                {
                    this.props.dataSource.rowIDs[sectionId].length == 0 ?
                        <Image resizeMode={'center'} style={styles.shrinkBtnStyle} source={require('../imgs/public/shrink_btn.png')}/> :
                        <Image resizeMode={'center'} style={styles.shrinkBtnStyle} source={require('../imgs/public/shrinkup_btn.png')}/>
                }

            </TouchableOpacity>
        )
    }

    _listView() {
        let {dataSource} = this.props;
        this.ds = this.ds.cloneWithRowsAndSections(dataSource.dataBlob, dataSource.sectionIDs, dataSource.rowIDs);
        return (
            <ListView dataSource={this.ds}
                      renderRow={this._renderRow.bind(this)}
                      renderSectionHeader={this._renderSection.bind(this)}
                      enableEmptySections={true}
                      style={styles.listView}
            />
        )
    }

    _mainView() {
        return (
            <View style={commonStyles.container}>
                <NavBar style={styles.navBarStyle} title='常见问题' back={true} onPress={()=>this.props.router.pop()}/>
                {this._listView()}
            </View>
        )
    }

    render() {
        console.log(this.props.status);
        return this._mainView();
    }
}

const styles = StyleSheet.create({
    listView: {

    },
    cell: {
        backgroundColor: '#f0f0f0',
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
    navBarStyle: {
        backgroundColor: '#1b82d2'
    },
    sectionHeader: {
        backgroundColor: 'white',
        height: 44,
        borderBottomWidth: 1 / PixelRatio.get(),
        borderBottomColor: '#ccc',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between'
    },
    sectionTitleStyle: {
        marginLeft: 20
    },
    shrinkBtnStyle: {
        marginRight: 20
    }
});

function transformData(data) {
    let dataBlob = {};
    let sectionIDs = [];
    let rowIDs = [];

    for (var i = 0 ; i < data.length ; i++){
        //    1.拿到所有的sectionId
        sectionIDs.push(i);

        //    2.把组中的内容放入dataBlob内容中
        dataBlob[i] = {
            category: data[i].category,
            img: data[i].img
        };

        //    3.设置改组中每条数据的结构
        rowIDs[i] = [];

        //    4.取出改组中所有的数据
        var cars = data[i].list;

        //    5.便利cars,设置每组的列表数据
        for (var j = 0 ; j < cars.length ; j++){
            //    改组中的每条对应的rowId
            rowIDs[i].push(j);

            // 把每一行中的内容放入dataBlob对象中
            dataBlob[i+':'+j] = cars[j];
        }
    }
    return {dataBlob, sectionIDs, rowIDs};
}

const mapStateToProps = (state) => {
    return {
        dataSource: transformData(state.answersStore.data),
        status: state.answersStore.status
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        getAnswers: ()=> {
            dispatch(getAnswers())
        },
        switchSection: (section) => {
            dispatch(switchSection(section))
        }
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(Answers);