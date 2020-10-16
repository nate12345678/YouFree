import { createStore } from 'redux';

const ACTIONS = {
	SET_TOKEN: '[AUTH] Set token',
	CLEAR_TOKEN: '[AUTH] Clear token',
	SET_SELF: '[AUTH] Set self user',
	CLEAR_SELF: '[AUTH] Clear self user',
	SET_THEME: '[THEME] Set theme',
	SET_ERROR: '[ERROR] Set error msg',
	CLEAR_ERROR: '[ERROR] Clear error msg'
}

export function setToken(token) {
	return {
		type: ACTIONS.SET_TOKEN,
		payload: token
	};
}

export function clearToken() {
	return {
		type: ACTIONS.CLEAR_TOKEN
	};
}

export function setSelf(self) {
	return {
		type: ACTIONS.SET_SELF,
		payload: self
	};
}

export function clearSelf() {
	return {
		type: ACTIONS.CLEAR_SELF
	};
}

export function setTheme(theme) {
	return {
		type: ACTIONS.SET_THEME,
		payload: theme
	};
}

export function setError(message) {
	return {
		type: ACTIONS.SET_ERROR,
		payload: message
	};
}

export function clearError(message) {
	return {
		type: ACTIONS.CLEAR_ERROR
	};
}


const INITIAL_STATE = {
	token: null,
	self: null,
	theme: 'light',
	errorMessage: null
};

const reducer = function (state = INITIAL_STATE, action) {
	switch (action.type) {
		case ACTIONS.SET_TOKEN:
			return {
				...state,
				token: action.payload
			};
		case ACTIONS.CLEAR_TOKEN:
			return {
				...state,
				token: null
			};
		case ACTIONS.SET_SELF:
			return {
				...state,
				self: action.payload
			};
		case ACTIONS.CLEAR_SELF:
			return {
				...state,
				self: null
			};
		case ACTIONS.SET_THEME:
			return {
				...state,
				theme: action.payload
			};
		case ACTIONS.SET_ERROR:
			return {
				...state,
				errorMessage: action.payload
			};
		case ACTIONS.CLEAR_ERROR:
			return {
				...state,
				error: null
			};
		default:
			return state;
	}
};

export const store = createStore(reducer);
