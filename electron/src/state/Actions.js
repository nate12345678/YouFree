
export const Actions = {
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
		type: Actions.SET_TOKEN,
		payload: token
	};
}

export function clearToken() {
	return {
		type: Actions.CLEAR_TOKEN
	};
}

export function setSelf(self) {
	return {
		type: Actions.SET_SELF,
		payload: self
	};
}

export function clearSelf() {
	return {
		type: Actions.CLEAR_SELF
	};
}

export function setTheme(theme) {
	return {
		type: Actions.SET_THEME,
		payload: theme
	};
}

export function setError(message) {
	return {
		type: Actions.SET_ERROR,
		payload: message
	};
}

export function clearError() {
	return {
		type: Actions.CLEAR_ERROR
	};
}
