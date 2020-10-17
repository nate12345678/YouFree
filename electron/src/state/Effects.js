import youfree from '../api/Youfree';
import {
	fetchFriendSchedulesBegin,
	fetchFriendSchedulesSuccess,
	fetchMyScheduleBegin,
	fetchMyScheduleSuccess,
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

export function login(email, password, remember) {
	return (dispatch) => {
		dispatch(loginBegin());

		return youfree.login(email, password).then(
			(response) => {
				const token = response.headers.token;
				const user = response.data;

				if (remember) {
					localStorage.setItem('token', token);
					localStorage.setItem('self', JSON.stringify(user));
				}

				dispatch(loginSuccess(token, user));
			},
			(error) => handleError(dispatch, error)
		);
	};
}

export function logout() {
	return (dispatch, getState) => {
		dispatch(logoutBegin());

		return youfree.logout(getState().token).then(
			(response) => {
				localStorage.clear();
				dispatch(logoutSuccess());
			},
			(error) => handleError(dispatch, error)
		);
	};
}

export function fetchMySchedule() {
	return (dispatch, getState) => {
		// Dispatch START action
		dispatch(fetchMyScheduleBegin());

		// Use API to get schedule
		return youfree.getSchedule(getState().token, getState().self.id).then(
			(response) => dispatch(fetchMyScheduleSuccess(response.data)),
			(error) => handleError(dispatch, error)
		);
	};
}

export function fetchFriendSchedules() {
	return (dispatch, getState) => {
		dispatch(fetchFriendSchedulesBegin());

		return youfree.getFriendSchedules(getState().token).then(
			(response) => dispatch(fetchFriendSchedulesSuccess(response.data)),
			(error) => handleError(dispatch, error)
		);
	};
}
