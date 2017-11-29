import { combineReducers } from 'redux';
import serviceReducer from './service';
import priceReducer from './price';
import answersReducer from './answers';

export default combineReducers({
    serviceStore: serviceReducer,
    priceStore: priceReducer,
    answersStore: answersReducer
});