import youfree from '../api/Youfree';
import { FriendRequestNotification, User } from '../models/Responses';
import {
	addFriendSuccess,
	addIntervalSuccess,
	createUserBegin,
	createUserSuccess,
	deleteFriendSuccess,
	deleteIntervalSuccess,
	fetchFriendSchedulesBegin,
	fetchFriendSchedulesSuccess,
	fetchMyScheduleBegin,
	fetchMyScheduleSuccess,
	getFriendsBegin,
	getFriendsSuccess,
	getPendingRequestsBegin,
	getPendingRequestsSuccess,
	loginBegin,
	loginSuccess,
	logoutBegin,
	logoutSuccess,
	receiveNotification,
	searchUsersBegin,
	searchUsersSuccess,
	setError,
	setSelf,
	setThemeSuccess,
	setToken,
	updateIntervalSuccess
} from './Actions';
import Notifier from '../api/Notifier';

const handleError = (dispatch, error: any) => {
	if (error.response !== undefined) {
		console.log(error.response);
		dispatch(setError(error.response.data));
		return;
	}

	console.log('An unknown error has occurred');
	dispatch(setError('An unknown error has occurred'));
};

const handleNotification = (dispatch) => (frame) => {
	const notifications: FriendRequestNotification[] = JSON.parse(frame.body)
	dispatch(receiveNotification(notifications));
}

export const initApp = () => async (dispatch) => {
	// Auto-login
	const token = localStorage.getItem('token');
	const self = JSON.parse(localStorage.getItem('self'));
	if (token && self) {
		dispatch(setToken(token));
		dispatch(setSelf(self));

		Notifier.connect(self.id, token, handleNotification(dispatch));
	}

	// Load theme
	const theme = localStorage.getItem('theme');
	if (theme) {
		dispatch(setThemeSuccess(theme));
	}
};

export const setTheme = (theme: string) => async (dispatch, getState) => {
	const from = getState().theme;
	const to = theme;

	Notifier.sendHello();

	document.body.classList.replace(from, to);
	localStorage.setItem('theme', to);
	dispatch(setThemeSuccess(to));
};

export const createUser = (email: string, username: string, password: string, remember: boolean) => async (dispatch) => {
	dispatch(createUserBegin());

	try {
		const response = await youfree.createUser(email, username, password);
		const token = response.headers.token;
		const self = response.data;

		if (remember) {
			localStorage.setItem('token', token);
			localStorage.setItem('self', JSON.stringify(self));
		}

		Notifier.connect(self.id, token, handleNotification(dispatch));
		dispatch(createUserSuccess(token, self));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const login = (email: string, password: string, remember: boolean) => async (dispatch) => {
	dispatch(loginBegin());

	try {
		const response = await youfree.login(email, password);
		const token = response.headers.token;
		const self = response.data;

		if (remember) {
			localStorage.setItem('token', token);
			localStorage.setItem('self', JSON.stringify(self));
		}

		Notifier.connect(self.id, token, handleNotification(dispatch));
		dispatch(loginSuccess(token, self));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const logout = () => async (dispatch, getState) => {
	dispatch(logoutBegin());
	Notifier.disconnect();

	try {
		await youfree.logout(getState().token);
		localStorage.clear();
		dispatch(logoutSuccess());
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const fetchMySchedule = () => async (dispatch, getState) => {
	dispatch(fetchMyScheduleBegin());

	try {
		const response = await youfree.getSchedule(getState().token, getState().self.id);
		dispatch(fetchMyScheduleSuccess(response.data));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const addInterval = (dayOfWeek: number, startMin: number, endMin: number) => async (dispatch, getState) => {
	try {
		const response = await youfree.addInterval(getState().token, dayOfWeek, startMin, endMin);
		dispatch(addIntervalSuccess(response.data));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const updateInterval = (intervalId: number, dayOfWeek: number, startMin: number, endMin: number) => async (dispatch, getState) => {
	try {
		const response = await youfree.updateInterval(getState().token, intervalId, dayOfWeek, startMin, endMin);
		dispatch(updateIntervalSuccess(response.data));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const deleteInterval = (intervalId: number) => async (dispatch, getState) => {
	try {
		const response = await youfree.deleteInterval(getState().token, intervalId);
		dispatch(deleteIntervalSuccess(response.data));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const getFriendSchedules = () => async (dispatch, getState) => {
	dispatch(fetchFriendSchedulesBegin());

	try {
		const response = await youfree.getFriendSchedules(getState().token);
		dispatch(fetchFriendSchedulesSuccess(response.data));
	} catch (error) {
		console.log(error);
		handleError(dispatch, error);
	}
};

export const getFriends = () => async (dispatch, getState) => {
	dispatch(getFriendsBegin());

	try {
		const response = await youfree.getFriends(getState().token);
		dispatch(getFriendsSuccess(response.data));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const addFriend = (user: User) => async (dispatch, getState) => {
	try {
		// eslint-disable-next-line
		const response = await youfree.addFriend(getState().token, user.id);
		dispatch(addFriendSuccess(user));
	} catch (error) {
		handleError(dispatch, error);
	}
}

export const deleteFriend = (user: User) => async (dispatch, getState) => {
	try {
		// eslint-disable-next-line
		const response = await youfree.deleteFriend(getState().token, user.id);
		dispatch(deleteFriendSuccess(user));
	} catch (error) {
		handleError(dispatch, error);
	}
}

export const getPendingRequests = () => async (dispatch, getState) => {
	dispatch(getPendingRequestsBegin());

	try {
		const response = await youfree.getPending(getState().token);
		dispatch(getPendingRequestsSuccess(response.data));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const searchUsers = (query: string) => async (dispatch, getState) => {
	dispatch(searchUsersBegin());

	try {
		const searchUsersResponse = await youfree.searchUsers(getState().token, query);
		const userRelationships = searchUsersResponse.data.map(result => ({
			...result.user,
			relationship: result.relationship
		}));
		dispatch(searchUsersSuccess(userRelationships));
	} catch (error) {
		handleError(dispatch, error);
	}
};
