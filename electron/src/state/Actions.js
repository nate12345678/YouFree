
export const Actions = {
	SET_TOKEN: '[AUTH] Set token',
	CLEAR_TOKEN: '[AUTH] Clear token',
	SET_SELF: '[AUTH] Set self user',
	CLEAR_SELF: '[AUTH] Clear self user',
	SET_THEME: '[THEME] Set theme',
	SET_ERROR: '[ERROR] Set error msg',
	CLEAR_ERROR: '[ERROR] Clear error msg',

	CREATE_USER_BEGIN: '[AUTH] Begin create user',
	CREATE_USER_SUCCESS: '[AUTH] Successful create user',
	LOGIN_BEGIN: '[AUTH] Begin login',
	LOGIN_SUCCESS: '[AUTH] Successful login',
	LOGOUT_BEGIN: '[AUTH] Begin logout',
	LOGOUT_SUCCESS: '[AUTH] Successful logout',

	FETCH_MY_SCHEDULE_BEGIN: '[SCHEDULE] Begin fetching schedule',
	FETCH_MY_SCHEDULE_SUCCESS: '[SCHEDULE] Successfully got schedule',

	FETCH_FRIEND_SCHEDULES_BEGIN: '[DASHBOARD] Begin fetching friend schedules',
	FETCH_FRIEND_SCHEDULES_SUCCESS: '[DASHBOARD] Successfully got friend schedules'
};

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

export function createUserBegin() {
	return {
		type: Actions.CREATE_USER_BEGIN
	};
}

export function createUserSuccess(token, self) {
	return {
		type: Actions.CREATE_USER_SUCCESS,
		payload: {
			token: token,
			self: self
		}
	};
}

export function loginBegin() {
	return {
		type: Actions.LOGIN_BEGIN
	};
}

export function loginSuccess(token, self) {
	return {
		type: Actions.LOGIN_SUCCESS,
		payload: {
			token: token,
			self: self
		}
	};
}

export function logoutBegin() {
	return {
		type: Actions.LOGOUT_BEGIN
	};
}

export function logoutSuccess() {
	return {
		type: Actions.LOGOUT_SUCCESS
	};
}

export function fetchMyScheduleBegin() {
	return {
		type: Actions.FETCH_MY_SCHEDULE_BEGIN
	};
}

export function fetchMyScheduleSuccess(schedule) {
	return {
		type: Actions.FETCH_MY_SCHEDULE_SUCCESS,
		payload: schedule
	};
}

export function fetchFriendSchedulesBegin() {
	return {
		type: Actions.FETCH_FRIEND_SCHEDULES_BEGIN
	};
}

export function fetchFriendSchedulesSuccess(schedules) {
	return {
		type: Actions.FETCH_FRIEND_SCHEDULES_SUCCESS,
		payload: schedules
	};
}
