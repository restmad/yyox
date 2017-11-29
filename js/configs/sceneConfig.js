'use strict';
import React from 'react';

import {
    Navigator,
    Dimensions
} from 'react-native';

const { width } = Dimensions.get('window');

const baseConfig = Navigator.SceneConfigs.PushFromRight;
const popGestureConfig = Object.assign({}, baseConfig.gestures.pop, {
    edgeHitWidth: width / 3
});


const fullPopGestureConfig = Object.assign({}, Navigator.SceneConfigs.FloatFromBottom.gestures.pop, {
    edgeHitWidth: width
});

export const noneGesPop = Object.assign({}, baseConfig, {
    gestures: null
});


export const customFloatFromRight = Object.assign({}, baseConfig, {
    gestures: {
        pop: popGestureConfig
    }
});


export const customFloatFromBottom = Object.assign({}, Navigator.SceneConfigs.FloatFromBottom, {
    gestures: {
        pop: fullPopGestureConfig
    }
});
