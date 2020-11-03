import {
	applyMiddleware,
	createStore
} from 'redux';
import thunk from 'redux-thunk';
import { Actions } from './Actions';

const INITIAL_STATE = {
	token: null,
	self: null,
	theme: 'light',
	mySchedule: null,
	friendSchedules: null,
	friends: {
		items: []
	},
	pendingRequests: {
		items: []
	},
	searchResults: {
		items: []
	},
	errorMessage: null
};

const reducer = function (state = INITIAL_STATE, action) {
	switch (action.type) {
		case Actions.SET_TOKEN:
			return {
				...state,
				token: action.payload
			};
		case Actions.CLEAR_TOKEN:
			return {
				...state,
				token: null
			};
		case Actions.SET_SELF:
			return {
				...state,
				self: action.payload
			};
		case Actions.CLEAR_SELF:
			return {
				...state,
				self: null
			};
		case Actions.SET_THEME_SUCCESS:
			return {
				...state,
				theme: action.payload
			};
		case Actions.SET_ERROR:
			return {
				...state,
				errorMessage: action.payload
			};
		case Actions.CLEAR_ERROR:
			return {
				...state,
				errorMessage: null
			};
		case Actions.CREATE_USER_SUCCESS:
			// Fallthrough
		case Actions.LOGIN_SUCCESS:
			return {
				...state,
				token: action.payload.token,
				self: action.payload.self
			};
		case Actions.LOGOUT_SUCCESS:
			return INITIAL_STATE;
		case Actions.ADD_INTERVAL_SUCCESS: // Fallthrough
		case Actions.UPDATE_INTERVAL_SUCCESS: // Fallthrough
		case Actions.DELETE_INTERVAL_SUCCESS: // Fallthrough
		case Actions.FETCH_MY_SCHEDULE_SUCCESS:
			return {
				...state,
				mySchedule: action.payload
			};
		case Actions.FETCH_FRIEND_SCHEDULES_SUCCESS:
			return {
				...state,
				friendSchedules: action.payload
			};
		case Actions.GET_FRIENDS_SUCCESS:
			return {
				...state,
				friends: createEntityState(action.payload, 'id')
			};
		case Actions.GET_PENDING_REQUESTS_SUCCESS:
			return {
				...state,
				pendingRequests: createEntityState(action.payload, 'id')
			};
		case Actions.SEARCH_USERS_SUCCESS:
			return {
				...state,
				searchResults: createEntityState(action.payload, 'id')
			};
		case Actions.ADD_FRIEND_SUCCESS:
			// We don't actually add the friend to the 'friends list' entity state
			// We reload the friends list on the friends page, so it gets overwritten
			// And I don't want to deal with inserting the user into proper alphabetical order in list
			return {
				...state,
				pendingRequests: removeFromEntityState(state.pendingRequests, action.payload.id),
				searchResults: updateSearchResultsAddFriend(state.searchResults, action.payload.id)
				// friends: addToEntityState(state.friends, action.payload, 'id')
			};
		case Actions.DELETE_FRIEND_SUCCESS:
			return {
				...state,
				pendingRequests: removeFromEntityState(state.pendingRequests, action.payload.id),
				searchResults: updateSearchResultsDeleteFriend(state.searchResults, action.payload.id),
				friends: removeFromEntityState(state.friends, action.payload.id)
				// TODO: remove from friendSchedules
			};
		default:
			return state;
	}
};

export const store = createStore(reducer, applyMiddleware(thunk));


function updateSearchResultsAddFriend(searchResults, userId) {
	if (!searchResults.hasOwnProperty(userId)) return searchResults;

	const index = searchResults[userId];
	const updatedItem = { ...searchResults.items[index] };

	switch (updatedItem.relationship) {
		case 'PENDING':
			updatedItem.relationship = 'FRIENDS';
			break;
		case 'NONE':
			updatedItem.relationship = 'SENT';
			break;
		default:
			console.error(`Should not be able to add friend with state ${updatedItem.relationship}`);
			break;
	}

	return updateEntityStateItem(searchResults, updatedItem, index);
}


function updateSearchResultsDeleteFriend(searchResults, userId) {
	if (!searchResults.hasOwnProperty(userId)) return searchResults;

	const index = searchResults[userId];
	const updatedItem = { ...searchResults.items[index] };

	switch (updatedItem.relationship) {
		case 'FRIENDS': // Fallthrough
		case 'SENT':  // Fallthrough
		case 'PENDING':
			updatedItem.relationship = 'NONE';
			break;
		default:
			console.error(`Should not be able to remove friend with state ${updatedItem.relationship}`);
			break;
	}

	return updateEntityStateItem(searchResults, updatedItem, index);
}


function createEntityState(entities, idName) {
	const entityState = { items: entities };
	for (let [index, entity] of entities.entries()) {
		const id = entity[idName];
		entityState[id] = index;
	}

	return entityState;
}


// eslint-disable-next-line
function addToEntityState(lastEntityState, entity, idName) {
	return {
		...lastEntityState,
		[entity[idName]]: lastEntityState.items.length,
		items: lastEntityState.items.concat(entity)
	};
}


function updateEntityStateItem(lastEntityState, updatedItem, index) {
	const updatedEntityState = { ...lastEntityState };
	const updatedItems = { ...lastEntityState.items };

	updatedItems[index] = updatedItem;
	updatedEntityState.items = updatedItems;

	return updatedEntityState;
}


function removeFromEntityState(lastEntityState, id) {
	let nextEntityState = { ...lastEntityState };

	// Only remove if ID exists in entity state
	if (lastEntityState.hasOwnProperty(id)) {
		nextEntityState.items = lastEntityState.items.filter((user, index) => index !== lastEntityState[id]);
		delete nextEntityState[id];
	}

	return nextEntityState;
}
