import youfree from '../api/Youfree';
import {
	createUserBegin,
	createUserSuccess,
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
	setError
} from './Actions';

const handleError = (dispatch, error) => {
	if (error.response !== undefined) {
		console.log(error.response);
		dispatch(setError(error.response.data));
		return;
	}

	console.log('An unknown error has occurred');
	dispatch(setError('An unknown error has occurred'));
};

export const createUser = (email, username, password, remember) => async (dispatch) => {
	dispatch(createUserBegin());

	try {
		const response = await youfree.createUser(email, username, password);
		const token = response.headers.token;
		const self = response.data;

		if (remember) {
			localStorage.setItem('token', token);
			localStorage.setItem('self', JSON.stringify(self));
		}

		dispatch(createUserSuccess(token, self));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const login = (email, password, remember) => async (dispatch) => {
	dispatch(loginBegin());

	try {
		const response = await youfree.login(email, password);
		const token = response.headers.token;
		const self = response.data;

		if (remember) {
			localStorage.setItem('token', token);
			localStorage.setItem('self', JSON.stringify(self));
		}

		dispatch(loginSuccess(token, self));
	} catch (error) {
		handleError(dispatch, error);
	}
};

export const logout = () => async (dispatch, getState) => {
	dispatch(logoutBegin());

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

export const fetchFriendSchedules = () => async (dispatch, getState) => {
	dispatch(fetchFriendSchedulesBegin());

	try {
		const response = await youfree.getFriendSchedules(getState().token);
		dispatch(fetchFriendSchedulesSuccess(response.data));
	} catch (error) {
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

export const getPendingRequests = () => async (dispatch, getState) => {
	dispatch(getPendingRequestsBegin());

	try {
		const response = await youfree.getPending(getState().token);
		dispatch(getPendingRequestsSuccess(response.data));
	} catch (error) {
		handleError(dispatch, error);
	}
};
