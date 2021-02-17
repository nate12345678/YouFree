import { FriendRequestNotification, NamedSchedule, RelatedUser, Schedule, User } from '../models/Responses';

export interface Action {
	type: Actions;
	payload?: any;
}

export enum Actions {
	SET_TOKEN = '[AUTH] Set token',
	CLEAR_TOKEN = '[AUTH] Clear token',
	SET_SELF = '[AUTH] Set self user',
	CLEAR_SELF = '[AUTH] Clear self user',
	SET_ERROR = '[ERROR] Set error msg',
	CLEAR_ERROR = '[ERROR] Clear error msg',

	SET_THEME_SUCCESS = '[THEME] Set theme',

	CREATE_USER_BEGIN = '[AUTH] Begin create user',
	CREATE_USER_SUCCESS = '[AUTH] Successful create user',
	LOGIN_BEGIN = '[AUTH] Begin login',
	LOGIN_SUCCESS = '[AUTH] Successful login',
	LOGOUT_BEGIN = '[AUTH] Begin logout',
	LOGOUT_SUCCESS = '[AUTH] Successful logout',

	FETCH_MY_SCHEDULE_BEGIN = '[SCHEDULE] Begin fetching schedule',
	FETCH_MY_SCHEDULE_SUCCESS = '[SCHEDULE] Successfully got schedule',
	ADD_INTERVAL_SUCCESS = '[SCHEDULE] Successfully added interval',
	UPDATE_INTERVAL_SUCCESS = '[SCHEDULE] Successfully edited interval',
	DELETE_INTERVAL_SUCCESS = '[SCHEDULE] Successfully deleted interval',

	FETCH_FRIEND_SCHEDULES_BEGIN = '[DASHBOARD] Begin fetching friend schedules',
	FETCH_FRIEND_SCHEDULES_SUCCESS = '[DASHBOARD] Successfully got friend schedules',

	GET_FRIENDS_BEGIN = '[PEOPLE] Begin getting friends',
	GET_FRIENDS_SUCCESS = '[PEOPLE] Successfully got friends',
	GET_PENDING_REQUESTS_BEGIN = '[PEOPLE] Begin getting pending requests',
	GET_PENDING_REQUESTS_SUCCESS = '[PEOPLE] Successfully got pending requests',
	SEARCH_USERS_BEGIN = '[PEOPLE] Begin searching for users',
	SEARCH_USERS_SUCCESS = '[PEOPLE] Successfully searched users',
	ADD_FRIEND_SUCCESS = '[PEOPLE] Successfully sent friend request',
	DELETE_FRIEND_SUCCESS = '[PEOPLE] Successfully deleted friend',

	RECEIVE_NOTIFICATION = '[NOTIFICATION] Received notification',
	ACK_NOTIFICATIONS_BEGIN = '[NOTIFICATION] Begin acknowledge notifications'
}

export const setToken = (token: string): Action => ({
	type: Actions.SET_TOKEN,
	payload: token
});

export const clearToken = (): Action => ({
	type: Actions.CLEAR_TOKEN
});

export const setSelf = (self: User): Action => ({
	type: Actions.SET_SELF,
	payload: self
});

export const clearSelf = (): Action => ({
	type: Actions.CLEAR_SELF
});

export const setThemeSuccess = (theme: string): Action => ({
	type: Actions.SET_THEME_SUCCESS,
	payload: theme
});

export const setError = (message: string | object): Action => ({
	type: Actions.SET_ERROR,
	payload: message
});

export const clearError = (): Action => ({
	type: Actions.CLEAR_ERROR
});

export const createUserBegin = (): Action => ({
	type: Actions.CREATE_USER_BEGIN
});

export const createUserSuccess = (token: string, self: User): Action => ({
	type: Actions.CREATE_USER_SUCCESS,
	payload: {
		token: token,
		self: self
	}
});

export const loginBegin = (): Action => ({
	type: Actions.LOGIN_BEGIN
});

export const loginSuccess = (token: string, self: User): Action => ({
	type: Actions.LOGIN_SUCCESS,
	payload: {
		token: token,
		self: self
	}
});

export const logoutBegin = (): Action => ({
	type: Actions.LOGOUT_BEGIN
});

export const logoutSuccess = (): Action => ({
	type: Actions.LOGOUT_SUCCESS
});

export const fetchMyScheduleBegin = (): Action => ({
	type: Actions.FETCH_MY_SCHEDULE_BEGIN
});

export const fetchMyScheduleSuccess = (schedule: Schedule): Action => ({
	type: Actions.FETCH_MY_SCHEDULE_SUCCESS,
	payload: schedule
});

export const addIntervalSuccess = (schedule: Schedule): Action => ({
	type: Actions.ADD_INTERVAL_SUCCESS,
	payload: schedule
});

export const updateIntervalSuccess = (schedule: Schedule): Action => ({
	type: Actions.UPDATE_INTERVAL_SUCCESS,
	payload: schedule
});

export const deleteIntervalSuccess = (schedule: Schedule): Action => ({
	type: Actions.DELETE_INTERVAL_SUCCESS,
	payload: schedule
});

export const fetchFriendSchedulesBegin = (): Action => ({
	type: Actions.FETCH_FRIEND_SCHEDULES_BEGIN
});

export const fetchFriendSchedulesSuccess = (schedules: NamedSchedule[]): Action => ({
	type: Actions.FETCH_FRIEND_SCHEDULES_SUCCESS,
	payload: schedules
});

export const getFriendsBegin = (): Action => ({
	type: Actions.GET_FRIENDS_BEGIN
});

export const getFriendsSuccess = (friends: User[]): Action => ({
	type: Actions.GET_FRIENDS_SUCCESS,
	payload: friends
});

export const getPendingRequestsBegin = (): Action => ({
	type: Actions.GET_PENDING_REQUESTS_BEGIN
});

export const getPendingRequestsSuccess = (pendingRequests: User[]): Action => ({
	type: Actions.GET_PENDING_REQUESTS_SUCCESS,
	payload: pendingRequests
});

export const searchUsersBegin = (): Action => ({
	type: Actions.SEARCH_USERS_BEGIN
});

export const searchUsersSuccess = (users: RelatedUser[]): Action => ({
	type: Actions.SEARCH_USERS_SUCCESS,
	payload: users
});

export const addFriendSuccess = (user: User): Action => ({
	type: Actions.ADD_FRIEND_SUCCESS,
	payload: user
});

export const deleteFriendSuccess = (user: User): Action => ({
	type: Actions.DELETE_FRIEND_SUCCESS,
	payload: user
});

export const receiveNotification = (notifications: FriendRequestNotification[]): Action => ({
	type: Actions.RECEIVE_NOTIFICATION,
	payload: notifications
});

export const beginAckNotifications = (): Action => ({
	type: Actions.ACK_NOTIFICATIONS_BEGIN
});
